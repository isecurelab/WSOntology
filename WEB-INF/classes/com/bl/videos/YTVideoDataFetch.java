package com.bl.videos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.db.connection.SQLConnection;

import com.utility.CommonUtility;
import com.utility.Constant;

public class YTVideoDataFetch {

	private static org.apache.log4j.Logger log = Logger.getLogger(YTVideoDataFetch.class);
	CommonUtility commonUtility = new CommonUtility();
	YTVideoUtility ytUtility = new YTVideoUtility();

	public static void main(String[] args) {
		// System.out.println(new YTVideoDataFetch().executeRequest("SSL"));
		// System.out.println(new
		// YTVideoDataFetch().getVideoDataFromDB("3TDES"));
		System.out.println(new YTVideoDataFetch().getVideoPersonalizedDataFromDB("Credential", "at375"));
		// System.out.println(new
		// YTVideoDataFetch().insertPersonalizedVideoData("3TDES","at375","XLGRNpdofoM"));
		// System.out.println(new
		// YTVideoDataFetch().deletePersonalizedVideoData("3TDES","at375","XLGRNpdofoM"));

		// new
		// YTVideoDataFetch().jsonToDataObjVideo("{\"videoId\":\"T98CIaryCs4\",\"title\":\"What
		// is an Asset-Backed Security
		// ?\",\"thumbNail\":\"https://i.ytimg.com/vi/T98CIaryCs4/default.jpg\",\"className\":\"\",\"noLikes\":\"9\",\"noDisLike\":\"0\",\"dateTime\":\"2015-01-17T08:45:06.000Z\",\"studentId\":\"at375\"}");
	}

	public String executeRequest(String queryString) {

		CommonUtility.writeLog("Inside YTVideoDataFetch executeResquest method.", log, Constant.logLevelInfo);

		String youTubeAPIKey = commonUtility.getConfigValue(Constant.youTubeAPIKey);
		String youTubeGetVideoIdEndPoint = commonUtility.getConfigValue(Constant.youTubeGetVideoIdEndPoint);
		String searchDomain = commonUtility.getConfigValue(Constant.searchDomain);

		youTubeGetVideoIdEndPoint = MessageFormat.format(youTubeGetVideoIdEndPoint, queryString, searchDomain,
				youTubeAPIKey);

		return jsonToXML(commonUtility.getHTTPResponse(youTubeGetVideoIdEndPoint), queryString);

	}

	public String jsonToXML(String jsonData, String className) {

		StringBuffer stringBuffer = new StringBuffer();
		DataObjectVideo dVideo = null;
		JSONParser parser = new JSONParser();
		try {

			JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
			JSONArray jsonArray = (JSONArray) jsonObject.get("items");
			stringBuffer.append("<Results>");
			for (int i = 0; i < jsonArray.size(); i++) {
				dVideo = new DataObjectVideo();
				dVideo.setClassName(className);
				JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
				JSONObject jsonObject2 = (JSONObject) jsonObject1.get("id");
				JSONObject jsonObject3 = (JSONObject) jsonObject1.get("snippet");
				dVideo.setVideoId((String) jsonObject2.get("videoId"));
				dVideo.setPublishedDateTime((String) jsonObject3.get("publishedAt"));

				dVideo = ytUtility.getDescription(dVideo);
				dVideo = ytUtility.getStatistics(dVideo);
				stringBuffer.append("<Row Pinned=\"2\">");
				stringBuffer.append("<VedURL>" + dVideo.getVideoId() + "</VedURL>");
				if (dVideo.getDescription() != null)
					stringBuffer.append("<VedDesc>" + dVideo.getDescription().replaceAll("&", "and") + "</VedDesc>");
				else
					stringBuffer.append("<VedDesc>" + dVideo.getDescription() + "</VedDesc>");
				stringBuffer.append("<VedThumbNail>" + dVideo.getThumbnail() + "</VedThumbNail>");
				if (dVideo.getTitle() != null)
					stringBuffer.append("<VedTitle>" + dVideo.getTitle().replaceAll("&", "and") + "</VedTitle>");
				else
					stringBuffer.append("<VedTitle>" + dVideo.getTitle() + "</VedTitle>");
				stringBuffer.append("<NumDislikes>" + dVideo.getNoDisLikes() + "</NumDislikes>");
				stringBuffer.append("<NumLikes>" + dVideo.getNoLikes() + "</NumLikes>");
				stringBuffer.append("<PublishedDateTime>" + dVideo.getPublishedDateTime() + "</PublishedDateTime>");
				stringBuffer.append("<ViewCount>" + dVideo.getViewCount() + "</ViewCount>");
				stringBuffer.append("<NumRaters>" + dVideo.getNoRaters() + "</NumRaters>");
				stringBuffer.append("</Row>");
			}
			stringBuffer.append("</Results>");
		} catch (Exception ae) {
			ae.printStackTrace();
		}
		return stringBuffer.toString();
	}

