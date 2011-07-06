package jp.wasatter.item;

public interface TimelineItem {

	public String  serviceName();
	public String  screenName();
	public String  status_id();
	public String  text();
	public String  html();
	public String  profileImageUrl();
	public String  permaLink();
	public String  replyUserNick();
	public String  replyUserMessage();
	public long    epoch();
	public boolean favorited();
}
