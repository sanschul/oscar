/* A NON-STATIC VERSION OF SqlUtilBaseS (attempting to convert static to a non-static utility framework)
 * SqlUtilBase.java
 *
 * Created on July 11, 2006, 1:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class SqlUtilBase {
           //------------------private
   protected void runSQL(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
   }
   
   protected String runSQLinsert(String sql) {
       try {
           
           DBHandler.RunSQL(sql);
           sql = "SELECT LAST_INSERT_ID()";
           ResultSet rs = DBHandler.GetSQL(sql);
           rs.next();
           String lastID = oscar.Misc.getString(rs, "LAST_INSERT_ID()");
           rs.close();
           return(lastID);
       } catch (SQLException sqe) { MiscUtils.getLogger().error("Error", sqe); }
       return "";
   }
   
   protected ResultSet getSQL(String sql) {
       ResultSet rs = null;
       try {
           
           rs = DBHandler.GetSQL(sql);
       } catch (SQLException sqe) {
           MiscUtils.getLogger().error("Error", sqe);
       }
       return(rs);
   }
   
   protected String rsGetString(ResultSet rs, String column) throws SQLException {
       //protects agianst null values;
       String thisStr = oscar.Misc.getString(rs,column);
       if (thisStr == null) return "";
       return thisStr;
   }
}