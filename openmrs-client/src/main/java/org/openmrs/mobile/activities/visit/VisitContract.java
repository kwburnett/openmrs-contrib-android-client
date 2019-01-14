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

package org.openmrs.mobile.activities.visit;

import android.widget.TextView;

import org.openmrs.mobile.activities.IBaseDiagnosisView;
import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.event.VisitDashboardDataRefreshEvent;
import org.openmrs.mobile.models.Concept;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.models.VisitAttributeType;
import org.openmrs.mobile.models.VisitPhoto;
import org.openmrs.mobile.models.VisitPredefinedTask;
import org.openmrs.mobile.models.VisitTask;
import org.openmrs.mobile.utilities.ToastUtil;

import java.util.List;

public interface VisitContract {

	interface View extends BaseView<Presenter> {
		void getVisitCompleted(Visit visit);
	}

	interface Presenter extends BasePresenterContract {
		void getVisit(String visitUuid);
	}

	interface VisitDashboardPage {

		interface View extends BaseView<Presenter> {

			void displayRefreshingData(boolean visible);

			void onVisitDashboardRefreshEvent(VisitDashboardDataRefreshEvent event);
		}

		interface Presenter extends BasePresenterContract {

			void dataRefreshWasRequested();

			void dataRefreshEventOccurred(VisitDashboardDataRefreshEvent event);
		}
	}

	interface VisitTasks {

		interface View extends VisitDashboardPage.View {

			void setOpenVisitTasks(List<VisitTask> visitTaskList);

			void setClosedVisitTasks(List<VisitTask> visitTaskList);

			void setPredefinedTasks(List<VisitPredefinedTask> predefinedTasks);

			void setSelectedVisitTask(VisitTask visitTask);

			void setUnSelectedVisitTask(VisitTask visitTask);

			void setVisit(Visit visit);

			void clearTextField();

			void showTabSpinner(boolean visibility);
		}

		interface Presenter extends VisitDashboardPage.Presenter {

			void addVisitTasks(VisitTask visitTasks);

			void updateVisitTask(VisitTask visitTask);

			void createVisitTasksObject(String visitTask);
		}
	}

	interface VisitDetails {

		interface View extends VisitDashboardPage.View, IBaseDiagnosisView {

			void setVisit(Visit visit);

			void setPatientUUID(String uuid);

			void setVisitUUID(String uuid);

			void setConcept(Concept concept);

			void setAttributeTypes(List<VisitAttributeType> visitAttributeTypes);

			void showTabSpinner(boolean visibility);
		}

		interface Presenter extends VisitDashboardPage.Presenter {

			void getVisit();

			void getPatientUUID();

			void getVisitUUID();

			void getConceptAnswer(String uuid, String searchValue, TextView textView);
		}
	}

	interface VisitPhotos {

		interface View extends VisitDashboardPage.View {

			void updateVisitImageMetadata(List<VisitPhoto> visitPhotos);

			void viewImage(String photoUuidToView, List<String> visitPhotoUuids);

			void deleteImage(VisitPhoto visitPhoto);

			void reset();

			void refresh();

			String formatVisitImageDescription(String description, String uploadedOn, String uploadedBy);

			void showTabSpinner(boolean visibility);
		}

		interface Presenter extends VisitDashboardPage.Presenter {
			boolean isLoading();

			void refreshPhotosWhenVisible();

			void setLoading(boolean loading);

			void uploadPhoto(byte[] visitPhoto, String description);

			void deletePhoto(VisitPhoto visitPhoto);
		}
	}
}
