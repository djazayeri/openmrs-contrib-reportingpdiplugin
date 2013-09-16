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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openmrs.pentaho.plugin.reporting.rest.RestClient;
import org.openmrs.pentaho.plugin.reporting.rest.SimpleObject;
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 * Maintain the Dialog for the step settings
 */
public class OpenmrsReportingStepDialog extends BaseStepDialog implements StepDialogInterface {
	
	private OpenmrsReportingStepMeta meta;
	
	// connection settings widgets
	private Label openmrsServerUrlLabel;
	private TextVar openmrsServerUrlWidget;
	private Label usernameLabel;
	private TextVar usernameWidget;
	private Label passwordLabel;
	private TextVar passwordWidget;
	
	// dsd,cohort and report widgets
	private Label dsdLabel;
	private TextVar dsdWidget;
	private Label cdLabel;
	private TextVar cdWidget;
	private Text dsdOptions;
    private Label rptLabel;
    private TextVar rptWidget;

    //parameter widgets
    private Label paraLabel;
    private TextVar paraWidget;
	
	public OpenmrsReportingStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		meta = (OpenmrsReportingStepMeta) in;
	}
	
	/**
	 * @see org.pentaho.di.trans.step.StepDialogInterface#open()
	 */
	@Override
	public String open() {
	    Shell parent = getParent();
	    Display display = parent.getDisplay();
	    
	    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
        props.setLook(shell);
        setShellImage(shell, meta);
        
        ModifyListener lsMod = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                meta.setChanged();
                setComboValues();
            }
        };
        backupChanged = meta.hasChanged();
        
        FormLayout formLayout = new FormLayout();
        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;
 
        shell.setLayout(formLayout);
        shell.setText("OpenMRS Reporting Input"); 
 
        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;
        
        /*************************************************
         * STEP NAME ENTRY
         *************************************************/
 
        // Stepname line
        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText("Step Name");
        props.setLook(wlStepname);
        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment(0, 0);
        fdlStepname.right = new FormAttachment(middle, -margin);
        fdlStepname.top = new FormAttachment(0, margin);
        wlStepname.setLayoutData(fdlStepname);
 
        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment(middle, 0);
        fdStepname.top = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);
        wStepname.setLayoutData(fdStepname);
        
        /*************************************************
         * OPENMRS CONNECTION GROUP
         *************************************************/
        Group gConnect = new Group(shell, SWT.SHADOW_ETCHED_IN);
        gConnect.setText("OpenMRS Server Connection");
        FormLayout gConnectLayout = new FormLayout();
        gConnectLayout.marginWidth = 3;
        gConnectLayout.marginHeight = 3;
        gConnect.setLayout(gConnectLayout);
        props.setLook(gConnect);
        
        // OpenMRS server url
        openmrsServerUrlLabel = new Label(gConnect, SWT.RIGHT);
        openmrsServerUrlLabel.setText("OpenMRS Server URL");
        props.setLook(openmrsServerUrlLabel);
        FormData fdlOpenmrsServerUrl = new FormData();
        fdlOpenmrsServerUrl.top = new FormAttachment(0, margin);
        fdlOpenmrsServerUrl.left = new FormAttachment(0, 0);
        fdlOpenmrsServerUrl.right = new FormAttachment(middle, -margin);
        openmrsServerUrlLabel.setLayoutData(fdlOpenmrsServerUrl);
        openmrsServerUrlWidget = new TextVar(transMeta, gConnect, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        openmrsServerUrlWidget.addModifyListener(lsMod);
        //openmrsServerUrlWidget.setToolTipText("Tooltip");
        props.setLook(openmrsServerUrlWidget);
        FormData fdOpenmrsServerUrl = new FormData();
        fdOpenmrsServerUrl.top = new FormAttachment(0, margin);
        fdOpenmrsServerUrl.left = new FormAttachment(middle, 0);
        fdOpenmrsServerUrl.right = new FormAttachment(100, 0);
        openmrsServerUrlWidget.setLayoutData(fdOpenmrsServerUrl);
        
        // username
        usernameLabel = new Label(gConnect, SWT.RIGHT);
        usernameLabel.setText("Username");
        props.setLook(usernameLabel);
        FormData fdlUsername = new FormData();
        fdlUsername.top = new FormAttachment(openmrsServerUrlWidget, margin);
        fdlUsername.left = new FormAttachment(0, 0);
        fdlUsername.right = new FormAttachment(middle, -margin);
        usernameLabel.setLayoutData(fdlUsername);
        usernameWidget = new TextVar(transMeta, gConnect, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        usernameWidget.addModifyListener(lsMod);
        //usernameWidget.setToolTipText("Tooltip");
        props.setLook(usernameWidget);
        FormData fdUsername = new FormData();
        fdUsername.top = new FormAttachment(openmrsServerUrlWidget, margin);
        fdUsername.left = new FormAttachment(middle, 0);
        fdUsername.right = new FormAttachment(100, 0);
        usernameWidget.setLayoutData(fdUsername);
        
        // password
        passwordLabel = new Label(gConnect, SWT.RIGHT);
        passwordLabel.setText("Password");
        props.setLook(passwordLabel);
        FormData fdlPassword = new FormData();
        fdlPassword.top = new FormAttachment(usernameWidget, margin);
        fdlPassword.left = new FormAttachment(0, 0);
        fdlPassword.right = new FormAttachment(middle, -margin);
        passwordLabel.setLayoutData(fdlPassword);
        passwordWidget = new TextVar(transMeta, gConnect, SWT.SINGLE | SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
        passwordWidget.addModifyListener(lsMod);
        //passwordWidget.setToolTipText("Tooltip");
        props.setLook(passwordWidget);
        FormData fdPassword = new FormData();
        fdPassword.top = new FormAttachment(usernameWidget, margin);
        fdPassword.left = new FormAttachment(middle, 0);
        fdPassword.right = new FormAttachment(100, 0);
        passwordWidget.setLayoutData(fdPassword);
        
        FormData fdConnect = new FormData();
        fdConnect.left = new FormAttachment(0, 0);
        fdConnect.right = new FormAttachment(100, 0);
        fdConnect.top = new FormAttachment(wStepname, margin);
        gConnect.setLayoutData(fdConnect);
        
        /*************************************************
        // dsd and cohort def widgets
        *************************************************/
        
        Group gDefinition = new Group(shell, SWT.SHADOW_ETCHED_IN);
        gDefinition.setText("Definitions");
        FormLayout gDefinitionLayout = new FormLayout();
        gDefinitionLayout.marginWidth = 3;
        gDefinitionLayout.marginHeight = 3;
        gDefinition.setLayout(gDefinitionLayout);
        props.setLook(gDefinition);
        
        // dataset definition
        dsdLabel = new Label(gDefinition, SWT.RIGHT);
        dsdLabel.setText("Dataset Definition");
        props.setLook(dsdLabel);
        FormData fdlDsd = new FormData();
        fdlDsd.top = new FormAttachment(0, margin);
        fdlDsd.left = new FormAttachment(0, 0);
        fdlDsd.right = new FormAttachment(middle, -margin);
        dsdLabel.setLayoutData(fdlDsd);
        dsdWidget = new TextVar(transMeta, gDefinition, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        dsdWidget.addModifyListener(lsMod);
        //dsdWidget.setToolTipText("Tooltip");
        props.setLook(dsdWidget);
        FormData fdDsd = new FormData();
        fdDsd.top = new FormAttachment(0, margin);
        fdDsd.left = new FormAttachment(middle, 0);
        fdDsd.right = new FormAttachment(100, 0);
        dsdWidget.setLayoutData(fdDsd);
        
        // cohort definition
        cdLabel = new Label(gDefinition, SWT.RIGHT);
        cdLabel.setText("Cohort Definition");
        props.setLook(cdLabel);
        FormData fdlCd = new FormData();
        fdlCd.top = new FormAttachment(dsdWidget, margin);
        fdlCd.left = new FormAttachment(0, 0);
        fdlCd.right = new FormAttachment(middle, -margin);
        cdLabel.setLayoutData(fdlCd);
        cdWidget = new TextVar(transMeta, gDefinition, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        cdWidget.addModifyListener(lsMod);
        //cdWidget.setToolTipText("Tooltip");
        props.setLook(cdWidget);
        FormData fdCd = new FormData();
        fdCd.top = new FormAttachment(dsdWidget, margin);
        fdCd.left = new FormAttachment(middle, 0);
        fdCd.right = new FormAttachment(100, 0);
        cdWidget.setLayoutData(fdCd);

        // report definition
        rptLabel = new Label(gDefinition, SWT.RIGHT);
        rptLabel.setText("Report Definition");
        props.setLook(rptLabel);
        FormData fdlRpt = new FormData();
        fdlRpt.top = new FormAttachment(cdWidget, margin);
        fdlRpt.left = new FormAttachment(0, 0);
        fdlRpt.right = new FormAttachment(middle, -margin);
        rptLabel.setLayoutData(fdlRpt);
        rptWidget = new TextVar(transMeta, gDefinition, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        rptWidget.addModifyListener(lsMod);
        //cdWidget.setToolTipText("Tooltip");
        props.setLook(rptWidget);
        FormData fdRpt = new FormData();
        fdRpt.top = new FormAttachment(cdWidget, margin);
        fdRpt.left = new FormAttachment(middle, 0);
        fdRpt.right = new FormAttachment(100, 0);
        rptWidget.setLayoutData(fdRpt);

        
        // dsd options (hacky)
        dsdOptions = new Text(gDefinition, SWT.MULTI | SWT.LEFT);
        props.setLook(dsdOptions);
        dsdOptions.setSize(400, 300);
        FormData fdDsdOptions = new FormData();
        fdDsdOptions.top = new FormAttachment(rptWidget, 2*margin);
        fdDsdOptions.left = new FormAttachment(0, 0);
        // do I need a right?
        dsdOptions.setLayoutData(fdDsdOptions);
        
        FormData fdDefinition = new FormData();
        fdDefinition.left = new FormAttachment(0, 0);
        fdDefinition.right = new FormAttachment(100, 0);
        fdDefinition.top = new FormAttachment(gConnect, margin);
        gDefinition.setLayoutData(fdDefinition);

        /*************************************************
         // Parameters
         *************************************************/

        Group ParaDefinition = new Group(shell, SWT.SHADOW_ETCHED_IN);
        ParaDefinition.setText("Parameters");
        FormLayout ParaDefinitionLayout = new FormLayout();
        ParaDefinitionLayout.marginWidth = 3;
        ParaDefinitionLayout.marginHeight = 3;
        ParaDefinition.setLayout(ParaDefinitionLayout);
        props.setLook(ParaDefinition);

        // parameters
        paraLabel = new Label(ParaDefinition, SWT.RIGHT);
        paraLabel.setText("Parameters");
        props.setLook(paraLabel);
        FormData fdlPara = new FormData();
        fdlPara.top = new FormAttachment(0, margin);
        fdlPara.left = new FormAttachment(0, 0);
        fdlPara.right = new FormAttachment(middle, -margin);
        paraLabel.setLayoutData(fdlPara);
        paraWidget = new TextVar(transMeta,ParaDefinition, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        paraWidget.addModifyListener(lsMod);
        //dsdWidget.setToolTipText("Tooltip");
        props.setLook(paraWidget);
        FormData fdPara = new FormData();
        fdPara.top = new FormAttachment(0, margin);
        fdPara.left = new FormAttachment(middle, 0);
        fdPara.right = new FormAttachment(100, 0);
        paraWidget.setLayoutData(fdPara);

        FormData Paradef = new FormData();
        Paradef.left = new FormAttachment(0, 0);
        Paradef.right = new FormAttachment(100, 0);
        Paradef.top = new FormAttachment(gDefinition, margin);
        ParaDefinition.setLayoutData(Paradef);

        
        /*************************************************
        // OK AND CANCEL BUTTONS
        *************************************************/
 
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel"); 
 
        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin,ParaDefinition);
 
        // Add listeners
        lsCancel = new Listener() {
            public void handleEvent(Event e) {
                cancel();
            }
        };
        lsOK = new Listener() {
            public void handleEvent(Event e) {
                ok();
            }
        };
 
        wCancel.addListener(SWT.Selection, lsCancel);
        wOK.addListener(SWT.Selection, lsOK);
        
        /*************************************************
        // DEFAULT ACTION LISTENERS
        *************************************************/
 
        lsDef = new SelectionAdapter() {
            public void widgetDefaultSelected(SelectionEvent e) {
                ok();
            }
        };
 
        wStepname.addSelectionListener(lsDef);
        openmrsServerUrlWidget.addSelectionListener(lsDef);
        usernameWidget.addSelectionListener(lsDef);
        passwordWidget.addSelectionListener(lsDef);
        dsdWidget.addSelectionListener(lsDef);    //by meeeeeeeeeeeee
        cdWidget.addSelectionListener(lsDef);

        rptWidget.addSelectionListener(lsDef);
        paraWidget.addSelectionListener(lsDef);
 
        // Detect X or ALT-F4 or something that kills this window...
        shell.addShellListener(new ShellAdapter() {
            public void shellClosed(ShellEvent e) {
                cancel();
            }
        });
 
        // Set the shell size, based upon previous time...
        setSize();
        
        /*************************************************
        // POPULATE AND OPEN DIALOG
        *************************************************/
 
        getData();
        setComboValues();

       /// populateDialog();
 
        meta.setChanged(backupChanged);
 
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        return stepname;
	}
	
	
	// Collect data from the meta and place it in the dialog
    public void getData() {
    	wStepname.selectAll();
    	setIfNotNull(openmrsServerUrlWidget, meta.getOpenmrsServerUrl());
    	setIfNotNull(usernameWidget, meta.getUsername());
    	setIfNotNull(passwordWidget, meta.getPassword());
    	setIfNotNull(dsdWidget, meta.getDataSetDefinition());     // by meeeeeeeeeeeeeeeeeeeeeeeeee
    	setIfNotNull(cdWidget, meta.getCohortDefinition());

        setIfNotNull(rptWidget, meta.getReportDefinition());
        setIfNotNull(paraWidget, meta.getParameters());
    }

    
    // asynchronous filling of the combo boxes
    private void setComboValues() {
    	Runnable dsdOptionLoader = new Runnable() {
            public void run() {
            	try {
	            	String url = variables.environmentSubstitute(openmrsServerUrlWidget.getText());
	            	String user = variables.environmentSubstitute(usernameWidget.getText());
	            	String pass = variables.environmentSubstitute(passwordWidget.getText());
	            	RestClient client = new RestClient(url, user, pass);
	            	StringBuilder sb = new StringBuilder();
	            	for (Object dsd : client.getAllDataSetDefinitions()) {
	            		sb.append(SimpleObject.path(dsd, "uuid") + ": " + SimpleObject.path(dsd, "display") + "\n");
	            	}
	            	if (dsdOptions != null)
	            		dsdOptions.setText(sb.toString());
            	} catch (Exception ex) {
            		logBasic("Failure getting available DSDs", ex);
            	}
            }
        };
        new Thread(dsdOptionLoader).start();
    	/*
        Runnable fieldLoader = new Runnable() {
            public void run() {
                try {
                    prevFields = transMeta.getPrevStepFields(stepname);
                } catch (KettleException e) {
                    prevFields = new RowMeta();
                    String msg = BaseMessages.getString(PKG, "VoldemortDialog.DoMapping.UnableToFindInput");
                    logError(msg);
                }
                String[] prevStepFieldNames = prevFields.getFieldNames();
                Arrays.sort(prevStepFieldNames);
                fieldColumn.setComboValues(prevStepFieldNames);
 
            }
        };
        new Thread(fieldLoader).start();
        */
    }   
	
    private void setIfNotNull(TextVar widget, String valueToSet) {
    	if (valueToSet != null)
        	widget.setText(valueToSet);
    }

	private void cancel() {
        stepname = null;
        meta.setChanged(backupChanged);
        dispose();
    }
	
	// let the meta know about the entered data
    private void ok() {
    	stepname = wStepname.getText(); // return value
    	
    	meta.setOpenmrsServerUrl(openmrsServerUrlWidget.getText());
    	meta.setUsername(usernameWidget.getText());
    	meta.setPassword(passwordWidget.getText());

    	meta.setDataSetDefinition(dsdWidget.getText());
    	meta.setCohortDefinition(cdWidget.getText());
        meta.setReportDefinition(rptWidget.getText());
        meta.setParameters(paraWidget.getText());

    	
    	dispose();
    }
	
}
