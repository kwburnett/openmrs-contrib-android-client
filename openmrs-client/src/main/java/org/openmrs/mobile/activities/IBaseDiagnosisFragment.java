package org.openmrs.mobile.activities;

import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.openmrs.mobile.models.Encounter;
import org.openmrs.mobile.models.Observation;
import org.openmrs.mobile.models.Visit;

public interface IBaseDiagnosisFragment {

	void setDiagnoses(Visit visit);

	void initializeListeners();

	void initializeViewFields(View v);

	void setSearchDiagnosisView(AutoCompleteTextView view);

	AutoCompleteTextView getSearchDiagnosisView();

	TextInputEditText getClinicalNoteView();
	void setClinicalNoteView(TextInputEditText clinicalNoteView);

	void setNoPrimaryDiagnoses(TextView view);
	TextView getNoPrimaryDiagnoses();

	void setNoSecondaryDiagnoses(TextView view);
	TextView getNoSecondaryDiagnoses();

	void setPrimaryDiagnosesRecycler(RecyclerView view);
	RecyclerView getPrimaryDiagnosesRecycler();

	void setSecondaryDiagnosesRecycler(RecyclerView view);
	RecyclerView getSecondaryDiagnosesRecycler();

	void setLoadingProgressBar(RelativeLayout view);
	RelativeLayout getLoadingProgressBar();

	void setDiagnosesContent(LinearLayout view);
	LinearLayout getDiagnosesContent();

	void setEncounter(Encounter encounter);
	Encounter getEncounter();

	void setVisit(Visit visit);
	Visit getVisit();

	void setObservation(Observation obs);
	Observation getObservation();

	IBaseDiagnosisView getBaseDiagnosisView();

	void createPatientSummaryMergeDialog(String mergePatientSummaryText);

	void setLoading(boolean loading);
}