	public String getVideoDataFromDB(String className) {

		CommonUtility.writeLog("Inside YTVideoDataFetch getVideoDataFromDB method.", log, Constant.logLevelInfo);

		StringBuffer stringBuffer = new StringBuffer();

		try {

			Connection con = new SQLConnection().getConnection();
			PreparedStatement pStatement = con
					.prepareStatement(commonUtility.getConfigValue(Constant.youTubeVideoFromDB));
			pStatement.setString(1, className);
			System.out.println(pStatement);
			ResultSet rs = pStatement.executeQuery();

			stringBuffer.append("<Results>");
			while (rs.next()) {
				stringBuffer.append("<Row Pinned=\"2\">");
				stringBuffer.append("<VedURL>" + rs.getString("videoId") + "</VedURL>");
				stringBuffer.append("<VedDesc>N.A.</VedDesc>");
				stringBuffer.append("<VedThumbNail>" + rs.getString("Thumbnail") + "</VedThumbNail>");
				stringBuffer.append("<VedTitle>" + rs.getString("Title").replaceAll("&", "and") + "</VedTitle>");
				stringBuffer.append("<NumDislikes>" + rs.getString("NoDisLike") + "</NumDislikes>");
				stringBuffer.append("<NumLikes>" + rs.getString("NoLikes") + "</NumLikes>");
				stringBuffer.append("<PublishedDateTime>" + rs.getString("DateTime") + "</PublishedDateTime>");
				
				stringBuffer.append("</Row>");
			}
			stringBuffer.append("</Results>");

		} catch (Exception ae) {
			ae.printStackTrace();
		}
		CommonUtility.writeLog("Return From DB : " + stringBuffer.toString(), log, Constant.logLevelInfo);
		return stringBuffer.toString();
	}

