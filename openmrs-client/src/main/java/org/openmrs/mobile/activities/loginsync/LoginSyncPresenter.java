package org.openmrs.mobile.activities.loginsync;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.os.SystemClock;

import androidx.annotation.Nullable;

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.event.SyncEvent;
import org.openmrs.mobile.event.SyncPullEvent;
import org.openmrs.mobile.event.SyncPushEvent;
import org.openmrs.mobile.sync.SyncManager;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.TimeConstants;

public class LoginSyncPresenter extends BasePresenter implements LoginSyncContract.Presenter {

	private LoginSyncContract.View view;
	private OpenMRS openMRS;
	private SyncManager syncManager;

	private double totalSyncPushes;
	private double totalSyncPulls;
	private double entitiesPushed;
	private double entitiesPulled;
	private String currentDownloadingSubscription;
	private ScheduledExecutorService networkConnectivityCheckScheduler;
	private ScheduledFuture<?> networkConnectivityCheckScheduledTask;
	private @Nullable
	Boolean networkConnectionIsFast;
	private boolean arePushing = false;
	private boolean arePulling = false;
	private boolean trimHasBeenCompleted = false;
	private Runnable measureConnectivityTask;

	private final int DELAY = 500;
	private long dataSyncStartTime = 0;

	/**
	 * Conservative estimate of patient information (including context) based on tests with several patients and the
	 * size of all pertinent information
	 */
	private final double AVERAGE_SIZE_OF_PATIENT_PAYLOAD_IN_KB = 80;

	/**
	 * This could be calculated to give better estimates, but is picked ad hoc as of now
	 */
	private final double AVERAGE_NUMBER_OF_PATIENTS_TO_SYNC = 10;

	public LoginSyncPresenter(LoginSyncContract.View view, OpenMRS openMRS, SyncManager syncManager) {
		this.view = view;
		this.openMRS = openMRS;
		this.syncManager = syncManager;

		this.view.setPresenter(this);
		entitiesPulled = 0;
		entitiesPushed = 0;
	}

	@Override
	public void sync() {
		syncManager.requestInitialSync();
	}

	@Override
	public void subscribe() {
		//intentionally left blank
	}

	public void onSyncPushEvent(SyncPushEvent syncPushEvent) {
		arePushing = true;
		double progress = 0, previousProgress = 0;
		double previousEntitiesPushed = 0;

		// First check for information to determine of progress should be updated
		switch (syncPushEvent.getMessage()) {
			case ApplicationConstants.EventMessages.Sync.Push.TOTAL_RECORDS:
				totalSyncPushes = syncPushEvent.getTotalItems();
				break;
			case ApplicationConstants.EventMessages.Sync.Push.RECORD_REMOTE_PUSH_COMPLETE:
				entitiesPushed++;
				break;
		}

		if (entitiesPushed > 0) {
			previousEntitiesPushed = entitiesPushed - 1;
			previousProgress = (entitiesPushed - 1) / totalSyncPushes;
		}
		if (totalSyncPushes == entitiesPushed) {
			progress = 100;
			entitiesPushed = 0;
			arePushing = false;
		} else {
			progress = entitiesPushed / totalSyncPushes * 100D;
		}

		switch (syncPushEvent.getMessage()) {
			case ApplicationConstants.EventMessages.Sync.Push.RECORD_REMOTE_PUSH_STARTING:
				view.updateSyncPushProgressForStartingRecord(progress, (int) entitiesPushed, (int) totalSyncPushes);
				break;
			case ApplicationConstants.EventMessages.Sync.Push.RECORD_REMOTE_PUSH_COMPLETE:
				view.updateSyncPushProgressForCompletingRecord(previousProgress, (int) previousEntitiesPushed,
						(int) totalSyncPushes);
				break;
		}

		if (progress == 100) {
			view.notifySyncPushComplete();
		}
	}

