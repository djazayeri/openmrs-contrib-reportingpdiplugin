package org.openmrs.pentaho.plugin.reporting.rest.dto;

import java.util.List;
import java.util.Map;

/**
 * Evaluated CohortDefinition is deserialized to this class.
 */
public class CohortData {

    public String uuid;

    public CohortDefinition definition;
    
    public List<Map<String,Object>> members;

    public List links;

    public String resourceVersion;
}
