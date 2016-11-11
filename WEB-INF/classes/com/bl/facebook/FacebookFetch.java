package com.bl.facebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User; //locha
import com.utility.CommonUtility;
import com.utility.Constant;

public class FacebookFetch {

	public static void main(String[] args) {
		

		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		System.out.println("Fectch");
//		String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
		User me = fbclient.fetchObject("me", User.class);
		System.out.println(me.getName());
		System.out.println(me.getLanguages());
	}

}
