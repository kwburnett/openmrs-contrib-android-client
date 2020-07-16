/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 */
package org.openmrs.mobile.activities.patientlist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openmrs.mobile.R;
import org.openmrs.mobile.models.PatientListContext;
import org.openmrs.mobile.utilities.StringUtils;

import java.util.List;

/**
 * Display {@link PatientListContext}s
 */
public class PatientListModelRecyclerViewAdapter
		extends RecyclerView.Adapter<PatientListModelRecyclerViewAdapter.PatientListModelViewHolder> {

	private OnAdapterInteractionListener listener;

	private List<PatientListContext> items;

	public PatientListModelRecyclerViewAdapter(OnAdapterInteractionListener listener) {
		this.listener = listener;
	}

	@Override
	public PatientListModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_model_row, parent, false);
		return new PatientListModelViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(PatientListModelViewHolder holder, int position) {
		PatientListContext patientListContext = items.get(position);

		holder.headerContent.setText(StringUtils.stripHtmlTags(patientListContext.getHeaderContent()));
		holder.bodyContent.setText(StringUtils.stripHtmlTags(patientListContext.getBodyContent()));
		holder.rowLayout.setOnClickListener(v -> {
			if (patientListContext.getPatient() != null && listener != null) {
				listener.patientSelected(patientListContext.getPatient().getUuid());
			}
		});
	}

	@Override
	public int getItemCount() {
		if (items != null) {
			return items.size();
		}

		return 0;
	}

	public void addItems(List<PatientListContext> items) {
		this.items.addAll(items);

		notifyDataSetChanged();
	}

	public List<PatientListContext> getItems() {
		return items;
	}

	public void setItems(List<PatientListContext> items) {
		this.items = items;

		notifyDataSetChanged();
	}

	public void clearItems() {
		if (this.items != null) {
			this.items.clear();
		}
	}

	class PatientListModelViewHolder extends RecyclerView.ViewHolder {
		private LinearLayout rowLayout;
		private TextView headerContent;
		private TextView bodyContent;

		public PatientListModelViewHolder(View itemView) {
			super(itemView);
			rowLayout = (LinearLayout)itemView;
			headerContent = (TextView)itemView.findViewById(R.id.headerContent);
			bodyContent = (TextView)itemView.findViewById(R.id.bodyContent);
		}
	}

	public interface OnAdapterInteractionListener {

		void patientSelected(String patientUuid);
	}
}
