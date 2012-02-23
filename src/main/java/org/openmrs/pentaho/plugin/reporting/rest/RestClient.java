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

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openmrs.pentaho.plugin.reporting.rest.dto.Dataset;
import org.pentaho.di.core.Const;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;


/**
 *
 */
public class RestClient {

	private String rootUrl;
	private String username;
	private String password;
	private Client client;

	public RestClient(String rootUrl, String username, String password) {
		this.rootUrl = rootUrl;
		this.username = username;
		this.password = password;
		Authenticator.setDefault(new MyAuthenticator(this.username, this.password));
		client = Client.create();
		client.addFilter(new LoggingFilter(System.out));
	}

	class MyAuthenticator extends Authenticator {
		
		private String username;
		private String password;
		
		/**
         * @param username
         * @param password
         */
        public MyAuthenticator(String username, String password) {
	        this.username = username;
	        this.password = password;
        }

		/**
		 * @see java.net.Authenticator#getPasswordAuthentication()
		 */
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			// TODO figure out the right character encoding
		    return new PasswordAuthentication(username, password.toCharArray());
		}
	}
	
	private WebResource getReportingResource() {
		return client.resource(rootUrl + "/ws/rest/reporting");
	}
	
	private WebResource getResource(String resourceName) {
		return getReportingResource().path(resourceName);
	}
	
	public List<Map<String, Object>> searchDataSetDefinitions(String queryParam) throws Exception {
		if (queryParam == null)
			queryParam = "";
		WebResource resource = getResource("datasetdefinition");
		resource.accept(MediaType.APPLICATION_JSON_TYPE);
		resource.queryParam("q", queryParam);
		String json = resource.get(String.class);
		return handleSearchResults(json);
	}
	
	public List<Map<String, Object>> getAllDataSetDefinitions() throws Exception {
		WebResource resource = getResource("datasetdefinition");
		resource.accept(MediaType.APPLICATION_JSON_TYPE);
		String json = resource.get(String.class);
		return handleJsonList(json);
	}
	
    public SimpleObject getDataSetDefinition(String uuid) throws Exception {
		WebResource resource = getResource("datasetdefinition/" + uuid);
		resource.accept(MediaType.APPLICATION_JSON_TYPE);
		try {
			String json = resource.get(String.class);
			return handleJsonObject(json);
		} catch (UniformInterfaceException ue) {
			ClientResponse response = ue.getResponse();
			if (response.getStatus() == 404)
				return null;
			else
				throw ue;
		}
    }
    
    public Dataset evaluateDataSet(String dsdUuid, String cohortDefinitionUuid) throws Exception {
    	WebResource resource = getResource("dataset/" + dsdUuid);
    	if (!Const.isEmpty(cohortDefinitionUuid))
    		resource = resource.queryParam("cohort", cohortDefinitionUuid);
		resource.accept(MediaType.APPLICATION_JSON_TYPE);
		String json = resource.get(String.class);
		return handleJsonObject(json, Dataset.class);
    }
	
	private static SimpleObject handleJsonObject(String json) throws Exception {
    	return handleJsonObject(json, SimpleObject.class);
    }
	
    private static <T> T handleJsonObject(String json, Class<T> clazz) throws Exception {
    	return new ObjectMapper().readValue(json, clazz);
    }

	
	private static List<Map<String, Object>> handleJsonList(String json) throws Exception {
		return new ObjectMapper().readValue(json, List.class);
	}
	
	private static List<Map<String, Object>> handleSearchResults(String json) throws Exception {
    	Map<String, Object> object = handleJsonObject(json);
    	return (List<Map<String, Object>>) object.get("results");
    }

	/**
     * Checks whether we can get a connection
     * 
     * @return
     */
    public boolean testConnection() {
	    try {
	    	getDataSetDefinition("nothing-with-this-uuid");
	    } catch (Exception ex) {
	    	return false;
	    }
	    return true;
    }
	
}
