package org.openmrs.pentaho.plugin.reporting.rest.dto;

import java.util.List;
import java.util.Map;

/**
*Evaluated ReportData is deserialized to this class.
 */
public class Reportdatas {

    public String uuid;

    public String reportName;

    public List<Map<String, Object>>  dataRows;

    public DatasetMetadata metadata;

    public String resourceVersion;
}
