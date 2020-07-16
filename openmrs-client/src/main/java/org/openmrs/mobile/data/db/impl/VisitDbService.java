package org.openmrs.mobile.data.db.impl;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.openmrs.mobile.data.db.BaseEntityDbService;
import org.openmrs.mobile.data.db.EntityDbService;
import org.openmrs.mobile.data.db.Repository;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Encounter_Table;
import org.openmrs.mobile.models.Resource;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.models.VisitAttribute;
import org.openmrs.mobile.models.VisitAttribute_Table;
import org.openmrs.mobile.models.Visit_Table;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class VisitDbService extends BaseEntityDbService<Visit> implements EntityDbService<Visit> {
	private static VisitAttribute_Table visitAttributeTable;
	private static Encounter_Table encounterTable;

	static {
		visitAttributeTable = (VisitAttribute_Table)FlowManager.getInstanceAdapter(VisitAttribute.class);
		encounterTable = (Encounter_Table)FlowManager.getInstanceAdapter(Encounter.class);
	}

	@Inject
	public VisitDbService(Repository repository) {
		super(repository);
	}

	@Override
	protected ModelAdapter<Visit> getEntityTable() {
		return (Visit_Table)FlowManager.getInstanceAdapter(Visit.class);
	}

	public Visit endVisit(@NonNull Visit visit) {
		checkNotNull(visit);

		if (visit.getStopDatetime() == null) {
			visit.setStopDatetime(new Date());
		}

		visit = save(visit);

		return visit;
	}

	public void deleteLocalRelatedObjects(@NonNull Visit visit) {
		checkNotNull(visit);

		repository.deleteAll(visitAttributeTable, VisitAttribute_Table.visit_uuid.eq(visit.getUuid()),
				new Method("LENGTH", VisitAttribute_Table.uuid).lessThanOrEq(Resource.LOCAL_UUID_LENGTH));
		repository.deleteAll(encounterTable, Encounter_Table.visit_uuid.eq(visit.getUuid()),
				new Method("LENGTH", Encounter_Table.uuid).lessThanOrEq(Resource.LOCAL_UUID_LENGTH));
	}

	public void deleteAllVisitAttributes(@NonNull Visit visit) {
		repository.deleteAll(visitAttributeTable, VisitAttribute_Table.visit_uuid.eq(visit.getUuid()));
	}

	public void removeLocalVisitAttributesNotFoundInREST(@NonNull Visit visit) {
		checkNotNull(visit);

		if (visit.getAttributes().isEmpty()) {
			return;
		}

		// create a group of SQLOperators
		List<SQLOperator> operators =
				OperatorGroup.clause(
						VisitAttribute_Table.visit_uuid.eq(visit.getUuid()))
						.and(VisitAttribute_Table.uuid.notIn(getVisitAttributeUuids(visit))).getConditions();

		repository.deleteAll(visitAttributeTable, operators.toArray(new SQLOperator[operators.size()]));
	}

	public void saveVisitAttributes(@NonNull Visit visit) {
		checkNotNull(visit);

		deleteAllVisitAttributes(visit);
		repository.saveAll(visitAttributeTable, visit.getAttributes());
	}

	private List<String> getVisitAttributeUuids(Visit visit) {
		List<String> uuids = new ArrayList<>();
		for (VisitAttribute visitAttribute: visit.getAttributes()) {
			uuids.add(visitAttribute.getUuid());
		}

		return uuids;
	}
}
