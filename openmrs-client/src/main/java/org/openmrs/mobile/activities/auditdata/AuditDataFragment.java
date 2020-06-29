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

package org.openmrs.mobile.activities.auditdata;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.joda.time.LocalDateTime;
import org.openmrs.mobile.R;
import org.openmrs.mobile.activities.ACBaseActivity;
import org.openmrs.mobile.activities.ACBaseFragment;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.models.Concept;
import org.openmrs.mobile.models.ConceptAnswer;
import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.EncounterType;
import org.openmrs.mobile.models.Form;
import org.openmrs.mobile.models.Location;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Person;
import org.openmrs.mobile.models.Provider;
import org.openmrs.mobile.models.Resource;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.FontsUtil;
import org.openmrs.mobile.utilities.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormAnswers.ANSWER_NEGATIVE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormAnswers.ANSWER_NO;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_FIRST_RESPIRATORY_RATE_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_INTUBATION_AT_GCS;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE;

import static org.openmrs.mobile.utilities.ApplicationConstants.ObservationLocators.SCHEDULED_IN_CLINIC;
import static org.openmrs.mobile.utilities.ApplicationConstants.ObservationLocators.NOT_SCHEDULED_IN_CLINIC;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormAnswers.ANSWER_POSITIVE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormAnswers.ANSWER_UNKNOWN;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormAnswers.ANSWER_YES;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_NEGATIVE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_NO;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_PLANNED;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_POSITIVE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_UNKNOWN;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_UNPLANNED;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ANSWER_YES;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_AUDIT_COMPLETE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_CD4_COUNT;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_DEATH_IN_HOSPITAL;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_FIRST_GCS_SCORE_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_FIRST_HEART_RATE_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_FIRST_MAP_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_FIRST_SBP_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_HBA1C;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_HDU_COMGMT;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_HDU_STAY;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_HIV_STATUS;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_ICU_STAY;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_INFECTION_CONFIRMED_SUSPECTED;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_INPATIENT_SERVICE_TYPE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_CLASSIFICATION_OF_DEATH_TYPE;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_MECHANICAL_VENTILATIN;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_PALLIATIVE_CONSULT;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_PATIENT_DIABETIC;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_PREOP_RISK_ASSESMENT;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_RECIEVED_VAOSPRESSORS;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_SURGERY_HOSPITAL_STAY;
import static org.openmrs.mobile.utilities.ApplicationConstants.AuditFormConcepts.CONCEPT_WARD_STAY_DURING_ADMISSION;
import static org.openmrs.mobile.utilities.ApplicationConstants.EncounterTypeDisplays.AUDITDATA;
import static org.openmrs.mobile.utilities.ApplicationConstants.EncounterTypeEntity.AUDIT_DATA_UUID;
import static org.openmrs.mobile.utilities.ApplicationConstants.FORM_UUIDS.AUDIT_DATA_FORM_UUID;
import static org.openmrs.mobile.utilities.ApplicationConstants.ObservationLocators.SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE;
import static org.openmrs.mobile.utilities.ApplicationConstants.ValidationFieldValues.AUDIT_1ST_RESPIRATORY_RATE_MAX;
import static org.openmrs.mobile.utilities.ApplicationConstants.ValidationFieldValues.AUDIT_1ST_RESPIRATORY_RATE_MIN;

