package org.openmrs.mobile.listeners.watcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.utilities.StringUtils;

//import org.openmrs.mobile.activities.login.LocationArrayAdapter;

//Class used to extract view validation logic
public class LoginValidatorWatcher implements TextWatcher, AdapterView.OnItemSelectedListener {

	private EditText mUrl;
	private EditText mUsername;
	private EditText mPassword;
	private Spinner mLocation;

	private Button mLoginButton;

	private boolean urlChanged;
	private boolean locationErrorOccurred;

	public LoginValidatorWatcher(EditText urlEditText, EditText usernameEditText, EditText passwordEditText,
			Spinner locationSpinner, Button loginButton) {
		this.mUrl = urlEditText;
		this.mUsername = usernameEditText;
		this.mPassword = passwordEditText;
		this.mLocation = locationSpinner;
		this.mLoginButton = loginButton;

		mUrl.addTextChangedListener(this);
		mUsername.addTextChangedListener(this);
		mPassword.addTextChangedListener(this);
		mLocation.setOnItemSelectedListener(this);
	}

	public boolean isUrlChanged() {
		return urlChanged;
	}

	public void setUrlChanged(boolean urlChanged) {
		this.urlChanged = urlChanged;
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		// This method is intentionally empty
	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
		// This method is intentionally empty
	}

	@Override
	public void afterTextChanged(Editable editable) {
		// If URL text changed
		if (editable.toString().hashCode() == mUrl.getText().toString().hashCode()) {
			urlChanged(editable);
		}
		mLoginButton.setEnabled(isAllDataValid());
	}

	private void urlChanged(Editable editable) {
		if ((!OpenMRS.getInstance().getServerUrl().equals(editable.toString())) &&
				StringUtils.notEmpty(editable.toString())) {
			setUrlChanged(true);
		} else if (OpenMRS.getInstance().getServerUrl().equals(editable.toString())) {
			setUrlChanged(false);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		mLoginButton.setEnabled(isAllDataValid());
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {
		// This method is intentionally empty
	}

	private boolean isAllDataValid() {

		boolean result =
				validateNotEmpty(mUsername) && validateNotEmpty(mPassword) && validateNotEmpty(mLocation) && !urlChanged;

		if (locationErrorOccurred && urlChanged) {
			mLocation.setEnabled(false);
		} else {
			mLocation.setEnabled(true);
		}

		if (!result && (!locationErrorOccurred && urlChanged)) {
			mLocation.setEnabled(false);
		}

		return result;
	}

	private boolean validateNotEmpty(EditText editText) {
		return StringUtils.notEmpty(editText.getText().toString());
	}

	private boolean validateNotEmpty(Spinner spinner) {
		return spinner.getSelectedItemPosition() >= 0 && spinner.getItemIdAtPosition(spinner.getSelectedItemPosition())
				>= 0;
	}

	public boolean isLocationErrorOccurred() {
		return locationErrorOccurred;
	}

	public void setLocationErrorOccurred(boolean locationErrorOccurred) {
		this.locationErrorOccurred = locationErrorOccurred;
	}
}
