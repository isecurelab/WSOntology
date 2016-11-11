package com.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.bl.videos.YTVideoDataFetch;

public class CommonUtility {
	private static org.apache.log4j.Logger log = Logger.getLogger(CommonUtility.class);
	
	public static void main(String[] args) {

		System.out.println(new CommonUtility().getConfigValue("URL"));
	}

	public String getConfigValue(String key) {

		ResourceBundle rb = ResourceBundle.getBundle(Constant.envPropFileName);
		String env = rb.getString(Constant.envPropFileKey);
		rb = ResourceBundle.getBundle(env + Constant.envConfigFileSuffix);

		return rb.getString(key);
	}

	public String getHTTPResponse(String httpRequestUrl) {

		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";

		CommonUtility.writeLog("Http Request Url : "+httpRequestUrl, log, Constant.logLevelInfo);
		
		try {
			url = new URL(httpRequestUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(Constant.httpGetMethod);
			
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			CommonUtility.writeLog("Http Status Code : "+conn.getResponseCode(), log, Constant.logLevelInfo);

			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		CommonUtility.writeLog("Http Response : "+result, log, Constant.logLevelInfo);
		
		return result;
	}

	public static void writeLog(String logMessage, org.apache.log4j.Logger log, String logLevel) {
		
		if (logLevel.equals(Constant.logLevelInfo))
			log.info( logMessage);

	}
}
