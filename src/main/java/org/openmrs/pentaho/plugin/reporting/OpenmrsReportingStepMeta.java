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

import java.util.List;
import java.util.Map;

import org.openmrs.pentaho.plugin.reporting.rest.RestClient;
import org.pentaho.di.core.CheckResult;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMeta;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;


/**
 *
 */
public class OpenmrsReportingStepMeta extends BaseStepMeta implements StepMetaInterface {

	private String openmrsServerUrl;
	private String username;
	private String password;
	
    /**
     * @return the openmrsServerUrl
     */
    public String getOpenmrsServerUrl() {
    	return openmrsServerUrl;
    }
	
    /**
     * @param openmrsServerUrl the openmrsServerUrl to set
     */
    public void setOpenmrsServerUrl(String openmrsServerUrl) {
    	this.openmrsServerUrl = openmrsServerUrl;
    }
	
    /**
     * @return the username
     */
    public String getUsername() {
    	return username;
    }
	
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
    	this.username = username;
    }
	
    /**
     * @return the password
     */
    public String getPassword() {
    	return password;
    }
	
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
    	this.password = password;
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#check(java.util.List, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.row.RowMetaInterface, java.lang.String[], java.lang.String[], org.pentaho.di.core.row.RowMetaInterface)
     */
    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String[] input,
                      String[] output, RowMetaInterface info) {
	    if (Const.isEmpty(openmrsServerUrl)) {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_ERROR, "OpenMRS Server URL is required", stepMeta));
	    } else {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_OK, "OpenMRS Server URL is set", stepMeta));
	    }
	    if (Const.isEmpty(username)) {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Username is required", stepMeta));
	    } else {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_OK, "Username is set", stepMeta));
	    }
	    if (Const.isEmpty(password)) {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Password is required", stepMeta));
	    } else {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_OK, "Password is set", stepMeta));
	    }
	    if (!new RestClient(openmrsServerUrl, username, password).testConnection()) {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_ERROR, "Failed to connect to web service with these settings", stepMeta));
	    } else {
	    	remarks.add(new CheckResult(CheckResult.TYPE_RESULT_OK, "Successfully tested Web Service", stepMeta));
	    }
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#getStep(org.pentaho.di.trans.step.StepMeta, org.pentaho.di.trans.step.StepDataInterface, int, org.pentaho.di.trans.TransMeta, org.pentaho.di.trans.Trans)
     */
    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
	    return new OpenmrsReportingStep(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#getStepData()
     */
    @Override
    public StepDataInterface getStepData() {
	    return new OpenmrsReportingStepData();
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#loadXML(org.w3c.dom.Node, java.util.List, java.util.Map)
     */
    @Override
    public void loadXML(Node stepDomNode, List<DatabaseMeta> databases, Map<String, Counter> sequenceCounters) throws KettleXMLException {
	    openmrsServerUrl = XMLHandler.getTagValue(stepDomNode, "openmrsServerUrl");
	    username = XMLHandler.getTagValue(stepDomNode, "username");
	    password = XMLHandler.getTagValue(stepDomNode, "password");
    }

    /**
     * @see StepMetaInterface#getXML()
     */
    @Override
    public String getXML() throws KettleException {
    	// TODO escape vars
        return "<openmrsReportingStep openmrsServerUrl=\"" + openmrsServerUrl + "\" username=\"" + username + "\" password=\"" + password + "\"/>";
    }
    
	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#readRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, java.util.List, java.util.Map)
     */
    @Override
    public void readRep(Repository repository, ObjectId stepIdInRepository, List<DatabaseMeta> databases, Map<String, Counter> sequenceCounters)
        throws KettleException {
	    openmrsServerUrl = repository.getStepAttributeString(stepIdInRepository, "openmrsServerUrl");
	    username = repository.getStepAttributeString(stepIdInRepository, "username");
	    password = repository.getStepAttributeString(stepIdInRepository, "password");
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#saveRep(org.pentaho.di.repository.Repository, org.pentaho.di.repository.ObjectId, org.pentaho.di.repository.ObjectId)
     */
    @Override
    public void saveRep(Repository repository, ObjectId idOfTransformation, ObjectId idOfStep) throws KettleException {
	    repository.saveStepAttribute(idOfTransformation, idOfStep, "openmrsServerUrl", openmrsServerUrl);
	    repository.saveStepAttribute(idOfTransformation, idOfStep, "username", username);
	    repository.saveStepAttribute(idOfTransformation, idOfStep, "password", password);
    }

	/**
     * @see org.pentaho.di.trans.step.StepMetaInterface#setDefault()
     */
    @Override
    public void setDefault() {
	    openmrsServerUrl = "http://localhost:8018/openmrs18";
	    username = "admin";
	    password = "test";
    }
    
    /**
     * @see org.pentaho.di.trans.step.BaseStepMeta#getFields(org.pentaho.di.core.row.RowMetaInterface, java.lang.String, org.pentaho.di.core.row.RowMetaInterface[], org.pentaho.di.trans.step.StepMeta, org.pentaho.di.core.variables.VariableSpace)
     */
    @Override
    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
                          VariableSpace space) throws KettleStepException {
    	// this is an input-only step--nothing should be passed into us, but if it is, we clear it
    	inputRowMeta.clear();
    	{
	        ValueMetaInterface field = new ValueMeta("uuid", ValueMetaInterface.TYPE_STRING);
	        field.setOrigin(name);
	        inputRowMeta.addValueMeta(field);
    	}
    	{
	        ValueMetaInterface field = new ValueMeta("display", ValueMetaInterface.TYPE_STRING);
	        field.setOrigin(name);
	        inputRowMeta.addValueMeta(field);
    	}
    }
	
}
