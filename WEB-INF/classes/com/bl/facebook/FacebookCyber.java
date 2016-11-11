package com.bl.facebook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.utility.CommonUtility;
import com.utility.Constant;

import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.api.PostMethods;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class FacebookCyber {
	public static void main(String[] args) throws FacebookException {
		// Create conf builder and set authorization and access keys

		/*
		 * String accessToken =
		 * "EAACEdEose0cBALwX3vQKGFiFlsQ7Ee7tvjDrvtOKCbZAEco8jMZBCxwIekAVVSZCUCfAJ4wVJSaMokFDP0axCWrddYtz0ar3AuWy5LhQM6cY4LzJHabb0BLaVrWw55NyC6e3OUVQ7OlVZAMQSS8W3ZALUXjtfzzRpCxOPM3lhXMIa8fzkDvUP";
		 * FacebookClient fbClient = new DefaultFacebookClient(accessToken);
		 * User me = fbClient.fetchObject("me" , User.class);
		 * System.out.println(me.getName());
		 */

		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);

		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setDebugEnabled(true);
		System.out.println("Cyber");
		configurationBuilder.setOAuthAppId(appId);
		configurationBuilder.setOAuthAppSecret(appSecret);
		configurationBuilder.setOAuthAccessToken(accesstoken);
		configurationBuilder
				.setOAuthPermissions("email, publish_stream, id, name, first_name, last_name, read_stream , generic");
		configurationBuilder.setUseSSL(true);
		configurationBuilder.setJSONStoreEnabled(true);

		// Create configuration and get Facebook instance
		Configuration configuration = (Configuration) configurationBuilder.build();
		FacebookFactory ff = new FacebookFactory(configuration);
		facebook4j.Facebook Facebook = ff.getInstance();

		try {
			// Set search string and get results
			String searchPost = "Cyber Security";
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_hh_mm");
			String fileName = "D:\\FacebookConfigFolder\\File\\" + searchPost + "_" + simpleDateFormat.format(date)
					+ ".txt";
			String results = getFacebookPostes(Facebook, searchPost);
			File file = new File(fileName);

			if (!file.exists()) {
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(results);
				bw.close();
				System.out.println("Completed");
			}
		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	// This method is used to get Facebook posts based on the search string set
	// // above
	public static String getFacebookPostes(facebook4j.Facebook facebook, String searchPost) throws FacebookException {
		String searchResult = "Item : " + searchPost + "\n";
		StringBuffer searchMessage = new StringBuffer();
		ResponseList<Post> results = null;
		try {
			results = ((PostMethods) facebook).getPosts(searchPost);
		} catch (facebook4j.FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Post post : results) {
			System.out.println(post.getMessage());
			searchMessage.append(post.getMessage() + "\n");
			for (int j = 0; j < post.getComments().size(); j++) {
				searchMessage.append(post.getComments().get(j).getFrom().getName() + ", ");
				searchMessage.append(post.getComments().get(j).getMessage() + ", ");
				searchMessage.append(post.getComments().get(j).getCreatedTime() + ", ");
				searchMessage.append(post.getComments().get(j).getLikeCount() + "\n");
			}
		}
		String feedString = getFacebookPostes(facebook, searchPost);
		searchResult = searchResult + searchMessage.toString();
		searchResult = searchResult + feedString;
		return searchResult;
	}

	// // This method is used to create JSON object from data string
	public static String stringToJson(String data) {
		JsonConfig cfg = new JsonConfig();
		try {
			JSONObject jsonObject = JSONObject.fromObject(data, cfg);
			System.out.println("JSON = " + jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "JSON Created";
	}
}
