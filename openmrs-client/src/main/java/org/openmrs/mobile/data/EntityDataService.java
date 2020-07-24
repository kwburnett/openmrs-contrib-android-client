package org.openmrs.mobile.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.openmrs.mobile.models.BaseOpenmrsEntity;
import org.openmrs.mobile.models.Patient;

import java.util.List;

/**
 * Represents classes the provide data services for {@link BaseOpenmrsEntity} objects.
 * @param <E> The entity class
 */
public interface EntityDataService<E extends BaseOpenmrsEntity> extends DataService<E> {
	/**
	 * Gets entities associated with the specified patient.
	 * @param patient    The patient to search for
	 * @param options    The {@link QueryOptions} settings to use for this operation
	 * @param pagingInfo The paging information or null to exclude paging
	 * @param callback
	 */
	void getByPatient(@NonNull Patient patient, @Nullable QueryOptions options,
			@Nullable PagingInfo pagingInfo,
			@NonNull GetCallback<List<E>> callback);
}
