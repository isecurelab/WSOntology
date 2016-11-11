package com.restapi.method;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import com.bl.articles.DBLPDataFetch;
import com.bl.google.books.GoogleBookDataFetch;
import com.bl.google.images.GoogleImageDataFetch;
import com.bl.owl.OWLDataFetch;
import com.bl.reddit.RedditDataFetch;
import com.bl.slides.SlideShareDataFetch;
import com.bl.twitter.TwitterDataFetch;
import com.bl.videos.YTVideoDataFetch;

@Path("/api")
public class RestApiMethod {
	private static org.apache.log4j.Logger log = Logger.getLogger(RestApiMethod.class);

	@Path("/setGeneralVideoToDB")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String setGeneralVideoToDB(String queryString) {

		return Integer.toString(new YTVideoDataFetch().setGeneralVideoData(queryString));
	}
		
	@Path("/getVideoFromWeb/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getVideoFromWeb(@PathParam("queryString") String queryString) {
		queryString = getSearchTerm(queryString).toString();
		String returnValue = new YTVideoDataFetch().executeRequest(queryString);

		if (returnValue != null) {

			return returnValue;
		} else {

			return "false";
		}
	}

	@Path("/getVideoFromDB/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getVideoFromDB(@PathParam("queryString") String queryString) {

		String returnValue = new YTVideoDataFetch().getVideoDataFromDB(queryString.replaceAll("_", ""));
		;

		if (returnValue != null) {

			return returnValue;
		} else {

			return "false";
		}
	}

	@Path("/getPersonalizedvideoFromDB/{queryString}/{studentId}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getPersonalizedvideoFromDB(@PathParam("queryString") String queryString,
			@PathParam("studentId") String studentId) {

		String returnValue = new YTVideoDataFetch().getVideoPersonalizedDataFromDB(queryString.replaceAll("_", ""),
				studentId);

		if (returnValue != null) {

			return returnValue;
		} else {

			return "false";
		}
	}

	@Path("/setPersonalizedVideoToDB/{className}/{studentId}/{videoId}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String setPersonalizedVideoToDB(@PathParam("className") String className, @PathParam("studentId") String studentId,
			@PathParam("videoId") String videoId) {

		return Integer.toString(new YTVideoDataFetch().insertPersonalizedVideoData(className, studentId, videoId));
	}
	
	@Path("/deletePersonalizedVideoFromDB/{className}/{studentId}/{videoId}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String deletePersonalizedVideoFromDB(@PathParam("className") String className, @PathParam("studentId") String studentId,
			@PathParam("videoId") String videoId) {

		return Integer.toString(new YTVideoDataFetch().deletePersonalizedVideoData(className, studentId, videoId));
	}

	@Path("/getArticleData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getArticleData(@PathParam("queryString") String queryString) {

		queryString = getSearchTerm(queryString).toString();
		String returnValue = new DBLPDataFetch().executeRequest(queryString);

		return returnValue;
	}

	@Path("/getTwitterData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getTwitterData(@PathParam("queryString") String queryString) {

		queryString = getSearchTerm(queryString).toString();
		String returnValue = new TwitterDataFetch().executeRequest(queryString);

		return returnValue;
	}

	@Path("/getRedditData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getRedditData(@PathParam("queryString") String queryString) {

		queryString = getSearchTerm(queryString).toString();
		String returnValue = new RedditDataFetch().executeRequest(queryString);

		return returnValue;
	}

	@Path("/getGoogleImageData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getGoogleImageData(@PathParam("queryString") String queryString) {

		queryString = getSearchTerm(queryString).toString();
		String returnValue = new GoogleImageDataFetch().executeRequest(queryString);

		return returnValue;
	}

	@Path("/getDescriptionFromOwl/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getDescriptionFromOwl(@Context HttpServletRequest req, @PathParam("queryString") String queryString) {

		return new OWLDataFetch().getOwlData(queryString.replaceAll(" ", ""));
	}

	@Path("/getBookPageFromOwl/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getBookPageFromOwl(@PathParam("queryString") String queryString) {

		return new OWLDataFetch().getBookPage(queryString.replaceAll(" ", ""));
	}

	@Path("/getGoogleBookData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getGoogleBookData(@PathParam("queryString") String queryString) {

		String returnResult = new GoogleBookDataFetch().executeRequest(queryString);

		return returnResult;
	}

	@Path("/getSlideShareData/{queryString}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getSlideShareData(@PathParam("queryString") String queryString) {

		SlideShareDataFetch slFetch = new SlideShareDataFetch();
		queryString = getSearchTerm(queryString).toString();

		String returnValue = slFetch.executeRequest(queryString);

		return returnValue;

	}

	public StringBuffer getSearchTerm(String searchterm) {
		String[] word = searchterm.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");

		StringBuffer concatWord = null;
		for (String w : word) {
			if (null != concatWord) {
				concatWord.append("+");
				concatWord.append(w);
			} else
				concatWord = new StringBuffer(w);
		}

		return concatWord;

	}
}
