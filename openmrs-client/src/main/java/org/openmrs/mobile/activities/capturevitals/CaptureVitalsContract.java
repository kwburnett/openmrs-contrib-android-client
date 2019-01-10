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

package org.openmrs.mobile.activities.capturevitals;

import android.content.Context;

import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Location;

public interface CaptureVitalsContract {

	interface View extends BaseView<Presenter> {

		void showErrorView(android.view.View... view);

		void hideErrorView(android.view.View... view);

		void setLocation(Location location);

		Context getContext();

		void disableButton();

		void showProgressBar(Boolean visibility);

		void showPageSpinner(boolean visibility);

		void hideSoftKeys();

		void captureVitalsSaveComplete();
	}

	interface Presenter extends BasePresenterContract {

		void fetchLocation(String locationUuid);

		void attemptSave(Encounter encounter);
	}

}