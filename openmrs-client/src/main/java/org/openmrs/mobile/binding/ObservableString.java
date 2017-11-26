package org.openmrs.mobile.binding;

import static org.openmrs.mobile.utilities.ApplicationConstants.EMPTY_STRING;

import android.databinding.BaseObservable;
import android.databinding.BindingConversion;
import android.os.Parcel;
import android.os.Parcelable;
import org.openmrs.mobile.utilities.StringUtils;

import java.io.Serializable;

/**
 * An observable class that holds a primitive string.
 * <p>
 * Observable field classes may be used instead of creating an Observable object:
 * <pre><code>public class MyDataObject {
 *     public final ObservableString name = new ObservableString();
 * }</code></pre>
 * Fields of this type should be declared final because bindings only detect changes in the
 * field's value, not of the field itself.
 * <p>
 * This class is parcelable and serializable but callbacks are ignored when the object is
 * parcelled / serialized. Unless you add custom callbacks, this will not be an issue because
 * data binding framework always re-registers callbacks when the view is bound.
 */
public class ObservableString extends BaseObservable implements Parcelable, Serializable {
	static final long serialVersionUID = 1L;
	private String value = EMPTY_STRING;

	/**
	 * Creates an ObservableString with the given initial value.
	 *
	 * @param value the initial value for the ObservableString
	 */
	public ObservableString(String value) {
		this.value = value;
	}

	/**
	 * Creates an ObservableString with the initial value of <code>""</code>.
	 */
	public ObservableString() {
	}

	/**
	 * @return the stored value.
	 */
	public String get() {
		return value;
	}

	/**
	 * Set the stored value.
	 */
	public void set(String value) {
		if (value.equals(this.value)) {
			this.value = value;
			notifyChange();
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(value);
	}

	public static final Parcelable.Creator<ObservableString> CREATOR
			= new Parcelable.Creator<ObservableString>() {

		@Override
		public ObservableString createFromParcel(Parcel source) {
			return new ObservableString(source.readString());
		}

		@Override
		public ObservableString[] newArray(int size) {
			return new ObservableString[size];
		}
	};

	@BindingConversion
	public static String convertToString(ObservableString observableString) {
		return observableString.get();
	}

	public boolean isNullOrEmpty() {
		return StringUtils.isNullOrEmpty(value);
	}
}
