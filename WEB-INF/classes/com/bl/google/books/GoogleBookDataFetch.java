package com.bl.google.books;

import java.text.MessageFormat;
import org.apache.log4j.Logger;
import com.utility.CommonUtility;
import com.utility.Constant;


public class GoogleBookDataFetch {

	private static org.apache.log4j.Logger log = Logger.getLogger(GoogleBookDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();

	public static void main(String[] args) {
		System.out.println(new GoogleBookDataFetch().executeRequest("SSL"));
	}

	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside GoogleBookDataFetch executeResquest method.", log, Constant.logLevelInfo);
		String googleBooksAPIKey = commonUtility.getConfigValue(Constant.googleBooksAPIKey);
		String googleBooksEndPoint = commonUtility.getConfigValue(Constant.googleBooksEndPoint);
		String searchDomain = commonUtility.getConfigValue(Constant.searchDomain);
		
		googleBooksEndPoint = MessageFormat.format(googleBooksEndPoint, queryString,searchDomain,googleBooksAPIKey);

		return commonUtility.getHTTPResponse(googleBooksEndPoint);

	}

}
