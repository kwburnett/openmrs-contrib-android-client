package org.openmrs.mobile.data.db.impl;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.openmrs.mobile.data.DatabaseHelper;
import org.openmrs.mobile.data.db.BaseMetadataDbService;
import org.openmrs.mobile.data.db.MetadataDbService;
import org.openmrs.mobile.data.db.Repository;
import org.openmrs.mobile.models.VisitPredefinedTask;
import org.openmrs.mobile.models.VisitPredefinedTask_Table;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

public class VisitPredefinedTaskDbService extends BaseMetadataDbService<VisitPredefinedTask>
		implements MetadataDbService<VisitPredefinedTask> {
	private DatabaseHelper databaseHelper;

	@Inject
	public VisitPredefinedTaskDbService(Repository repository, DatabaseHelper databaseHelper) {
		super(repository);
		this.databaseHelper = databaseHelper;
	}

	@Override
	protected ModelAdapter<VisitPredefinedTask> getEntityTable() {
		return (VisitPredefinedTask_Table)FlowManager.getInstanceAdapter(VisitPredefinedTask.class);
	}
}

