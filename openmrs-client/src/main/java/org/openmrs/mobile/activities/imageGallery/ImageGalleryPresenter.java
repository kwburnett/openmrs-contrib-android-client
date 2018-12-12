package org.openmrs.mobile.activities.imageGallery;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.data.DataService;
import org.openmrs.mobile.data.impl.VisitPhotoDataService;
import org.openmrs.mobile.models.VisitPhoto;

public class ImageGalleryPresenter extends BasePresenter implements ImageGalleryContract.Presenter {

	private ImageGalleryContract.View view;
	private VisitPhotoDataService visitPhotoDataService;

	private int numberOfPhotosToFetch, numberOfPhotosFetched;
	private List<VisitPhoto> visitPhotos;

	public ImageGalleryPresenter(ImageGalleryContract.View view) {
		this.view = view;

		visitPhotoDataService = dataAccess().visitPhoto();
		visitPhotos = new ArrayList<>();
	}

	@Override
	public void subscribe() {
		// Intentionally left blank
	}

	public void getVisitPhotos(List<String> visitPhotoUuids) {
		numberOfPhotosFetched = 0;
		numberOfPhotosToFetch = visitPhotoUuids.size();
		visitPhotos.clear();
		for (String visitPhotoUuid : visitPhotoUuids) {
			visitPhotoDataService.getByUuid(visitPhotoUuid, null, new DataService.GetCallback<VisitPhoto>() {

				@Override
				public void onCompleted(VisitPhoto visitPhoto) {
					visitPhotos.add(visitPhoto);
					updatePhotosIfPhotoFetchCompleted();
				}

				@Override
				public void onError(Throwable t) {
					updatePhotosIfPhotoFetchCompleted();
				}
			});
		}
	}

	private void updatePhotosIfPhotoFetchCompleted() {
		numberOfPhotosFetched++;
		if (numberOfPhotosToFetch == numberOfPhotosFetched) {
			view.setVisitPhotos(visitPhotos);
		}
	}
}