	public String getVideoPersonalizedDataFromDB(String className, String studentId) {

		CommonUtility.writeLog("Inside YTVideoDataFetch getVideoPersonalizedDataFromDB method.", log,
				Constant.logLevelInfo);

		StringBuffer stringBuffer = new StringBuffer();
		try {
			Connection con = new SQLConnection().getConnection();

			if (con != null) {

				PreparedStatement pStatement = con
						.prepareStatement(commonUtility.getConfigValue(Constant.youTubePersonalizedVideoFromDB));
				pStatement.setString(1, className);
				pStatement.setString(2, className);
				pStatement.setString(3, studentId);
				pStatement.setString(4, className);
				pStatement.setString(5, className);
				pStatement.setString(6, studentId);
				ResultSet rs = pStatement.executeQuery();
				System.out.println(pStatement);
				stringBuffer.append("<Results>");
				while (rs.next()) {
					System.out.println(rs.getString("VideoId"));
					stringBuffer.append("<Row>");
					stringBuffer.append("<VedURL>" + rs.getString("VideoId") + "</VedURL>");
					stringBuffer.append("<VedDesc>N.A.</VedDesc>");
					stringBuffer.append("<VedThumbNail>" + rs.getString("Thumbnail") + "</VedThumbNail>");
					stringBuffer.append("<VedTitle>" + rs.getString("Title").replaceAll("&", "and") + "</VedTitle>");
					stringBuffer.append("<NumDislikes>" + rs.getString("NoDisLike") + "</NumDislikes>");
					stringBuffer.append("<NumLikes>" + rs.getString("NoLikes") + "</NumLikes>");
					stringBuffer.append("<PublishedDateTime>" + rs.getString("DateTime") + "</PublishedDateTime>");
					// stringBuffer.append("<ViewCount>" +
					// rs.getString("ViewCount") + "</ViewCount>");
					// stringBuffer.append("<NumRaters>" +
					// rs.getString("NoRaters") + "</NumRaters>");
					stringBuffer.append("</Row>");
				}
				stringBuffer.append("</Results>");

				con.close();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		}
		CommonUtility.writeLog("Return From DB : " + stringBuffer.toString(), log, Constant.logLevelInfo);
		return stringBuffer.toString();
	}

	public int insertPersonalizedVideoData(String className, String studentId, String videoId) {

		CommonUtility.writeLog("Inside YTVideoDataFetch insertPersonalizedVideoData method.", log,
				Constant.logLevelInfo);

		try {
			System.out.println(commonUtility.getConfigValue(Constant.youTubePersonalizedVideoToDB));
			Connection con = new SQLConnection().getConnection();
			PreparedStatement pStatement = con
					.prepareStatement(commonUtility.getConfigValue(Constant.youTubePersonalizedVideoToDB));
			pStatement.setString(1, studentId);
			pStatement.setString(2, className);
			pStatement.setString(3, videoId);
			

			int returnValue = pStatement.executeUpdate();
			DataObjectVideo dVideo = new DataObjectVideo();
			dVideo.setVideoId(videoId);
			dVideo.setClassName(className);
			dVideo = ytUtility.getDescription(dVideo);
			dVideo = ytUtility.getStatistics(dVideo);
			setDescriptionData(dVideo);

			return returnValue;

		} catch (Exception ae) {
			ae.printStackTrace();
		}

		return 0;
	}

	public int setDescriptionData(DataObjectVideo dVideo) {
		CommonUtility.writeLog("Inside YTVideoDataFetch setGeneralVideoData method.", log, Constant.logLevelInfo);
		try {

			Connection con = new SQLConnection().getConnection();
			PreparedStatement pStatement = con
					.prepareStatement(commonUtility.getConfigValue(Constant.youTubeGeneralVideoDataToDB));
			pStatement.setString(1, dVideo.getVideoId());
			pStatement.setString(2, dVideo.getTitle());
			pStatement.setString(3, dVideo.getThumbnail());
			pStatement.setString(4, dVideo.getClassName());
			pStatement.setString(5, dVideo.getNoLikes());
			pStatement.setString(6, dVideo.getNoDisLikes());
			pStatement.setString(7, dVideo.getPublishedDateTime());
			System.out.println(pStatement);
			int returnValue = pStatement.executeUpdate();

			// insertPersonalizedVideoData(dVideo.getClassName(),
			// dVideo.getStudentId(), dVideo.getVideoId());
			return returnValue;

		} catch (Exception ae) {

			ae.printStackTrace();
		}

		return 0;
	}

	public int setGeneralVideoData(String queryString) {
		DataObjectVideo dVideo = jsonToDataObjVideo(queryString);
		CommonUtility.writeLog("Inside YTVideoDataFetch setGeneralVideoData method.", log, Constant.logLevelInfo);
		try {

			Connection con = new SQLConnection().getConnection();
			PreparedStatement pStatement = con
					.prepareStatement(commonUtility.getConfigValue(Constant.youTubeGeneralVideoDataToDB));
			pStatement.setString(1, dVideo.getVideoId());
			pStatement.setString(2, dVideo.getTitle());
			pStatement.setString(3, dVideo.getThumbnail());
			pStatement.setString(4, dVideo.getClassName());
			pStatement.setString(5, dVideo.getNoLikes());
			pStatement.setString(6, dVideo.getNoDisLikes());
			pStatement.setString(7, dVideo.getPublishedDateTime());

			int returnValue = pStatement.executeUpdate();

			// insertPersonalizedVideoData(dVideo.getClassName(),
			// dVideo.getStudentId(), dVideo.getVideoId());
			return returnValue;

		} catch (Exception ae) {

			ae.printStackTrace();
		}

		return 0;
	}

	public DataObjectVideo jsonToDataObjVideo(String jsonData) {
		System.out.println(jsonData);
		DataObjectVideo dVideo = new DataObjectVideo();
		JSONParser parser = new JSONParser();

		//
		// JSONArray jsonArray = (JSONArray) jsonObject.get("items");
		// stringBuffer.append("<Results>");
		// for (int i = 0; i < jsonArray.size(); i++) {
		// dVideo = new DataObjectVideo();
		// dVideo.setClassName(className);
		// JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
		// JSONObject jsonObject2 = (JSONObject) jsonObject1.get("id");
		// JSONObject jsonObject3 = (JSONObject) jsonObject1.get("snippet");
		// dVideo.setVideoId((String) jsonObject2.get("videoId"));
		// dVideo.setPublishedDateTime((String) jsonObject3.get("publishedAt"));
		//

		try {
			JSONObject jsonObject1 = (JSONObject) parser.parse(jsonData);
			JSONObject jsonObject = (JSONObject) ((JSONArray) jsonObject1.get("item")).get(0);
			JSONObject jsonSnippet = (JSONObject) jsonObject.get("snippet");
			dVideo.setVideoId(jsonObject.get("id").toString());
			dVideo.setPublishedDateTime(jsonSnippet.get("publishedAt").toString());
			ytUtility.getDescription(dVideo);
			ytUtility.getStatistics(dVideo);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dVideo;
	}

	public int deletePersonalizedVideoData(String className, String studentId, String videoId) {

		CommonUtility.writeLog("Inside YTVideoDataFetch deletePersonalizedVideoData method.", log,
				Constant.logLevelInfo);
		try {

			Connection con = new SQLConnection().getConnection();
			PreparedStatement pStatement = con
					.prepareStatement(commonUtility.getConfigValue(Constant.youTubePersonalizedVideoDeleteDB));
			pStatement.setString(1, studentId);
			pStatement.setString(2, className);
			pStatement.setString(3, videoId);

			return pStatement.executeUpdate();

		} catch (Exception ae) {

		}

		return 0;
	}
}
