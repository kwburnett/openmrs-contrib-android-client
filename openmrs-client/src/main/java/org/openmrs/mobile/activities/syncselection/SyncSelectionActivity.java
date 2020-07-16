package org.openmrs.mobile.activities.syncselection;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.loginsync.LoginSyncActivity;
import org.openmrs.mobile.activities.patientlist.PatientListActivity;

public class SyncSelectionActivity extends ACBaseActivity implements SyncSelectionFragment.OnFragmentInteractionListener {

	public SyncSelectionContract.Presenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_sync_selection, frameLayout);
		setTitle(R.string.title_select_patient_lists_to_sync);

		disableActionBarNavigation();

		// Create fragment
		SyncSelectionFragment syncSelectionFragment =
				(SyncSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.syncSelectionContentFrame);
		if (syncSelectionFragment == null) {
			syncSelectionFragment = SyncSelectionFragment.newInstance();
		}
		if (!syncSelectionFragment.isActive()) {
			addFragmentToActivity(getSupportFragmentManager(),
					syncSelectionFragment, R.id.syncSelectionContentFrame);
		}

		presenter = new SyncSelectionPresenter(syncSelectionFragment);
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
	}

	@Override
	public void syncSelectionComplete(boolean syncSelectionNeeded) {
		Intent intent;
		if (syncSelectionNeeded) {
			intent = new Intent(this, PatientListActivity.class);
		} else {
			intent = new Intent(this, LoginSyncActivity.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}
}
