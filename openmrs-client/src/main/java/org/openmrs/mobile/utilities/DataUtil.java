package org.openmrs.mobile.utilities;

import org.openmrs.mobile.annotation.Validate;

import java.lang.reflect.Field;
import java.util.List;

public class DataUtil {
	public static boolean isValid(Object data) {
		try {
			Field[] fields = data.getClass().getDeclaredFields();
			// If this is a list, we need to check each entity inside it
			if (data instanceof List<?> && ((List<?>) data).size() > 0) {
				for (Object listItemData : (List<?>) data) {
					fields = listItemData.getClass().getDeclaredFields();
					for (Field field : fields) {
						if (isFieldInvalid(field, data)) {
							return false;
						}
					}
				}
			} else {
				for (Field field : fields) {
					if (isFieldInvalid(field, data)) {
						return false;
					}
				}
			}
		} catch (Exception ex) {
			// There was an error somewhere, so don't hold up the code
		}
		return true;
	}

	private static boolean isFieldInvalid(Field field, Object data) {
		try {
			Validate validate = field.getAnnotation(Validate.class);
			if (validate != null) {
				Object fieldData = field.get(data);
				boolean validationResult = (boolean) validate.clazz().getMethod(validate.method()).invoke(fieldData);
				return validate.negate() == validationResult;
			}
		} catch (Exception ex) {
			// There was an error somewhere, but don't hold up the code
		}
		return false;
	}
}
