/*
 *  Copyright 2015 Fabio Collini.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openmrs.mobile.binding;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import org.openmrs.mobile.R;

public class CustomDataBindings {

	@BindingAdapter("app:text")
	public static void bindEditText(EditText view, final ObservableString observableString) {
		Pair<ObservableString, TextWatcher> pair = (Pair) view.getTag(R.id.bound_observable_text);
		if (pair == null || pair.first != observableString) {
			if (pair != null) {
				view.removeTextChangedListener(pair.second);
			}
			TextWatcher watcher = new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					observableString.set(s.toString());
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			};
			view.setTag(R.id.bound_observable_text, new Pair<>(observableString, watcher));
			view.addTextChangedListener(watcher);
		}
		String newValue = observableString.get();
		if (!view.getText().toString().equals(newValue)) {
			view.setText(newValue);
		}
	}

	@BindingAdapter("app:onAfterTextChanged")
	public static void bindAfterTextChanged(EditText view, final Runnable runnable) {
		Pair<Runnable, TextWatcher> pair = (Pair) view.getTag(R.id.bound_observable_onAfterTextChanged);
		if (pair == null || pair.first != runnable) {
			if (pair != null) {
				view.removeTextChangedListener(pair.second);
			}
			TextWatcher watcher = new TextWatcher() {

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable s) {
					runnable.run();
				}
			};
			view.setTag(R.id.bound_observable_onAfterTextChanged, new Pair<>(runnable, watcher));
			view.addTextChangedListener(watcher);
		}
	}

	@BindingAdapter("app:onFocusLeave")
	public static void bindFocusChange(EditText view, final Runnable runnable) {
		Pair<Runnable, View.OnFocusChangeListener> pair = (Pair) view.getTag(R.id.bound_observable_onFocusChange);
		if (view.getTag(R.id.bound_observable_onFocusChange) == null) {
			View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						runnable.run();
					}
				}
			};
			view.setTag(R.id.bound_observable_onFocusChange, runnable);
			view.setOnFocusChangeListener(onFocusChangeListener);
		}
	}

	@BindingAdapter("app:checked_changed")
	public static void bindRadioGroup(RadioGroup view, final ObservableString observableString) {
		if (view.getTag(R.id.bound_observable_checked_changed) != observableString) {
			view.setTag(R.id.bound_observable_checked_changed, observableString);
			view.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override public void onCheckedChanged(RadioGroup group, int checkedId) {
					for (int i = 0; i < group.getChildCount(); i++) {
						final View child = group.getChildAt(i);
						if (checkedId == child.getId()) {
							observableString.set(child.getTag().toString());
							break;
						}
					}
				}
			});
		}
		String newValue = observableString.get();
		for (int i = 0; i < view.getChildCount(); i++) {
			final View child = view.getChildAt(i);
			if (child.getTag().toString().equals(newValue)) {
				((RadioButton) child).setChecked(true);
				break;
			}
		}
	}

	@BindingAdapter("app:checked_changed")
	public static void bindCheckBox(CheckBox view, final ObservableBoolean observableBoolean) {
		if (observableBoolean != null) {
			if (view.getTag(R.id.bound_observable_checked_changed) != observableBoolean) {
				view.setTag(R.id.bound_observable_checked_changed, observableBoolean);
				view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						observableBoolean.set(isChecked);
					}
				});
			}
			boolean newValue = observableBoolean.get();
			if (view.isChecked() != newValue) {
				view.setChecked(newValue);
			}
		}
	}

	@BindingAdapter("app:showHidePassword")
	public static void bindPasswordDisplay(EditText view, final ObservableBoolean observableBoolean) {
		if (observableBoolean.get()) {
			view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
			view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
	}

	@BindingAdapter(value = {"app:entries", "app:selectedItem"})
	public static void bindSpinner(Spinner view, ObservableArrayList<String> entries,
			ObservableString selectedItem) {
		ArrayAdapter<String> adapter =  new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item,
				entries);
		if (view.getTag(R.id.bound_observable_spinner) == null) {
			view.setTag(R.id.bound_observable_spinner, entries);
			view.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					selectedItem.set(adapter.getItem(position));
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		view.setAdapter(adapter);
		int selectedItemPosition = 0;
		for (int i = 0; i < entries.size(); i++) {
			if (entries.get(i).equals(selectedItem.get())) {
				selectedItemPosition = i;
				break;
			}
		}
		view.setSelection(selectedItemPosition);
	}

	@BindingAdapter("app:textHtml")
	public static void bindHtmlText(TextView view, String text) {
		if (text != null) {
			view.setText(Html.fromHtml(text));
		} else {
			view.setText("");
		}
	}

	@BindingAdapter("app:visibleOrGone")
	public static void bindVisibleOrGone(View view, boolean b) {
		view.setVisibility(b ? View.VISIBLE : View.GONE);
	}

	@BindingAdapter("app:visible")
	public static void bindVisible(View view, boolean b) {
		view.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
	}

	@BindingAdapter("app:enabled")
	public static void bindEnabled(View view, boolean b) {
		view.setEnabled(b);
	}
}
