package com.liferay.portal.kernel.backgroundtask;

import com.liferay.portal.DuplicateLockException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.model.Lock;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portal.service.LockLocalServiceUtil;

/**
 * Created by liferay on 1/12/2015.
 */
public class SingletonBackgroundTaskExecutor
	extends DelegatingLockBackgroundTaskExecutor {

	public SingletonBackgroundTaskExecutor(
		BackgroundTaskExecutor backgroundTaskExecutor) {

		super(backgroundTaskExecutor);
	}

	protected BackgroundTaskResult handleDuplicateLock(Lock lock)
		throws DuplicateLockException{

		return new BackgroundTaskResult(
			BackgroundTaskConstants.STATUS_FAILED,
			"Singleton background task already in progress");
	}

	protected boolean isLockable() {
		return isSingleton();
	}

}
