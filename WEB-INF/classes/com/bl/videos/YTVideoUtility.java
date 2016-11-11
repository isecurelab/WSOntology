package com.bl.videos;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.utility.CommonUtility;
import com.utility.Constant;

public class YTVideoUtility {

	private static org.apache.log4j.Logger log = Logger.getLogger(YTVideoUtility.class);
	CommonUtility commonUtility = new CommonUtility();

	public static void main(String[] args) {

		YTVideoUtility utility = new YTVideoUtility();

		DataObjectVideo dVideo = new DataObjectVideo();
		dVideo.setVideoId("CLV2iTtQRDE");
		utility.getDescription(dVideo);
		utility.getStatistics(dVideo);
	}

	public DataObjectVideo getDescription(DataObjectVideo dVideo) {

		String youTubeAPIKey = commonUtility.getConfigValue(Constant.youTubeAPIKey);
		String youTubeDescriptionEndPoint = commonUtility.getConfigValue(Constant.youTubeDescriptionEndPoint);
		youTubeDescriptionEndPoint = MessageFormat.format(youTubeDescriptionEndPoint, dVideo.getVideoId(),
				youTubeAPIKey);

		String jsonData = commonUtility.getHTTPResponse(youTubeDescriptionEndPoint);
		JSONParser parser = new JSONParser();

		try {
			JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
			JSONArray jsonArray = (JSONArray) jsonObject.get("items");

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
				JSONObject jsonObject2 = (JSONObject) jsonObject1.get("snippet");
				JSONObject jsonObject3 = (JSONObject) jsonObject2.get("thumbnails");
				JSONObject jsonObject4 = (JSONObject) jsonObject3.get("default");
				dVideo.setTitle((String) jsonObject2.get("title"));
				dVideo.setDescription((String) jsonObject2.get("description"));
				dVideo.setThumbnail((String) jsonObject4.get("url"));
				dVideo.setPublishedDateTime(jsonObject2.get("publishedAt").toString());
			}
			return dVideo;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}

	public String getDescriptionByVideoId(String VideoId) {
		DataObjectVideo dVideo = new DataObjectVideo();
		dVideo.VideoId = VideoId;
		String youTubeAPIKey = commonUtility.getConfigValue(Constant.youTubeAPIKey);
		String youTubeDescriptionEndPoint = commonUtility.getConfigValue(Constant.youTubeDescriptionEndPoint);
		youTubeDescriptionEndPoint = MessageFormat.format(youTubeDescriptionEndPoint, VideoId, youTubeAPIKey);

		String returnData = commonUtility.getHTTPResponse(youTubeDescriptionEndPoint);
		System.out.println(returnData);
		return returnData;
	}

	public DataObjectVideo getStatistics(DataObjectVideo dVideo) {

		String youTubeAPIKey = commonUtility.getConfigValue(Constant.youTubeAPIKey);
		String youTubeStatisticEndPoint = commonUtility.getConfigValue(Constant.youTubeStatisticEndPoint);
		youTubeStatisticEndPoint = MessageFormat.format(youTubeStatisticEndPoint, dVideo.getVideoId(), youTubeAPIKey);

		String jsonData = commonUtility.getHTTPResponse(youTubeStatisticEndPoint);
		JSONParser parser = new JSONParser();

		try {

			JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
			JSONArray jsonArray = (JSONArray) jsonObject.get("items");

			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
				JSONObject jsonObject2 = (JSONObject) jsonObject1.get("statistics");
				dVideo.setViewCount((String) jsonObject2.get("viewCount"));
				dVideo.setNoLikes((String) jsonObject2.get("likeCount"));
				dVideo.setNoDisLikes((String) jsonObject2.get("dislikeCount"));
				dVideo.setFavCount((String) jsonObject2.get("favoriteCount"));
				dVideo.setCommentCount((String) jsonObject2.get("commentCount"));

			}
			return dVideo;

		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return null;
	}
}
