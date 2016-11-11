package com.bl.reddit;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import org.apache.log4j.Logger;
import com.utility.CommonUtility;
import com.utility.Constant;

public class RedditDataFetch {

	
	private static org.apache.log4j.Logger log = Logger.getLogger(RedditDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();
	
	
	
	public static void main(String[] args) {
		RedditDataFetch redditDataFetch = new RedditDataFetch();
		System.out.println(redditDataFetch.executeRequest("Security"));
	}

	
	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside RedditDataFetch executeResquest method.", log, Constant.logLevelInfo);
			
		String redditEndPoint = commonUtility.getConfigValue(Constant.redditEndPoint);
		redditEndPoint = MessageFormat.format(redditEndPoint, queryString);

		
		return xmlTransformation(commonUtility.getHTTPResponse(redditEndPoint));

	}
	
	
	public String xmlTransformation(String xml){
		String result=null;
	    try {
            StringReader reader = new StringReader(xml);
            StringWriter writer = new StringWriter();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            String xsltLocation=commonUtility.getConfigValue(Constant.redditXSLTLocation);
            Transformer transformer = tFactory.newTransformer(
                    new javax.xml.transform.stream.StreamSource(xsltLocation));

            transformer.transform(
                    new javax.xml.transform.stream.StreamSource(reader), 
                    new javax.xml.transform.stream.StreamResult(writer));

             result = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
	    
	    return result;
	}
}
