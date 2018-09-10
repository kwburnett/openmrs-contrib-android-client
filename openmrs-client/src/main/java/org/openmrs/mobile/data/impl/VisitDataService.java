package org.openmrs.mobile.data.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.openmrs.mobile.data.BaseEntityDataService;
import org.openmrs.mobile.data.EntityDataService;
import org.openmrs.mobile.data.PagingInfo;
import org.openmrs.mobile.data.QueryOptions;
import org.openmrs.mobile.data.db.impl.EntitySyncInfoDbService;
import org.openmrs.mobile.data.db.impl.ObsDbService;
import org.openmrs.mobile.data.db.impl.VisitDbService;
import org.openmrs.mobile.data.rest.impl.VisitRestServiceImpl;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.SyncAction;
import org.openmrs.mobile.models.Visit;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class VisitDataService extends BaseEntityDataService<Visit, VisitDbService, VisitRestServiceImpl>
		implements EntityDataService<Visit> {

	private EntitySyncInfoDbService entitySyncInfoDbService;
	private ObsDbService obsDbService;

	@Inject
	public VisitDataService(EntitySyncInfoDbService entitySyncInfoDbService, ObsDbService obsDbService) {
		this.entitySyncInfoDbService = entitySyncInfoDbService;
		this.obsDbService = obsDbService;
	}

	public void endVisit(@NonNull String uuid, @NonNull Visit visit, @NonNull GetCallback<Visit> callback) {
		checkNotNull(uuid);
		checkNotNull(visit);
		checkNotNull(callback);

		executeSingleCallback(callback, QueryOptions.REMOTE,
				() -> {
					Visit result = dbService.endVisit(visit);
					syncLogService.save(visit, SyncAction.DELETED);
					return result;
				},
				() -> restService.endVisit(uuid, visit));
	}

	public void updateVisit(Visit existingVisit, Visit updatedVisit, GetCallback<Visit> callback) {

		executeSingleCallback(callback, QueryOptions.REMOTE,
				() -> {
					existingVisit.processRelationships();
					Visit result = dbService.save(existingVisit);
					syncLogService.save(existingVisit, SyncAction.UPDATED);
					return result;
				},
				() -> restService.updateVisit(updatedVisit),
				(e) -> {
					dbService.saveVisitAttributes(e);
				});
	}

	@Override
	public void getByPatient(@NonNull Patient patient, @Nullable QueryOptions options, @Nullable PagingInfo pagingInfo,
			@NonNull GetCallback<List<Visit>> callback) {
		checkNotNull(patient);
		checkNotNull(callback);

		executeMultipleCallback(callback, options, pagingInfo,
				() -> dbService.getByPatient(patient, options, pagingInfo),
				() -> restService.getByPatient(patient.getUuid(), options, pagingInfo),
				(e) -> {
					// check if there are any voided obs from the server and delete them locally
					for (Visit visit : e) {
						for (Encounter encounter : visit.getEncounters()) {
							obsDbService.removeLocalObservationsNotFoundInREST(encounter);
						}
					}
					dbService.saveAll(e);

					// We determine a patient's sync is updated based on a successful pull of visits from the REST service
					entitySyncInfoDbService.saveLastSyncInfo(patient, new Date());
				});
	}

	@Override
	public void getByUuid(@NonNull String uuid, @Nullable QueryOptions options,
			@NonNull GetCallback<Visit> callback) {
		checkNotNull(uuid);
		checkNotNull(callback);

		executeSingleCallback(callback, options,
				() -> dbService.getByUuid(uuid, options),
				() -> restService.getByUuid(uuid, options),
				(e) -> {
					dbService.deleteAllVisitAttributes(e);
					dbService.save(e);
				});
	}
}
