package jp.wasatter.item;

import org.json.JSONObject;

import twitter4j.Status;

public class WassrItem implements TimelineItem{
	public Status twitterStatus;
	private static final String PERMA_LINK = "http://twitter.com/[screenName]/status/[id]";
	
	public WassrItem(JSONObject json) {
	}

	public String screenName() {
		return twitterStatus.getUser().getScreenName();
	}

	public String status_id() {
		return String.valueOf(twitterStatus.getId());
	}

	public String text() {
		return twitterStatus.getText();
	}

	public String html() {
		return twitterStatus.getText();
	}

	public String profileImageUrl() {
		return twitterStatus.getUser().getProfileImageURL().toString();
	}

	public String permaLink() {
		// TODO 自動生成されたメソッド・スタブ
		return PERMA_LINK.replace("[screenName]", screenName()).replace("[id]", status_id());
	}

	public String replyUserNick() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public String replyUserMessage() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	public long epoch() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	public boolean favorited() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
