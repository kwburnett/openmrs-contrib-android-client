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

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseFragment;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.models.BaseOpenmrsObject;
import org.openmrs.mobile.models.ConceptAnswer;
import org.openmrs.mobile.models.Resource;
import org.openmrs.mobile.models.VisitAttribute;
import org.openmrs.mobile.models.VisitAttributeType;
import org.openmrs.mobile.models.VisitType;
import org.openmrs.mobile.utilities.DateUtils;
import org.openmrs.mobile.utilities.StringUtils;
import org.openmrs.mobile.utilities.ViewUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEditVisitFragment extends ACBaseFragment<AddEditVisitContract.Presenter>
		implements AddEditVisitContract.View {

	private static final String ARG_PATIENT_UUID = "patientUuid";
	private static final String ARG_VISIT_UUID = "visitUuid";
	private static final String ARG_ARE_ENDING_VISIT = "areEndingVisit";

	private boolean areEndingVisit;

	private OnFragmentInteractionListener listener;

	private static TableRow.LayoutParams marginParams;
	private TableLayout visitTableLayout;
	private RelativeLayout progressBar, addEditVisitProgressBar;
	private LinearLayout addEditVisitScreen;
	private Spinner visitTypeDropdown;
	private Button visitSubmitButton;
	private Map<String, VisitAttribute> visitAttributeMap = new HashMap<>();
	private Map<View, VisitAttributeType> viewVisitAttributeTypeMap = new HashMap<>();
	private String patientUuid, visitUuid, providerUuid, visitStopDate;
	private EditText visitStartDateInput, visitEndDateInput;
	private TextView visitStartDateLabel, visitEndDateLabel;
	private TableRow visitTypeRow;

	public static AddEditVisitFragment newInstance(String patientUuid, String visitUuid, Boolean areEndingVisit) {
		AddEditVisitFragment fragment = new AddEditVisitFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PATIENT_UUID, patientUuid);
		args.putString(ARG_VISIT_UUID, visitUuid);
		args.putBoolean(ARG_ARE_ENDING_VISIT, areEndingVisit);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			patientUuid = getArguments().getString(ARG_PATIENT_UUID);
			visitUuid = getArguments().getString(ARG_VISIT_UUID);
			areEndingVisit = getArguments().getBoolean(ARG_ARE_ENDING_VISIT);
			presenter = new AddEditVisitPresenter(this, patientUuid, visitUuid, areEndingVisit);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.fragment_add_edit_visit, container, false);
		visitTableLayout = (TableLayout)root.findViewById(R.id.visitTableLayout);
		progressBar = (RelativeLayout)root.findViewById(R.id.visitLoadingProgressBar);
		addEditVisitProgressBar = (RelativeLayout)root.findViewById(R.id.addEditVisitProgressBar);
		addEditVisitScreen = (LinearLayout)root.findViewById(R.id.addEditVisitScreen);
		visitTypeDropdown = (Spinner)root.findViewById(R.id.visit_type);
		visitSubmitButton = (Button)root.findViewById(R.id.visitSubmitButton);
		visitStartDateLabel = (TextView)root.findViewById(R.id.visitStartDateLabel);
		visitEndDateLabel = (TextView)root.findViewById(R.id.visitEndDateLabel);
		visitStartDateInput = (EditText)root.findViewById(R.id.visitStartDateInput);
		visitEndDateInput = (EditText)root.findViewById(R.id.visitEndDateInput);
		visitTypeRow = (TableRow)root.findViewById(R.id.visitTypeRow);

		addListeners();
		buildMarginLayout();

		return root;
	}

	private void addListeners() {
		visitSubmitButton.setOnClickListener(v -> {
			if (!presenter.isProcessing()) {
				buildVisitAttributeValues();
			}
		});

		visitStartDateInput.setOnClickListener(v -> {
			DateTime dateTime = DateUtils.convertTimeString(
					DateUtils.convertTime(presenter.getVisit().getStartDatetime().getTime(),
							DateUtils.OPEN_MRS_REQUEST_FORMAT));

			createVisitDatePicker(dateTime, System.currentTimeMillis(), true);
		});

		visitEndDateInput.setOnClickListener(v -> {
			if (presenter.getVisit().getStopDatetime() != null) {
				DateTime dateTime = DateUtils.convertTimeString(
						DateUtils.convertTime(presenter.getVisit().getStopDatetime().getTime(),
								DateUtils.OPEN_MRS_REQUEST_FORMAT));

				createVisitDatePicker(dateTime, 0, false);
			}
		});
	}

	private void createVisitDatePicker(DateTime dateTime, long maxDate, boolean startDate) {
		if (context == null) {
			return;
		}

		int currentYear = dateTime.getYear();
		int currentMonth = dateTime.getMonthOfYear() - 1;
		int currentDay = dateTime.getDayOfMonth();

		DatePickerDialog mDatePicker = new DatePickerDialog(context,
				(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) -> {
					if (startDate) {
						presenter.getVisit()
								.setStartDatetime(DateUtils.constructDate(selectedyear, selectedmonth, selectedday));
						visitStartDateInput.setText(DateUtils.convertTime(presenter.getVisit().getStartDatetime()
										.getTime(),
								DateUtils.OPEN_MRS_REQUEST_PATIENT_FORMAT));
					} else {
						presenter.getVisit()
								.setStopDatetime(DateUtils.constructDate(selectedyear, selectedmonth, selectedday));
						visitEndDateInput.setText(DateUtils.convertTime(presenter.getVisit().getStopDatetime().getTime(),
								DateUtils.OPEN_MRS_REQUEST_PATIENT_FORMAT));
					}
				}, currentYear, currentMonth, currentDay);

		if (maxDate > 0)
			mDatePicker.getDatePicker().setMaxDate(maxDate);

		mDatePicker.show();
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			listener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void initView(boolean startVisit) {
		Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
		if (startVisit) {
			toolbar.setTitle(getString(R.string.label_start_visit));
		} else {
			visitSubmitButton.setText(R.string.update_visit);
			toolbar.setTitle(getString(R.string.label_edit_visit));
		}

		visitStartDateInput.setText(
				DateUtils.convertTime(presenter.getVisit().getStartDatetime().getTime(),
						DateUtils.OPEN_MRS_REQUEST_PATIENT_FORMAT));

		if (presenter.getVisit().getStopDatetime() != null) {
			visitEndDateLabel.setVisibility(View.VISIBLE);
			visitEndDateInput.setVisibility(View.VISIBLE);
			visitEndDateInput.setText(
					DateUtils.convertTime(presenter.getVisit().getStopDatetime().getTime(),
							DateUtils.OPEN_MRS_REQUEST_PATIENT_FORMAT));
			if (presenter.isEndVisit()) {
				visitStartDateInput.setVisibility(View.GONE);
				visitStartDateLabel.setVisibility(View.GONE);
				loadEndVisitView();
			}
		}

		setSpinnerVisibility(false);
	}

	@Override
	public void loadEndVisitView() {
		showPageSpinner(false);
		visitSubmitButton.setText(R.string.label_end_visit);
		visitTypeRow.setVisibility(View.GONE);

		visitSubmitButton.setOnClickListener(v -> {
			if (!presenter.isProcessing()) {
				presenter.endVisit();
			}
		});

	}

	private void buildVisitAttributeValues() {
		for (Map.Entry<View, VisitAttributeType> set : viewVisitAttributeTypeMap.entrySet()) {
			View componentType = set.getKey();
			VisitAttribute visitAttribute = new VisitAttribute();
			visitAttribute.setAttributeType(set.getValue());

			if (componentType instanceof RadioButton) {
				visitAttribute.setValue(((RadioButton)componentType).isChecked() ? "true" : "false");
			} else if (componentType instanceof EditText) {
				String value = ViewUtils.getInput((EditText)componentType);
				visitAttribute.setValue(value != null ? value : " ");
			}

			if (visitAttribute.getValue() != null) {
				visitAttributeMap.put(set.getValue().getUuid(), visitAttribute);
			}
		}

		if (!presenter.isProcessing()) {
			setSpinnerVisibility(true);
			if (Resource.isLocalUuid(presenter.getVisit().getUuid())) {
				presenter.startVisit(new ArrayList<>(visitAttributeMap.values()));
			} else {
				String visitEndDate = null;
				if (visitEndDateInput.getText() != null) {
					visitEndDate = visitEndDateInput.getText().toString();
				}
				presenter.updateVisit(visitEndDate, new ArrayList<>(visitAttributeMap.values()));
			}
		}
	}

	@Override
	public void loadVisitAttributeTypeFields(List<VisitAttributeType> visitAttributeTypes) {
		if (context == null) {
			return;
		}
		try {
			for (VisitAttributeType visitAttributeType : visitAttributeTypes) {
				TableRow row = new TableRow(context);
				row.setPadding(0, 20, 0, 10);
				TextView label = new TextView(context);
				label.setText(visitAttributeType.getDisplay() + ":");
				label.setTextSize(17);
				label.setTextColor(getResources().getColor(R.color.dark_grey));
				row.addView(label, 0);

				String datatypeClass = visitAttributeType.getDatatypeClassname();
				if (StringUtils.isBlank(datatypeClass)) {
					continue;
				}

				if (datatypeClass.equalsIgnoreCase("org.openmrs.customdatatype.datatype.BooleanDatatype")) {
					RadioButton booleanType = new RadioButton(context);
					booleanType.setLayoutParams(marginParams);

					// set default value
					Boolean defaultValue = new Boolean(presenter.searchVisitAttributeValueByType(visitAttributeType));
					if (defaultValue != null) {
						booleanType.setChecked(defaultValue);
					}

					row.addView(booleanType, 1);
					viewVisitAttributeTypeMap.put(booleanType, visitAttributeType);
				} else if (datatypeClass.equalsIgnoreCase("org.openmrs.customdatatype.datatype.DateDatatype")) {
					EditText dateType = new EditText(context);
					dateType.setFocusable(true);
					dateType.setTextSize(17);
					dateType.setLayoutParams(marginParams);

					// set default value
					String defaultValue = presenter.searchVisitAttributeValueByType(visitAttributeType);
					if (StringUtils.notEmpty(defaultValue)) {
						dateType.setText(defaultValue);
					}
					row.addView(dateType, 1);
					viewVisitAttributeTypeMap.put(dateType, visitAttributeType);
				} else if (datatypeClass
						.equalsIgnoreCase("org.openmrs.module.coreapps.customdatatype.CodedConceptDatatype")) {
					// get coded concept uuid
					String conceptUuid = visitAttributeType.getDatatypeConfig();
					Spinner conceptAnswersDropdown = new Spinner(context);
					conceptAnswersDropdown.setLayoutParams(marginParams);
					presenter.getConceptAnswer(conceptUuid, conceptAnswersDropdown);
					row.addView(conceptAnswersDropdown, 1);
					viewVisitAttributeTypeMap.put(conceptAnswersDropdown, visitAttributeType);
				} else if (datatypeClass.equalsIgnoreCase("org.openmrs.customdatatype.datatype.FreeTextDatatype")) {
					EditText freeTextType = new EditText(context);
					freeTextType.setFocusable(true);
					freeTextType.setTextSize(17);
					freeTextType.setLayoutParams(marginParams);

					// set default value
					String defaultValue = presenter.searchVisitAttributeValueByType(visitAttributeType);
					if (StringUtils.notEmpty(defaultValue)) {
						freeTextType.setText(defaultValue.trim());
					}

					row.addView(freeTextType, 1);
					viewVisitAttributeTypeMap.put(freeTextType, visitAttributeType);
				}

				visitTableLayout.addView(row);
			}
		} catch (Exception e) {
			// There was probably an instance with the context being null in the for loop, so log it and don't crash the
			// app
			logger.e(e);
		}
	}

	@Override
	public void updateConceptAnswersView(Spinner conceptNamesDropdown, List<ConceptAnswer> conceptAnswers) {
		if (context == null) {
			return;
		}

		VisitAttributeType visitAttributeType = viewVisitAttributeTypeMap.get(conceptNamesDropdown);
		ArrayAdapter<ConceptAnswer> conceptNameArrayAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_dropdown_item, conceptAnswers);
		conceptNamesDropdown.setAdapter(conceptNameArrayAdapter);

		// set existing visit attribute if any
		String visitTypeUuid = presenter.searchVisitAttributeValueByType(visitAttributeType);
		if (visitTypeUuid != null) {
			setDefaultDropdownSelection(conceptNameArrayAdapter, visitTypeUuid, conceptNamesDropdown);
		}

		conceptNamesDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ConceptAnswer conceptAnswer = conceptAnswers.get(position);
				VisitAttribute visitAttribute = new VisitAttribute();
				visitAttribute.setValue(conceptAnswer.getUuid());
				visitAttribute.setAttributeType(visitAttributeType);
				removeVisitAttributeTypeInMap(visitAttributeType, visitAttributeMap);
				visitAttributeMap.put(conceptAnswer.getUuid(), visitAttribute);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	private void removeVisitAttributeTypeInMap(VisitAttributeType visitAttributeType,
			Map<String, VisitAttribute> visitAttributeMap) {
		if (visitAttributeMap == null || visitAttributeType == null) {
			return;
		}

		for (Map.Entry<String, VisitAttribute> set : visitAttributeMap.entrySet()) {
			if (set.getValue().getAttributeType().equals(visitAttributeType)) {
				visitAttributeMap.remove(set.getKey());
				break;
			}
		}
	}

	@Override
	public void updateVisitTypes(List<VisitType> visitTypes) {
		if (context == null) {
			return;
		}

		ArrayAdapter<VisitType> visitTypeArrayAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_dropdown_item, visitTypes);
		visitTypeDropdown.setAdapter(visitTypeArrayAdapter);

		// set existing visit type if any
		if (presenter.getVisit().getVisitType() != null) {
			setDefaultDropdownSelection(visitTypeArrayAdapter, presenter.getVisit().getVisitType().getUuid(),
					visitTypeDropdown);
		}

		visitTypeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				presenter.getVisit().setVisitType(visitTypes.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}

	@Override
	public void setSpinnerVisibility(boolean visible) {
		progressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	@Override
	public boolean isActive() {
		return isAdded();
	}

	@Override
	public void endVisitComplete() {
		if (listener != null) {
			listener.endVisit(patientUuid);
		}
	}

	@Override
	public void startVisitComplete(String visitUuid) {
		if (listener != null) {
			listener.visitStarted(patientUuid, visitUuid);
		}
	}

	@Override
	public void updateVisitComplete(String visitUuid, boolean isNewInstance) {
		visitSubmitButton.setEnabled(false);
		if (listener != null) {
			listener.visitUpdated(patientUuid, visitUuid);
		}
	}

	@Override
	public void auditDataNotCompleted(String visitUuid) {
		if (listener != null) {
			listener.completeAuditDataBeforeEndingVisit(patientUuid, visitUuid);
		}
	}

	@Override
	public void showPageSpinner(boolean visibility) {
		if (visibility) {
			addEditVisitProgressBar.setVisibility(View.VISIBLE);
			addEditVisitScreen.setVisibility(View.GONE);
		} else {
			addEditVisitProgressBar.setVisibility(View.GONE);
			addEditVisitScreen.setVisibility(View.VISIBLE);
		}
	}

	private void buildMarginLayout() {
		if (marginParams == null) {
			marginParams = new TableRow.LayoutParams(
					TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
			marginParams.setMargins(70, 0, 0, 0);
		}
	}

	private <T extends BaseOpenmrsObject> void setDefaultDropdownSelection(ArrayAdapter<T> arrayAdapter, String searchUuid,
			Spinner dropdown) {
		for (int count = 0; count < arrayAdapter.getCount(); count++) {
			if (arrayAdapter.getItem(count).getUuid().equalsIgnoreCase(searchUuid)) {
				dropdown.setSelection(count);
			}
		}
	}

	public interface OnFragmentInteractionListener {

		void endVisit(String patientUuid);

		void visitStarted(String patientUuid, String visitUuid);

		void visitUpdated(String patientUuid, String visitUuid);

		void completeAuditDataBeforeEndingVisit(String patientUuid, String visitUuid);
	}
}
