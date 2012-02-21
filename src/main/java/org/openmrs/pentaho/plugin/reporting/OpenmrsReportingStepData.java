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
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;


/**
 *
 */
public class OpenmrsReportingStepData extends BaseStepData implements StepDataInterface {

	private RestClient restClient;
	
	public OpenmrsReportingStepData() {
		super();
	}
	
    /**
     * @return the restClient
     */
    public RestClient getRestClient() {
    	return restClient;
    }
	
    /**
     * @param restClient the restClient to set
     */
    public void setRestClient(RestClient restClient) {
    	this.restClient = restClient;
    }
	
}
