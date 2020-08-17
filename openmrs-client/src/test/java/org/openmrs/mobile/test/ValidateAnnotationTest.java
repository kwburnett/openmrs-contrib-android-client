package org.openmrs.mobile.test;

import org.junit.Test;
import org.openmrs.mobile.test.validationTestClasses.DataFive;
import org.openmrs.mobile.test.validationTestClasses.DataFour;
import org.openmrs.mobile.test.validationTestClasses.DataOne;
import org.openmrs.mobile.test.validationTestClasses.DataSix;
import org.openmrs.mobile.test.validationTestClasses.DataThree;
import org.openmrs.mobile.test.validationTestClasses.DataTwo;
import org.openmrs.mobile.utilities.DataUtil;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ValidateAnnotationTest {
	@Test
	public void shouldValidateCorrectly() {
		DataOne dataOne = new DataOne();
		assertThat(DataUtil.isValid(dataOne), is(equalTo(false)));

		dataOne.prop = "";
		assertThat(DataUtil.isValid(dataOne), is(equalTo(false)));

		dataOne.prop = "hello";
		assertThat(DataUtil.isValid(dataOne), is(equalTo(true)));

		DataTwo dataTwo = new DataTwo();
		assertThat(DataUtil.isValid(dataTwo), is(equalTo(true)));

		dataTwo.prop = "";
		assertThat(DataUtil.isValid(dataTwo), is(equalTo(true)));

		dataTwo.prop = "hello";
		assertThat(DataUtil.isValid(dataTwo), is(equalTo(false)));
	}

	@Test
	public void dataTypeMismatchWithValidationMethodResultInFailingValidation() {
		DataThree data = new DataThree();
		assertThat(DataUtil.isValid(data), is(equalTo(false)));

		data.prop = true;
		assertThat(DataUtil.isValid(data), is(equalTo(false)));
	}

	@Test
	public void privateAndProtectedFieldsCanBeValidated() {
		DataFour dataFour = new DataFour();
		assertThat(DataUtil.isValid(dataFour), is(equalTo(false)));

		dataFour.setProp("hello");
		assertThat(DataUtil.isValid(dataFour), is(equalTo(true)));

		DataSix dataSix = new DataSix();
		assertThat(DataUtil.isValid(dataSix), is(equalTo(false)));

		dataSix.setProp("hello");
		assertThat(DataUtil.isValid(dataSix), is(equalTo(true)));
	}

	@Test
	public void validatingWithNonExistentMethodsResultsInInvalidObjects() {
		DataFive data = new DataFive();
		assertThat(DataUtil.isValid(data), is(equalTo(false)));

		data.prop = "hello";
		assertThat(DataUtil.isValid(data), is(equalTo(false)));
	}

	@Test
	public void listsOfEntitiesToValidateAreValidIffAllEntitiesAreValid() {
		List<DataOne> data = Arrays.asList(new DataOne(), new DataOne("hello"));
		assertThat(DataUtil.isValid(data), is(equalTo(false)));

		data = Arrays.asList(new DataOne("sup"), new DataOne("hello"));
		assertThat(DataUtil.isValid(data), is(equalTo(true)));
	}
}
