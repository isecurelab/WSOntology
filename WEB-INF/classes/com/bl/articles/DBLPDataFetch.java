package com.bl.articles;


import java.text.MessageFormat;
import org.apache.log4j.Logger;
import com.utility.CommonUtility;
import com.utility.Constant;

public class DBLPDataFetch {

	private static org.apache.log4j.Logger log = Logger.getLogger(DBLPDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();
	
	
	
	public static void main(String[] args) {
		DBLPDataFetch dbLogic = new DBLPDataFetch();
		System.out.println(dbLogic.executeRequest("SSL"));
	}

	
	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside DBLPDatafetchLogic executeResquest method.", log, Constant.logLevelInfo);
			
		String dblpEndPoint = commonUtility.getConfigValue(Constant.dblpEndPoint);
		String searchDomain = commonUtility.getConfigValue(Constant.searchDomain);
		
		dblpEndPoint = MessageFormat.format(dblpEndPoint, queryString,searchDomain);

		return commonUtility.getHTTPResponse(dblpEndPoint);

	}

}
