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
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;


/**
 *
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
        // OK AND CANCEL BUTTONS
        *************************************************/
 
        wOK = new Button(shell, SWT.PUSH);
        wOK.setText("OK");
        wCancel = new Button(shell, SWT.PUSH);
        wCancel.setText("Cancel"); 
 
        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, gConnect);
 
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
    }

    
    // asynchronous filling of the combo boxes
    private void setComboValues() {
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
    	
    	dispose();
    }
	
}
