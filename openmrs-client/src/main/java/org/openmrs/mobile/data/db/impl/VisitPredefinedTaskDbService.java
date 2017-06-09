package org.openmrs.mobile.data.db.impl;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.openmrs.mobile.data.db.BaseMetadataDbService;
import org.openmrs.mobile.data.db.MetadataDbService;
import org.openmrs.mobile.models.VisitPredefinedTask;
import org.openmrs.mobile.models.VisitPredefinedTask_Table;

public class VisitPredefinedTaskDbService extends BaseMetadataDbService<VisitPredefinedTask>
		implements MetadataDbService<VisitPredefinedTask> {

	@Override
	protected ModelAdapter<VisitPredefinedTask> getEntityTable() {
		return (VisitPredefinedTask_Table)FlowManager.getInstanceAdapter(VisitPredefinedTask.class);
	}
}

