// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.MessageResources;

import oscar.oscarPrevention.Prevention;
import oscar.oscarPrevention.PreventionDS;
import oscar.oscarPrevention.PreventionData;
import oscar.oscarPrevention.PreventionDisplayConfig;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

/**
 *  Creates DAO for left navbar of encounter form
 * 
 */
public class EctDisplayPreventionAction extends EctDisplayAction {    
    private static final String cmd = "preventions";
    private long startTime = System.currentTimeMillis();
    
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    	
    	boolean a = true;
    	Vector v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.preventions");
        String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
        a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (Vector) v.get(1));
    	if(!a) {
    		return true; //Prevention link won't show up on new CME screen.
    	} else {
    	       
        //set lefthand module heading and link
        String winName = "prevention" + bean.demographicNo;
        String url = "popupPage(700,960,'" + winName + "', '" + request.getContextPath() + "/oscarPrevention/index.jsp?demographic_no=" + bean.demographicNo + "')";        
        Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Prevent"));
        Dao.setLeftURL(url);
        
        //set righthand link to same as left so we have visual consistency with other modules
        url += ";return false;";
        Dao.setRightURL(url);        
        Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action 
        
        //list warnings first as module items
        PreventionData pd = new PreventionData();
        Prevention p = pd.getPrevention(bean.demographicNo);
        PreventionDS pf = PreventionDS.getInstance();          
        
        try{
            pf.getMessages(p);
        }catch(Exception dsException){
            return false;
        }
                        
        //now we list prevention modules as items
        PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
        ArrayList prevList = pdc.getPreventions();
        Hashtable warningTable = p.getWarningMsgs();    
        
         
       
        String highliteColour = "FF0000";
        String inelligibleColour = "FF6600";
        String pendingColour = "FF00FF";
        Date date = null;
        //Date defaultDate = new Date(System.currentTimeMillis());
        url += "; return false;";
        ArrayList warnings = new ArrayList();
        ArrayList items = new ArrayList();
        String result;
        for (int i = 0 ; i < prevList.size(); i++){ 
            NavBarDisplayDAO.Item item = Dao.Item();
            Hashtable h = (Hashtable) prevList.get(i);
            String prevName = (String) h.get("name");
            ArrayList alist = pd.getPreventionData(prevName, bean.demographicNo); 
            boolean show = pdc.display(h, bean.demographicNo,alist.size()); 
            if( show ) {                                    
                if( alist.size() > 0 ) {
                    Hashtable hdata = (Hashtable) alist.get(alist.size()-1);
                    Hashtable hExt = (Hashtable)pd.getPreventionKeyValues((String)hdata.get("id"));
                    result = (String)hExt.get("result");

                    date = (Date)hdata.get("prevention_date_asDate");
                    item.setDate(date);
                    
                    if( hdata.get("refused").equals("2") ) {
                        item.setColour(inelligibleColour);
                    }
                    else if( result != null && result.equalsIgnoreCase("pending") ) {
                        item.setColour(pendingColour);
                    }
                }
                else {
                    item.setDate(null);
                }                                                                
                
                String title = StringUtils.maxLenString((String)h.get("name"),  MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
                item.setTitle(title);
                item.setLinkTitle((String)h.get("desc"));
                item.setURL(url);
                
                //if there's a warning associated with this prevention set item apart
                if( warningTable.containsKey(prevName) ){                    
                    item.setColour(highliteColour);                    
                    warnings.add(item);
                }
                else {
                    items.add(item);                                    
                }
            }
        }
        
        //sort items without warnings chronologically
        Dao.sortItems(items, NavBarDisplayDAO.DATESORT_ASC); 
               
        //add warnings to Dao array first so they will be at top of list
        for(int idx = 0; idx < warnings.size(); ++idx )
            Dao.addItem((NavBarDisplayDAO.Item)warnings.get(idx));
        
        //now copy remaining sorted items
        for(int idx = 0; idx < items.size(); ++idx)
            Dao.addItem((NavBarDisplayDAO.Item)items.get(idx));
                
        return true;
    }
   }
    
    public String getCmd() {
      return cmd;
    }
    
}