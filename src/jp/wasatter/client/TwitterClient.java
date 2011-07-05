package jp.wasatter.client;

import java.util.ArrayList;
import java.util.Iterator;
import jp.wasatter.item.TimelineItem;
import jp.wasatter.item.TwitterItem;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterClient {

	public static ArrayList<TimelineItem> getUserTimeLine(Twitter client) throws TwitterException {
		ResponseList<Status> res = client.getUserTimeline();
		Iterator<Status> it = res.iterator();
		ArrayList<TimelineItem> list = new ArrayList<TimelineItem>();
		while(it.hasNext()) {
			Status status = it.next();
			TwitterItem item = new TwitterItem(status);
			list.add(item);
		}
		return list;
	}
}
