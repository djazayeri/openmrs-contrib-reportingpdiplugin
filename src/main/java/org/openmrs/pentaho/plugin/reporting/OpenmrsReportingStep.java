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

import java.util.*;

import org.openmrs.pentaho.plugin.reporting.rest.RestClient;
import org.openmrs.pentaho.plugin.reporting.rest.dto.DatasetColumn;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

/**
 * This class is responsible for the actual row processing when the transformation runs
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
        
        data.restClient = new RestClient(environmentSubstitute(meta.getOpenmrsServerUrl()),
        	environmentSubstitute(meta.getUsername()), environmentSubstitute(meta.getPassword()));
        
        if (!data.restClient.testConnection()) {
        	logError("Failed to connect to OpenMRS server");
        }
        
        return super.init(smi, sdi);
    }
    
    /**
     * @see org.pentaho.di.trans.step.BaseStep#dispose(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
     */
    @Override
    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
    	((OpenmrsReportingStepData) sdi).reset();
        super.dispose(smi, sdi);
    }
    
    /**
     * @see org.pentaho.di.trans.step.BaseStep#processRow(org.pentaho.di.trans.step.StepMetaInterface, org.pentaho.di.trans.step.StepDataInterface)
     */
    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    	meta = (OpenmrsReportingStepMeta) smi;
    	data = (OpenmrsReportingStepData) sdi;
    	
    	if (!first)
    		throw new RuntimeException("Assertion failed, this should only be called once");
    	first = false;
        
    	// this is an input-only step, so we're not waiting for any input (and ignoring any that exists)
		try {
			// prepare output field structure
			if (getInputRowMeta() != null)
				data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			else
				data.outputRowMeta = new RowMeta();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

            // get parameters
            Map<String,String> para=null;
            if(meta.getParameters()!=null){
                para=Util.getParam(meta.getParameters());
            }
			// hit the webservice and store the result to be processed on the data object
            if(meta.getReportDefinition()==null && meta.getDataSetDefinition()==null){
                data.evaluatedCohort = data.restClient.evaluateCohort(meta.getCohortDefinition(),para);

                long rowsWritten = 0;
                for (Map<String, Object> row : data.evaluatedCohort.members) {
                    Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());

                    String val=(String)row.get("uuid") ;
                    outputRow[0] = val;
                    String val1=(String)row.get("display");
                    outputRow[1] = val1;

                    putRow(data.outputRowMeta, outputRow);
                    ++rowsWritten;

                    if (checkFeedback(rowsWritten) && log.isBasic())
                        logBasic("Wrote " + rowsWritten + " rows");
                }

                setOutputDone();
                return false;

            }else if(meta.getReportDefinition()==null){
                data.evaluatedDataSet = data.restClient.evaluateDataSet(meta.getDataSetDefinition(), meta.getCohortDefinition(),para);
                long rowsWritten = 0;
                for (Map<String, Object> row : data.evaluatedDataSet.rows) {
                    Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());

                    int index = 0;
                    for (DatasetColumn col : data.evaluatedDataSet.metadata.columns) {
                        outputRow[index] = Util.getValue(row, col);
                        ++index;
                    }
                    putRow(data.outputRowMeta, outputRow);
                    ++rowsWritten;

                    if (checkFeedback(rowsWritten) && log.isBasic())
                        logBasic("Wrote " + rowsWritten + " rows");
                }
                setOutputDone();
                return false;

            }else if(meta.getCohortDefinition()==null && meta.getDataSetDefinition()==null){
                data.evaluatedReport = data.restClient.evaluateReport(meta.getReportDefinition(),para);

                long rowsWritten = 0;
              for (Map<String, Object> row : data.evaluatedReport.dataRows) {
                  Object[] outputRow = RowDataUtil.allocateRowData(data.outputRowMeta.size());

                  int index = 0;
                  for (DatasetColumn col : data.evaluatedReport.metadata.columns) {

                      outputRow[index] = Util.getValue(row, col);
                      ++index;
                  }
                  putRow(data.outputRowMeta, outputRow);
                  ++rowsWritten;

                  if (checkFeedback(rowsWritten) && log.isBasic())
                      logBasic("Wrote " + rowsWritten + " rows");
              }
                setOutputDone();
                return false;
            }
        }catch (Exception e) {
            throw new KettleStepException(e);
        }
        return false;
    }
	
}
