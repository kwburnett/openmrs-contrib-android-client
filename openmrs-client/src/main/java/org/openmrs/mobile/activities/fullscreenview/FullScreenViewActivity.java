package org.openmrs.mobile.activities.fullscreenview;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.adapter.FullScreenImageAdapter;
import org.openmrs.mobile.models.VisitPhoto;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.ToastUtil;

public class FullScreenViewActivity extends ACBaseActivity implements FullScreenViewContract.View {

	private FullScreenImageAdapter fullScreenImageAdapter;
	private ViewPager viewPager;
	private FullScreenViewContract.Presenter presenter;
	private int initialPosition;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);
		intitializeToolbar();

		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			actionBar.setTitle(ApplicationConstants.EMPTY_STRING);
		}

		viewPager = (ViewPager) findViewById(R.id.fullscreenPager);

		Intent intent = getIntent();
		initialPosition = intent.getIntExtra("position", 0);
		ArrayList<String> visitPhotoUuids = intent.getStringArrayListExtra("visitPhotoUuids");

		fullScreenImageAdapter = new FullScreenImageAdapter(FullScreenViewActivity.this);

		presenter = new FullScreenViewPresenter(this);
		presenter.getVisitPhotos(visitPhotoUuids);

		viewPager.setAdapter(fullScreenImageAdapter);
	}

	@Override
	public void setVisitPhotos(List<VisitPhoto> visitPhotos) {
		fullScreenImageAdapter.setVisitPhotos(visitPhotos);
		viewPager.setCurrentItem(initialPosition);
	}

	@Override
	public void setPresenter(FullScreenViewContract.Presenter presenter) {
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
