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
package org.openmrs.mobile.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.dialog.CustomFragmentDialog;
import org.openmrs.mobile.activities.findpatientrecord.FindPatientRecordActivity;
import org.openmrs.mobile.activities.login.LoginActivity;
import org.openmrs.mobile.activities.patientlist.PatientListActivity;
import org.openmrs.mobile.application.Logger;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.bundle.CustomDialogBundle;
import org.openmrs.mobile.net.AuthorizationManager;
import org.openmrs.mobile.utilities.ApplicationConstants;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class ACBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	protected final OpenMRS openMRS = OpenMRS.getInstance();
	protected final Logger logger = openMRS.getLogger();
	protected FragmentManager fragmentManager;
	protected CustomFragmentDialog customFragmentDialog;
	protected DrawerLayout drawer;
	protected AuthorizationManager authorizationManager;
	protected FrameLayout frameLayout;

	private MenuItem syncbutton;
	private Toolbar toolbar;
	private OpenMRS instance = OpenMRS.getInstance();
	private ActionBarDrawerToggle toggle;
	private boolean loading;
	private BroadcastReceiver networkStateReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acbase);
		fragmentManager = getSupportFragmentManager();
		frameLayout = (FrameLayout)findViewById(R.id.content_frame);
		intitializeToolbar();
		intitializeNavigationDrawer();

		authorizationManager = openMRS.getAuthorizationManager();

		networkStateReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				openMRS.getNetworkUtils().startSamplingConnectivity();
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		openMRS.getNetworkUtils().startSamplingConnectivity();
		supportInvalidateOptionsMenu();

		boolean activityIsLoginActivity = this instanceof LoginActivity;
		if (!activityIsLoginActivity && (!authorizationManager.isUserLoggedIn() ||
				authorizationManager.hasUserSessionExpiredDueToInactivity())) {
			authorizationManager.moveToLoginActivity();
		} else if (!activityIsLoginActivity) {
			authorizationManager.trackUserInteraction();
		}

		registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	protected void onPause() {
		super.onPause();
		openMRS.getNetworkUtils().stopSamplingConnectivity();

		unregisterReceiver(networkStateReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basic_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu menu) {
		return true;
	}

	public void logout() {
		openMRS.clearUserPreferencesData();
		authorizationManager.moveToLoginActivity();
		finish();
	}

	private void showLogoutDialog() {
		CustomDialogBundle bundle = new CustomDialogBundle();
		bundle.setTitleViewMessage(getString(R.string.logout_dialog_title));
		bundle.setTextViewMessage(getString(R.string.logout_dialog_message));
		bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.LOGOUT);
		bundle.setRightButtonText(getString(R.string.logout_dialog_button));
		createAndShowDialog(bundle, ApplicationConstants.DialogTAG.LOGOUT_DIALOG_TAG);
	}

	public void showStartVisitImpossibleDialog(CharSequence title) {
		CustomDialogBundle bundle = new CustomDialogBundle();
		bundle.setTitleViewMessage(getString(R.string.start_visit_unsuccessful_dialog_title));
		bundle.setTextViewMessage(getString(R.string.start_visit_unsuccessful_dialog_message, title));
		bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.DISMISS);
		bundle.setRightButtonText(getString(R.string.dialog_button_ok));
		createAndShowDialog(bundle, ApplicationConstants.DialogTAG.START_VISIT_IMPOSSIBLE_DIALOG_TAG);
	}

	public void showStartVisitDialog(CharSequence title) {
		CustomDialogBundle bundle = new CustomDialogBundle();
		bundle.setTitleViewMessage(getString(R.string.start_visit_dialog_title));
		bundle.setTextViewMessage(getString(R.string.start_visit_dialog_message, title));
		bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.START_VISIT);
		bundle.setRightButtonText(getString(R.string.dialog_button_confirm));
		bundle.setLeftButtonAction(CustomFragmentDialog.OnClickAction.DISMISS);
		bundle.setLeftButtonText(getString(R.string.dialog_button_cancel));
		createAndShowDialog(bundle, ApplicationConstants.DialogTAG.START_VISIT_DIALOG_TAG);
	}

	public void showDeletePatientDialog() {
		CustomDialogBundle bundle = new CustomDialogBundle();
		bundle.setTitleViewMessage(getString(R.string.action_delete_patient));
		bundle.setTextViewMessage(getString(R.string.delete_patient_dialog_message));
		bundle.setRightButtonAction(CustomFragmentDialog.OnClickAction.DELETE_PATIENT);
		bundle.setRightButtonText(getString(R.string.dialog_button_confirm));
		bundle.setLeftButtonAction(CustomFragmentDialog.OnClickAction.DISMISS);
		bundle.setLeftButtonText(getString(R.string.dialog_button_cancel));
		createAndShowDialog(bundle, ApplicationConstants.DialogTAG.DELET_PATIENT_DIALOG_TAG);
	}

	public void createAndShowDialog(CustomDialogBundle bundle, String tag) {
		CustomFragmentDialog instance = CustomFragmentDialog.newInstance(bundle);
		instance.show(fragmentManager, tag);
	}

	public void moveUnauthorizedUserToLoginScreen() {
		openMRS.clearUserPreferencesData();
		Intent intent = new Intent(this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
	}

	public void showProgressDialog(int dialogMessageId) {
		showProgressDialog(getString(dialogMessageId));
	}

	public void dismissCustomFragmentDialog() {
		if (customFragmentDialog != null) {
			customFragmentDialog.dismiss();
		}
	}

	protected void showProgressDialog(String dialogMessage) {
		CustomDialogBundle bundle = new CustomDialogBundle();
		bundle.setProgressViewMessage(getString(R.string.progress_dialog_message));
		bundle.setProgressDialog(true);
		bundle.setTitleViewMessage(dialogMessage);
		customFragmentDialog = CustomFragmentDialog.newInstance(bundle);
		customFragmentDialog.setCancelable(false);
		customFragmentDialog.setRetainInstance(true);
		customFragmentDialog.show(fragmentManager, dialogMessage);
	}

	public void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
			@NonNull Fragment fragment, int frameId) {
		checkNotNull(fragmentManager);
		checkNotNull(fragment);
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(frameId, fragment);
		transaction.commit();
	}

	protected void intitializeToolbar() {
		toolbar = (Toolbar)findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
		}
	}

	private void intitializeNavigationDrawer() {
		drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
		toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.label_open, R.string.label_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
		Menu menu = navigationView.getMenu();
		MenuItem logoutMenuItem = menu.findItem(R.id.navLogout);
		if (logoutMenuItem != null) {
			logoutMenuItem.setTitle(getString(R.string.action_logout) + " " + openMRS.getUsername());
		}
		navigationView.setNavigationItemSelectedListener(this);

		View privacyPolicy = findViewById(R.id.privacyPolicy);
		privacyPolicy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_privacy_policy)));
				startActivity(browserIntent);
			}
		});
	}

	protected void disableActionBarNavigation() {
		toggle.setDrawerIndicatorEnabled(false);
		toggle.syncState();
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		int selectedId = item.getItemId();
		drawer.closeDrawer(GravityCompat.START);
		openActivity(selectedId);
		return true;
	}

	private void openActivity(int selectedId) {
		drawer.closeDrawer(GravityCompat.START);
		switch (selectedId) {
			case R.id.navItemFindPatientRecord:
				Intent intent = new Intent(this, FindPatientRecordActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				break;
			case R.id.navItemPatientLists:
				intent = new Intent(this, PatientListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
				break;
			case R.id.navLogout:
				showLogoutDialog();
				break;
			default:
				break;
		}
	}

	public void createSnackbar(String message) {
		int colorWhite = ContextCompat.getColor(this, R.color.color_white);
		// create instance
		Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE);
		// set action button color
		snackbar.setActionTextColor(colorWhite);
		// get snackbar view
		View snackbarView = snackbar.getView();
		// change snackbar text color
		int snackbarTextId = com.google.android.material.R.id.snackbar_text;
		TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
		textView.setTextColor(colorWhite);
		// change snackbar background
		//snackbarView.setBackgroundColor(Color.MAGENTA);
		//change button text
		snackbar.setActionTextColor(Color.YELLOW);
		snackbar.setAction(R.string.label_dismiss, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snackbar.dismiss();
			}
		});
		snackbar.show();
	}

	public void createToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

	public static void hideSoftKeyboard(Activity activity) {
		if (activity == null) {
			return;
		}
		InputMethodManager inputMethodManager =
				(InputMethodManager)activity.getSystemService(
						Activity.INPUT_METHOD_SERVICE);
		View windowToken = activity.getCurrentFocus();

		if (windowToken != null) {
			inputMethodManager.hideSoftInputFromWindow(
					windowToken.getWindowToken(), 0);
		}
	}

	public static void showSoftKeyboard(Activity activity) {
		if (activity == null) {
			return;
		}
		InputMethodManager inputMethodManager =
				(InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View windowToken = activity.getCurrentFocus();

		if (windowToken != null) {
			inputMethodManager.showSoftInput(windowToken, inputMethodManager.SHOW_IMPLICIT);
		}
	}

	protected void runAfterPageDisplayedToUser(View view, Runnable runnable) {

		view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// At this point, the layout is complete and has been drawn. Put it in a handler to ensure the UI thread
				// is not slowed by this runnable's execution
				new Handler().post(runnable);
				view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
			}
		});
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}
}
