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

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.data.DataService;
import org.openmrs.mobile.data.QueryOptions;
import org.openmrs.mobile.data.impl.EncounterDataService;
import org.openmrs.mobile.data.impl.LocationDataService;
import org.openmrs.mobile.data.impl.PatientDataService;
import org.openmrs.mobile.data.impl.VisitDataService;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Location;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.Visit;

import static org.openmrs.mobile.utilities.ApplicationConstants.EncounterTypeDisplays.AUDITDATA;

public class AuditDataPresenter extends BasePresenter implements AuditDataContract.Presenter {

	private AuditDataContract.View auditDataView;
	private DataService<Patient> patientDataService;
	private VisitDataService visitDataService;

	private EncounterDataService encounterDataService;
	private LocationDataService locationDataService;

	public AuditDataPresenter(AuditDataContract.View view) {
		this.auditDataView = view;
		this.auditDataView.setPresenter(this);
		this.patientDataService = new PatientDataService();
		this.visitDataService = new VisitDataService();
		this.encounterDataService = new EncounterDataService();
		this.locationDataService = new LocationDataService();
	}

	@Override
	public void subscribe() {
	}

	@Override
	public void fetchVisit(String visitUuid) {
		auditDataView.showPageSpinner(true);
		DataService.GetCallback<Visit> fetchEncountersCallback = new DataService.GetCallback<Visit>() {
			@Override
			public void onCompleted(Visit visit) {
				auditDataView.setVisit(visit);
				auditDataView.showPageSpinner(false);
				for (Encounter encounter : visit.getEncounters()) {
					switch (encounter.getEncounterType().getDisplay()) {
						case AUDITDATA:
							auditDataView.updateSubmitButtonText();
							fetchEncounter(encounter.getUuid());
							break;
					}
				}
			}

			@Override
			public void onError(Throwable t) {
				auditDataView.showPageSpinner(false);
				t.printStackTrace();
			}
		};
		visitDataService.getByUUID(visitUuid, QueryOptions.LOAD_RELATED_OBJECTS, fetchEncountersCallback);
	}

	private void fetchEncounter(String uuid) {
		auditDataView.showPageSpinner(true);
		DataService.GetCallback<Encounter> fetchEncountercallback = new DataService.GetCallback<Encounter>() {
			@Override
			public void onCompleted(Encounter encounter) {
				auditDataView.showPageSpinner(false);
				auditDataView.setEncounterUuid(encounter.getUuid());
				auditDataView.updateFormFields(encounter);
			}

			@Override
			public void onError(Throwable t) {
				auditDataView.showPageSpinner(false);
				t.printStackTrace();
			}
		};
		encounterDataService.getByUUID(uuid, QueryOptions.LOAD_RELATED_OBJECTS, fetchEncountercallback);
	}

	@Override
	public void saveEncounter(Encounter encounter, boolean isNewEncounter) {
		auditDataView.showProgressBar(true);
		DataService.GetCallback<Encounter> serverResponceCallback = new DataService.GetCallback<Encounter>() {
			@Override
			public void onCompleted(Encounter encounter) {
				auditDataView.showProgressBar(false);
				if (encounter == null) {

				} else {
					auditDataView.goBackToVisitPage();
				}
			}

			@Override
			public void onError(Throwable t) {
				auditDataView.showProgressBar(false);
				t.printStackTrace();
			}
		};

		if (isNewEncounter) {
			encounterDataService.create(encounter, serverResponceCallback);
		} else {
			encounterDataService.update(encounter, serverResponceCallback);
		}
	}

	@Override
	public void fetchLocation(String locationUuid) {
		DataService.GetCallback<Location> locationDataServiceCallback = new DataService.GetCallback<Location>() {
			@Override
			public void onCompleted(Location location) {
				//set location in the fragment and start loading other fields
				auditDataView.setLocation(location);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}
		};

		locationDataService.getByUUID(locationUuid, QueryOptions.LOAD_RELATED_OBJECTS, locationDataServiceCallback);
	}

}

