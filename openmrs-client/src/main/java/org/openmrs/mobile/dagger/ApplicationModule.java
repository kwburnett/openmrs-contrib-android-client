package org.openmrs.mobile.dagger;

import javax.inject.Singleton;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.net.AuthorizationManager;
import org.openmrs.mobile.sync.SyncManager;

@Module
public class ApplicationModule {
	protected OpenMRS openMRS;
	protected ReceiverComponent receiverComponent;
	protected SyncComponent syncComponent;

	public ApplicationModule(OpenMRS openMRS) {
		this.openMRS = openMRS;
		this.receiverComponent = DaggerReceiverComponent.create();
		this.syncComponent = DaggerSyncComponent.create();
	}

	@Provides
	@Singleton
	public Context providesContext() {
		return providesOpenMRS();
	}

	@Provides
	@Singleton
	public OpenMRS providesOpenMRS() {
		return openMRS;
	}

	@Provides
	@Singleton
	public SyncManager provideSyncManager() {
		return new SyncManager(providesOpenMRS(), receiverComponent.syncReceiver(), syncComponent.syncService());
	}

	@Provides
	@Singleton
	public AuthorizationManager provideAuthorizationManager() {
		return new AuthorizationManager(openMRS);
	}
}
