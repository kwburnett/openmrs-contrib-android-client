package org.openmrs.mobile.event;

import androidx.annotation.Nullable;

public class SyncPullEvent extends SyncEvent {

	public SyncPullEvent(String message, @Nullable String entity, @Nullable Integer totalItemsToPull) {
		super(message, entity, totalItemsToPull);
	}
}
