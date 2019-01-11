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

package org.openmrs.mobile.activities.patientdashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.BaseDiagnosisFragment;
import org.openmrs.mobile.activities.IBaseDiagnosisView;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Location;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.FontsUtil;
import org.openmrs.mobile.utilities.StringUtils;
import org.openmrs.mobile.utilities.ToastUtil;

import java.util.LinkedList;

public class PatientDashboardFragment extends BaseDiagnosisFragment<PatientDashboardContract.Presenter>
		implements PatientDashboardContract.View, PatientVisitsRecyclerAdapter.OnAdapterInteractionListener {

	private OnFragmentInteractionListener listener;
	public static final int START_VISIT = 1;
	public static final int EDIT_PATIENT = 2;

	private FloatingActionButton startVisitButton, editPatient;
	private Patient patient;
	private Location location;
	private RelativeLayout dashboardScreen, noPatientDataLayout;
	private ProgressBar dashboardProgressBar;
	private TextView noVisitNoteLabel, noPatientDataLabel;
	private String patientUuid;
	private PatientVisitsRecyclerAdapter patientVisitsRecyclerAdapter;
	private FloatingActionMenu patientDashboardMenu;
	private RecyclerView patientVisitsRecyclerView;
	private SwipeRefreshLayout patientVisitsSwipeRefreshView;
	private RecyclerView.OnScrollListener patientVisitsOnScrollListener;

	private LinkedList<Visit> patientVisits = new LinkedList<>();

	public static PatientDashboardFragment newInstance() {
		return new PatientDashboardFragment();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			listener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		patientVisitsRecyclerView.removeOnScrollListener(patientVisitsOnScrollListener);
		if (patientVisitsRecyclerAdapter != null) {
			patientVisitsRecyclerAdapter.destroy();
		}
	}

	@Override
	public void patientNotAvailable() {
		if (listener != null) {
			listener.patientNotAvailable();
		}
	}

	@Override
	public void alertOfflineAndPatientNotFound() {
		ToastUtil.notify(getString(R.string.no_network_and_no_patient_data_in_database));
	}

	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View fragmentView = inflater.inflate(R.layout.fragment_patient_dashboard, container, false);

		initializeViewFields(fragmentView);
		initializeListeners(startVisitButton, editPatient);

		//set start index incase it's cached somewhere
		presenter.fetchPatientData();
		FontsUtil.setFont(context.findViewById(android.R.id.content));

		return fragmentView;
	}

	private void initializeListeners(FloatingActionButton... params) {
		for (FloatingActionButton patientActionButtons : params) {
			patientActionButtons.setOnClickListener(
					view -> floatingActionButtonSelected(patientActionButtons.getId()));
		}

		patientVisitsOnScrollListener = new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				//Contact address header
				View patientContactInfo = recyclerView.findViewById(R.id.container_patient_address_info);
				if (listener != null) {
					listener.patientContactInformationPresent(patientContactInfo != null);
				}
			}
		};

		patientVisitsRecyclerView.addOnScrollListener(patientVisitsOnScrollListener);

		patientVisitsSwipeRefreshView.setOnRefreshListener(() -> presenter.dataRefreshWasRequested());
	}

	private void floatingActionButtonSelected(int selectedId) {
		patientDashboardMenu.close(true);
		if (listener != null) {
			switch (selectedId) {
				case R.id.start_visit:
					listener.onPatientActionSelected(START_VISIT, patientUuid);
					break;
				case R.id.edit_Patient:
					listener.onPatientActionSelected(EDIT_PATIENT, patientUuid);
					break;
			}
		}
	}

	@Override
	public void initializeViewFields(View fragmentView) {
		startVisitButton = (FloatingActionButton)fragmentView.findViewById(R.id.start_visit);
		editPatient = (FloatingActionButton)fragmentView.findViewById(R.id.edit_Patient);
		dashboardScreen = (RelativeLayout)fragmentView.findViewById(R.id.dashboardScreen);
		dashboardProgressBar = (ProgressBar)fragmentView.findViewById(R.id.dashboardProgressBar);
		noVisitNoteLabel = (TextView)fragmentView.findViewById(R.id.noVisitNoteLabel);
		patientDashboardMenu = (FloatingActionMenu)fragmentView.findViewById(R.id.patientDashboardMenu);
		patientDashboardMenu.setClosedOnTouchOutside(true);
		patientVisitsRecyclerView = (RecyclerView)fragmentView.findViewById(R.id.patientVisitsRecyclerView);
		noPatientDataLabel = (TextView)fragmentView.findViewById(R.id.noPatientDataLabel);
		noPatientDataLayout = (RelativeLayout)fragmentView.findViewById(R.id.noPatientDataLayout);
		patientVisitsSwipeRefreshView = (SwipeRefreshLayout)fragmentView.findViewById(R.id.patientVisitsSwipeRefreshView);
	}

	@Override
	public void patientContacts(Patient patient) {
		this.patient = patient;
		patientUuid = patient.getUuid();
		OpenMRS.getInstance().setPatientUuid(patientUuid);
	}

	@Override
	public void notifyAllPatientVisitsFetched() {
		patientVisitsRecyclerAdapter.setFullDataSetHasBeenLoaded();
	}

	@Override
	public void addPatientVisits(LinkedList<Visit> visits) {

		// This should always be null (added for loading purposes) when this method is called, but adding in case it's not
		// for some reason
		if (patientVisits.getLast() == null) {
			patientVisits.remove(patientVisits.size() - 1);
		}
		patientVisits.addAll(visits);
		patientVisitsRecyclerAdapter.notifyDataSetChanged();
		patientVisitsRecyclerAdapter.setLoaded();
	}

	@Override
	public void setPatientVisits(LinkedList<Visit> visits) {

		patientVisits = visits;

		//hasActiveVisit = false;
		for (Visit visit : patientVisits) {
			if (visit.getStopDatetime() == null) {
				//hasActiveVisit = true;
				startVisitButton.setVisibility(View.GONE);
				OpenMRS.getInstance().setVisitUuid(visit.getUuid());
				break;
			}
		}

		if (patientVisitsRecyclerAdapter != null) {
			patientVisitsRecyclerAdapter.destroy();
		}

		if (context != null) {
			patientVisitsRecyclerAdapter =
					new PatientVisitsRecyclerAdapter(patientVisitsRecyclerView, patientVisits, context,
							this, this, patient);
			patientVisitsRecyclerAdapter.setOnLoadMoreListener(() -> {
				// Add a null for loading indicator
				patientVisits.add(null);
				patientVisitsRecyclerAdapter.notifyItemInserted(patientVisits.size() - 1);
				presenter.loadResults();
			});
			patientVisitsRecyclerView.setAdapter(patientVisitsRecyclerAdapter);
		}
	}

	@Override
	public void displayRefreshingData(boolean visible) {
		patientVisitsSwipeRefreshView.setRefreshing(visible);
	}

	@Override
	public void setProviderUuid(String providerUuid) {
		if (StringUtils.isBlank(providerUuid))
			return;
		SharedPreferences.Editor editor = OpenMRS.getInstance().getPreferences().edit();
		editor.putString(ApplicationConstants.BundleKeys.PROVIDER_UUID_BUNDLE, providerUuid);
		editor.commit();
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void showSavingClinicalNoteProgressBar(boolean show) {
		patientVisitsRecyclerAdapter.updateSavingClinicalNoteProgressBar(show);
	}

	@Override
	public void showPageSpinner(boolean visibility) {
		if (visibility) {
			dashboardProgressBar.setVisibility(View.VISIBLE);
			dashboardScreen.setVisibility(View.GONE);
		} else {
			dashboardProgressBar.setVisibility(View.GONE);
			dashboardScreen.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void showNoVisits(boolean visibility) {
		if (visibility) {
			noVisitNoteLabel.setVisibility(View.VISIBLE);
		} else {
			noVisitNoteLabel.setVisibility(View.GONE);
		}
	}

	@Override
	public void updateClinicVisitNote(Observation observation, Encounter encounter) {
		patientVisitsRecyclerAdapter.updateClinicalNoteObs(observation, encounter);
	}

	@Override
	public void showNoPatientData(boolean visible) {
		noPatientDataLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
		dashboardProgressBar.setVisibility(visible ? View.GONE : View.VISIBLE);
		dashboardScreen.setVisibility(visible ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		patientUuid = ApplicationConstants.EMPTY_STRING;
		OpenMRS.getInstance().setPatientUuid(patientUuid);
	}

	@Override
	public IBaseDiagnosisView getDiagnosisView() {
		return this;
	}

	@Override
	public IBaseDiagnosisView getBaseDiagnosisView() {
		return this;
	}

	@Override
	public void showTabSpinner(boolean show) {
		showSavingClinicalNoteProgressBar(show);
	}

	@Override
	public void setLoading(boolean loading) {
		if (listener != null) {
			listener.fragmentProcessing(loading);
		}
	}

	@Override
	public void onVisitSelected(String visitUuid) {
		if (listener != null) {
			listener.onVisitSelected(patientUuid, visitUuid);
		}
	}

	public interface OnFragmentInteractionListener {

		void onPatientActionSelected(int action, String patientUuid);

		void fragmentProcessing(boolean isLoading);

		void patientContactInformationPresent(boolean isPatientContactInformationPresent);

		void patientNotAvailable();

		void onVisitSelected(String patientUuid, String visitUuid);
	}
}
