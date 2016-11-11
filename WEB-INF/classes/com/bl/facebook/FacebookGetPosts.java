package com.bl.facebook;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Post;
import com.utility.CommonUtility;
import com.utility.Constant;

import java.util.List;

public class FacebookGetPosts {

	public static void main(String[] args) {
		
		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		
		System.out.println("GetPosts");
		// TODO Auto-generated method stub
		//String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
		Connection<Post> result = fbclient.fetchConnection("me/feed", Post.class);
		
		for(List<Post> page : result)
		{ 
			for(Post aPost: page)
			{ 
				System.out.println(aPost.getMessage());
			}
		}
		
	}

}