public class AuditDataFragment extends ACBaseFragment<AuditDataContract.Presenter>
		implements AuditDataContract.View {

	private static final String ARG_PATIENT_UUID = "patientUuid";
	private static final String ARG_VISIT_UUID = "visitUuid";

	private OnFragmentInteractionListener listener;

	private Visit visit;
	//private Patient patient;
	private View fragmentView;
	private EditText cd4, hBa1c, firstIcuHeartRate, firstIcuRespiratoryRate, firstGcsScore;
	private String encounterUuid = null;
	private String visitUuid, patientUuid, locationUuid;
	private OpenMRS instance = OpenMRS.getInstance();

	private Observation deathInHospitalObservation, palliativeConsultObservation, preopRiskAssessmentObservation,
			icuStayObservation, hduStayObservation, hduComgmtObservation, hivPositiveObservation, cd4Observation,
			hBa1cObservation, inpatientServiceTypeObservation, auditCompleteObservation, mechanicalVentilationObservation,
			vaospressorsObservation, confirmedInfectionObservation, firstSbpObservation, firstMapObservation,
			priorSedetionObservation, surgeryObservation, firstIcuHeartRateObservation, firstGcsScoreObservation,
			patientDiabeticObservation, wardStayAdmissionObservation, firstIcuRespiratoryRateObservation,
			intubationObservation, signedOffOnConsultBeforeDischargeObservation;

	// andr-414 Create Audit Data "death in hospital" options
	private Observation classificationOfDeathTypeObservation;

	private RadioButton deathInHospitalYes, deathInHospitalNo, palliativeConsultYes, palliativeConsultNo,
			palliativeConsultUknown, preopRiskAssessmentYes, preopRiskAssessmentNo, preopRiskAssessmentUknown, icuStayYes,
			icuStayNo, icuStayUnknown, hduStayYes, hduStayNo, hduStayUnknown, hduComgmtYes, hduComgmtNo, hduComgmtUnknown,
			hivPositiveYes, hivPositiveNo, hivPositiveUnknown, mechanical_ventilation_yes, mechanical_ventilation_no,
			mechanical_ventilation_unknown, vaospressors_yes, vaospressors_no, vaospressors_unknown,
			confirmed_infection_yes, confirmed_infection_no, confirmed_infection_unknown, first_sbp_yes, first_sbp_no,
			first_sbp_unknown, any_prior_sedetion_yes, any_prior_sedetion_no, any_prior_sedetion_unknown, surgery_na,
			surgery_planned, surgery_unplanned, first_map_yes, first_map_no, first_map_unknown, ward_stay_admission_yes,
			ward_stay_admission_no, ward_stay_admission_unknown, patient_diabetic_yes, patient_diabetic_no,
			patient_diabetic_unknown, intubatedYes, intubatedNo, intubatedUnknown,
			signed_off_on_consult_before_discharge_yes, signed_off_on_consult_before_discharge_no, auditCompleteYes, auditCompleteNo;

	// andr-414
	private Spinner classificationOfDeathType;

	private Spinner inpatientServiceType;
	private RelativeLayout progressBar, auditDataFormProgressBar;
	private LinearLayout auditDataFormScreen, extraFormAdditions, hduCoManage;
	private ScrollView auditScrollView;
	private Button submitForm;
	private List<ConceptAnswer> conceptAnswerList, classificationOfDeathList;
	private String inpatientServiceTypeSelectedUuid, displayInpatientServiceType;
	private Boolean displayExtraFormFields, displayCd4CountField, displayHbA1CField, displayHduCoManageField;
	private TextInputLayout hba1cTextLayout, cd4TextInputLayout;
	private TextView errorFirstGcsScore, errorHba1c, errorFirstRespiratoryRate;

	private ConceptAnswer initialInpatientTypeServiceSelection, initialClassificationOfDeathSelection;

	private String encounterInpatientService;

	private String encounterClassificationOfDeath;

	// andr-414
	private String classificationOfDeathTypeSelectedUuid;

	private Encounter visitEncounter;

	public static AuditDataFragment newInstance(String patientUuid, String visitUuid) {
		AuditDataFragment fragment = new AuditDataFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PATIENT_UUID, patientUuid);
		args.putString(ARG_VISIT_UUID, visitUuid);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			patientUuid = getArguments().getString(ARG_PATIENT_UUID);
			visitUuid = getArguments().getString(ARG_VISIT_UUID);
			presenter = new AuditDataPresenter(this, visitUuid);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		fragmentView = inflater.inflate(R.layout.fragment_audit_form, container, false);
		presenter.fetchInpatientTypeServices();

		initViewFields();

		initRadioButtonListeners(deathInHospitalYes, deathInHospitalNo, palliativeConsultYes, palliativeConsultNo,
				palliativeConsultUknown, preopRiskAssessmentYes, preopRiskAssessmentNo, preopRiskAssessmentUknown,
				icuStayYes, icuStayNo, icuStayUnknown, hduStayYes, hduStayNo, hduStayUnknown, hduComgmtYes, hduComgmtNo,
				hduComgmtUnknown, hivPositiveYes, hivPositiveNo, hivPositiveUnknown, mechanical_ventilation_yes,
				mechanical_ventilation_no, mechanical_ventilation_unknown, vaospressors_yes, vaospressors_no,
				vaospressors_unknown, confirmed_infection_yes, confirmed_infection_no, confirmed_infection_unknown,
				first_sbp_yes, first_sbp_no, first_sbp_unknown, any_prior_sedetion_yes, any_prior_sedetion_no,
				any_prior_sedetion_unknown, surgery_na, surgery_planned, surgery_unplanned, first_map_yes, first_map_no,
				first_map_unknown, ward_stay_admission_yes, ward_stay_admission_no, ward_stay_admission_unknown,
				patient_diabetic_yes, patient_diabetic_no, patient_diabetic_unknown, intubatedYes, intubatedNo,
				intubatedUnknown, signed_off_on_consult_before_discharge_yes, signed_off_on_consult_before_discharge_no, auditCompleteYes, auditCompleteNo);

		//We start by fetching by location, required for creating encounters
		if (!instance.getLocation().equalsIgnoreCase(null)) {
			locationUuid = instance.getParentLocationUuid();
		}

		initObservations();
		initClassificationOfDeathOptions();
		addListeners();

		initialInpatientTypeServiceSelection = new ConceptAnswer();
		initialInpatientTypeServiceSelection.setDisplay(getString(R.string.inpatient_service_type_prompt));

		initialClassificationOfDeathSelection= new ConceptAnswer();
		initialClassificationOfDeathSelection.setDisplay(getString(R.string.classification_of_death));

		displayExtraFormFields = false;
		// Font config
		FontsUtil.setFont((ViewGroup)this.context.findViewById(android.R.id.content));

		return fragmentView;
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

	private void addListeners() {
		inpatientServiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ConceptAnswer conceptAnswer = conceptAnswerList.get(position);

				if (conceptAnswer == initialInpatientTypeServiceSelection) {
					if (inpatientServiceTypeObservation != null) {
						setObservationVoided(inpatientServiceTypeObservation);
					}
					return;
				}

				inpatientServiceTypeSelectedUuid = conceptAnswer.getUuid();
				inpatientServiceTypeObservation = setObservationFields(inpatientServiceTypeObservation,
						CONCEPT_INPATIENT_SERVICE_TYPE, conceptAnswer.getUuid(),
						ApplicationConstants.ObservationLocators.TYPE_OF_INPATIENT_SERVICE + conceptAnswer.getDisplay());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		classificationOfDeathType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				ConceptAnswer conceptAnswer = classificationOfDeathList.get(position);

				if (conceptAnswer != null && initialClassificationOfDeathSelection != null &&
						conceptAnswer.getDisplay().equalsIgnoreCase(initialClassificationOfDeathSelection.getDisplay())) {
					if (classificationOfDeathTypeObservation != null) {
						setObservationVoided(classificationOfDeathTypeObservation);
					}
					return;
				}

				classificationOfDeathTypeSelectedUuid = conceptAnswer.getUuid();
				classificationOfDeathTypeObservation = setObservationFields(classificationOfDeathTypeObservation,
						CONCEPT_CLASSIFICATION_OF_DEATH_TYPE, conceptAnswer.getUuid(),
						ApplicationConstants.ObservationLocators.CLASSIFICATION_OF_DEATH_TYPE + conceptAnswer.getDisplay());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		firstGcsScore.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int startPos, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				hasValidGcsScore();
			}
		});

		hBa1c.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int startPos, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				validateHba1c();
			}
		});

		firstIcuRespiratoryRate.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validateFirstRespiratoryRate();
			}
		});

		submitForm.setOnClickListener(v -> {
			if (hasValidGcsScore() && validateFirstRespiratoryRate()) {
				performDataSend();
			}
		});
	}

	private void initViewFields() {

		deathInHospitalYes = fragmentView.findViewById(R.id.is_death_in_hospital_yes);
		deathInHospitalNo = fragmentView.findViewById(R.id.is_death_in_hospital_no);
		palliativeConsultYes = fragmentView.findViewById(R.id.is_palliative_consult_yes);
		palliativeConsultNo = fragmentView.findViewById(R.id.is_palliative_consult_no);
		palliativeConsultUknown = fragmentView.findViewById(R.id.is_palliative_consult_unknown);
		preopRiskAssessmentYes = fragmentView.findViewById(R.id.is_preop_risk_assessment_only_yes);
		preopRiskAssessmentNo = fragmentView.findViewById(R.id.is_preop_risk_assessment_only_no);
		preopRiskAssessmentUknown = fragmentView.findViewById(R.id.is_preop_risk_assessment_only_unknown);
		icuStayYes = fragmentView.findViewById(R.id.is_icu_stay_yes);
		icuStayNo = fragmentView.findViewById(R.id.is_icu_stay_no);
		icuStayUnknown = fragmentView.findViewById(R.id.is_icu_stay_unknown);
		hduStayYes = fragmentView.findViewById(R.id.is_hdu_stay_yes);
		hduStayNo = fragmentView.findViewById(R.id.is_hdu_stay_no);
		hduStayUnknown = fragmentView.findViewById(R.id.is_hdu_stay_unknown);
		hduComgmtYes = fragmentView.findViewById(R.id.is_hdu_comgmt_yes);
		hduComgmtNo = fragmentView.findViewById(R.id.is_hdu_comgmt_no);
		hduComgmtUnknown = fragmentView.findViewById(R.id.is_hdu_comgmt_unknown);
		hivPositiveYes = fragmentView.findViewById(R.id.is_hiv_positive_yes);
		hivPositiveNo = fragmentView.findViewById(R.id.is_hiv_positive_no);
		hivPositiveUnknown = fragmentView.findViewById(R.id.is_hiv_positive_unknown);
		auditCompleteYes = fragmentView.findViewById(R.id.audit_complete_yes);
		auditCompleteNo = fragmentView.findViewById(R.id.audit_complete_no);
		cd4 = fragmentView.findViewById(R.id.cd4);
		hBa1c = fragmentView.findViewById(R.id.hba1c);
		inpatientServiceType = fragmentView.findViewById(R.id.inpatient_service_type);
		submitForm = fragmentView.findViewById(R.id.submitConfirm);
		extraFormAdditions = fragmentView.findViewById(R.id.extraFormAdditions);
		hduCoManage = fragmentView.findViewById(R.id.hduCoManage);
		firstIcuHeartRate = fragmentView.findViewById(R.id.firstIcuHeartRate);
		firstIcuRespiratoryRate = fragmentView.findViewById(R.id.firstIcuRespiratoryRate);
		firstGcsScore = fragmentView.findViewById(R.id.firstGcsScore);
		mechanical_ventilation_yes = fragmentView.findViewById(R.id.mechanical_ventilation_yes);
		mechanical_ventilation_no = fragmentView.findViewById(R.id.mechanical_ventilation_no);
		mechanical_ventilation_unknown = fragmentView.findViewById(R.id.mechanical_ventilation_unknown);
		vaospressors_yes = fragmentView.findViewById(R.id.vaospressors_yes);
		vaospressors_no = fragmentView.findViewById(R.id.vaospressors_no);
		vaospressors_unknown = fragmentView.findViewById(R.id.vaospressors_unknown);
		confirmed_infection_yes = fragmentView.findViewById(R.id.confirmed_infection_yes);
		confirmed_infection_no = fragmentView.findViewById(R.id.confirmed_infection_no);
		confirmed_infection_unknown = fragmentView.findViewById(R.id.confirmed_infection_unknown);
		first_sbp_yes = fragmentView.findViewById(R.id.first_sbp_yes);
		first_sbp_no = fragmentView.findViewById(R.id.first_sbp_no);
		first_sbp_unknown = fragmentView.findViewById(R.id.first_sbp_unknown);
		any_prior_sedetion_yes = fragmentView.findViewById(R.id.any_prior_sedetion_yes);
		any_prior_sedetion_no = fragmentView.findViewById(R.id.any_prior_sedetion_no);
		any_prior_sedetion_unknown = fragmentView.findViewById(R.id.any_prior_sedetion_unknown);
		surgery_na = fragmentView.findViewById(R.id.surgery_na);
		surgery_planned = fragmentView.findViewById(R.id.surgery_planned);
		surgery_unplanned = fragmentView.findViewById(R.id.surgery_unplanned);
		first_map_yes = fragmentView.findViewById(R.id.first_map_yes);
		first_map_no = fragmentView.findViewById(R.id.first_map_no);
		first_map_unknown = fragmentView.findViewById(R.id.first_map_unknown);
		ward_stay_admission_yes = fragmentView.findViewById(R.id.ward_stay_admission_yes);
		ward_stay_admission_no = fragmentView.findViewById(R.id.ward_stay_admission_no);
		ward_stay_admission_unknown = fragmentView.findViewById(R.id.ward_stay_admission_unknown);
		patient_diabetic_yes = fragmentView.findViewById(R.id.patient_diabetic_yes);
		patient_diabetic_no = fragmentView.findViewById(R.id.patient_diabetic_no);
		patient_diabetic_unknown = fragmentView.findViewById(R.id.patient_diabetic_unknown);
		cd4TextInputLayout = fragmentView.findViewById(R.id.cd4TextInputLayout);
		hba1cTextLayout = fragmentView.findViewById(R.id.hba1cTextLayout);
		errorFirstGcsScore = fragmentView.findViewById(R.id.invalidGscError);
		errorFirstRespiratoryRate = fragmentView.findViewById(R.id.invalidFirstRespiratoryRate);
		errorHba1c = fragmentView.findViewById(R.id.invalidHba1cError);
		progressBar = fragmentView.findViewById(R.id.auditDataRelativeView);
		auditDataFormProgressBar = fragmentView.findViewById(R.id.auditDataFormProgressBar);
		auditDataFormScreen = fragmentView.findViewById(R.id.auditDataFormScreen);
		auditScrollView = fragmentView.findViewById(R.id.auditDataFormScrollView);
		intubatedYes = fragmentView.findViewById(R.id.intubationDone);
		intubatedNo = fragmentView.findViewById(R.id.intubationNotDone);
		intubatedUnknown = fragmentView.findViewById(R.id.intubationNotKnown);
		signed_off_on_consult_before_discharge_yes = fragmentView.findViewById(R.id
				.signed_off_on_consult_before_discharge_yes);
		signed_off_on_consult_before_discharge_no = fragmentView.findViewById(R.id
				.signed_off_on_consult_before_discharge_no);

		//andr-414
		classificationOfDeathType = fragmentView.findViewById(R.id.classification_of_death_type);
	}

	private void initObservations() {
		mechanicalVentilationObservation = vaospressorsObservation = confirmedInfectionObservation = firstSbpObservation =
				firstMapObservation = priorSedetionObservation = surgeryObservation = firstIcuHeartRateObservation =
						firstGcsScoreObservation = deathInHospitalObservation = palliativeConsultObservation =
								preopRiskAssessmentObservation = icuStayObservation = hduStayObservation =
										hduComgmtObservation = hivPositiveObservation = firstIcuRespiratoryRateObservation =
												auditCompleteObservation = hBa1cObservation = cd4Observation =
														patientDiabeticObservation = wardStayAdmissionObservation =
																intubationObservation = null;
		signedOffOnConsultBeforeDischargeObservation = null;
	}

	private void initRadioButtonListeners(RadioButton... params) {
		for (RadioButton radioButton : params) {
			radioButton.setOnClickListener(
					view -> applyEvent(radioButton.getId()));
		}
	}

	private void initCheckboxListeners(CheckBox... params) {
		for (CheckBox checkBox : params) {
			checkBox.setOnClickListener(
					view -> applyEvent(checkBox.getId()));
		}
	}

	private void initClassificationOfDeathOptions() {
		classificationOfDeathList = new ArrayList<>();
		classificationOfDeathList.add(new ConceptAnswer("", getString(R.string.classification_of_death)));
		classificationOfDeathList.add(new ConceptAnswer("24718921-c543-40c8-8860-72c8bd2f6042",
				getString(R.string.anticipated_death_following_terminal_illness_name)));
		classificationOfDeathList.add(new ConceptAnswer("b4a4946f-b6d7-42f7-9312-5177cf1f7f07",
				getString(R.string.expected_death_clinical_situation_name)));
		classificationOfDeathList.add(new ConceptAnswer("5cd85418-e409-40cf-a13f-8cbc7cbca206",
				getString(R.string.unexpected_death_not_preventable_name)));
		classificationOfDeathList.add(new ConceptAnswer("31afb19a-40fb-47ee-bc09-86b04d13cd69",
				getString(R.string.preventable_death_name)));
		classificationOfDeathList.add(new ConceptAnswer("0d29fff3-58f5-474c-9ce3-93c1b71f2aab",
				getString(R.string.unexpected_death_medical_intervention_name)));

		if (context != null) {
			ArrayAdapter<ConceptAnswer> adapter = new ArrayAdapter<>(context, android.R.layout
					.simple_spinner_dropdown_item, classificationOfDeathList);
			classificationOfDeathType.setAdapter(adapter);
		}

		int selectedPosition = classificationOfDeathType.getSelectedItemPosition();

		if (selectedPosition >= 0) {
			classificationOfDeathType.setSelection(selectedPosition);
		} else if (!StringUtils.isNullOrEmpty(encounterClassificationOfDeath)) {
			updateInpatientDisplaySelection(encounterClassificationOfDeath);
		}
	}

	private void applyEvent(int id) {
		switch (id) {
			case R.id.is_death_in_hospital_yes:
				deathInHospitalObservation = setObservationFields(deathInHospitalObservation, CONCEPT_DEATH_IN_HOSPITAL,
						CONCEPT_ANSWER_YES, ApplicationConstants.ObservationLocators.HOSPITAL_DEATH +
								ApplicationConstants.ObservationLocators.YES);
				showAnimateView(true, classificationOfDeathType);
				break;

			case R.id.is_death_in_hospital_no:
				deathInHospitalObservation = setObservationFields(deathInHospitalObservation, CONCEPT_DEATH_IN_HOSPITAL,
						CONCEPT_ANSWER_NO, ApplicationConstants.ObservationLocators.HOSPITAL_DEATH +
								ApplicationConstants.ObservationLocators.NO);
				showAnimateView(false, classificationOfDeathType);
				break;

			case R.id.is_palliative_consult_yes:
				palliativeConsultObservation = setObservationFields(palliativeConsultObservation,
						CONCEPT_PALLIATIVE_CONSULT, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.PALLIATIVE_CARE_CONSULT +
								ApplicationConstants.ObservationLocators.YES);

				break;

			case R.id.is_palliative_consult_no:
				palliativeConsultObservation = setObservationFields(palliativeConsultObservation,
						CONCEPT_PALLIATIVE_CONSULT,
						CONCEPT_ANSWER_NO, ApplicationConstants.ObservationLocators.PALLIATIVE_CARE_CONSULT +
								ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.is_palliative_consult_unknown:
				palliativeConsultObservation = setObservationFields(palliativeConsultObservation,
						CONCEPT_PALLIATIVE_CONSULT,
						CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.PALLIATIVE_CARE_CONSULT +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.is_preop_risk_assessment_only_yes:
				preopRiskAssessmentObservation = setObservationFields(preopRiskAssessmentObservation,
						CONCEPT_PREOP_RISK_ASSESMENT, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.PREOP_RISK_ASSESSMENT +
								ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.is_preop_risk_assessment_only_no:
				preopRiskAssessmentObservation = setObservationFields(preopRiskAssessmentObservation,
						CONCEPT_PREOP_RISK_ASSESMENT, CONCEPT_ANSWER_NO,
						ApplicationConstants.ObservationLocators.PREOP_RISK_ASSESSMENT + ApplicationConstants
								.ObservationLocators.NO);
				break;

			case R.id.is_preop_risk_assessment_only_unknown:
				preopRiskAssessmentObservation = setObservationFields(preopRiskAssessmentObservation,
						CONCEPT_PREOP_RISK_ASSESMENT, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.PREOP_RISK_ASSESSMENT +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.is_icu_stay_yes:
				icuStayObservation = setObservationFields(icuStayObservation,
						CONCEPT_ICU_STAY, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.ICU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.YES);
				showAnimateView(true, extraFormAdditions);
				break;

			case R.id.is_icu_stay_no:
				icuStayObservation = setObservationFields(icuStayObservation,
						CONCEPT_ICU_STAY, CONCEPT_ANSWER_NO,
						ApplicationConstants.ObservationLocators.ICU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.NO);
				showAnimateView(false, extraFormAdditions);
				voidExtraICUObservations();
				break;

			case R.id.is_icu_stay_unknown:
				icuStayObservation = setObservationFields(icuStayObservation,
						CONCEPT_ICU_STAY, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.ICU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				showAnimateView(false, extraFormAdditions);
				voidExtraICUObservations();
				break;

			case R.id.is_hdu_stay_yes:
				hduStayObservation = setObservationFields(hduStayObservation,
						CONCEPT_HDU_STAY, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.HDU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.YES);
				showAnimateView(true, hduCoManage);
				break;

			case R.id.is_hdu_stay_no:
				hduStayObservation = setObservationFields(hduStayObservation,
						CONCEPT_HDU_STAY, CONCEPT_ANSWER_NO,
						ApplicationConstants.ObservationLocators.HDU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.NO);
				showAnimateView(false, hduCoManage);
				setObservationVoided(hduComgmtObservation);
				break;

			case R.id.is_hdu_stay_unknown:
				hduStayObservation = setObservationFields(hduStayObservation,
						CONCEPT_HDU_STAY, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.HDU_STAY_DURING_ADMISSION +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				showAnimateView(false, hduCoManage);
				setObservationVoided(hduComgmtObservation);
				break;

			case R.id.is_hdu_comgmt_yes:
				hduComgmtObservation = setObservationFields(hduComgmtObservation,
						CONCEPT_HDU_COMGMT, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.HDU_COMGMT +
								ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.is_hdu_comgmt_no:
				hduComgmtObservation = setObservationFields(hduComgmtObservation,
						CONCEPT_HDU_COMGMT, CONCEPT_ANSWER_NO,
						ApplicationConstants.ObservationLocators.HDU_COMGMT +
								ApplicationConstants.ObservationLocators.NO);

				break;
			case R.id.is_hdu_comgmt_unknown:
				hduComgmtObservation = setObservationFields(hduComgmtObservation,
						CONCEPT_HDU_COMGMT, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.HDU_COMGMT +
								ApplicationConstants.ObservationLocators.UNKNOWN);

				break;

			case R.id.is_hiv_positive_yes:
				hivPositiveObservation =
						setObservationFields(hivPositiveObservation, CONCEPT_HIV_STATUS,
								CONCEPT_ANSWER_POSITIVE, ApplicationConstants.ObservationLocators.HIV_INFECTED +
										"POSITIVE");
				showAnimateView(true, cd4TextInputLayout);
				break;

			case R.id.is_hiv_positive_no:
				hivPositiveObservation = setObservationFields(hivPositiveObservation,
						CONCEPT_HIV_STATUS, CONCEPT_ANSWER_NEGATIVE,
						ApplicationConstants.ObservationLocators.HIV_INFECTED + "NEGATIVE");
				showAnimateView(false, cd4TextInputLayout);
				setObservationVoided(cd4Observation);
				break;

			case R.id.is_hiv_positive_unknown:
				hivPositiveObservation = setObservationFields(hivPositiveObservation,
						CONCEPT_HIV_STATUS, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.HIV_INFECTED +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				showAnimateView(false, cd4TextInputLayout);
				setObservationVoided(cd4Observation);
				break;

			case R.id.mechanical_ventilation_yes:
				mechanicalVentilationObservation =
						setObservationFields(mechanicalVentilationObservation, CONCEPT_MECHANICAL_VENTILATIN,
								CONCEPT_ANSWER_YES, ApplicationConstants.ObservationLocators.MECHANICAL_VENTILATION +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.mechanical_ventilation_no:
				mechanicalVentilationObservation =
						setObservationFields(mechanicalVentilationObservation, CONCEPT_MECHANICAL_VENTILATIN,
								CONCEPT_ANSWER_NO, ApplicationConstants.ObservationLocators.MECHANICAL_VENTILATION +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.mechanical_ventilation_unknown:
				mechanicalVentilationObservation =
						setObservationFields(mechanicalVentilationObservation, CONCEPT_MECHANICAL_VENTILATIN,
								CONCEPT_ANSWER_UNKNOWN, ApplicationConstants.ObservationLocators.MECHANICAL_VENTILATION +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.confirmed_infection_yes:
				confirmedInfectionObservation =
						setObservationFields(confirmedInfectionObservation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.INFECTION_CONFIRMED_SUSPECTED +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.confirmed_infection_no:
				confirmedInfectionObservation =
						setObservationFields(confirmedInfectionObservation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.INFECTION_CONFIRMED_SUSPECTED +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.confirmed_infection_unknown:
				confirmedInfectionObservation =
						setObservationFields(confirmedInfectionObservation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
								CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.INFECTION_CONFIRMED_SUSPECTED +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.vaospressors_yes:
				vaospressorsObservation =
						setObservationFields(vaospressorsObservation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_YES, ApplicationConstants.ObservationLocators.RECEIVED_VAOSPRESSORS +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.vaospressors_no:
				vaospressorsObservation =
						setObservationFields(vaospressorsObservation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_NO, ApplicationConstants.ObservationLocators.RECEIVED_VAOSPRESSORS +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.vaospressors_unknown:
				vaospressorsObservation =
						setObservationFields(vaospressorsObservation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.RECEIVED_VAOSPRESSORS +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.first_sbp_yes:
				firstSbpObservation =
						setObservationFields(firstSbpObservation, CONCEPT_FIRST_SBP_ICU,
								CONCEPT_ANSWER_YES, ApplicationConstants.ObservationLocators.FIRST_SBP_ICU +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.first_sbp_no:
				firstSbpObservation =
						setObservationFields(firstSbpObservation, CONCEPT_FIRST_SBP_ICU, CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.FIRST_SBP_ICU +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.first_sbp_unknown:
				firstSbpObservation =
						setObservationFields(firstSbpObservation, CONCEPT_FIRST_SBP_ICU, CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.FIRST_SBP_ICU +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.any_prior_sedetion_yes:
				priorSedetionObservation =
						setObservationFields(priorSedetionObservation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.SEDETION_PRIOR_FIRST_GCS_SCORE_ICU +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.any_prior_sedetion_no:
				priorSedetionObservation =
						setObservationFields(priorSedetionObservation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.SEDETION_PRIOR_FIRST_GCS_SCORE_ICU +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.any_prior_sedetion_unknown:
				priorSedetionObservation =
						setObservationFields(priorSedetionObservation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
								CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.SEDETION_PRIOR_FIRST_GCS_SCORE_ICU +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.surgery_na:
				surgeryObservation =
						setObservationFields(surgeryObservation, CONCEPT_SURGERY_HOSPITAL_STAY,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.SURGERY_DURING_HOSPITAL_STAY +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.surgery_planned:
				surgeryObservation =
						setObservationFields(surgeryObservation, CONCEPT_SURGERY_HOSPITAL_STAY, CONCEPT_ANSWER_PLANNED,
								ApplicationConstants.ObservationLocators.SURGERY_DURING_HOSPITAL_STAY +
										ApplicationConstants.ObservationLocators.SCHEDULED_IN_CLINIC);
				break;

			case R.id.surgery_unplanned:
				surgeryObservation =
						setObservationFields(surgeryObservation, CONCEPT_SURGERY_HOSPITAL_STAY, CONCEPT_ANSWER_UNPLANNED,
								ApplicationConstants.ObservationLocators.SURGERY_DURING_HOSPITAL_STAY +
										ApplicationConstants.ObservationLocators.NOT_SCHEDULED_IN_CLINIC);
				break;

			case R.id.first_map_yes:
				firstMapObservation =
						setObservationFields(firstMapObservation, CONCEPT_FIRST_MAP_ICU,
								CONCEPT_ANSWER_YES, ApplicationConstants.ObservationLocators.FIRST_MAP +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.first_map_no:
				firstMapObservation =
						setObservationFields(firstMapObservation, CONCEPT_FIRST_MAP_ICU, CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.FIRST_MAP +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.first_map_unknown:
				firstMapObservation =
						setObservationFields(firstMapObservation, CONCEPT_FIRST_MAP_ICU, CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.FIRST_MAP +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;

			case R.id.ward_stay_admission_yes:
				wardStayAdmissionObservation =
						setObservationFields(wardStayAdmissionObservation, CONCEPT_WARD_STAY_DURING_ADMISSION,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.WARD_STAY_DURING_ADMISSION +
										ApplicationConstants.ObservationLocators.YES);
				break;

			case R.id.ward_stay_admission_no:
				wardStayAdmissionObservation =
						setObservationFields(wardStayAdmissionObservation, CONCEPT_WARD_STAY_DURING_ADMISSION,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.WARD_STAY_DURING_ADMISSION +
										ApplicationConstants.ObservationLocators.NO);
				break;

			case R.id.ward_stay_admission_unknown:
				wardStayAdmissionObservation =
						setObservationFields(wardStayAdmissionObservation, CONCEPT_WARD_STAY_DURING_ADMISSION,
								CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.WARD_STAY_DURING_ADMISSION +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				break;
			case R.id.patient_diabetic_yes:
				patientDiabeticObservation =
						setObservationFields(patientDiabeticObservation, CONCEPT_PATIENT_DIABETIC,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.PATIENT_DIABETIC +
										ApplicationConstants.ObservationLocators.YES);
				showAnimateView(true, hba1cTextLayout);
				break;

			case R.id.patient_diabetic_no:
				patientDiabeticObservation =
						setObservationFields(patientDiabeticObservation, CONCEPT_PATIENT_DIABETIC, CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.PATIENT_DIABETIC +
										ApplicationConstants.ObservationLocators.NO);
				showAnimateView(false, hba1cTextLayout);
				setObservationVoided(hBa1cObservation);
				break;

			case R.id.patient_diabetic_unknown:
				patientDiabeticObservation =
						setObservationFields(patientDiabeticObservation, CONCEPT_PATIENT_DIABETIC, CONCEPT_ANSWER_UNKNOWN,
								ApplicationConstants.ObservationLocators.PATIENT_DIABETIC +
										ApplicationConstants.ObservationLocators.UNKNOWN);
				showAnimateView(false, hba1cTextLayout);
				setObservationVoided(hBa1cObservation);
				break;

			case R.id.intubationDone:
				intubationObservation = setObservationFields(intubationObservation, CONCEPT_INTUBATION_AT_GCS, CONCEPT_ANSWER_YES,
						ApplicationConstants.ObservationLocators.INTUBATION_ON_FIRST_GCS +
								ApplicationConstants.ObservationLocators.YES);
				break;
			case R.id.intubationNotDone:
				intubationObservation = setObservationFields(intubationObservation, CONCEPT_INTUBATION_AT_GCS, CONCEPT_ANSWER_NO,
						ApplicationConstants.ObservationLocators.INTUBATION_ON_FIRST_GCS +
								ApplicationConstants.ObservationLocators.NO);
				break;
			case R.id.intubationNotKnown:
				intubationObservation = setObservationFields(intubationObservation, CONCEPT_INTUBATION_AT_GCS, CONCEPT_ANSWER_UNKNOWN,
						ApplicationConstants.ObservationLocators.INTUBATION_ON_FIRST_GCS +
								ApplicationConstants.ObservationLocators.UNKNOWN);
				break;
			case R.id.signed_off_on_consult_before_discharge_yes:
				signedOffOnConsultBeforeDischargeObservation = setObservationFields
						(signedOffOnConsultBeforeDischargeObservation, CONCEPT_SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE + ApplicationConstants.ObservationLocators.YES);
				break;
			case R.id.signed_off_on_consult_before_discharge_no:
				signedOffOnConsultBeforeDischargeObservation = setObservationFields
						(signedOffOnConsultBeforeDischargeObservation, CONCEPT_SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE + ApplicationConstants.ObservationLocators.NO);
				break;
			case R.id.audit_complete_yes:
				auditCompleteObservation = setObservationFields
						(auditCompleteObservation, CONCEPT_AUDIT_COMPLETE,
								CONCEPT_ANSWER_YES,
								ApplicationConstants.ObservationLocators.AUDIT_DATA_COMPLETE + ApplicationConstants.ObservationLocators.YES);
				break;
			case R.id.audit_complete_no:
				auditCompleteObservation = setObservationFields
						(auditCompleteObservation, CONCEPT_AUDIT_COMPLETE,
								CONCEPT_ANSWER_NO,
								ApplicationConstants.ObservationLocators.AUDIT_DATA_COMPLETE + ApplicationConstants.ObservationLocators.NO);
				break;
			default:
				break;
		}
	}

	private Observation setObservationFields(Observation observation,
			String questionConceptUuid, String answerConceptUuid) {
		return setObservationFields(observation, questionConceptUuid, answerConceptUuid, null);
	}

	/**
	 * This function  assigns values to an Observation object
	 * @param observation         the observation that you want to set the values
	 * @param questionConceptUuid The uuid of the Concept that the is used a question
	 * @param answerConceptUuid   The uuid of the Concept that the user has selected as the answer
	 */
	private Observation setObservationFields(Observation observation,
			String questionConceptUuid, String answerConceptUuid, String display) {

		if (observation == null) {
			observation = new Observation();
		}

		//here we assign all observations current time
		LocalDateTime localDateTime = new LocalDateTime();
		String timeString = localDateTime.toString();

		Concept concept = new Concept();
		concept.setUuid(questionConceptUuid);

		Person person = new Person();
		person.setUuid(patientUuid);

		observation.setConcept(concept);
		observation.setPerson(person);
		observation.setValue(answerConceptUuid);
		observation.setObsDatetime(timeString);

		if (display != null) {
			observation.setDisplay(display);
		}

		return observation;
	}

	@Override
	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	@Override
	public void setEncounterUuid(String encounterUuid) {
		this.encounterUuid = encounterUuid;
	}

	@Override
	public void updateSubmitButtonText() {
		submitForm.setText(R.string.update_audit_data);
	}

	@Override
	public void showPageSpinner(boolean visibility) {
		if (visibility) {
			auditDataFormProgressBar.setVisibility(View.VISIBLE);
			auditDataFormScreen.setVisibility(View.GONE);
		} else {
			auditDataFormProgressBar.setVisibility(View.GONE);
			auditDataFormScreen.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideSoftKeys() {
		ACBaseActivity.hideSoftKeyboard(context);
	}

	@Override
	public void auditDataSaveComplete() {
		if (listener != null) {
			listener.auditDataSaveComplete(patientUuid, visitUuid);
		}
	}

	@Override
	public void finishView() {
		if (listener != null) {
			listener.finishView();
		}
	}

	@Override
	public void setInpatientTypeServices(List<ConceptAnswer> conceptAnswers) {
		conceptAnswerList = new ArrayList<>();

		conceptAnswerList.add(initialInpatientTypeServiceSelection);
		conceptAnswerList.addAll(conceptAnswers);

		int selectedInpatientServiceTypePosition = inpatientServiceType.getSelectedItemPosition();
		if (context != null) {
			ArrayAdapter<ConceptAnswer> adapter = new ArrayAdapter<>(context, android.R.layout
					.simple_spinner_dropdown_item, conceptAnswerList);
			inpatientServiceType.setAdapter(adapter);
		}

		if (selectedInpatientServiceTypePosition >= 0) {
			inpatientServiceType.setSelection(selectedInpatientServiceTypePosition);
		} else if (!StringUtils.isNullOrEmpty(encounterInpatientService)) {
			updateInpatientDisplaySelection(encounterInpatientService);
		}
	}

	@Override
	public void showProgressBar(Boolean visibility) {
		if (visibility) {
			progressBar.setVisibility(View.VISIBLE);
			auditScrollView.setVisibility(View.GONE);
		} else {
			progressBar.setVisibility(View.GONE);
			auditScrollView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void updateFormFields(Encounter encounter) {
		this.visitEncounter = encounter;
		for (Observation observation : encounter.getObs()) {
			if (observation == null || observation.getDisplay() == null) {
				continue;
			}

			ArrayList displayString = StringUtils.splitStrings(observation.getDisplay(), ":");
			String observationName = displayString.get(0).toString() + ": ";
			String displayValue = displayString.get(1).toString().trim();

			switch (observationName) {
				case ApplicationConstants.ObservationLocators.HOSPITAL_DEATH:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						showAnimateView(true, classificationOfDeathType);
						deathInHospitalYes.setChecked(true);
						deathInHospitalObservation =
								setObservationFields(observation, CONCEPT_DEATH_IN_HOSPITAL,
										CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						showAnimateView(false, classificationOfDeathType);
						deathInHospitalNo.setChecked(true);
						deathInHospitalObservation =
								setObservationFields(observation, CONCEPT_DEATH_IN_HOSPITAL,
										CONCEPT_ANSWER_NO);
					}

					break;

				case ApplicationConstants.ObservationLocators.PALLIATIVE_CARE_CONSULT:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						palliativeConsultYes.setChecked(true);
						palliativeConsultObservation =
								setObservationFields(observation, CONCEPT_PALLIATIVE_CONSULT,
										CONCEPT_ANSWER_YES);
					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						palliativeConsultNo.setChecked(true);
						palliativeConsultObservation =
								setObservationFields(observation, CONCEPT_PALLIATIVE_CONSULT,
										CONCEPT_ANSWER_NO);
					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						palliativeConsultUknown.setChecked(true);
						palliativeConsultObservation =
								setObservationFields(observation, CONCEPT_PALLIATIVE_CONSULT, CONCEPT_ANSWER_UNKNOWN);
					}

					break;

				case ApplicationConstants.ObservationLocators.PREOP_RISK_ASSESSMENT:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						preopRiskAssessmentYes.setChecked(true);
						preopRiskAssessmentObservation =
								setObservationFields(observation, CONCEPT_PREOP_RISK_ASSESMENT,
										CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						preopRiskAssessmentNo.setChecked(true);
						preopRiskAssessmentObservation =
								setObservationFields(observation, CONCEPT_PREOP_RISK_ASSESMENT,
										CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						preopRiskAssessmentUknown.setChecked(true);
						preopRiskAssessmentObservation =
								setObservationFields(observation, CONCEPT_PREOP_RISK_ASSESMENT, CONCEPT_ANSWER_UNKNOWN);

					}

					break;

				case ApplicationConstants.ObservationLocators.ICU_STAY_DURING_ADMISSION:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						icuStayYes.setChecked(true);
						icuStayObservation = setObservationFields(observation, CONCEPT_ICU_STAY,
								CONCEPT_ANSWER_YES);
						showAnimateView(true, extraFormAdditions);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						icuStayObservation = setObservationFields(observation, CONCEPT_ICU_STAY,
								CONCEPT_ANSWER_NO);
						icuStayNo.setChecked(true);
						showAnimateView(false, extraFormAdditions);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						icuStayUnknown.setChecked(true);
						icuStayObservation = setObservationFields(observation, CONCEPT_ICU_STAY, CONCEPT_ANSWER_UNKNOWN);
						showAnimateView(false, extraFormAdditions);
					}

					break;

				case ApplicationConstants.ObservationLocators.HDU_STAY_DURING_ADMISSION:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						hduStayYes.setChecked(true);
						hduStayObservation = setObservationFields(observation, CONCEPT_HDU_STAY,
								CONCEPT_ANSWER_YES);
						showAnimateView(true, hduCoManage);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						hduStayNo.setChecked(true);
						hduStayObservation = setObservationFields(observation, CONCEPT_HDU_STAY,
								CONCEPT_ANSWER_NO);
						showAnimateView(false, hduCoManage);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						hduStayUnknown.setChecked(true);
						hduStayObservation = setObservationFields(observation, CONCEPT_HDU_STAY, CONCEPT_ANSWER_UNKNOWN);
						showAnimateView(false, hduCoManage);

					}

					break;
				case ApplicationConstants.ObservationLocators.HDU_COMGMT:

					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						hduComgmtYes.setChecked(true);
						hduComgmtObservation = setObservationFields(observation, CONCEPT_HDU_COMGMT,
								CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						hduComgmtNo.setChecked(true);
						hduComgmtObservation = setObservationFields(observation, CONCEPT_HDU_COMGMT,
								CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						hduComgmtUnknown.setChecked(true);
						hduComgmtObservation = setObservationFields(observation,
								CONCEPT_HDU_COMGMT, CONCEPT_ANSWER_UNKNOWN);
					}

					break;
				case ApplicationConstants.ObservationLocators.HIV_INFECTED:

					if (displayValue.equalsIgnoreCase(ANSWER_POSITIVE)) {
						hivPositiveYes.setChecked(true);
						hivPositiveObservation = setObservationFields(observation, CONCEPT_HIV_STATUS,
								CONCEPT_ANSWER_POSITIVE);
						showAnimateView(true, cd4TextInputLayout);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NEGATIVE)) {
						hivPositiveNo.setChecked(true);
						hivPositiveObservation = setObservationFields(observation, CONCEPT_HIV_STATUS,
								CONCEPT_ANSWER_NEGATIVE);
						showAnimateView(false, cd4TextInputLayout);
					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						hivPositiveUnknown.setChecked(true);
						hivPositiveObservation =
								setObservationFields(observation, CONCEPT_HIV_STATUS, CONCEPT_ANSWER_UNKNOWN);
						showAnimateView(false, cd4TextInputLayout);

					}
					break;

				case ApplicationConstants.ObservationLocators.CD4_COUNT:
					cd4Observation = setObservationFields(observation, CONCEPT_CD4_COUNT, displayValue);
					cd4.setText(displayValue);
					break;

				case ApplicationConstants.ObservationLocators.GLYCOSYLATED_HEMOGLOBIN:
					hBa1cObservation = setObservationFields(observation, CONCEPT_HBA1C, displayValue);
					hBa1c.setText(displayValue);
					break;

				case ApplicationConstants.ObservationLocators.TYPE_OF_INPATIENT_SERVICE:
					encounterInpatientService = displayValue;
					updateInpatientDisplaySelection(displayValue);
					inpatientServiceTypeObservation = setObservationFields(observation, CONCEPT_INPATIENT_SERVICE_TYPE,
							inpatientServiceTypeSelectedUuid);

					break;

				case ApplicationConstants.ObservationLocators.AUDIT_DATA_COMPLETE:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						auditCompleteYes.setChecked(true);
						auditCompleteObservation = setObservationFields(observation, CONCEPT_AUDIT_COMPLETE,
								CONCEPT_ANSWER_YES);
					} else {
						auditCompleteNo.setChecked(true);
						auditCompleteObservation =
								setObservationFields(observation, CONCEPT_AUDIT_COMPLETE, CONCEPT_ANSWER_NO);
					}

					break;

				case ApplicationConstants.ObservationLocators.FIRST_HEART_RATE:
					firstIcuHeartRateObservation =
							setObservationFields(observation, CONCEPT_FIRST_HEART_RATE_ICU, displayValue);
					firstIcuHeartRate.setText(displayValue);
					break;

				case ApplicationConstants.ObservationLocators.FIRST_RESPIRATORY_RATE:
					firstIcuRespiratoryRateObservation =
							setObservationFields(observation, CONCEPT_FIRST_RESPIRATORY_RATE_ICU, displayValue);
					firstIcuRespiratoryRate.setText(displayValue);
					break;

				case ApplicationConstants.ObservationLocators.FIRST_GCS_SCORE:
					firstGcsScoreObservation = setObservationFields(observation, CONCEPT_FIRST_GCS_SCORE_ICU, displayValue);
					firstGcsScore.setText(displayValue);
					break;

				case ApplicationConstants.ObservationLocators.MECHANICAL_VENTILATION:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						mechanical_ventilation_yes.setChecked(true);
						mechanicalVentilationObservation = setObservationFields(observation, CONCEPT_MECHANICAL_VENTILATIN,
								CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						mechanical_ventilation_no.setChecked(true);
						mechanicalVentilationObservation =
								setObservationFields(observation, CONCEPT_MECHANICAL_VENTILATIN, CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						mechanical_ventilation_unknown.setChecked(true);
						mechanicalVentilationObservation =
								setObservationFields(observation, CONCEPT_MECHANICAL_VENTILATIN, CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.RECEIVED_VAOSPRESSORS:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						vaospressors_yes.setChecked(true);
						vaospressorsObservation = setObservationFields(observation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						vaospressors_no.setChecked(true);
						vaospressorsObservation = setObservationFields(observation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						vaospressors_unknown.setChecked(true);
						vaospressorsObservation = setObservationFields(observation, CONCEPT_RECIEVED_VAOSPRESSORS,
								CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.SURGERY_DURING_HOSPITAL_STAY:
					if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						surgery_na.setChecked(true);
						surgeryObservation = setObservationFields(observation, CONCEPT_SURGERY_HOSPITAL_STAY,
								CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(SCHEDULED_IN_CLINIC)) {
						surgery_planned.setChecked(true);
						surgeryObservation = setObservationFields(observation, CONCEPT_SURGERY_HOSPITAL_STAY,
								CONCEPT_ANSWER_PLANNED);

					} else if (displayValue.equalsIgnoreCase(NOT_SCHEDULED_IN_CLINIC)) {
						surgery_unplanned.setChecked(true);
						surgeryObservation = setObservationFields(observation, CONCEPT_SURGERY_HOSPITAL_STAY,
								CONCEPT_ANSWER_UNPLANNED);
					}

					break;
				case ApplicationConstants.ObservationLocators.INFECTION_CONFIRMED_SUSPECTED:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						confirmed_infection_yes.setChecked(true);
						confirmedInfectionObservation =
								setObservationFields(observation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
										CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						confirmed_infection_no.setChecked(true);
						confirmedInfectionObservation =
								setObservationFields(observation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
										CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						confirmed_infection_unknown.setChecked(true);
						confirmedInfectionObservation =
								setObservationFields(observation, CONCEPT_INFECTION_CONFIRMED_SUSPECTED,
										CONCEPT_ANSWER_UNKNOWN);
					}

					break;
				case ApplicationConstants.ObservationLocators.FIRST_SBP_ICU:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						first_sbp_yes.setChecked(true);
						firstSbpObservation = setObservationFields(observation, CONCEPT_FIRST_SBP_ICU,
								CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						first_sbp_no.setChecked(true);
						firstSbpObservation = setObservationFields(observation, CONCEPT_FIRST_SBP_ICU,
								CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						first_sbp_unknown.setChecked(true);
						firstSbpObservation =
								setObservationFields(observation, CONCEPT_FIRST_SBP_ICU, CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.FIRST_MAP:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						first_map_yes.setChecked(true);
						firstMapObservation = setObservationFields(observation, CONCEPT_FIRST_MAP_ICU,
								CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						first_map_no.setChecked(true);
						firstMapObservation = setObservationFields(observation, CONCEPT_FIRST_MAP_ICU,
								CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						first_map_unknown.setChecked(true);
						firstMapObservation =
								setObservationFields(observation, CONCEPT_FIRST_MAP_ICU, CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.SEDETION_PRIOR_FIRST_GCS_SCORE_ICU:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						any_prior_sedetion_yes.setChecked(true);
						priorSedetionObservation =
								setObservationFields(observation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
										CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						any_prior_sedetion_no.setChecked(true);
						priorSedetionObservation =
								setObservationFields(observation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
										CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						any_prior_sedetion_unknown.setChecked(true);
						priorSedetionObservation =
								setObservationFields(observation, CONCEPT_SEDETION_PRIOR_FIRST_GCS_SCORE_ICU,
										CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.WARD_STAY_DURING_ADMISSION:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						ward_stay_admission_yes.setChecked(true);
						wardStayAdmissionObservation =
								setObservationFields(observation, CONCEPT_WARD_STAY_DURING_ADMISSION,
										CONCEPT_ANSWER_YES);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						ward_stay_admission_no.setChecked(true);
						wardStayAdmissionObservation =
								setObservationFields(observation, CONCEPT_WARD_STAY_DURING_ADMISSION,
										CONCEPT_ANSWER_NO);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						ward_stay_admission_unknown.setChecked(true);
						wardStayAdmissionObservation =
								setObservationFields(observation, CONCEPT_WARD_STAY_DURING_ADMISSION,
										CONCEPT_ANSWER_UNKNOWN);

					}

					break;
				case ApplicationConstants.ObservationLocators.PATIENT_DIABETIC:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						patient_diabetic_yes.setChecked(true);
						patientDiabeticObservation =
								setObservationFields(observation, CONCEPT_PATIENT_DIABETIC,
										CONCEPT_ANSWER_YES);
						showAnimateView(true, hba1cTextLayout);

					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						patient_diabetic_no.setChecked(true);
						patientDiabeticObservation =
								setObservationFields(observation, CONCEPT_PATIENT_DIABETIC,
										CONCEPT_ANSWER_NO);
						showAnimateView(false, hba1cTextLayout);

					} else if (displayValue.equalsIgnoreCase(ANSWER_UNKNOWN)) {
						patient_diabetic_unknown.setChecked(true);
						patientDiabeticObservation =
								setObservationFields(observation, CONCEPT_PATIENT_DIABETIC,
										CONCEPT_ANSWER_UNKNOWN);
						showAnimateView(false, hba1cTextLayout);

					}

				case ApplicationConstants.ObservationLocators.INTUBATION_ON_FIRST_GCS:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						intubatedYes.setChecked(true);
						intubationObservation = setObservationFields(observation, CONCEPT_INTUBATION_AT_GCS,
								CONCEPT_ANSWER_YES);
					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						intubatedNo.setChecked(true);
						intubationObservation = setObservationFields(observation, CONCEPT_INTUBATION_AT_GCS,
								CONCEPT_ANSWER_NO);
					} else {
						intubatedUnknown.setChecked(true);
						intubationObservation = setObservationFields(observation, CONCEPT_INTUBATION_AT_GCS,
								CONCEPT_ANSWER_UNKNOWN);
					}
					break;

				case ApplicationConstants.ObservationLocators.SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE:
					if (displayValue.equalsIgnoreCase(ANSWER_YES)) {
						signed_off_on_consult_before_discharge_yes.setChecked(true);
						signedOffOnConsultBeforeDischargeObservation = setObservationFields(observation, SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE,
								CONCEPT_ANSWER_YES);
					} else if (displayValue.equalsIgnoreCase(ANSWER_NO)) {
						signed_off_on_consult_before_discharge_no.setChecked(true);
						signedOffOnConsultBeforeDischargeObservation = setObservationFields(observation, SIGNED_OFF_ON_CONSULT_BEFORE_DISCHARGE,
								CONCEPT_ANSWER_NO);
					}
					break;

				// andr-414
				case ApplicationConstants.ObservationLocators.CLASSIFICATION_OF_DEATH_TYPE:
					encounterClassificationOfDeath = displayValue;
					updateClassificationOfDeathDisplaySelection(displayValue);
					classificationOfDeathTypeObservation = setObservationFields(observation, CONCEPT_CLASSIFICATION_OF_DEATH_TYPE,
							classificationOfDeathTypeSelectedUuid);

					break;

				default:
					break;
			}
		}

	}

	private void updateInpatientDisplaySelection(String displayValue) {
		if (conceptAnswerList != null) {
			for (int i = 0; i < conceptAnswerList.size(); i++) {
				if (conceptAnswerList.get(i).getDisplay().equalsIgnoreCase(displayValue)) {
					inpatientServiceType.setSelection(i);
					inpatientServiceTypeSelectedUuid = conceptAnswerList.get(i).getUuid();
				}
			}
		}
	}

	private void updateClassificationOfDeathDisplaySelection(String value) {
		if (classificationOfDeathList != null) {
			for (int i = 0; i < classificationOfDeathList.size(); i++) {
				if (classificationOfDeathList.get(i).getDisplay().equalsIgnoreCase(value)) {
					classificationOfDeathType.setSelection(i);
					classificationOfDeathTypeSelectedUuid = classificationOfDeathList.get(i).getUuid();
				}
			}
		}
	}

	private void performDataSend() {
		List<Observation> observationsToVoid = new ArrayList<>();
		List<Observation> observations = new ArrayList<>();

		if (deathInHospitalObservation != null) {
			observations.add(deathInHospitalObservation);
			if (deathInHospitalObservation.getDisplay() != null &&
					deathInHospitalObservation.getDisplay().equals(ApplicationConstants.ObservationLocators.YES)) {
				classificationOfDeathTypeObservation = null;
			}
		}

		if (palliativeConsultObservation != null) {
			observations.add(palliativeConsultObservation);
		}

		if (preopRiskAssessmentObservation != null) {
			observations.add(preopRiskAssessmentObservation);
		}

		if (icuStayObservation != null) {
			observations.add(icuStayObservation);
		}

		if (hduStayObservation != null) {
			observations.add(hduStayObservation);
		}

		if (hduComgmtObservation != null) {
			observations.add(hduComgmtObservation);
		}

		if (hivPositiveObservation != null) {
			observations.add(hivPositiveObservation);
		}

		if (mechanicalVentilationObservation != null) {
			observations.add(mechanicalVentilationObservation);
		}

		if (vaospressorsObservation != null) {
			observations.add(vaospressorsObservation);
		}

		if (surgeryObservation != null) {
			observations.add(surgeryObservation);
		}
		if (confirmedInfectionObservation != null) {
			observations.add(confirmedInfectionObservation);
		}

		if (firstSbpObservation != null) {
			observations.add(firstSbpObservation);
		}

		if (firstMapObservation != null) {
			observations.add(firstMapObservation);
		}
		if (priorSedetionObservation != null) {
			observations.add(priorSedetionObservation);
		}

		if (patientDiabeticObservation != null) {
			observations.add(patientDiabeticObservation);
		}

		if (wardStayAdmissionObservation != null) {
			observations.add(wardStayAdmissionObservation);
		}

		if (signedOffOnConsultBeforeDischargeObservation != null) {
			observations.add(signedOffOnConsultBeforeDischargeObservation);
		}

		if (firstIcuHeartRate.getText() != null && firstIcuHeartRate.getText().length() > 0) {
			if (Float.valueOf(firstIcuHeartRate.getText().toString()) >= 0
					&& Float.valueOf(firstIcuHeartRate.getText().toString()) <= 240) {
				firstIcuHeartRateObservation =
						setObservationFields(firstIcuHeartRateObservation, CONCEPT_FIRST_HEART_RATE_ICU,
								firstIcuHeartRate.getText().toString(),
								ApplicationConstants.ObservationLocators.FIRST_HEART_RATE + firstIcuHeartRate.getText()
										.toString());
				observations.add(firstIcuHeartRateObservation);
			}
		}

		if (firstIcuRespiratoryRate.getText() != null && firstIcuRespiratoryRate.getText().length() > 0) {
			if (Float.valueOf(firstIcuRespiratoryRate.getText().toString()) >= AUDIT_1ST_RESPIRATORY_RATE_MIN
					&& Float.valueOf(firstIcuRespiratoryRate.getText().toString()) <= AUDIT_1ST_RESPIRATORY_RATE_MAX) {
				firstIcuRespiratoryRateObservation =
						setObservationFields(firstIcuRespiratoryRateObservation, CONCEPT_FIRST_RESPIRATORY_RATE_ICU,
								firstIcuRespiratoryRate.getText().toString(),
								ApplicationConstants.ObservationLocators.FIRST_RESPIRATORY_RATE +
										firstIcuRespiratoryRate.getText().toString());
				observations.add(firstIcuRespiratoryRateObservation);
			}
		} else if (firstIcuRespiratoryRateObservation != null) {
			observationsToVoid.add(firstIcuRespiratoryRateObservation);
		}

		if (firstGcsScore.getText() != null && firstGcsScore.getText().length() > 0) {
			if (Float.valueOf(firstGcsScore.getText().toString()) >= 1
					&& Float.valueOf(firstGcsScore.getText().toString()) <= 15) {
				firstGcsScoreObservation = setObservationFields(firstGcsScoreObservation, CONCEPT_FIRST_GCS_SCORE_ICU,
						firstGcsScore.getText().toString(),
						ApplicationConstants.ObservationLocators.FIRST_GCS_SCORE + firstGcsScore.getText().toString());
				observations.add(firstGcsScoreObservation);
			}
		}

		if (intubationObservation != null ){
			observations.add(intubationObservation);
		}

		if (cd4.getText() != null && cd4.getText().length() > 0) {
			cd4Observation = setObservationFields(cd4Observation, CONCEPT_CD4_COUNT, cd4.getText().toString(),
					ApplicationConstants.ObservationLocators.CD4_COUNT + cd4.getText().toString());
			observations.add(cd4Observation);
		}

		if (hBa1c.getText() != null && hBa1c.getText().length() > 0) {
			if (Float.valueOf(hBa1c.getText().toString()) > 3
					&& Float.valueOf(hBa1c.getText().toString()) < 26) {
				hBa1cObservation = setObservationFields(hBa1cObservation, CONCEPT_HBA1C, hBa1c.getText().toString(),
						ApplicationConstants.ObservationLocators.GLYCOSYLATED_HEMOGLOBIN + hBa1c.getText().toString());
				observations.add(hBa1cObservation);
			}
		}

		if (inpatientServiceTypeObservation != null) {
			inpatientServiceTypeObservation = setObservationFields(inpatientServiceTypeObservation,
					CONCEPT_INPATIENT_SERVICE_TYPE, inpatientServiceTypeSelectedUuid);
			observations.add(inpatientServiceTypeObservation);
		}

		if (classificationOfDeathTypeObservation != null) {
			classificationOfDeathTypeObservation = setObservationFields(classificationOfDeathTypeObservation,
					CONCEPT_CLASSIFICATION_OF_DEATH_TYPE, classificationOfDeathTypeSelectedUuid);
			observations.add(classificationOfDeathTypeObservation);
		}

		if (auditCompleteObservation != null) {
			observations.add(auditCompleteObservation);
		}

		boolean isNewEncounter = false;
		Encounter encounter = getVisitEncounter(observationsToVoid, observations);
		if (encounter.getUuid() == null || Resource.isLocalUuid(encounter.getUuid())) {
			isNewEncounter = true;
		}

		presenter.saveUpdateEncounter(encounter, isNewEncounter);
	}

	private Encounter getVisitEncounter(List<Observation> observationsToVoid, List<Observation> observations) {
		if (visitEncounter != null) {
			setObservations(visitEncounter, observationsToVoid, observations);
			 return visitEncounter;
		}

		//create location instance
		Location location = new Location();
		location.setUuid(locationUuid);

		//create form instance
		Form auditDataForm = new Form();
		auditDataForm.setUuid(AUDIT_DATA_FORM_UUID);

		//create encountertype
		EncounterType auditFormEncounterType = new EncounterType();
		auditFormEncounterType.setUuid(AUDIT_DATA_UUID);
		auditFormEncounterType.setDisplay(AUDITDATA);

		//create provider
		Provider provider = new Provider();
		provider.setUuid(instance.getCurrentLoggedInUserInfo().get(ApplicationConstants.UserKeys.USER_UUID));


		List<Provider> providers = new ArrayList<>();
		providers.add(provider);

		Encounter encounter = new Encounter();
		//encounter.setUuid(encounterUuid);

		setObservations(encounter, observationsToVoid, observations);
		encounter.setForm(auditDataForm);
		encounter.setLocation(location);
		encounter.setProvider(instance.getCurrentLoggedInUserInfo().get(ApplicationConstants.UserKeys.USER_UUID));
		encounter.setEncounterType(auditFormEncounterType);
		encounter.setDisplay(AUDITDATA);

		if (visit != null) {
			encounter.setVisit(visit);
			// set startdatetime == visit startdatetime
			if (visit.getStartDatetime() != null) {
				if (encounter.getEncounterDatetime() == null ||
						encounter.getEncounterDatetime().before(visit.getStartDatetime())) {
					encounter.setDateCreated(visit.getStartDatetime());
					encounter.setEncounterDatetime(visit.getStartDatetime());
				}
			}

			if (visit.getPatient() != null) {
				encounter.setPatient(visit.getPatient());
			}
		}

		return encounter;
	}

	private void setObservations(Encounter encounter, List<Observation> observationsToVoid, List<Observation> observations) {
		for (Observation observationToVoid : observationsToVoid) {
			if (observationToVoid != null &&
					presenter.isObservationExistingForCurrentEncounter(observationToVoid)) {
				setObservationVoided(observationToVoid);
				observations.add(observationToVoid);
			}
		}

		encounter.setObs(observations);
	}

	public void voidExtraICUObservations() {
		setObservationVoided(mechanicalVentilationObservation);
		setObservationVoided(vaospressorsObservation);
		setObservationVoided(surgeryObservation);
		setObservationVoided(confirmedInfectionObservation);
		setObservationVoided(firstSbpObservation);
		setObservationVoided(firstMapObservation);
		setObservationVoided(firstIcuHeartRateObservation);
		setObservationVoided(firstIcuRespiratoryRateObservation);
		setObservationVoided(firstGcsScoreObservation);
		setObservationVoided(priorSedetionObservation);
	}

	private void setObservationVoided(Observation observation) {
		if (observation != null) {
			observation.setVoided(true);
		}
	}

	private void showAnimateView(boolean visibility, View view) {
		if (visibility) {
			view.setVisibility(View.VISIBLE);
			view.animate().alpha(1.0f).setDuration(2000);
		} else {
			view.setVisibility(View.GONE);
			view.animate().alpha(0.0f).setDuration(2000);
		}
	}

	private boolean hasValidGcsScore() {
		if (firstGcsScore.getText().toString().length() == 0) {
			errorFirstGcsScore.setVisibility(View.GONE);
		} else if (Float.parseFloat(firstGcsScore.getText().toString()) > 2 &&
				Float.parseFloat(firstGcsScore.getText().toString()) < 16) {
			errorFirstGcsScore.setVisibility(View.GONE);
			return true;
		} else {
			errorFirstGcsScore.setVisibility(View.VISIBLE);
			errorFirstGcsScore.setText(getString(R.string.error_gcs_score,
					ApplicationConstants.ValidationFieldValues.AUDIT_GCS_SCORE_MIN,
					ApplicationConstants.ValidationFieldValues.AUDIT_GCS_SCORE_MAX));
			return false;
		}
		return true;
	}

	private boolean validateFirstRespiratoryRate() {
		if (firstIcuRespiratoryRate.getText().toString().length() == 0) {
			errorFirstRespiratoryRate.setVisibility(View.GONE);
		} else if (Float.parseFloat(firstIcuRespiratoryRate.getText().toString()) >= AUDIT_1ST_RESPIRATORY_RATE_MIN &&
				Float.parseFloat(firstIcuRespiratoryRate.getText().toString()) <= AUDIT_1ST_RESPIRATORY_RATE_MAX) {
			errorFirstRespiratoryRate.setVisibility(View.GONE);
			return true;
		} else {
			errorFirstRespiratoryRate.setVisibility(View.VISIBLE);
			errorFirstRespiratoryRate.setText(getString(R.string.error_first_respiratory_rate,
					AUDIT_1ST_RESPIRATORY_RATE_MIN, AUDIT_1ST_RESPIRATORY_RATE_MAX));
			return false;
		}
		return true;
	}

	private boolean validateHba1c() {
		if (hBa1c.getText().toString().length() == 0) {
			errorHba1c.setVisibility(View.GONE);
		} else if (Float.parseFloat(hBa1c.getText().toString()) > 3 &&
				Float.parseFloat(hBa1c.getText().toString()) < 26) {
			errorHba1c.setVisibility(View.GONE);
			return true;
		} else {
			errorHba1c.setVisibility(View.VISIBLE);
			errorHba1c.setText(getString(R.string.error_hba1c,
					ApplicationConstants.ValidationFieldValues.AUDIT_HBA1C_MIN,
					ApplicationConstants.ValidationFieldValues.AUDIT_HBA1C_MAX));
			return false;
		}
		return true;
	}

	public interface OnFragmentInteractionListener {

		void auditDataSaveComplete(String patientUuid, String visitUuid);

		void finishView();
	}
}
