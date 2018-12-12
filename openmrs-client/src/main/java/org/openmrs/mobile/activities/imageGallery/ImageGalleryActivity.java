package org.openmrs.mobile.activities.imageGallery;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.adapter.ImageGalleryImageAdapter;
import org.openmrs.mobile.models.VisitPhoto;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.ToastUtil;

public class ImageGalleryActivity extends ACBaseActivity implements ImageGalleryContract.View {

	private ImageGalleryImageAdapter imageGalleryImageAdapter;
	private ViewPager viewPager;
	private ImageGalleryContract.Presenter presenter;
	private String initialPhotoUuid;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_gallery);
		intitializeToolbar();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(ApplicationConstants.EMPTY_STRING);
		}

		viewPager = (ViewPager) findViewById(R.id.fullscreenPager);
		imageGalleryImageAdapter = new ImageGalleryImageAdapter(ImageGalleryActivity.this);
		presenter = new ImageGalleryPresenter(this);
		viewPager.setAdapter(imageGalleryImageAdapter);

		Intent intent = getIntent();
		initialPhotoUuid = intent.getExtras().getString(ApplicationConstants.BundleKeys.EXTRA_VISIT_PHOTO_UUID);
		if (initialPhotoUuid != null) {
			ArrayList<String> visitPhotoUuids = intent.getExtras()
					.getStringArrayList(ApplicationConstants.BundleKeys.EXTRA_VISIT_PHOTO_UUIDS);
			presenter.getVisitPhotos(visitPhotoUuids);
		} else {
			VisitPhoto tempVisitPhoto = new VisitPhoto();
			String tempPhotoPath = intent.getExtras().getString(ApplicationConstants.BundleKeys.EXTRA_TEMP_VISIT_PHOTO_PATH);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap tempPhoto = BitmapFactory.decodeFile(tempPhotoPath, options);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			tempPhoto.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
			tempVisitPhoto.setImage(byteArrayOutputStream.toByteArray());

			imageGalleryImageAdapter.hideDetails();

			setVisitPhotos(new ArrayList<>(Arrays.asList(tempVisitPhoto)));
		}
	}

	@Override
	public void setVisitPhotos(List<VisitPhoto> visitPhotos) {
		imageGalleryImageAdapter.setVisitPhotos(visitPhotos);
		int initialPosition = 0;
		for (VisitPhoto visitPhoto : visitPhotos) {
			if (visitPhoto.getUuid().equalsIgnoreCase(initialPhotoUuid)) {
				break;
			}
			initialPosition++;
		}
		viewPager.setCurrentItem(initialPosition);
	}

	@Override
	public void setPresenter(ImageGalleryContract.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void runOnUIThread(Runnable runnable) {
		super.runOnUiThread(runnable);
	}

	@Override
	public void showToast(String message, ToastUtil.ToastType toastType) {
		ToastUtil.showShortToast(this, toastType, message);
	}

	@Override
	public boolean onSupportNavigateUp(){
		finish();
		return true;
	}
}
