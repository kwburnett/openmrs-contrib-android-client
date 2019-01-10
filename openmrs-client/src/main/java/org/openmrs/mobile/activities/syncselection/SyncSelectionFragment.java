package org.openmrs.mobile.activities.syncselection;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseFragment;
import org.openmrs.mobile.listeners.PatientListSyncSwitchToggle;
import org.openmrs.mobile.models.PatientList;

public class SyncSelectionFragment extends ACBaseFragment<SyncSelectionContract.Presenter>
		implements SyncSelectionContract.View {

	private OnFragmentInteractionListener listener;

	private LinearLayoutManager layoutManager;
	private RecyclerView syncSelectionModelRecyclerView;
	private ProgressBar screenProgressBar;
	private Button advanceButton;

	private SyncSelectionModelRecycleViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_sync_selection, container, false);

		initViewFields(rootView);
		registerListeners();

		return rootView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		adapter = new SyncSelectionModelRecycleViewAdapter(new PatientListSyncSwitchToggle() {

			@Override
			public void toggleSyncSelection(PatientList patientList, boolean isSelected) {
				presenter.toggleSyncSelection(patientList, isSelected);
			}
		});
		syncSelectionModelRecyclerView.setAdapter(adapter);

		syncSelectionModelRecyclerView.setLayoutManager(layoutManager);
		syncSelectionModelRecyclerView.setNestedScrollingEnabled(false);
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

	private void initViewFields(View rootView) {
		screenProgressBar = (ProgressBar) rootView.findViewById(R.id.syncSelectionScreenProgressBar);
		advanceButton = (Button) rootView.findViewById(R.id.moveForwardButton);

		layoutManager = new LinearLayoutManager(context);
		syncSelectionModelRecyclerView = (RecyclerView) rootView.findViewById(R.id.syncSelectionModelRecyclerView);
	}

	private void registerListeners() {
		advanceButton.setOnClickListener(v -> {
			presenter.saveUsersSyncSelections();
		});
	}

	public static SyncSelectionFragment newInstance() {
		return new SyncSelectionFragment();
	}

	public void toggleScreenProgressBar(boolean makeVisible) {
		if (makeVisible) {
			screenProgressBar.setVisibility(View.VISIBLE);
		} else {
			screenProgressBar.setVisibility(View.INVISIBLE);
		}
	}

	public void updateAdvanceButton(boolean isAtLeastOnePatientListSelected) {
		if (isAtLeastOnePatientListSelected) {
			advanceButton.setText(getString(R.string.save_patient_list_sync_selections));
		} else {
			advanceButton.setText(getString(R.string.skip_patient_list_sync_selections));
		}
	}

	public void displayPatientLists(List<PatientList> patientLists) {
		adapter.setItems(patientLists);
	}

	public void syncSelectionSaveComplete(boolean skipSyncing) {
		if (listener != null) {
			listener.syncSelectionComplete(skipSyncing);
		}
	}

	public interface OnFragmentInteractionListener {

		void syncSelectionComplete(boolean syncSelectionNeeded);
	}
}
