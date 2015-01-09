package com.liferay.portal.search.backgroundtask;

import com.liferay.portal.SystemException;
import com.liferay.portal.dao.shard.ShardUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskConstants;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatus;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskStatusMessageTranslator;
import com.liferay.portal.kernel.backgroundtask.BaseBackgroundTaskExecutor;
import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.concurrent.ThreadPoolExecutor;
import com.liferay.portal.kernel.executor.PortalExecutorManagerUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.BaseAsyncDestination;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.search.SearchEngineInitializer;
import com.liferay.portal.search.lucene.LuceneHelperUtil;
import com.liferay.portal.search.lucene.cluster.LuceneClusterUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.util.PropsValues;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by liferay on 12/2/2014.
 */
public class IndexBackgroundTaskExecutor extends BaseBackgroundTaskExecutor {

	public IndexBackgroundTaskExecutor() {
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		Map<String, Serializable> taskContextMap =
			backgroundTask.getTaskContextMap();

		long[] companyIds = (long[]) taskContextMap.get("companyIds");
		String portletId = (String) taskContextMap.get("portletId");

		doReindex(companyIds, portletId);

		BackgroundTaskResult backgroundTaskResult = new BackgroundTaskResult(
			BackgroundTaskConstants.STATUS_SUCCESSFUL);

		return backgroundTaskResult;
	}

	protected void doReindex(long[] companyIds, String portletId)
		throws Exception {

		Set<String> usedSearchEngineIds = new HashSet<String>();

		if (Validator.isNull(portletId)) {
			for (long companyId : companyIds) {
				try {
					SearchEngineInitializer searchEngineInitializer =
						new SearchEngineInitializer(companyId);

					searchEngineInitializer.reindex();

					usedSearchEngineIds.addAll(
						searchEngineInitializer.getUsedSearchEngineIds());
				}
				catch (Exception e) {
					_log.error(e, e);
				}
			}
		}
		else {
			Portlet portlet = PortletLocalServiceUtil.getPortletById(
				companyIds[0], portletId);

			if (portlet == null) {
				return;
			}

			List<Indexer> indexers = portlet.getIndexerInstances();

			if (indexers == null) {
				return;
			}

			Set<String> searchEngineIds = new HashSet<String>();

			for (Indexer indexer : indexers) {
				searchEngineIds.add(indexer.getSearchEngineId());
			}

			for (String searchEngineId : searchEngineIds) {
				for (long companyId : companyIds) {
					SearchEngineUtil.deletePortletDocuments(
						searchEngineId, companyId, portletId, true);
				}
			}

			for (Indexer indexer : indexers) {
				for (long companyId : companyIds) {
					ShardUtil.pushCompanyService(companyId);

					try {
						indexer.reindex(
							new String[] {String.valueOf(companyId)});

						usedSearchEngineIds.add(indexer.getSearchEngineId());
					}
					catch (Exception e) {
						_log.error(e, e);
					}
					finally {
						ShardUtil.popCompanyService();
					}
				}
			}
		}

		if (!LuceneHelperUtil.isLoadIndexFromClusterEnabled()) {
			return;
		}

		Set<BaseAsyncDestination> searchWriterDestinations =
			new HashSet<BaseAsyncDestination>();

		MessageBus messageBus = MessageBusUtil.getMessageBus();

		for (String usedSearchEngineId : usedSearchEngineIds) {
			String searchWriterDestinationName =
				SearchEngineUtil.getSearchWriterDestinationName(
					usedSearchEngineId);

			Destination destination = messageBus.getDestination(
				searchWriterDestinationName);

			if (destination instanceof BaseAsyncDestination) {
				BaseAsyncDestination baseAsyncDestination =
					(BaseAsyncDestination)destination;

				searchWriterDestinations.add(baseAsyncDestination);
			}
		}

		submitClusterIndexLoadingSyncJob(searchWriterDestinations, companyIds);
	}

	protected void submitClusterIndexLoadingSyncJob(
		Set<BaseAsyncDestination> baseAsyncDestinations, long[] companyIds)
		throws Exception {

		if (_log.isInfoEnabled()) {
			StringBundler sb = new StringBundler(
				baseAsyncDestinations.size() + 1);

			sb.append("[");

			for (BaseAsyncDestination baseAsyncDestination :
				baseAsyncDestinations) {

				sb.append(baseAsyncDestination.getName());
				sb.append(", ");
			}

			sb.setStringAt("]", sb.index() - 1);

			_log.info(
				"Synchronizecluster index loading for destinations " +
					sb.toString());
		}

		int totalWorkersMaxSize = 0;

		for (BaseAsyncDestination baseAsyncDestination :
			baseAsyncDestinations) {

			totalWorkersMaxSize += baseAsyncDestination.getWorkersMaxSize();
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				"There are " + totalWorkersMaxSize +
					" synchronization threads");
		}

