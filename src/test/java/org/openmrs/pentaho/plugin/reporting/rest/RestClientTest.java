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
package org.openmrs.pentaho.plugin.reporting.rest;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.pentaho.plugin.reporting.rest.dto.Dataset;
import org.openmrs.pentaho.plugin.reporting.rest.dto.DatasetColumn;


/**
 *
 */
	
public class RestClientTest {
	
	RestClient client;
	
	@Before
	public void before() {
		client = new RestClient("http://localhost:8018/openmrs18", "admin", "test");
	}
	
	@Test
	public void shouldListDataSetDefinitions() throws Exception {
		List<Map<String, Object>> definitions = client.getAllDataSetDefinitions();
		printAsJson(definitions);
	}
	
	@Test
	public void shouldRetrieveOneDataSetDefinition() throws Exception {
		SimpleObject dsd = client.getDataSetDefinition("7df8c778-2578-4573-b360-6373197d5cd1");
		printAsJson(dsd);
	}
	
	@Test
	public void shouldEvaluateDataSetDefinition() throws Exception {
		Dataset ds = client.evaluateDataSet("7df8c778-2578-4573-b360-6373197d5cd1", null);
		printAsJson(ds);
		for (DatasetColumn c : ds.metadata.columns) {
			System.out.println("Column: " + c.label + " (" + c.name + ") " + c.datatype);
		}
		for (Map<String, Object> row : ds.rows) {
			System.out.print("Row: ");
			for (Map.Entry<String, Object> e : row.entrySet())
				System.out.print(e.getKey() + ": " + e.getValue());
			System.out.println();
		}
		
	}
	
	@Test
	public void shouldTestConnection() throws Exception {
		System.out.println("Testing Connection...");
		Assert.assertTrue(client.testConnection());
	}

    private void printAsJson(Object object) throws Exception {
    	new ObjectMapper().writeValue(System.out, object);
    }
	
}
