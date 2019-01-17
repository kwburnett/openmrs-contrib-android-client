package org.openmrs.mobile.activities.loginsync;

import android.content.Intent;
import android.os.Bundle;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.patientlist.PatientListActivity;
import org.openmrs.mobile.event.SyncEvent;
import org.openmrs.mobile.event.SyncPullEvent;
import org.openmrs.mobile.event.SyncPushEvent;

public class LoginSyncActivity extends ACBaseActivity implements LoginSyncFragment.OnFragmentInteractionListener {

	public LoginSyncContract.Presenter presenter;

	private EventBus eventBus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		eventBus = openMRS.getEventBus();

		setContentView(R.layout.activity_login_sync);

		// Create fragment
		LoginSyncFragment loginSyncFragment =
				(LoginSyncFragment) fragmentManager.findFragmentById(R.id.loginSyncContentFrame);
		if (loginSyncFragment == null) {
			loginSyncFragment = LoginSyncFragment.newInstance();
		}
		if (!loginSyncFragment.isActive()) {
			addFragmentToActivity(fragmentManager, loginSyncFragment, R.id.loginSyncContentFrame);
		}

		presenter = new LoginSyncPresenter(loginSyncFragment, openMRS, openMRS.getSyncManager());
	}

	@Override
	protected void onStart() {
		super.onStart();
		eventBus.register(this);
		presenter.sync();
	}

	@Override
	protected void onStop() {
		eventBus.unregister(this);
		super.onStop();
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSyncPushEvent(SyncPushEvent syncPushEvent) {
		presenter.onSyncPushEvent(syncPushEvent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSyncPullEvent(SyncPullEvent syncPullEvent) {
		presenter.onSyncPullEvent(syncPullEvent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onSyncEvent(SyncEvent syncEvent) {
		presenter.onSyncEvent(syncEvent);
	}

	@Override
	public void syncIsFinished() {
		Intent intent = new Intent(this, PatientListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
		finish();
	}
}
