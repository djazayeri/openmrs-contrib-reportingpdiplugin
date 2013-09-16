package org.openmrs.pentaho.plugin.reporting.rest.dto;

import java.util.List;
import java.util.Map;

/**
 * CohortDefinition description in CohortData is deserialized to this class
 */
public class CohortDefinition {

    public String uuid;
    
    public String name;

    public String description;
    
    public List<Parameter> parameters;

    public List links;

    public String resourceVersion;
}
