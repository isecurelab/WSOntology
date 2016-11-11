package com.bl.google.images;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import com.utility.CommonUtility;
import com.utility.Constant;

public class GoogleImageDataFetch {

	private static org.apache.log4j.Logger log = Logger.getLogger(GoogleImageDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();

	public static void main(String[] args) {
		GoogleImageDataFetch googleImageDataFetch = new GoogleImageDataFetch();
		System.out.println(googleImageDataFetch.executeRequest("SSL"));
	}

	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside GoogleImageDataFetch executeResquest method.", log, Constant.logLevelInfo);

		String googleImageEndPoint = commonUtility.getConfigValue(Constant.googleImageEndPoint);
		String searchDomain = commonUtility.getConfigValue(Constant.searchDomain);
		googleImageEndPoint = MessageFormat.format(googleImageEndPoint, queryString,searchDomain);

		return jsonToXML(commonUtility.getHTTPResponse(googleImageEndPoint));

	}

	public String jsonToXML(String jsonData) {

		ObjectMapper mapper = new ObjectMapper();
		StringBuilder toret = new StringBuilder("<Images>");
		JsonNode rootNode = null;
		try {
			rootNode = mapper.readValue(jsonData, JsonNode.class);

			Iterator<JsonNode> results = rootNode.get("responseData").get("results").getElements();
			while (results.hasNext()) {
				JsonNode n = results.next();
				String imgURL = n.get("url").getTextValue();
				String wwwURL = n.get("originalContextUrl").getTextValue();
				String title = n.get("title").getTextValue();
				toret.append("<Image>" + "<Img>" + imgURL + "</Img>" + "<Src>" + wwwURL + "</Src>" + "<Title>" + title
						+ "</Title>" + "</Image>");
			}
			toret.append("</Images>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toret.toString();
	}
}
