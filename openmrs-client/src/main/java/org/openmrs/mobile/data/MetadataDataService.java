package org.openmrs.mobile.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.openmrs.mobile.models.BaseOpenmrsMetadata;

import java.util.List;

/**
 * Represents classes the provide data services for {@link BaseOpenmrsMetadata} objects.
 * @param <E> The entity class
 */
public interface MetadataDataService<E extends BaseOpenmrsMetadata> extends DataService<E> {
	/**
	 * Gets entities with the specified name fragment.
	 * @param name       The name to search for
	 * @param options    The {@link QueryOptions} settings to use for this operation
	 * @param pagingInfo The paging information or null to exclude paging
	 * @param callback
	 */
	void getByNameFragment(@NonNull String name, @Nullable QueryOptions options,
			@Nullable PagingInfo pagingInfo,
			@NonNull GetCallback<List<E>> callback);
}
