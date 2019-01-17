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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import android.util.Log;
import com.crashlytics.android.Crashlytics;

public class BandaHealthLogger implements Logger {

	private static final String TAG = "Banda Health";

	private static BandaHealthLogger logger = null;

	public BandaHealthLogger() {
		logger = this;
	}

	@Override
	public void v(final String msg) {
		Crashlytics.log(Log.VERBOSE, TAG, msg);
	}

	@Override
	public void v(final String msg, Throwable throwable) {
		Crashlytics.logException(throwable);
		Crashlytics.log(Log.VERBOSE, TAG, msg);
	}

	@Override
	public void d(final String msg) {
		Crashlytics.log(Log.DEBUG, TAG, msg);
	}

	@Override
	public void d(final String msg, Throwable throwable) {
		Crashlytics.logException(throwable);
		Crashlytics.log(Log.DEBUG, TAG, msg);
	}

	@Override
	public void i(final String msg) {
		Crashlytics.log(Log.INFO, TAG, msg);
	}

	@Override
	public void i(final String msg, Throwable throwable) {
		Crashlytics.logException(throwable);
		Crashlytics.log(Log.INFO, TAG, msg);
	}

	@Override
	public void w(final String msg) {
		Crashlytics.log(Log.WARN, TAG, msg);
	}

	@Override
	public void w(final String msg, Throwable throwable) {
		Crashlytics.logException(throwable);
		Crashlytics.log(Log.WARN, TAG, msg);
	}

	@Override
	public void e(final String msg) {
		Crashlytics.log(Log.ERROR, TAG, msg);
	}

	@Override
	public void e(final String msg, Throwable throwable) {
		Crashlytics.logException(throwable);
		Crashlytics.log(Log.ERROR, TAG, msg);
	}

	@Override
	public void e(Throwable throwable) {
		Crashlytics.logException(throwable);
	}

	@Override
	public void setUser(String user) {
		Crashlytics.setUserIdentifier(user);
	}
}
