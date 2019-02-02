package org.openmrs.mobile.dagger;

import okhttp3.OkHttpClient;
import org.openmrs.mobile.application.CrashlyticsLogger;
import org.openmrs.mobile.application.Logger;
import org.openmrs.mobile.data.db.Repository;
import org.openmrs.mobile.data.db.impl.RepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {
	@Provides
	@Singleton
	public Repository providesRepository() {
		return new RepositoryImpl();
	}

	@Provides
	@Singleton
	public OkHttpClient provideOkHttpClient() {
		return new OkHttpClient();
	}

	@Provides
	@Singleton
	public Logger provideLogger() {
		return new CrashlyticsLogger();
	}
}
