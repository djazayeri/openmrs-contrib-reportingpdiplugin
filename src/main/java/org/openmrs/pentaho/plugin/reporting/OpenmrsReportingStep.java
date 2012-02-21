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
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;



/**
 *
 */
public class OpenmrsReportingStep extends BaseStep implements StepInterface {

	OpenmrsReportingStepData data;
	OpenmrsReportingStepMeta meta;
	
	/**
     * @param stepMeta
     * @param stepDataInterface
     * @param copyNr
     * @param transMeta
     * @param trans
     */
    public OpenmrsReportingStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta,
        Trans trans) {
	    super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }
    
    /**
     * @see org.pentaho.di.trans.step.BaseStep#init(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
     */
    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (OpenmrsReportingStepMeta) smi;
        data = (OpenmrsReportingStepData) sdi;
        
        data.setRestClient(new RestClient(environmentSubstitute(meta.getOpenmrsServerUrl()),
        	environmentSubstitute(meta.getUsername()), environmentSubstitute(meta.getPassword())));
        
        if (!data.getRestClient().testConnection())
        	return false;
        
        return super.init(smi, sdi);
    }
    
    /**
     * @see org.pentaho.di.trans.step.BaseStep#dispose(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
     */
    @Override
    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        data.setRestClient(null);
        super.dispose(smi, sdi);
    }
	
}
