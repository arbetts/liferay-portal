/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.tools;

import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.util.InitUtil;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Preston Crary
 */
public class DBUpgraderUtil {

	public static DBUpgrader getDbUpgrader() {
		return _dbUpgrader;
	}

	public static void main(String[] args) {
		try {
			StopWatch stopWatch = new StopWatch();

			stopWatch.start();

			InitUtil.initWithSpring(true);

			upgrade();
			verify();

			System.out.println(
				"\nCompleted upgrade and verify processes in " +
					(stopWatch.getTime() / Time.SECOND) + " seconds");

			System.exit(0);
		}
		catch (Exception e) {
			e.printStackTrace();

			System.exit(1);
		}
	}

	public static void upgrade() throws Exception {
		_dbUpgrader.upgrade();
	}

	public static void verify() throws Exception {
		_dbUpgrader.verify();
	}

	public void setDbUpgrader(DBUpgrader dbUpgrader) {
		_dbUpgrader = dbUpgrader;
	}

	private static DBUpgrader _dbUpgrader;

}