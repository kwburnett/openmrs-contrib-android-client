package org.openmrs.mobile.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.openmrs.mobile.R;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.models.VisitPhoto;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.DateUtils;
import org.openmrs.mobile.widget.TouchImageView;

public class ImageGalleryImageAdapter extends PagerAdapter {

	private Activity activity;
	private List<VisitPhoto> visitPhotos;
	private boolean hideDetails = false;
	private Object[] views;

	public ImageGalleryImageAdapter(Activity activity) {
		this.activity = activity;
		visitPhotos = new ArrayList<>();
	}

	public void setVisitPhotos(List<VisitPhoto> visitPhotos) {
		this.visitPhotos.clear();
		this.visitPhotos.addAll(visitPhotos);
		views = new Object[visitPhotos.size()];
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object item) {
		for (int i = 0; i < views.length; i++) {
			if (views[i] != null && views[i].equals(item)) {
				return i;
			}
		}
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return visitPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ConstraintLayout) object);
	}

	@Override
	@NonNull
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		TouchImageView imageDisplay;

		// This creates a view with the photo that can be zoomed, and
		// adds the photo information down at the bottom of the screen
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = layoutInflater.inflate(R.layout.layout_image_gallery_image, container, false);

		imageDisplay = (TouchImageView) viewLayout.findViewById(R.id.imageDisplay);

		// Get the image and add it to the zoomable image view
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		VisitPhoto visitPhoto = visitPhotos.get(position);
		Bitmap bitmap = BitmapFactory.decodeByteArray(visitPhoto.getImageColumn().getBlob(), 0,
				visitPhoto.getImageColumn().getBlob().length, options);
		imageDisplay.setImageBitmap(bitmap);

		// Get the metadata text of the image (description, provider, etc.) and display it in the right TextView
		TextView descriptionView = (TextView) viewLayout.findViewById(R.id.photoDetails);
		String uploadedBy;
		if (visitPhoto.getCreator() != null) {
			uploadedBy = visitPhoto.getCreator().getDisplay();
		} else {
			// must have been uploaded locally
			uploadedBy = OpenMRS.getInstance().getUserPersonName();
		}

		String description = visitPhoto.getFileCaption();
		if (description == null) {
			description = ApplicationConstants.EMPTY_STRING;
		}
		descriptionView.setText(activity.getString(R.string.visit_image_description, description,
				DateUtils.calculateRelativeDate(visitPhoto.getDateCreated()), uploadedBy));
		if (hideDetails) {
			descriptionView.setVisibility(View.GONE);
		}

		// Add the view to our view pager and keep track of where the view is (for deleting and removal purposes)
		((ViewPager) container).addView(viewLayout);
		views[position] = viewLayout;

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ConstraintLayout) object);
	}

	public void removePhoto(ViewGroup container, int position) {
		visitPhotos.remove(position);
		Object[] newViews = new Object[visitPhotos.size()];
		int j = 0;
		for (int i = 0; i < views.length; i++) {
			if (i != position) {
				newViews[j++] = views[i];
			}
		}
		views = newViews;
		notifyDataSetChanged();
	}

	public void hideDetails() {
		hideDetails = true;
	}

	public VisitPhoto getItem(int position) {
		return visitPhotos.get(position);
	}
}
