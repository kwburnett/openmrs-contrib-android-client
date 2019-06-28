package org.openmrs.mobile.event;

public class PatientRefreshEvent extends OpenMRSEvent {

    private String patientUuid;

    public PatientRefreshEvent(String message, String patientUuid) {
        super(message);

        this.patientUuid = patientUuid;
    }

    public String getPatientUuid() {
        return patientUuid;
    }
}
