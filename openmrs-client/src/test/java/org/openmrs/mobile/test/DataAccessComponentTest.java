package org.openmrs.mobile.test;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openmrs.mobile.dagger.DataAccessComponent;
import org.openmrs.mobile.data.impl.ConceptAnswerDataService;
import org.openmrs.mobile.data.impl.ConceptDataService;
import org.openmrs.mobile.data.impl.EncounterDataService;
import org.openmrs.mobile.data.impl.LocationDataService;
import org.openmrs.mobile.data.impl.ObsDataService;
import org.openmrs.mobile.data.impl.PatientDataService;
import org.openmrs.mobile.data.impl.PatientIdentifierTypeDataService;
import org.openmrs.mobile.data.impl.PatientListContextDataService;
import org.openmrs.mobile.data.impl.PatientListDataService;
import org.openmrs.mobile.data.impl.PersonAttributeTypeDataService;
import org.openmrs.mobile.data.impl.ProviderDataService;
import org.openmrs.mobile.data.impl.SessionDataService;
import org.openmrs.mobile.data.impl.UserDataService;
import org.openmrs.mobile.data.impl.VisitAttributeTypeDataService;
import org.openmrs.mobile.data.impl.VisitDataService;
import org.openmrs.mobile.data.impl.VisitNoteDataService;
import org.openmrs.mobile.data.impl.VisitPhotoDataService;
import org.openmrs.mobile.data.impl.VisitPredefinedTaskDataService;
import org.openmrs.mobile.data.impl.VisitTaskDataService;
import org.openmrs.mobile.data.impl.VisitTypeDataService;

public class DataAccessComponentTest implements DataAccessComponent {

	@Mock
	private ConceptAnswerDataService conceptAnswerDataService;
	@Mock
	private ConceptDataService conceptDataService;
	@Mock
	private EncounterDataService encounterDataService;
	@Mock
	private LocationDataService locationDataService;
	@Mock
	private ObsDataService obsDataService;
	@Mock
	private PatientDataService patientDataService;
	@Mock
	private PatientIdentifierTypeDataService patientIdentifierTypeDataService;
	@Mock
	private PatientListContextDataService patientListContextDataService;
	@Mock
	private PatientListDataService patientListDataService;
	@Mock
	private PersonAttributeTypeDataService personAttributeTypeDataService;
	@Mock
	private ProviderDataService providerDataService;
	@Mock
	private SessionDataService sessionDataService;
	@Mock
	private UserDataService userDataService;
	@Mock
	private VisitAttributeTypeDataService visitAttributeTypeDataService;
	@Mock
	private VisitDataService visitDataService;
	@Mock
	private VisitNoteDataService visitNoteDataService;
	@Mock
	private VisitPhotoDataService visitPhotoDataService;
	@Mock
	private VisitPredefinedTaskDataService visitPredefinedTaskDataService;
	@Mock
	private VisitTaskDataService visitTaskDataService;
	@Mock
	private VisitTypeDataService visitTypeDataService;

	@Override
	public ConceptAnswerDataService conceptAnswer() {
		return conceptAnswerDataService;
	}

	@Override
	public ConceptDataService concept() {
		return conceptDataService;
	}

	@Override
	public EncounterDataService encounter() {
		return encounterDataService;
	}

	@Override
	public LocationDataService location() {
		return locationDataService;
	}

	@Override
	public ObsDataService obs() {
		return obsDataService;
	}

	@Override
	public PatientDataService patient() {
		return patientDataService;
	}

	@Override
	public PatientIdentifierTypeDataService patientIdentifierType() {
		return patientIdentifierTypeDataService;
	}

	@Override
	public PatientListContextDataService patientListContext() {
		return patientListContextDataService;
	}

	@Override
	public PatientListDataService patientList() {
		return patientListDataService;
	}

	@Override
	public PersonAttributeTypeDataService personAttributeType() {
		return personAttributeTypeDataService;
	}

	@Override
	public ProviderDataService provider() {
		return providerDataService;
	}

	@Override
	public SessionDataService session() {
		return sessionDataService;
	}

	@Override
	public UserDataService user() {
		return userDataService;
	}

	@Override
	public VisitAttributeTypeDataService visitAttributeType() {
		return visitAttributeTypeDataService;
	}

	@Override
	public VisitDataService visit() {
		return visitDataService;
	}

	@Override
	public VisitNoteDataService visitNote() {
		return visitNoteDataService;
	}

	@Override
	public VisitPhotoDataService visitPhoto() {
		return visitPhotoDataService;
	}

	@Override
	public VisitPredefinedTaskDataService visitPredefinedTask() {
		return visitPredefinedTaskDataService;
	}

	@Override
	public VisitTaskDataService visitTask() {
		return visitTaskDataService;
	}

	@Override
	public VisitTypeDataService visitType() {
		return visitTypeDataService;
	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
}
