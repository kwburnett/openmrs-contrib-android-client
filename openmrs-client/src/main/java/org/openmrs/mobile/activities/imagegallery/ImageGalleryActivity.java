package org.openmrs.mobile.activities.imagegallery;

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
import android.view.Menu;
import android.view.MenuItem;
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
	private boolean anyImageWasDeleted = false;
	private boolean showDeleteButton = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_gallery);
		intitializeToolbar();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(ApplicationConstants.EMPTY_STRING);
		}

		viewPager = (ViewPager) findViewById(R.id.imageGalleryPager);
		imageGalleryImageAdapter = new ImageGalleryImageAdapter(ImageGalleryActivity.this);
		presenter = new ImageGalleryPresenter(this);
		viewPager.setAdapter(imageGalleryImageAdapter);

		Intent intent = getIntent();
		initialPhotoUuid = intent.getExtras().getString(ApplicationConstants.BundleKeys.EXTRA_VISIT_PHOTO_UUID);
		boolean shouldHideDeleteButton = intent.getBooleanExtra(ApplicationConstants.BundleKeys.EXTRA_NO_DELETE, false);
		if (shouldHideDeleteButton) {
			showDeleteButton = false;
		}
		if (initialPhotoUuid != null) {
			ArrayList<String> visitPhotoUuids = intent.getExtras()
					.getStringArrayList(ApplicationConstants.BundleKeys.EXTRA_VISIT_PHOTO_UUIDS);
			presenter.getVisitPhotos(visitPhotoUuids);
		} else {
			// Since we have no photo UUID, we're viewing the photo stored on the phone that hasn't been saved yet
			VisitPhoto tempVisitPhoto = new VisitPhoto();
			String tempPhotoPath = intent.getExtras().getString(ApplicationConstants.BundleKeys.EXTRA_TEMP_VISIT_PHOTO_PATH);

			// To render the image, we need to get the byte array and give it to the ImageView
			Bitmap tempPhoto = BitmapFactory.decodeFile(tempPhotoPath);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			tempPhoto.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
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
	public void showImageDeleted(boolean wasDeleted) {
		if (wasDeleted) {
			showToast(getString(R.string.photo_was_deleted), ToastUtil.ToastType.SUCCESS);
		} else {
			showToast(getString(R.string.photo_was_not_deleted), ToastUtil.ToastType.ERROR);
		}
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (showDeleteButton) {
			// Inflate the menu to add the actions to the app bar
			getMenuInflater().inflate(R.menu.menu_image_gallery, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
			case R.id.action_delete_image:
				int indexOfImageBeingViewed = viewPager.getCurrentItem();
				VisitPhoto photoBeingViewed = imageGalleryImageAdapter.getItem(indexOfImageBeingViewed);
				presenter.deletePhoto(photoBeingViewed);
				imageGalleryImageAdapter.removePhoto(viewPager, indexOfImageBeingViewed);
				anyImageWasDeleted = true;
				if (viewPager.getChildCount() == 0) {
					this.onBackPressed();
				}
				return true;
			case android.R.id.home:
				this.onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(menuItem);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra(ApplicationConstants.BundleKeys.EXTRA_SHOULD_REFRESH, anyImageWasDeleted);
		setResult(RESULT_OK, intent);
		finish();
	}
}