	public void onSyncPullEvent(SyncPullEvent syncPullEvent) {
		arePulling = true;
		double progress;
		double trimSquenceProgressAdjustment = 1;

		// First check for information to determine of progress should be updated
		switch (syncPullEvent.getMessage()) {
			case ApplicationConstants.EventMessages.Sync.Pull.TOTAL_SUBSCRIPTIONS:
				totalSyncPulls = syncPullEvent.getTotalItems();
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.SUBSCRIPTION_REMOTE_PULL_COMPLETE:
				entitiesPulled++;
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.TRIM_COMPLETE:
				trimHasBeenCompleted = true;
				break;
		}

		if (totalSyncPulls == entitiesPulled && trimHasBeenCompleted) {
			progress = 100;
			entitiesPulled = 0;
			trimHasBeenCompleted = false;
			arePulling = false;
		} else {
			progress = entitiesPulled / (totalSyncPulls + trimSquenceProgressAdjustment) * 100D;
		}

		switch (syncPullEvent.getMessage()) {
			case ApplicationConstants.EventMessages.Sync.Pull.SUBSCRIPTION_REMOTE_PULL_STARTING:
				view.updateSyncPullProgressForStartingSubscription(progress, syncPullEvent.getEntity());
				currentDownloadingSubscription = syncPullEvent.getEntity();
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.SUBSCRIPTION_REMOTE_PULL_COMPLETE:
				view.updateSyncPullProgressForCompletingSubscription(progress, syncPullEvent.getEntity());
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.ENTITY_REMOTE_PULL_STARTING:
				if (syncPullEvent.getEntity() != null) {
					view.updateSyncPullProgressForStartingEntity(progress, currentDownloadingSubscription,
							syncPullEvent.getEntity());
				}
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.ENTITY_REMOTE_PULL_COMPLETE:
				if (syncPullEvent.getEntity() != null) {
					view.updateSyncPullProgressForCompletingEntity(progress, currentDownloadingSubscription,
							syncPullEvent.getEntity());
				}
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.TRIM_STARTING:
				view.updateSyncPullProgressForStartingTrim(progress);
				break;
			case ApplicationConstants.EventMessages.Sync.Pull.TRIM_COMPLETE:
				view.updateSyncPullProgressForCompletingTrim(progress);
				break;
		}

		if (progress == 100) {
			view.notifySyncPullComplete();
			navigateToNextActivity();
		}
	}

	private void navigateToNextActivity() {
		view.navigateToNextActivity();
	}

	public void onSyncEvent(SyncEvent syncEvent) {
		if (syncEvent.getMessage().equals(ApplicationConstants.EventMessages.Sync.CANT_SYNC_NO_NETWORK)) {
			view.notifyConnectionLost();
			navigateToNextActivity();
		}
	}

	public void startMeasuringConnectivity() {
		networkConnectionIsFast = null;

		measureConnectivityTask = () -> {
			Boolean previousConnectionSpeedIsFast = networkConnectionIsFast;
			determineNetworkConnectionSpeed();
			if (previousConnectionSpeedIsFast == null
					|| (previousConnectionSpeedIsFast && !networkConnectionIsFast)
					|| (!previousConnectionSpeedIsFast && networkConnectionIsFast)) {
				view.runOnUIThread(() -> notifyViewToUpdateConnectionDisplay());
			}
		};
		if (networkConnectivityCheckScheduler == null) {
			networkConnectivityCheckScheduler = Executors.newScheduledThreadPool(1);
		}
		dataSyncStartTime = SystemClock.elapsedRealtime();
		try {
			if (networkConnectivityCheckScheduledTask != null) {
				networkConnectivityCheckScheduledTask.cancel(true);
			}
			networkConnectivityCheckScheduledTask = networkConnectivityCheckScheduler
					.schedule(measureConnectivityTask, DELAY, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			openMRS.getLogger().e(e.getMessage(), e);
		}
	}

	private void determineNetworkConnectionSpeed() {
		try {
			Double averageNetworkSpeed = openMRS.getNetworkUtils().getCurrentConnectionSpeed();
			if (averageNetworkSpeed == null) {
				networkConnectionIsFast = false;
				return;
			}

			double estimatedTimeUntilDownloadCompletes = AVERAGE_NUMBER_OF_PATIENTS_TO_SYNC
					* AVERAGE_SIZE_OF_PATIENT_PAYLOAD_IN_KB
					/ averageNetworkSpeed;
			if (estimatedTimeUntilDownloadCompletes < TimeConstants.SECONDS_PER_MINUTE) {
				long dataSyncDuration = SystemClock.elapsedRealtime() - dataSyncStartTime;
				// If the sync is taking over a minute, it's obviously not a fast connection...
				if (dataSyncDuration > TimeConstants.MILLIS_PER_MINUTE) {
					stopMeasuringConnectivity();
					networkConnectionIsFast = false;
				} else {
					networkConnectionIsFast = true;
				}
			} else {
				/**
				 * If the network speed is fluctuating enough to have the message go from "fast" to "slow", stop the timer
				 * and just keep the message as "slow" to be safe. I'm assuming people like to see things will speed up, not
				 * that they're slowing down.
				 */

				if (networkConnectionIsFast != null && networkConnectionIsFast) {
					stopMeasuringConnectivity();
				}
				networkConnectionIsFast = false;
			}
		} catch (Exception e) {
			logger.e(e);
		}
	}

	private void notifyViewToUpdateConnectionDisplay() {
		if (arePushing) {
			if (networkConnectionIsFast != null && networkConnectionIsFast) {
				view.notifySyncPushConnectionIsFast();
			} else {
				view.notifySyncPushConnectionIsSlow();
			}
		}
		if (arePulling) {
			if (networkConnectionIsFast != null && networkConnectionIsFast) {
				view.notifySyncPullConnectionIsFast();
			} else {
				view.notifySyncPullConnectionIsSlow();
			}
		}
	}

	public void stopMeasuringConnectivity() {
		if (networkConnectivityCheckScheduledTask != null && !networkConnectivityCheckScheduledTask.isCancelled()) {
			networkConnectivityCheckScheduledTask.cancel(true);
		}
	}
}
