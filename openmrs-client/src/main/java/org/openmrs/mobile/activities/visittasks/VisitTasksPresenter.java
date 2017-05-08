/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.mobile.activities.visittasks;

import android.util.Log;

import org.openmrs.mobile.activities.BasePresenter;
import org.openmrs.mobile.application.OpenMRS;
import org.openmrs.mobile.data.DataService;
import org.openmrs.mobile.data.PagingInfo;
import org.openmrs.mobile.data.impl.VisitPredefinedTasksDataService;
import org.openmrs.mobile.data.impl.VisitTasksDataService;
import org.openmrs.mobile.models.Patient;
import org.openmrs.mobile.models.Visit;
import org.openmrs.mobile.models.VisitPredefinedTask;
import org.openmrs.mobile.models.VisitTask;
import org.openmrs.mobile.models.VisitTaskStatus;
import org.openmrs.mobile.utilities.ApplicationConstants;
import org.openmrs.mobile.utilities.NetworkUtils;
import org.openmrs.mobile.utilities.ToastUtil;

import java.util.List;

public class VisitTasksPresenter extends BasePresenter implements VisitTasksContract.Presenter {

	private VisitTasksContract.View visitTasksView;
	private VisitPredefinedTasksDataService visitPredefinedTasksDataService;
	private VisitTasksDataService visitTasksDataService;

	private int page = 0;
	private int limit = 10;

	public VisitTasksPresenter(VisitTasksContract.View view, OpenMRS openMRS) {
		this.visitTasksView = view;
		this.visitTasksView.setPresenter(this);
		this.visitPredefinedTasksDataService = new VisitPredefinedTasksDataService();
		this.visitTasksDataService = new VisitTasksDataService();
	}

	@Override
	public void subscribe() {

	}

	@Override
	public void getPredefinedTasks() {
		if (NetworkUtils.hasNetwork()) {
			DataService.GetMultipleCallback<VisitPredefinedTask> getMultipleCallback = new DataService
					.GetMultipleCallback<VisitPredefinedTask>() {
				@Override
				public void onCompleted(List<VisitPredefinedTask> visitPredefinedTasks) {
					if (visitPredefinedTasks.isEmpty()) {
						visitTasksView.setPredefinedTasks(visitPredefinedTasks);
						visitTasksView.showToast(ApplicationConstants.toastMessages.predefinedTaskInfo, ToastUtil.ToastType
								.NOTICE);
					} else {
						visitTasksView.setPredefinedTasks(visitPredefinedTasks);
						visitTasksView.showToast(ApplicationConstants.toastMessages.predefinedTaskSuccess, ToastUtil
								.ToastType
								.SUCCESS);
					}
				}

				@Override
				public void onError(Throwable t) {
					Log.e("Patient Error", "Error", t.fillInStackTrace());
					visitTasksView
							.showToast(ApplicationConstants.toastMessages.predefinedTaskError, ToastUtil.ToastType.ERROR);
				}
			};
			visitPredefinedTasksDataService.getAll(false, null, getMultipleCallback);
		} else {
			// get the users from the local storage.
		}
	}

	@Override
	public void getVisitTasks() {
		PagingInfo pagingInfo = new PagingInfo(page, limit);
		if (NetworkUtils.hasNetwork()) {
			DataService.GetMultipleCallback<VisitTask> getMultipleCallback = new DataService
					.GetMultipleCallback<VisitTask>() {
				@Override
				public void onCompleted(List<VisitTask> visitTasksList) {
					if (visitTasksList.isEmpty()) {
						visitTasksView.getVisitTasks(visitTasksList);
						visitTasksView.showToast(ApplicationConstants.toastMessages.predefinedTaskInfo, ToastUtil.ToastType
								.NOTICE);
					} else {
						visitTasksView.getVisitTasks(visitTasksList);
						visitTasksView
								.showToast(ApplicationConstants.moduleName.VISIT_TASKS
												+ ApplicationConstants.toastMessages.fetchSuccessMessage,
										ToastUtil.ToastType.SUCCESS);
					}
				}

				@Override
				public void onError(Throwable t) {
					Log.e("Patient Error", "Error", t.fillInStackTrace());
					visitTasksView
							.showToast(ApplicationConstants.moduleName.VISIT_TASKS + ApplicationConstants.toastMessages
									.fetchErrorMessage, ToastUtil.ToastType.ERROR);
				}
			};
			visitTasksDataService.getAll(ApplicationConstants.EMPTY_STRING, ApplicationConstants.PATIENT_UUID,
					ApplicationConstants.VISIT_UUID, pagingInfo, getMultipleCallback);
		} else {
			// get the users from the local storage.
		}
	}

	@Override
	public void displayAddTask(Boolean visibility) {
		visitTasksView.showAddTaskDialog(visibility);
	}

	@Override
	public void addVisitTasks(String visitTasks) {
		Patient patient = new Patient();
		patient.setUuid(ApplicationConstants.PATIENT_UUID);

		Visit visit = new Visit();
		visit.setUuid(ApplicationConstants.VISIT_UUID);

		VisitTask visitTask1 = new VisitTask();

		System.out.println(visitTasks + "===============================================");

		visitTask1.setName(visitTasks);
		visitTask1.setStatus(VisitTaskStatus.OPEN);
		visitTask1.setPatient(patient);
		visitTask1.setVisit(visit);

		if (NetworkUtils.hasNetwork()) {
			DataService.GetSingleCallback<VisitTask> getSingleCallback = new DataService
					.GetSingleCallback<VisitTask>() {

				@Override
				public void onCompleted(VisitTask entity) {
					System.out.println("===============Created Visit Task================");
					System.out.print(entity);
					visitTasksView
							.showToast(ApplicationConstants.moduleName.VISIT_TASKS
									+ ApplicationConstants.toastMessages.addSuccessMessage, ToastUtil.ToastType.SUCCESS);
				}

				@Override
				public void onError(Throwable t) {
					Log.e("Patient Error", "Error", t.fillInStackTrace());
					System.out.print(t);
					visitTasksView
							.showToast(ApplicationConstants.moduleName.VISIT_TASKS + ApplicationConstants.toastMessages
									.addErrorMessage, ToastUtil.ToastType.ERROR);
				}
			};
			visitTasksDataService.create(visitTask1, getSingleCallback);
		} else {
			// get the users from the local storage.
		}
	}

	public void updateVisitTask(VisitTask visitTask) {
		if (NetworkUtils.hasNetwork()) {
			DataService.GetSingleCallback<VisitTask> getSingleCallback = new DataService
					.GetSingleCallback<VisitTask>() {

				@Override
				public void onCompleted(VisitTask entity) {
					System.out.println("===============Updated Visit Task================");
					System.out.print(entity);
					visitTasksView
							.showToast(ApplicationConstants.moduleName.VISIT_TASKS + ApplicationConstants.toastMessages
									.updateSuccessMessage, ToastUtil.ToastType.SUCCESS);
				}

				@Override
				public void onError(Throwable t) {
					Log.e("Patient Error", "Error", t.fillInStackTrace());
					visitTasksView
							.showToast(ApplicationConstants.moduleName.VISIT_TASKS + ApplicationConstants.toastMessages
									.updateErrorMessage, ToastUtil.ToastType.ERROR);
				}
			};
			visitTasksDataService.update(visitTask, getSingleCallback);
		} else {
			// get the users from the local storage.
		}
	}
}
