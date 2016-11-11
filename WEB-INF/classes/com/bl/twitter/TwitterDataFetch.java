package com.bl.twitter;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.utility.CommonUtility;
import com.utility.Constant;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;

/**
 * @author Karthik
 *
 */
public class TwitterDataFetch {
	private static org.apache.log4j.Logger log = Logger.getLogger(TwitterDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();
	
	
	public static void main(String[] args) {
		System.out.println(new TwitterDataFetch().executeRequest("Confidentiality"));
	}
	public String executeRequest(String query) {
		StringBuilder toret = new StringBuilder("<tweets>");
		try {
			Twitter twitter = new TwitterFactory().getInstance();
			String twitterEndPoint = commonUtility.getConfigValue(Constant.twitterQueryAppender);
			String searchDomain = commonUtility.getConfigValue(Constant.searchDomain);
			twitterEndPoint = MessageFormat.format(twitterEndPoint,searchDomain);
			
			Query query1 = new Query((query +"+"+ twitterEndPoint).trim());
			log.info("Twitter Query:" + query1.getQuery());
			query1.setCount(20);
			QueryResult result;
			result = twitter.search(query1);
			for (Status status : result.getTweets()) {
				URLEntity[] links = status.getURLEntities();
				String urlstr = "#";
				if (links.length != 0) urlstr = links[0].getURL();
				toret.append( 
						"<tweet>" +
						" <user_name><![CDATA["+ status.getUser().getName() + "]]></user_name>" +
						" <user_screenname><![CDATA["+ status.getUser().getScreenName() + "]]></user_screenname>" +
						" <userimg><![CDATA["+ status.getUser().getMiniProfileImageURL() +"]]></userimg>" +	
						" <message><![CDATA["+ status.getText() +"]]></message>" +	
						" <date><![CDATA["+ status.getCreatedAt().toString() +"]]></date>" +	
						" <link><![CDATA["+ urlstr +"]]></link>" +	
						" <src>https://twitter.com/" + status.getUser().getScreenName() + "/status/"+ status.getId() +"</src>" +
						"</tweet>");
				//log.info("@" + status.getUser().getScreenName() + ":" + status.getText());
			}
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    toret.append("</tweets>");
	    return toret.toString();
		
		
	}
	
}
