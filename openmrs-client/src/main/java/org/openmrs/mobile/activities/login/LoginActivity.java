/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.login;

import static org.openmrs.mobile.utilities.ApplicationConstants.ErrorCodes.INVALID_URL;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.databinding.DataBindingUtil;
//import android.databinding.ObservableBoolean;
//import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.SparseArray;
import android.view.Menu;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.loginsync.LoginSyncActivity;
import org.openmrs.mobile.activities.syncselection.SyncSelectionActivity;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.databinding.ActivityLoginBinding;
import org.openmrs.mobile.listeners.watcher.LoginValidatorWatcher;
import org.openmrs.mobile.models.Location;
import org.openmrs.mobile.net.AuthorizationManager;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.ImageUtils;
import org.openmrs.mobile.utilities.StringUtils;
import org.openmrs.mobile.utilities.URLValidator;

public class LoginActivity extends ACBaseActivity implements LoginContract.ViewModel, LoginContract.View {

	private LoginContract.Presenter presenter;
	private ActivityLoginBinding binding;

//	public ObservableField<String> loginUrl = new ObservableField<>("http://test.com");
//	public ObservableField<String> username = new ObservableField<>();
//	public ObservableField<String> password = new ObservableField<>();
//
//	public ObservableBoolean showLoginUrlField = new ObservableBoolean(false);
//	public ObservableBoolean showPassword = new ObservableBoolean(false);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

		presenter = new LoginPresenter(this, openMRS);
		binding.setViewModel(this);

//		loginUrl.set(OpenMRS.getInstance().getServerUrl());
//
//		initViewFields(rootView);
//		initListeners();
//		loadLocations();
//		authorizationManager = openMRS.getAuthorizationManager();
		// Font config
//		FontsUtil.setFont((ViewGroup)this.getActivity().findViewById(android.R.id.content));
//
//		if (authorizationManager.hasUserSessionExpiredDueToInactivity()) {
//			mPresenter.userWasLoggedOutDueToInactivity();
//		}
//
//		return rootView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	private static List<HashMap<String, String>> locationsList;
	protected OpenMRS openMRS = OpenMRS.getInstance();
	private android.view.View rootView;
	private TextInputEditText url;
	private Button loginButton;
	private ProgressBar loadingProgressBar;
	private Spinner dropdownLocation;
	private SparseArray<Bitmap> bitmapCache;
	private LoginValidatorWatcher loginValidatorWatcher;
	private TextView changeUrlIcon;
	private TextInputLayout loginUrlTextLayout;
	private android.view.View viewsContainer;
	private AuthorizationManager authorizationManager;

	private void initViewFields(android.view.View mRootView) {
		viewsContainer = mRootView.findViewById(R.id.viewsContainer);
		url = (TextInputEditText)mRootView.findViewById(R.id.loginUrlField);
		dropdownLocation = (Spinner)mRootView.findViewById(R.id.locationSpinner);
//		username = (TextInputEditText)mRootView.findViewById(R.id.loginUsernameField);
//		username.setText(OpenMRS.getInstance().getUsername());
//		password = (TextInputEditText)mRootView.findViewById(R.id.loginPasswordField);
		loginButton = (Button)mRootView.findViewById(R.id.loginButton);
		loadingProgressBar = (ProgressBar)mRootView.findViewById(R.id.locationLoadingProgressBar);
		changeUrlIcon = (TextView)mRootView.findViewById(R.id.changeUrlIcon);
		loginUrlTextLayout = (TextInputLayout)mRootView.findViewById(R.id.loginUrlTextLayout);
//		showPassword = (CheckBox)mRootView.findViewById(R.id.checkboxShowPassword);
//		url.setText(loginUrl);

//		if (StringUtils.isNullOrEmpty(loginUrl)) {
//			showEditUrlEditField(true);
//		}
	}

