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

package org.oscarehr.ws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.jws.WebService;

import org.apache.cxf.annotations.GZIP;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.ws.transfer_objects.DataIdTransfer;
import org.oscarehr.ws.transfer_objects.PreventionTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@WebService
@Component
@GZIP(threshold = AbstractWs.GZIP_THRESHOLD)
public class PreventionWs extends AbstractWs {
	@Autowired
	private PreventionManager preventionManager;

	public PreventionTransfer getPrevention(Integer preventionId) {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		
		Prevention prevention = preventionManager.getPrevention(loggedInInfo,preventionId);

		if (prevention != null) {
			List<PreventionExt> preventionExts = preventionManager.getPreventionExtByPrevention(loggedInInfo,prevention.getId());
			return (PreventionTransfer.toTransfer(prevention, preventionExts));
		}

		return (null);
	}

	public PreventionTransfer[] getPreventionsUpdatedAfterDate(Date updatedAfterThisDateInclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		
		List<Prevention> preventions=preventionManager.getUpdatedAfterDate(loggedInInfo,updatedAfterThisDateInclusive, itemsToReturn);
		ArrayList<PreventionTransfer> results=new ArrayList<PreventionTransfer>();
		
		for (Prevention prevention : preventions)
		{
			List<PreventionExt> preventionExts = preventionManager.getPreventionExtByPrevention(loggedInInfo,prevention.getId());
			PreventionTransfer preventionTransfer=PreventionTransfer.toTransfer(prevention, preventionExts);
			results.add(preventionTransfer);
		}
		
		return(results.toArray(new PreventionTransfer[0]));
	}
	
	/**
	 * Get a list of DataIdTransfer objects for preventions starting with the passed in Id.
	 * @deprecated 2014-05-20 use getUpdatedAfterDate() instead
	 */
	public DataIdTransfer[] getPreventionDataIds(Boolean active, Integer startIdInclusive, int itemsToReturn) {
		LoggedInInfo loggedInInfo=getLoggedInInfo();
		
		Boolean archived = null;
		if (active != null) archived = !active;

		List<Prevention> preventions = preventionManager.getPreventionsByIdStart(loggedInInfo, archived, startIdInclusive, itemsToReturn);

		DataIdTransfer[] results = new DataIdTransfer[preventions.size()];
		for (int i = 0; i < preventions.size(); i++) {
			results[i] = getDataIdTransfer(preventions.get(i));
		}

		return (results);
	}

	private DataIdTransfer getDataIdTransfer(Prevention prevention) {
		DataIdTransfer result = new DataIdTransfer();

		Calendar cal = new GregorianCalendar();
		cal.setTime(prevention.getCreationDate());
		result.setCreateDate(cal);

		result.setCreatorProviderId(prevention.getProviderNo());
		result.setDataId(prevention.getId().toString());
		result.setDataType(Prevention.class.getSimpleName());
		result.setOwnerDemographicId(prevention.getDemographicId());

		return (result);
	}
}
