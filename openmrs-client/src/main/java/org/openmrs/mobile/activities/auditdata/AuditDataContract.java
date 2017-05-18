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

package org.openmrs.mobile.activities.auditdata;

import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Obs;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.Visit;

public interface AuditDataContract {

	interface View extends BaseView<Presenter> {
		void updateContactCard(Patient patient);

		void setVisit(Visit visit);

		void updateForm(Encounter encounter);

		void setEncounter(Encounter encounter);
	}

	interface Presenter extends BasePresenterContract {

		void fetchPatientDetails(String patientId);

		void fetchVisit(String patientId);

		void fetchEncounterObservations(Encounter encounter);

		void createEncounter(Encounter encounter);
	}

}