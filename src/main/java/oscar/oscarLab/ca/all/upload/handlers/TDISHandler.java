/*
 * HL7Handler
 * Upload handler
 * 
 */
package oscar.oscarLab.ca.all.upload.handlers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.upload.MessageUploader;
import oscar.oscarLab.ca.all.util.Utilities;

/**
 * 
 */
public class TDISHandler implements MessageHandler {

	Logger logger = Logger.getLogger(TDISHandler.class);

	public TDISHandler() {
		logger.info("NEW TDISHandler UPLOAD HANDLER instance just instantiated. ");
	}

	public String parse(String serviceName, String fileName, int fileId) {
		logger.info("ABOUT TO PARSE!");
		

		int i = 0;
		try {
			ArrayList messages = Utilities.separateMessages(fileName);
			
			for (i = 0; i < messages.size(); i++) {
				
				String msg = (String) messages.get(i);
				
			
				MessageUploader.routeReport(serviceName, "TDIS", msg, fileId);

			}
			
			// Since the gdml labs show more than one lab on the same page when
			// grouped
			// by accession number their abnormal status must be updated to
			// reflect the
			// other labs that they are grouped with aswell
			updateLabStatus(messages.size());
			logger.info("Parsed OK");
		} catch (Exception e) {
			MessageUploader.clean(fileId);
			logger.error("Could not upload message", e);
			return null;
		}
		return ("success");

	}

	// recheck the abnormal status of the last 'n' labs
	private void updateLabStatus(int n) throws SQLException {
		Hl7TextInfoDao hl7TextInfoDao = (Hl7TextInfoDao) SpringUtils.getBean("hl7TextInfoDao");
		 List<Hl7TextInfo> labList = hl7TextInfoDao.getAllLabsByLabNumberResultStatus();
		 ListIterator<Hl7TextInfo> iter = labList.listIterator();
		 
		 while (iter.hasNext() && n>0) {
			 if (!iter.next().getResultStatus().equals("A")) {
				 oscar.oscarLab.ca.all.parsers.MessageHandler h = Factory.getHandler(((Integer)iter.next().getLabNumber()).toString());
				 
	                int i=0;
	                int j=0;
	                String resultStatus = "";
	                while(resultStatus.equals("") && i < h.getOBRCount()){
	                    j = 0;
	                    while(resultStatus.equals("") && j < h.getOBXCount(i)){
	                        logger.info("obr("+i+") obx("+j+") abnormal ? : "+h.getOBXAbnormalFlag(i, j));
	                        if(h.isOBXAbnormal(i, j)){
	                            resultStatus = "A";
	                            hl7TextInfoDao.updateResultStatusByLabId("A", iter.next().getLabNumber());
	                            
	                        }
	                        j++;
	                    }
	                    i++;
	                }
			 }
			 n--;
		 }
	}

}