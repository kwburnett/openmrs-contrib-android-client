package org.openmrs.mobile.activities.visit;

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.data.impl.VisitDataService;
import org.openmrs.mobile.models.Visit;

public class VisitPresenter extends BasePresenter implements VisitContract.Presenter {

	private VisitContract.View visitView;
	private VisitDataService visitDataService;

	public VisitPresenter(VisitContract.View view) {
		visitView = view;

		visitDataService = dataAccess().visit();
	}

	@Override
	public Visit getVisit(String visitUuid) {
		return visitDataService.getLocalByUuid(visitUuid, null);
	}

	@Override
	public void subscribe() {
		// Intentionally left blank
	}
}
