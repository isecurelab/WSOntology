package com.bl.facebook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.utility.CommonUtility;
import com.utility.Constant;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.ResponseList;
import facebook4j.conf.Configuration;
import facebook4j.conf.ConfigurationBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FacebookSecurity {
	
	public static void main(String[] args) throws FacebookException { 
		
		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		
		
		// Make the configuration builder
		ConfigurationBuilder confBuilder = new ConfigurationBuilder();
		confBuilder.setDebugEnabled(true);
		System.out.println("Security");
		// Set application id, secret key and access token 
		confBuilder.setOAuthAppId(appId);
        confBuilder.setOAuthAppSecret(appSecret);
        confBuilder.setOAuthAccessToken(accesstoken);
		
        // Set permission
        confBuilder.setOAuthPermissions("email,publish_stream, id, name, first_name, last_name, generic"); 
        confBuilder.setUseSSL(true);
        confBuilder.setJSONStoreEnabled(true);
        
        // Create configuration object
        Configuration configuration = confBuilder.build();
        
        // Create facebook instance
        FacebookFactory ff = new FacebookFactory(configuration);
        Facebook facebook = ff.getInstance();

        try {
        	// Get facebook posts
        	String results = getFacebookPostes(facebook); 
			String responce = stringToJson(results); 
			
			// Create file and write to the file
			File file = new File("C:\\Facebook\\File\\test.txt");
			if (!file.exists())
			{
				file.createNewFile();
				FileWriter fw = new FileWriter(file.getAbsoluteFile()); 
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(results); bw.close(); 
				System.out.println("Writing complete"); 
			}				
        }
        catch (IOException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        }
	}
	
	public static String getFacebookPostes(Facebook facebook) throws FacebookException { 
		// Get posts for a particular search 
		ResponseList<Post> results = facebook.getPosts("Reebok"); 
		return results.toString();
	}
	
	public static String stringToJson(String data) { 
		// Create JSON object 
		JSONObject jsonObject = JSONObject.fromObject(data); 
		JSONArray message = (JSONArray) jsonObject.get("message"); 
		System.out.println("Message : "+message);
		return "Done"; 
	}
}



