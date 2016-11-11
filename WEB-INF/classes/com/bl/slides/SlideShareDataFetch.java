package com.bl.slides;

import java.security.MessageDigest;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import com.utility.CommonUtility;
import com.utility.Constant;

public class SlideShareDataFetch {

	public static void main(String[] args) {

		System.out.println(new SlideShareDataFetch().executeRequest("Attack"));
	}

	public static long ts;
	public static String hash;
	private static org.apache.log4j.Logger log = Logger.getLogger(SlideShareDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();

	public String SHA1(String slideShareSecretKey, long ts) {
		try {
			String a = slideShareSecretKey + ts;
			
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(a.getBytes());
			byte[] output = md.digest();

			hash = bytesToHex(output);

		} catch (Exception ae) {
			ae.printStackTrace();
		}
		return hash.toLowerCase();

	}

	public String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}

	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside SlideShareDataFetch executeResquest method.", log, Constant.logLevelInfo);

		String slideShareSecretKey = commonUtility.getConfigValue(Constant.slideShareSecretKey);
		String slideShareApiKey = commonUtility.getConfigValue(Constant.slideShareApiKey);
		
		long ts = System.currentTimeMillis() / 1000L;
		String h = SHA1(slideShareSecretKey, ts);

		String slideShareEndPoint = commonUtility.getConfigValue(Constant.slideShareEndPoint);
		slideShareEndPoint = MessageFormat.format(slideShareEndPoint, queryString, slideShareApiKey, h,""+ts);

		return commonUtility.getHTTPResponse(slideShareEndPoint);

	}

}
