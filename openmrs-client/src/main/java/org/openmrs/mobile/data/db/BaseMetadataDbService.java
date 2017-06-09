package org.openmrs.mobile.data.db;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.openmrs.mobile.data.PagingInfo;
import org.openmrs.mobile.data.QueryOptions;
import org.openmrs.mobile.models.BaseOpenmrsMetadata;

import java.util.List;

public abstract class BaseMetadataDbService<E extends BaseOpenmrsMetadata> extends BaseDbService<E>
		implements MetadataDbService<E> {
	@Override
	public List<E> getByNameFragment(String name, QueryOptions options, PagingInfo pagingInfo) {
		return executeQuery(options, pagingInfo, (w) -> w.where(getEntityTable().getProperty("name").like(name + "%")));
	}
}

