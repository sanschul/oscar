package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.CreateAnonymousClientAction;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.GroupNoteDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.GroupNoteLink;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;


public class GroupNoteAction {

	private static Logger logger = MiscUtils.getLogger();
	
	private static CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");	  
	private static CaseManagementNoteDAO caseManagementNoteDao=(CaseManagementNoteDAO)SpringUtils.getBean("caseManagementNoteDAO");
    private static CaseManagementIssueDAO caseManagementIssueDao=(CaseManagementIssueDAO)SpringUtils.getBean("caseManagementIssueDAO");
	private static IssueDAO issueDao = (IssueDAO)SpringUtils.getBean("IssueDAO");
	private static ProgramManager programManager = (ProgramManager) SpringUtils.getBean("programManager");
    private static AdmissionManager admissionManager = (AdmissionManager) SpringUtils.getBean("admissionManager");
	private static GroupNoteDao groupNoteDao = (GroupNoteDao)SpringUtils.getBean("groupNoteDao");
	private static DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	
	public static int saveGroupNote(CaseManagementEntryFormBean cform, String programId) {
		logger.info("saving group note");

		String ids[] = cform.getGroupNoteClientIds();
		int totalAnonymous = cform.getGroupNoteTotalAnonymous();
		
		//logger.info("group note will have " + ids.length + " clients, and " + totalAnonymous + " anonymous clients");
			
	
		List<GroupNoteLink> currentLinks = groupNoteDao.findLinksByNoteId(Integer.parseInt(cform.getNoteId()));
		if(currentLinks.size()>0) {
			for(GroupNoteLink link:currentLinks) {
				if(link.isAnonymous()) {
					Demographic d=demographicDao.getDemographic(String.valueOf(link.getDemographicNo()));
					if(d!=null){
						d.setPatientStatus("DL");
					}
					List<Admission> admissions = admissionManager.getAdmissions(link.getDemographicNo());
					for(Admission admission:admissions) {
						admission.setDischargeDate(new Date());
			            admission.setDischargeNotes("Auto-Discharge");
			            admission.setAdmissionStatus(Admission.STATUS_DISCHARGED);
			            admissionManager.saveAdmission(admission);	
					}			
				}
				link.setActive(false);
				groupNoteDao.merge(link);		
			}
		}
		
	
		List<Demographic> anonymousClients = new ArrayList<Demographic>();
		
		//create anonymous clients
		for(int x=0;x<totalAnonymous;x++) {
			Demographic d = CreateAnonymousClientAction.generateAnonymousClient(Integer.valueOf(programId));
			anonymousClients.add(d);
		}
		
		logger.info("created anonymous clients");
		
		//save links
		if(ids!=null) {
			for(String id:ids) {
				GroupNoteLink link = new GroupNoteLink();
				link.setNoteId(Integer.valueOf(cform.getNoteId()));
				link.setDemographicNo(Integer.valueOf(id));
				link.setActive(true);
				groupNoteDao.persist(link);
			}
		}
		
		for(Demographic d:anonymousClients) {
			GroupNoteLink link = new GroupNoteLink();
			link.setNoteId(Integer.valueOf(cform.getNoteId()));
			link.setDemographicNo(d.getDemographicNo());
			link.setAnonymous(true);
			link.setActive(true);
			groupNoteDao.persist(link);
		}

		logger.info("links saved");
        return 0;
	}
}