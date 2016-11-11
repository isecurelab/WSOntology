package com.bl.facebook;

import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.utility.CommonUtility;
import com.utility.Constant;

public class FacebookLikePages {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		
		System.out.println("LIkePages");
//		String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
		Connection<Page> result = fbclient.fetchConnection("me/likes", Page.class);
		
		int counter = 0;
		
		for(List<Page> feedPage : result)
		{ 
			for(Page page: feedPage)
			{ 
				System.out.println(page.getName());
				System.out.println("fb.com/"+page.getId());	
			}
			System.out.println("");
			counter++;
	}
	}
}
