package com.bl.facebook;

import com.facebook.api.schema.User;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.utility.CommonUtility;
import com.utility.Constant;

public class FacebookTest {
	public static void main(String[] args) {

		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);

		
		System.out.println("Test");
		// String accesstoken =
		// "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbClient = new DefaultFacebookClient(accesstoken);
		User me = fbClient.fetchObject("me", User.class);
		System.out.println(me.getName());

	}
}
