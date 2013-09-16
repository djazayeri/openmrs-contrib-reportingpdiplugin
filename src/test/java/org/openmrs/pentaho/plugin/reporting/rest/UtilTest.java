package org.openmrs.pentaho.plugin.reporting.rest;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.pentaho.plugin.reporting.Util;
import org.openmrs.pentaho.plugin.reporting.rest.dto.DatasetColumn;
import org.pentaho.di.core.row.ValueMetaInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sashrika
 * Date: 9/12/13
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class UtilTest {
      @Test
    public void testGetParam(){
          Map<String,String> m=new HashMap<String, String>();
          m.put("age","30");
          Assert.assertEquals(m, Util.getParam("age=30"));
    }

    @Test
    public void testGet(){
        Assert.assertEquals(ValueMetaInterface.TYPE_STRING, Util.getValueMetaInterface("java.lang.String"));
        Assert.assertEquals(ValueMetaInterface.TYPE_INTEGER, Util.getValueMetaInterface("java.lang.Integer"));
        Assert.assertEquals(ValueMetaInterface.TYPE_NUMBER, Util.getValueMetaInterface("java.lang.Double"));
        Assert.assertEquals(ValueMetaInterface.TYPE_DATE, Util.getValueMetaInterface("java.sql.Date"));
        Assert.assertEquals(ValueMetaInterface.TYPE_INTEGER, Util.getValueMetaInterface("java.lang.Long"));
    }

    @Test
    public void v()throws Exception{
        Map<String,Object> m=new HashMap<String,Object>();
        DatasetColumn c=new DatasetColumn();
        c.name="name";

        c.datatype="java.lang.String" ; m.put("name","abc");
        Assert.assertEquals(m.get("name"),(String)Util.getValue(m,c));

        c.datatype="java.lang.Double" ;  m.put("name",5.5);
        Assert.assertEquals(m.get("name"),Util.getValue(m,c));

    }
}
