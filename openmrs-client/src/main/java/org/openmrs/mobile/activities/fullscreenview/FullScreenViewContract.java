package org.openmrs.mobile.activities.fullscreenview;

import java.util.List;

import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.models.VisitPhoto;

public interface FullScreenViewContract {

	interface View extends BaseView<Presenter> {

		void setVisitPhotos(List<VisitPhoto> visitPhotos);
	}

	interface Presenter extends BasePresenterContract {

		void getVisitPhotos(List<String> visitPhotoUuids);
	}
}
