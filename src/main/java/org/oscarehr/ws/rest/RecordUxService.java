/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.ws.rest;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.ws.rest.conversion.summary.Summary;
import org.oscarehr.ws.rest.to.model.MenuItemTo1;
import org.oscarehr.ws.rest.to.model.SummaryTo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Path("/recordUX/")
@Component("recordUxService")
public class RecordUxService extends AbstractServiceImpl {
	private static final Logger logger = MiscUtils.getLogger();
	
	@Autowired
	private SecurityInfoManager securityInfoManager;

	/**
	$scope.recordtabs2 = [ 
	 {id : 0,name : 'Master',url : 'partials/master.html'},
	 {id : 1,name : 'Summary',url : 'partials/summary.html'},
	 {id : 2,name : 'Rx',url : 'partials/rx.jsp'},
	 {id : 3,name : 'Msg',url : 'partials/summary.html'},
	 {id : 4,name : 'Trackers',url : 'partials/tracker.jsp'},
	 {id : 5,name : 'Consults',url : 'partials/summary.html'},
	 {id : 6,name : 'Forms',url : 'partials/formview.html'},
	 {id : 7,name : 'Prevs/Measurements',url : 'partials/summary.html'},
	 {id : 8,name : 'Ticklers',url : 'partials/summary.html'},
	 {id : 9,name : 'MyOscar',url : 'partials/blank.jsp'},
	 {id : 10,name : 'Allergies',url : 'partials/summary.html'},
	 {id : 11,name : 'CPP',url : 'partials/cpp.html'},
	 {id : 12,name : 'Labs/Docs',url : 'partials/labview.html'},
	 {id : 13,name : 'Billing',url : 'partials/billing.jsp'}
	 ];
	...
	**/
	
