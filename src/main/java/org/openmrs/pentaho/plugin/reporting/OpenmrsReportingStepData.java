/**
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
package org.openmrs.pentaho.plugin.reporting;

import org.openmrs.pentaho.plugin.reporting.rest.RestClient;
import org.openmrs.pentaho.plugin.reporting.rest.dto.CohortData;
import org.openmrs.pentaho.plugin.reporting.rest.dto.Dataset;
import org.openmrs.pentaho.plugin.reporting.rest.dto.Reportdatas;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;


/**
 * This class store processing state, and cache tables
 */
public class OpenmrsReportingStepData extends BaseStepData implements StepDataInterface {

	public RestClient restClient;
	public RowMetaInterface outputRowMeta;
	public Dataset evaluatedDataSet;
    public Reportdatas evaluatedReport;
    public CohortData evaluatedCohort;
	
	public OpenmrsReportingStepData() {
		super();
	}
	
	public void reset() {
		restClient = null;
		outputRowMeta = null;
        evaluatedCohort = null;
        evaluatedReport =null;
        evaluatedDataSet =null;
	}
	
}
