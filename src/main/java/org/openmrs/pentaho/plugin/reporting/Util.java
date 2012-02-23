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

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.openmrs.pentaho.plugin.reporting.rest.dto.DatasetColumn;
import org.pentaho.di.core.row.ValueMetaInterface;


/**
 *
 */
public class Util {

    public static int getValueMetaInterface(String datatype) {
    	Class<?> c;
    	try {
	    	c = Class.forName(datatype);
    	} catch (Exception e) {
			throw new IllegalArgumentException("Class not found for " + datatype, e);
		}
	    if (String.class.equals(c)) {
	    	return ValueMetaInterface.TYPE_STRING;
	    } else if (Integer.class.equals(c)) {
	    	return ValueMetaInterface.TYPE_INTEGER;
	    } else if (Double.class.equals(c)) {
	    	return ValueMetaInterface.TYPE_NUMBER;
	    } else if (Date.class.equals(c) || java.sql.Timestamp.class.equals(c)) {
	    	return ValueMetaInterface.TYPE_DATE;
	    }
	    throw new IllegalArgumentException("Unrecognized datatype: " + datatype);
    }

    public static Object getValue(Map<String, Object> row, DatasetColumn col) throws Exception {
	    Object val = row.get(col.name);
	    if (val == null)
	    	return null;

	    Class<?> c;
    	try {
	    	c = Class.forName(col.datatype);
    	} catch (Exception e) {
			throw new IllegalArgumentException("Class not found for " + col.datatype, e);
		}
    	
    	DateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
    	
    	if (String.class.equals(c)) {
	    	return val.toString();
	    } else if (Integer.class.equals(c)) {
	    	// actually need a Long here
	    	return new Long((Integer) val);
	    } else if (Double.class.equals(c)) {
	    	return (Double) val;
	    } else if (Date.class.equals(c) || java.sql.Timestamp.class.equals(c)) {
	    	// this comes as a "yyyy-mm-dd" string
	    	return ymd.parse((String) val); 
	    }
	    throw new IllegalArgumentException("Unrecognized datatype: " + col.datatype);
    }
	
}
