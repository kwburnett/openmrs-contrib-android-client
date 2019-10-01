/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.application;

import android.util.Log;
import com.crashlytics.android.Crashlytics;

public class CrashlyticsLogger implements Logger {

	private static final String TAG = "Banda Health";

	@Override
	public void v(String msg) {
		v(TAG, msg);
	}

	@Override
	public void v(String tag, String msg) {
		try {
			Crashlytics.log(Log.VERBOSE, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void v(String msg, Throwable throwable) {
		v(TAG, msg, throwable);
	}

	@Override
	public void v(String tag, String msg, Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
			Crashlytics.log(Log.VERBOSE, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void d(String msg) {
		d(TAG, msg);
	}

	@Override
	public void d(String tag, String msg) {
		try {
			Crashlytics.log(Log.DEBUG, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void d(String msg, Throwable throwable) {
		d(TAG, msg, throwable);
	}

	@Override
	public void d(String tag, String msg, Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
			Crashlytics.log(Log.DEBUG, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void i(String msg) {
		i(TAG, msg);
	}

	@Override
	public void i(String tag, String msg) {
		try {
			Crashlytics.log(Log.INFO, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void i(String msg, Throwable throwable) {
		i(TAG, msg, throwable);
	}

	@Override
	public void i(String tag, String msg, Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
			Crashlytics.log(Log.INFO, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void w(String msg) {
		w(TAG, msg);
	}

	@Override
	public void w(String tag, String msg) {
		try {
			Crashlytics.log(Log.WARN, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void w(String msg, Throwable throwable) {
		w(TAG, msg, throwable);
	}

	@Override
	public void w(String tag, String msg, Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
			Crashlytics.log(Log.WARN, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void e(String msg) {
		e(TAG, msg);
	}

	@Override
	public void e(String tag, String msg) {
		try {
			Crashlytics.log(Log.ERROR, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void e(String msg, Throwable throwable) {
		e(TAG, msg, throwable);
	}

	@Override
	public void e(String tag, String msg, Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
			Crashlytics.log(Log.ERROR, tag, msg);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void e(Throwable throwable) {
		try {
			Crashlytics.logException(throwable);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}

	@Override
	public void setUser(String user) {
		try {
			Crashlytics.setUserIdentifier(user);
		} catch(IllegalStateException ex) {
			// crashlytics not initialized.
		}
	}
}