		CountDownLatch countDownLatch = new CountDownLatch(
			totalWorkersMaxSize + 1);

		ClusterLoadingSyncJob slaveClusterLoadingSyncJob =
			new ClusterLoadingSyncJob(companyIds, countDownLatch, false);

		for (BaseAsyncDestination baseAsyncDestination :
			baseAsyncDestinations) {

			ThreadPoolExecutor threadPoolExecutor =
				PortalExecutorManagerUtil.getPortalExecutor(
					baseAsyncDestination.getName());

			for (int i = 0; i < baseAsyncDestination.getWorkersMaxSize(); i++) {
				threadPoolExecutor.execute(slaveClusterLoadingSyncJob);
			}
		}

		ClusterLoadingSyncJob masterClusterLoadingSyncJob =
			new ClusterLoadingSyncJob(companyIds, countDownLatch, true);

		ThreadPoolExecutor threadPoolExecutor =
			PortalExecutorManagerUtil.getPortalExecutor(
				IndexBackgroundTaskExecutor.class.getName());

		threadPoolExecutor.execute(masterClusterLoadingSyncJob);
	}

	private static Log _log = LogFactoryUtil.getLog(
		IndexBackgroundTaskExecutor.class);

	private static MethodKey _loadIndexesFromClusterMethodKey = new MethodKey(
		LuceneClusterUtil.class, "loadIndexesFromCluster", long[].class,
		Address.class);

	private static class ClusterLoadingSyncJob implements Runnable {

		public ClusterLoadingSyncJob(
			long[] companyIds, CountDownLatch countDownLatch, boolean master) {

			_companyIds = companyIds;
			_countDownLatch = countDownLatch;
			_master = master;
		}

		@Override
		public void run() {
			_countDownLatch.countDown();

			String logPrefix = StringPool.BLANK;

			if (_log.isInfoEnabled()) {
				Thread currentThread = Thread.currentThread();

				if (_master) {
					logPrefix =
						"Monitor thread name " + currentThread.getName() +
							" with thread ID " + currentThread.getId();
				}
				else {
					logPrefix =
						"Thread name " + currentThread.getName() +
							" with thread ID " + currentThread.getId();
				}
			}

			if (!_master && _log.isInfoEnabled()) {
				_log.info(
					logPrefix + " synchronized on latch. Waiting for others.");
			}

			try {
				if (_master) {
					_countDownLatch.await();
				}
				else {
					boolean result = _countDownLatch.await(
						PropsValues.LUCENE_CLUSTER_INDEX_LOADING_SYNC_TIMEOUT,
						TimeUnit.MILLISECONDS);

					if (!result) {
						_log.error(
							logPrefix + " timed out. You may need to " +
								"re-trigger a reindex process.");
					}
				}
			}
			catch (InterruptedException ie) {
				if (_master) {
					_log.error(
						logPrefix + " was interrupted. Skip cluster index " +
							"loading notification.",
						ie);

					return;
				}
				else {
					_log.error(
						logPrefix + " was interrupted. You may need to " +
							"re-trigger a reindex process.",
						ie);
				}
			}

			if (_master) {
				Address localClusterNodeAddress =
					ClusterExecutorUtil.getLocalClusterNodeAddress();

				ClusterRequest clusterRequest =
					ClusterRequest.createMulticastRequest(
						new MethodHandler(
							_loadIndexesFromClusterMethodKey, _companyIds,
							localClusterNodeAddress),
						true);

				try {
					ClusterExecutorUtil.execute(clusterRequest);
				}
				catch (SystemException se) {
					_log.error(
						"Unable to notify peers to start index loading", se);
				}

				if (_log.isInfoEnabled()) {
					_log.info(
						logPrefix + " unlocked latch. Notified peers to " +
							"start index loading.");
				}
			}
		}

		private long[] _companyIds;
		private CountDownLatch _countDownLatch;
		private boolean _master;

	}

}
