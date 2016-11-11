package com.bl.facebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.utility.*;


public class ExtendedFBAccessToken {
	public static void main(String[] args)
	{
		//String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
//		String appId = "301808846835112";
//		String appSecret = "d5e54e19d2294a073ac40368130976e1";
		System.out.println("fbAceessToken");
		AccessToken exToken = fbclient.obtainExtendedAccessToken(appId, appSecret);
		
		System.out.println(exToken.getAccessToken());
		System.out.println(exToken.getExpires());
	}
}