	@GET
	@Path("/{demographicNo}/recordMenu")
	@Produces("application/json")
	public List<MenuItemTo1> getRecordMenu(@PathParam("demographicNo") Integer demographicNo){
		logger.error("getRecordMenu getting called for demo "+demographicNo);
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		
		int idCounter = 0;
		
		List<MenuItemTo1> menulist = new ArrayList<MenuItemTo1>();
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
			menulist.add(MenuItemTo1.generateStateMenuItem(idCounter, "Details", "record.details"));
		}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_eChart", "r", null)) {
			menulist.add(MenuItemTo1.generateStateMenuItem(idCounter++, "Summary", "record.summary"));
		}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.forms", "r", null) || securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.eforms", "r", null)) {
			menulist.add(MenuItemTo1.generateStateMenuItem(idCounter++, "Forms", "record.forms"));
		}
		
		//Remove until available
		//if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.documents", "r", null) || securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.labResult", "r", null) ) {
		//	menulist.add(MenuItemTo1.generateStateMenuItem(idCounter++, "Labs/Docs", "record.labsdocs"));
		//}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.prescriptions", "r", null)) {
			menulist.add(new MenuItemTo1(idCounter++, "Rx", "../oscarRx/choosePatient.do?demographicNo="+demographicNo));
		}
		//more
		MenuItemTo1 moreMenu = new MenuItemTo1(idCounter++, "More", null);
		moreMenu.setDropdown(true);
	
		List<MenuItemTo1> morelist = new ArrayList<MenuItemTo1>();
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.preventions", "r", null)) {
			morelist.add(new MenuItemTo1(idCounter++, "Preventions", "../oscarPrevention/index.jsp?demographic_no="+demographicNo));
		}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.viewTickler", "r", null)) {
			if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()) {
				morelist.add(new MenuItemTo1(idCounter++, "Tickler", "../Tickler.do?filter.demographicNo="+demographicNo));
			}else {
				morelist.add(new MenuItemTo1(idCounter++, "Tickler", "..//tickler/ticklerDemoMain.jsp?demoview="+demographicNo));
			}
		}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.DxRegistry", "r", null)) {
			morelist.add(new MenuItemTo1(idCounter++, "Disease Registry", "../oscarResearch/oscarDxResearch/setupDxResearch.do?quickList=&demographicNo="+demographicNo));
		}
			
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.oscarMsg", "r", null)) {
			morelist.add(new MenuItemTo1(idCounter++, "Messenger", "../oscarMessenger/DisplayDemographicMessages.do?orderby=date&boxType=3&demographic_no="+demographicNo));
		}
		// Requires EctSession bean to open the window.  I think it's best to just redo measurements in a better interface in the record with angular
		//if(checkPermissions("_newCasemgmt.measurements", roleName)){
		//	morelist.add(new MenuItemTo1(2, "Measurements", "../oscarEncounter/oscarMeasurements/SetupHistoryIndex.do?demographic_no="+demographicNo));
		//}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.consultations", "r", null)) {
			morelist.add(new MenuItemTo1(idCounter++, "Consultations", "..//oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de="+demographicNo));
		}
		
		if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.documents", "r", null)) {
			morelist.add(new MenuItemTo1(idCounter++, "Documents", "../dms/documentReport.jsp?function=demographic&doctype=lab&functionid="+demographicNo));
		}
		
		/*measurements,<a onclick="popupPage(600,1000,'measurements69','/oscar/oscarEncounter/oscarMeasurements/SetupHistoryIndex.do'); return false;" href="#">Measurements</a>
		 <a onclick="popupPage(500,900,'episode69','/oscar/Episode.do?method=list&amp;demographicNo=69'); return false;" href="#">Episodes</a>
		 <a onclick="popupPage(500,900,'pregnancy69','/oscar/Pregnancy.do?method=list&amp;demographicNo=69'); return false;" href="#">Pregnancies</a>
		 */ 
		if(!morelist.isEmpty()){  // If the more list is empty no sense in displaying it.
			moreMenu.setDropdownItems(morelist);
			menulist.add(moreMenu);
		}
		
		return menulist;
		/*
		 * ADD A WAY TO CHECK IF THE USER HAS AUTHORIZATION to specific modules.
		 * 		  
	    | _newCasemgmt.allergies                      
		| _newCasemgmt.riskFactors                       
		| _newCasemgmt.calculators           
		| _newCasemgmt.templates             
		| _newCasemgmt.cpp                   
		 */
	}
	 
	//This will be more dynamic in the future but will 
	@GET
	@Path("/{demographicNo}/summary/{summaryName}") //@Path("/leftsideSummary")
	@Produces("application/json")
	public List<SummaryTo1> getSummary(@PathParam("demographicNo") Integer demographicNo,@PathParam("summaryName") String summaryName){
		LoggedInInfo loggedInInfo = getLoggedInInfo();// LoggedInInfo.loggedInInfo.get();
		logger.debug("getting summary:"+summaryName+" for demo "+demographicNo+"  loggedInInfo "+loggedInInfo);
		List<SummaryTo1> summaryList = null;
		int count = 0;
		
		if("right".equals(summaryName )){
			summaryList = new ArrayList<SummaryTo1>();
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.documents", "r", null) || securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.labResult", "r", null) ) {
				summaryList.add(new SummaryTo1("Incoming",count++,"incoming"));
			}
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.decisionSupportAlerts", "r", null)) {
				summaryList.add(new SummaryTo1("Decision Support",count++,"dssupport")); 
			}
		}else if("left".equals(summaryName )){
			summaryList = new ArrayList<SummaryTo1>();
			
			summaryList.add(new SummaryTo1("Ongoing Concerns",count++,"ongoingconcerns"));
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.medicalHistory", "r", null)) {
				summaryList.add(new SummaryTo1("Medical History",count++,"medhx")); 
			}
			
			//summaryList[2] = new SummaryTo1("Social/Family History",2,"socfamhx");
			summaryList.add(new SummaryTo1("Social History",count++,"sochx"));
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.familyHistory", "r", null)) {
				summaryList.add(new SummaryTo1("Family History",count++,"famhx"));
			}
	
			summaryList.add(new SummaryTo1("Reminders",count++,"reminders"));
			
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.prescriptions", "r", null)) {
				summaryList.add(new SummaryTo1("Medications",count++,"meds"));  
			}
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.otherMeds", "r", null)) {
				summaryList.add(new SummaryTo1("Other Meds",count++,"othermeds"));
			}
			
			if(securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.forms", "r", null) || securityInfoManager.hasPrivilege(loggedInInfo, "_newCasemgmt.eforms", "r", null)) {
				summaryList.add(new SummaryTo1("Assessments",count++,"assessments"));
			}
			//summaryList[9] = new SummaryTo1("Outgoing",7,"outgoing");
		}
		return summaryList;
	}
	

	private static final Map<String, String> MY_MAP = createMap();

    private static Map<String, String> createMap() {
        Map<String, String> result = new HashMap<String, String>();
        result.put("ongoingconcerns","issueNoteSummary"); 
        result.put("medhx","issueNoteSummary"); 
		result.put("socfamhx","issueNoteSummary"); 		
		result.put("reminders","issueNoteSummary"); 
		result.put("meds","rxSummary");
		result.put("othermeds","issueNoteSummary"); 	
		result.put("assessments","formsSummary");
		result.put("outgoing","formsSummary");	
		result.put("sochx","issueNoteSummary"); 
		result.put("famhx","issueNoteSummary"); 
		result.put("incoming","labsDocsSummary");
		result.put("dssupport","decisionSupportSummary");
		
        return Collections.unmodifiableMap(result);
    }
	
	
	@GET
	@Path("/{demographicNo}/fullSummary/{summaryCode}")
	@Produces("application/json")
	public SummaryTo1 getFullSummmary(@PathParam("demographicNo") Integer demographicNo,@PathParam(value="summaryCode") String summaryCode){
		LoggedInInfo loggedInInfo = getLoggedInInfo();
		SummaryTo1 summary = null;
		
		Summary summaryInterface = (Summary) SpringUtils.getBean(MY_MAP.get(summaryCode));
		summary = summaryInterface.getSummary(loggedInInfo, demographicNo, summaryCode);
		
		logger.debug("outgoing summary object:"+summary);
		return summary;
	}
	
	
}