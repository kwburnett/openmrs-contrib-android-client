/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.mobile.activities.addeditvisit;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.auditdata.AuditDataActivity;
import org.openmrs.mobile.activities.patientdashboard.PatientDashboardActivity;
import org.openmrs.mobile.activities.patientheader.PatientHeaderFragment;
import org.openmrs.mobile.activities.patientheader.PatientHeaderPresenter;
import org.openmrs.mobile.activities.visit.VisitActivity;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.StringUtils;
import org.openmrs.mobile.utilities.TabUtil;
import org.openmrs.mobile.utilities.ToastUtil;

public class AddEditVisitActivity extends ACBaseActivity implements AddEditVisitFragment.OnFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLayoutInflater().inflate(R.layout.activity_add_edit_visit, frameLayout);

		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
		toolbar.setTitle(ApplicationConstants.EMPTY_STRING);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setElevation(0);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
		}
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String patientUuid =
					extras.getString(ApplicationConstants.BundleKeys.PATIENT_UUID_BUNDLE, ApplicationConstants
							.EMPTY_STRING);
			String visitUuid =
					extras.getString(ApplicationConstants.BundleKeys.VISIT_UUID_BUNDLE, ApplicationConstants.EMPTY_STRING);
			boolean areEndingVisit = extras.getBoolean(ApplicationConstants.BundleKeys.END_VISIT, false);
			if (StringUtils.notEmpty(patientUuid)) {
				AddEditVisitFragment addEditVisitFragment =
						(AddEditVisitFragment) fragmentManager.findFragmentById(R.id.addeditVisitContentFrame);
				if (addEditVisitFragment == null) {
					addEditVisitFragment = AddEditVisitFragment.newInstance(patientUuid, visitUuid, areEndingVisit);
				}

				if (!addEditVisitFragment.isActive()) {
					addFragmentToActivity(fragmentManager, addEditVisitFragment, R.id.addeditVisitContentFrame);
				}

				// patient header
				PatientHeaderFragment headerFragment =
						(PatientHeaderFragment) fragmentManager.findFragmentById(R.id.patientHeader);
				if (headerFragment == null) {
					headerFragment = PatientHeaderFragment.newInstance();
				}

				if (!headerFragment.isActive()) {
					addFragmentToActivity(fragmentManager, headerFragment, R.id.patientHeader);
				}

				new PatientHeaderPresenter(headerFragment, patientUuid);

			} else {
				ToastUtil.error(getString(R.string.no_patient_selected));
			}
		}
	}

	@Override
	public void onConfigurationChanged(final Configuration config) {
		super.onConfigurationChanged(config);
		TabUtil.setHasEmbeddedTabs(getSupportActionBar(), getWindowManager(),
				TabUtil.MIN_SCREEN_WIDTH_FOR_VISITDETAILSACTIVITY);
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		}
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void endVisit(String patientUuid) {
		finish();
		Intent intent = new Intent(this, PatientDashboardActivity.class);
		intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_UUID_BUNDLE, patientUuid);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void visitStarted(String patientUuid, String visitUuid) {
		goToVisitPage(patientUuid, visitUuid);
	}

	@Override
	public void visitUpdated(String patientUuid, String visitUuid) {
		goToVisitPage(patientUuid, visitUuid);
	}

	@Override
	public void completeAuditDataBeforeEndingVisit(String patientUuid, String visitUuid) {
		finish();
		Intent intent = new Intent(this, AuditDataActivity.class);
		intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_UUID_BUNDLE, patientUuid);
		intent.putExtra(ApplicationConstants.BundleKeys.VISIT_UUID_BUNDLE, visitUuid);
		startActivity(intent);
		ToastUtil.notifyLong(getString(R.string.complete_audit_data_form));
	}

	private void goToVisitPage(String patientUuid, String visitUuid) {
		finish();
		Intent intent = new Intent(this, VisitActivity.class);
		intent.putExtra(ApplicationConstants.BundleKeys.PATIENT_UUID_BUNDLE, patientUuid);
		intent.putExtra(ApplicationConstants.BundleKeys.VISIT_UUID_BUNDLE, visitUuid);
		startActivity(intent);
	}
}
