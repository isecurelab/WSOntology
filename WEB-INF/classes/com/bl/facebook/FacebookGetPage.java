package com.bl.facebook;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.utility.CommonUtility;
import com.utility.Constant;

public class FacebookGetPage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		System.out.println("GetPage");
		//String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
		Connection<Page> result = fbclient.fetchConnection("me/likes", Page.class);
		
		int counter = 0;
		
		for(List<Page> feedPage : result)
		{ 
			for(Page page: feedPage)
			{ 
				//System.out.println(page.getName());
				//System.out.println("fb.com/"+page.getId());	
								
				ObjectMapper mapper = new ObjectMapper();
				
				 //Object to JSON in file
				try {
					mapper.writeValue(new File("E:\\slob\\"+(counter++)+".json"), new FBPageData(Long.parseLong(page.getId()), page.getName()));
				} catch (JsonGenerationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 /*Connection<Post> pageFeed = fbclient.fetchConnection(page.getId() + "/feed", Post.class);
			     while (pageFeed.hasNext()) {
			            pageFeed = fbclient.fetchConnectionPage(pageFeed.getNextPageUrl(),Post.class);
			            for (List<Post> feed : pageFeed){
			                for (Post post : feed){     
			                    System.out.println(post.getMessage());
			                }
			            	
			            }
			           
						
			        }*/						
				 
				//System.out.println("fb.com/"+page.getDescription());
				System.out.println("");
				counter++;
			}					
		}
		System.out.println("Number of pages found:" +counter);
	}

}



