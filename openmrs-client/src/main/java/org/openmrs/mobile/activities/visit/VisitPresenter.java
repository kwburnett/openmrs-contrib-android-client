package org.openmrs.mobile.activities.visit;

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.data.DataService;
import org.openmrs.mobile.data.QueryOptions;
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
	public void getVisit(String visitUuid) {
		visitDataService.getByUuid(visitUuid, null, new DataService.GetCallback<Visit>() {

			@Override
			public void onCompleted(Visit visit) {
				if (visit != null) {
					visitView.getVisitCompleted(visit);
				}
			}

			@Override
			public void onError(Throwable t) {

			}
		});
	}

	@Override
	public void subscribe() {
		// Intentionally left blank
	}
}
