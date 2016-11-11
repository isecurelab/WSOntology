package com.bl.facebook;

public class FBPageData {
	
	private long pageId;
	private String pageName;
	
	public FBPageData(long pageId, String pageName)
	{
		this.pageId = pageId;
		this.pageName = pageName;
	}
	
	public long getPageId() {
		return pageId;
	}
	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
