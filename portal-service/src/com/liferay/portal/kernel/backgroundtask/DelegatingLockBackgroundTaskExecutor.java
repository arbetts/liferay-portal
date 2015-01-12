package com.liferay.portal.kernel.backgroundtask;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.Lock;
import com.liferay.portal.service.LockLocalServiceUtil;

/**
 * Created by liferay on 1/12/2015.
 */
public class DelegatingLockBackgroundTaskExecutor
	extends DelegatingBackgroundTaskExecutor {

	public DelegatingLockBackgroundTaskExecutor(
		BackgroundTaskExecutor backgroundTaskExecutor) {

		super(backgroundTaskExecutor);
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {
		Lock lock = null;

		String owner =
			backgroundTask.getName() + StringPool.POUND +
				backgroundTask.getBackgroundTaskId();

		try {
			if (isLockable()) {
				lock = acquireLock(backgroundTask, owner);

				if (!lock.isNew()) {
					return handleDuplicateLock(lock);
				}
			}

			BackgroundTaskExecutor backgroundTaskExecutor =
				getBackgroundTaskExecutor();

			return backgroundTaskExecutor.execute(backgroundTask);
		}
		finally {
			if (lock != null) {
				LockLocalServiceUtil.unlock(
					BackgroundTaskExecutor.class.getName(),
					backgroundTask.getTaskExecutorClassName(), owner);
			}
		}
	}

	protected BackgroundTaskResult handleDuplicateLock(Lock lock)
		throws DuplicateLockException{

		throw new DuplicateLockException(lock);
	}

	protected boolean isLockable() {
		return true;
	}

	protected Lock acquireLock(BackgroundTask backgroundTask, String owner)
		throws DuplicateLockException {

		Lock lock = null;

		while (true) {
			try {
				lock = LockLocalServiceUtil.lock(
					BackgroundTaskExecutor.class.getName(),
					backgroundTask.getTaskExecutorClassName(), owner);

				break;
			}
			catch (SystemException se) {
				if (_log.isDebugEnabled()) {
					_log.debug("Unable to acquire acquiring lock", se);
				}

				try {
					Thread.sleep(50);
				}
				catch (InterruptedException ie) {
					if (_log.isDebugEnabled()) {
						_log.debug(ie, ie);
					}
				}
			}
		}

		return lock;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DelegatingLockBackgroundTaskExecutor.class);

}
