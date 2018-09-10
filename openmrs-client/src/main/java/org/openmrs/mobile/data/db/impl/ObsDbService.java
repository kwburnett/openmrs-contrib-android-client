package org.openmrs.mobile.data.db.impl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.openmrs.mobile.data.PagingInfo;
import org.openmrs.mobile.data.QueryOptions;
import org.openmrs.mobile.data.db.BaseDbService;
import org.openmrs.mobile.data.db.DbService;
import org.openmrs.mobile.data.db.Repository;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Encounter_Table;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Observation_Table;
import org.openmrs.mobile.utilities.ApplicationConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class ObsDbService extends BaseDbService<Observation> implements DbService<Observation> {
	private static Observation_Table observationTable;

	static {
		observationTable = (Observation_Table)FlowManager.getInstanceAdapter(Observation.class);
	}

	@Inject
	public ObsDbService(Repository repository) {
		super(repository);
	}

	@Override
	protected ModelAdapter<Observation> getEntityTable() {
		return (Observation_Table)FlowManager.getInstanceAdapter(Observation.class);
	}

	public List<Observation> getVisitPhotoObservations(String visitUuid, QueryOptions options) {
		return executeQuery(options, null,
				(f) -> f.where(Observation_Table.concept_uuid.in(
						Arrays.asList(ApplicationConstants.ObservationLocators.VISIT_DOCUMENT_UUID.split(","))))
						.and(Observation_Table.encounter_uuid.in(
								SQLite.select(Encounter_Table.uuid).from(Encounter.class)
										.where(Encounter_Table.visit_uuid.eq(visitUuid)))));
	}

	public List<Observation> getByEncounter(@NonNull Encounter encounter, @Nullable QueryOptions options,
			@Nullable PagingInfo pagingInfo) {
		checkNotNull(encounter);

		return executeQuery(options, pagingInfo,
				(f) -> f.where(Observation_Table.encounter_uuid.eq(encounter.getUuid())));
	}

	public void removeLocalObservationsNotFoundInREST(@NonNull Encounter encounter) {
		checkNotNull(encounter);

		if (encounter.getObs().isEmpty()) {
			return;
		}

		// create a group of SQLOperators
		List<SQLOperator> operators =
				OperatorGroup.clause(
						Observation_Table.encounter_uuid.eq(encounter.getUuid()))
						.and(Observation_Table.uuid.notIn(getObservationUuids(encounter))).getConditions();

		repository.deleteAll(observationTable, operators.toArray(new SQLOperator[operators.size()]));
	}

	private List<String> getObservationUuids(Encounter encounter) {
		List<String> uuids = new ArrayList<>();
		for (Observation observation : encounter.getObs()) {
			uuids.add(observation.getUuid());
		}

		return uuids;
	}
}
