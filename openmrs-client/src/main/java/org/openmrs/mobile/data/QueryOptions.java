package org.openmrs.mobile.data;

import android.support.annotation.Nullable;

import org.openmrs.mobile.data.rest.RestConstants;

public class QueryOptions {
	private static final boolean DEFAULT_INCLUDE_INACTIVE = false;
	private static final boolean DEFAULT_LOAD_RELATED_OBJECTS = false;

	public static final QueryOptions DEFAULT = new QueryOptions(false, false);
	public static final QueryOptions INCLUDE_INACTIVE = new QueryOptions(true, false);
	public static final QueryOptions LOAD_RELATED_OBJECTS = new QueryOptions(false, true);

	private String cacheKey;
	private boolean includeInactive = DEFAULT_INCLUDE_INACTIVE;
	private boolean loadRelatedObjects = DEFAULT_LOAD_RELATED_OBJECTS;

	public QueryOptions() {
		this(DEFAULT_INCLUDE_INACTIVE, DEFAULT_LOAD_RELATED_OBJECTS);
	}

	public QueryOptions(boolean includeInactive, boolean loadRelatedObjects) {
		this.includeInactive = includeInactive;
		this.loadRelatedObjects = loadRelatedObjects;
	}

	public QueryOptions(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public static String getCacheKey(@Nullable QueryOptions options) {
		return options == null ? null : options.getCacheKey();
	}

	public static boolean getIncludeInactive(@Nullable QueryOptions options) {
		return options == null ? DEFAULT_INCLUDE_INACTIVE : options.includeInactive();
	}

	public static boolean getLoadRelatedObjects(@Nullable QueryOptions options) {
		return options == null ? DEFAULT_LOAD_RELATED_OBJECTS : options.loadRelatedObjects();
	}

	public static String getRepresentation(@Nullable QueryOptions options) {
		return getLoadRelatedObjects(options) ? RestConstants.Representations.FULL : RestConstants.Representations.DEFAULT;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public boolean includeInactive() {
		return includeInactive;
	}

	public void setIncludeInactive(boolean includeInactive) {
		this.includeInactive = includeInactive;
	}

	public boolean loadRelatedObjects() {
		return loadRelatedObjects;
	}

	public void setLoadRelatedObjects(boolean loadRelatedObjects) {
		this.loadRelatedObjects = loadRelatedObjects;
	}
}