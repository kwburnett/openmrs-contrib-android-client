package org.openmrs.mobile.dagger;

import okhttp3.OkHttpClient;
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
	public OkHttpClient provideOkHttpClient() {
		return new OkHttpClient();
	}
}
