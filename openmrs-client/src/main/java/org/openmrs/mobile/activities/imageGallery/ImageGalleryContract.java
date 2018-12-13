package org.openmrs.mobile.activities.imageGallery;

import java.util.List;

import org.openmrs.mobile.activities.BasePresenterContract;
import org.openmrs.mobile.activities.BaseView;
import org.openmrs.mobile.models.VisitPhoto;

public interface ImageGalleryContract {

	interface View extends BaseView<Presenter> {

		void setVisitPhotos(List<VisitPhoto> visitPhotos);

		void showImageDeleted(boolean wasDeleted);
	}

	interface Presenter extends BasePresenterContract {

		void getVisitPhotos(List<String> visitPhotoUuids);

		void deletePhoto(VisitPhoto visitPhoto);
	}
}
