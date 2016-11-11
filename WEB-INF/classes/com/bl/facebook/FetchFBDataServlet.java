package com.bl.facebook;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.utility.CommonUtility;
import com.utility.Constant;

/**
 * Servlet implementation class FetchFBDataServlet
 */
@WebServlet("/FetchFBDataServlet")
public class FetchFBDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FetchFBDataServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject json = new JSONObject();
		JSONArray jsonpages = new JSONArray();
		JSONObject jsonpage;

		CommonUtility cU = new CommonUtility();
		String accesstoken = cU.getConfigValue(Constant.facebookAccessToken);
		String appId = cU.getConfigValue(Constant.facebookAppID);
		String appSecret = cU.getConfigValue(Constant.facebookSecretID);
		System.out.println("Servlet");
//		String accesstoken = "EAACEdEose0cBAP9hoot5Ha7a8omF3CPxFYPlhzaU6ZBRcZCDDWOvVOntewYdMtoJRbrPtPJZABHbi4NuDmSUr3o9SZAnfVo5dAyD1kP0GwgSmqz6VZANvPhfV5GQgzgWfwcNw0HAF70ZA6yYBbPix2PmIA0lVDUWPRFLvCN7csUDYSsWlq4udm";
		FacebookClient fbclient = new DefaultFacebookClient(accesstoken);
		
		Connection<Page> result = fbclient.fetchConnection("me/likes", Page.class);
		
		for(List<Page> feedPage : result)
		{ 
			for(Page page: feedPage)
			{ 
				jsonpage = new JSONObject();
				try {
					jsonpage.put("PageID",page.getId());
					jsonpage.put("PageName",page.getName());
					jsonpages.put(jsonpage);
				} catch (JSONException e) {
					e.printStackTrace();
				}								
			}					
		}
		/*try {
			json.put("Pages", jsonpages);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		
		response.setContentType("application/json");
		response.getWriter().write(jsonpages.toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