	private void loadLocations() {

		locationsList = new ArrayList<>();
		String locationsStr = openMRS.getLocations();
		if (StringUtils.notEmpty(locationsStr)) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<HashMap<String, String>>>() {
			}.getType();
			locationsList = gson.fromJson(locationsStr, type);
		}

//		if (locationsList.isEmpty() && !StringUtils.isNullOrEmpty(loginUrl)) {
//			mPresenter.loadLocations(loginUrl);
//		} else if (!StringUtils.isNullOrEmpty(loginUrl)) {
//			updateLocationsSpinner(locationsList, loginUrl);
//		}

	}

	private void initListeners() {
		changeUrlIcon.setOnClickListener(view -> {
			if (loginUrlTextLayout.getVisibility() == android.view.View.VISIBLE) {
				showEditUrlEditField(false);
			} else {
				showEditUrlEditField(true);
			}
		});

//		loginValidatorWatcher = new LoginValidatorWatcher(url, username, password, dropdownLocation, loginButton);

		url.setOnFocusChangeListener((view, b1) -> {
			boolean isViewFocused = view.isFocused();
			boolean isUrlEntered = StringUtils.notEmpty(url.getText().toString());
			boolean isUrlChanged = loginValidatorWatcher.isUrlChanged();
			boolean isLocationErrorOccurred = loginValidatorWatcher.isLocationErrorOccurred();

			if (!isViewFocused && (!isUrlChanged || isUrlChanged && (isUrlEntered || isLocationErrorOccurred))) {
				setUrl(url.getText().toString());
				loginValidatorWatcher.setUrlChanged(false);
			}
		});

//		loginButton.setOnClickListener(v -> mPresenter.login(username.getText().toString(),
//				password.getText().toString(),
//				url.getText().toString(),
//				openMRS.getLastLoginServerUrl()));

//		showPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
//			if (isChecked) {
//				password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//			} else {
//				password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//			}
//		});
	}

	@Override
	public void login(boolean wipeDatabase) {
//		mPresenter.authenticateUser(username.getText().toString(),
//				password.getText().toString(),
//				url.getText().toString(), wipeDatabase);
	}

	private void showEditUrlEditField(boolean visibility) {
		if (!visibility) {
			loginUrlTextLayout.setVisibility(android.view.View.GONE);
		} else {
			loginUrlTextLayout.setVisibility(android.view.View.VISIBLE);
		}
	}

	private void setUrl(String url) {
		URLValidator.ValidationResult result = URLValidator.validate(url);
		if (result.isURLValid()) {
			//Append forward slash. Retrofit throws a serious error if a base url does not end with a forward slash
			url = result.getUrl();
			if (!url.endsWith("/")) {
				url += "/";
			}
//			mPresenter.loadLocations(url);

		} else if (!StringUtils.isNullOrEmpty(url)) {
			showMessage(INVALID_URL);
		}
	}

	@Override
	public void hideSoftKeys() {
//		View view = this.getActivity().getCurrentFocus();
//		if (view == null) {
//			view = new View(this.getActivity());
//		}
//		InputMethodManager inputMethodManager =
//				(InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public void showWarningDialog() {
//		CustomDialogBundle bundle = new CustomDialogBundle();
//		bundle.setTitleViewMessage(getString(R.string.warning_dialog_title));
//		bundle.setTextViewMessage(getString(R.string.warning_lost_data_dialog));
//		bundle.setRightButtonText(getString(R.string.dialog_button_ok));
//		bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.LOGIN);
//		((LoginActivity)this.getActivity())
//				.createAndShowDialog(bundle, ApplicationConstants.DialogTAG.WARNING_LOST_DATA_DIALOG_TAG);
	}

	@Override
	public void userAuthenticated(boolean isFirstAccessOfNewUrl) {
//		mPresenter.saveLocationsInPreferences(locationsList, dropdownLocation.getSelectedItemPosition());
		Intent intent;
		if (isFirstAccessOfNewUrl) {
			intent = new Intent(openMRS.getApplicationContext(), SyncSelectionActivity.class);
		} else {
			intent = new Intent(openMRS.getApplicationContext(), LoginSyncActivity.class);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		openMRS.getApplicationContext().startActivity(intent);
		finish();

	}

	@Override
	public void finishLoginActivity() {
		// Since the user has just logged in, update the interaction time so we don't have a (potentially) immediate
		// re-login
		authorizationManager.trackUserInteraction();
		finish();
	}

	@Override
	public void onResume() {
		super.onResume();
		bindDrawableResources();
	}

	private void bindDrawableResources() {
//		bitmapCache = new SparseArray<>();
//		ImageView bandaHealthLogo = (ImageView)getActivity().findViewById(R.id.bandaHealthLogo);
//		createImageBitmap(R.drawable.banda_logo, bandaHealthLogo.getLayoutParams());
//		bandaHealthLogo.setImageBitmap(bitmapCache.get(R.drawable.banda_logo));
	}

	private void createImageBitmap(Integer key, ViewGroup.LayoutParams layoutParams) {
		if (bitmapCache.get(key) == null) {
			bitmapCache.put(key, ImageUtils.decodeBitmapFromResource(getResources(), key,
					layoutParams.width, layoutParams.height));
		}
	}

	private void unbindDrawableResources() {
		if (null != bitmapCache) {
			for (int i = 0; i < bitmapCache.size(); i++) {
				Bitmap bitmap = bitmapCache.valueAt(i);
				bitmap.recycle();
			}
		}
	}

	@Override
	public void updateLoginFormLocations(List<Location> locationsList, String serverURL) {
//		loginUrl = serverURL;
		url.setText(serverURL);
		List<HashMap<String, String>> items = null;
		if (locationsList != null) {
			items = getLocationStringList(locationsList);
			updateLocationsSpinner(items, serverURL);
		} else {

		}

	}

	private void updateLocationsSpinner(List<HashMap<String, String>> locations, String serverURL) {
		locationsList = locations;
		setProgressBarVisibility(true);
//		loginUrl = serverURL;
		url.setText(serverURL);
		int selectedLocation = 0;
		int locationsSize = 0;
		if (locations != null) {
			locationsSize = locations.size();
		}
		String[] spinnerArray = new String[locationsSize];
		for (int i = 0; i < locationsSize; i++) {
			spinnerArray[i] = locations.get(i).get("display");
			if (locations.get(i).get("uuid").contains(OpenMRS.getInstance().getLocation())) {
				selectedLocation = i;
			}
		}

//		ArrayAdapter<String> adapter =
//				new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
//		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		dropdownLocation.setAdapter(adapter);
//		dropdownLocation.setSelection(selectedLocation);
//		setProgressBarVisibility(false);

	}

	@Override
	public void showMessage(String message) {
//		super.showError(message);
	}

	@Override
	public void showMessage(int errorCode) {
//		super.showError(errorCode);
	}

//	@Override
//	public void setProgressBarVisibility(boolean visible) {
//		if (visible) {
//			ACBaseActivity.hideSoftKeyboard(getActivity());
//		}
//		loadingProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
//	}

	private List<HashMap<String, String>> getLocationStringList(List<Location> locationList) {
		List<HashMap<String, String>> locations = new ArrayList<>();
		for (Location location : locationList) {
			HashMap<String, String> locationHashMap = new HashMap<>();
			locationHashMap.put("uuid", location.getUuid());
			locationHashMap.put("display", location.getName());
			locationHashMap.put("parentlocationuuid", location.getParentLocation() == null ?
					ApplicationConstants.EMPTY_STRING :
					location.getParentLocation().getUuid());
			locations.add(locationHashMap);
		}

		return locations;
	}

	@Override
	public void setPresenter(LoginContract.Presenter presenter) {

	}

	@Override
	public void runOnUIThread(Runnable runnable) {

	}
//
//	@Override
//	public void showHideLoginUrlField(CompoundButton buttonView, boolean isChecked) {
//		showLoginUrlField.set(isChecked);
//	}
}
