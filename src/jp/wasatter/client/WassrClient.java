package jp.wasatter.client;

import jp.wasatter.Wasatter;
import jp.wasatter.ui.SettingWassrAccountActivity;
import twitter4j.auth.Authorization;
import twitter4j.conf.ConfigurationContext;
import twitter4j.internal.http.HttpClient;
import twitter4j.internal.http.HttpClientConfiguration;
import twitter4j.internal.http.HttpClientFactory;
import twitter4j.internal.http.HttpRequest;

/**
 * Wassrクラス<br>
 * Wassrとの通信及び、データの入出力を行う。
 *
 * @author Senka/Takuji
 *
 */
public class WassrClient {
	private static final String FRIEND_TIMELINE_URL = "http://api.wassr.jp/statuses/friends_timeline.json";
	private static final String CHANNEL_TIMELINE_URL = "http://api.wassr.jp/channel_message/list.json?name_en=[name]";
	private static final String CHANNEL_PERMA_LINK = "http://wassr.jp/channel/[name]/messages/[rid]";
	private static final String REPLY_URL = "http://api.wassr.jp/statuses/replies.json";
	private static final String MYPOST_URL = "http://api.wassr.jp/statuses/user_timeline.json";
	private static final String ODAI_URL = "http://api.wassr.jp/statuses/user_timeline.json?id=odai";
	private static final String TODO_URL = "http://api.wassr.jp/todo/list.json";
	private static final String CHANNEL_LIST_URL = "http://api.wassr.jp/channel_user/user_list.json";
	private static final String TODO_STATUS_URL = "http://api.wassr.jp/todo/";
	private static final String UPDATE_TIMELINE_URL = "http://api.wassr.jp/statuses/update.json";
	private static final String UPDATE_CHANNEL_URL = "http://api.wassr.jp/channel_message/update.json?name_en=[channel]";
	private static final String FAVORITE_URL = "http://api.wassr.jp/favorites/create/[rid].json";
	private static final String FAVORITE_DEL_URL = "http://api.wassr.jp/favorites/destroy/[rid].json";
	private static final String FAVORITE_CHANNEL_URL = "http://api.wassr.jp/channel_favorite/toggle.json?channel_message_rid=[rid]";
	public static final String FAVORITE_ICON_URL = "http://wassr.jp/user/[user]/profile_img.png.16";
	private static final String TODO_START = "start";
	private static final String TODO_STOP = "stop";
	private static final String TODO_COMP = "done";
	private static final String TODO_DEL = "delete";
	
	private Wasatter app;
	
	public WassrClient(Wasatter app) {
		this.app = app;
	}
	
	public String getId(){
		return app.getPref(SettingWassrAccountActivity.ID, null);
	}
	
	public String getPass(){
		return app.getPref(SettingWassrAccountActivity.PASS, null);
	}
	
	public boolean isEnabled() {
		return getId() != null && getPass() != null;
	}
	
	private HttpClient getHttpClient() {
		HttpClientConfiguration conf = ConfigurationContext.getInstance();
		return HttpClientFactory.getInstance(conf);
	}
	
	public Authorization getAuthorization() {
		return new Authorization() {
			private static final long serialVersionUID = 1L;
			public boolean isEnabled() {
				return WassrClient.this.isEnabled();
			}
			
			public String getAuthorizationHeader(HttpRequest arg0) {
				String id = getId();
				String pass = getPass();
				return null;
			}
		};

	}
	
/*
	public static ArrayList<TimelineItem> getTimeLine() throws TwitterException {
		return WassrClient.getItems(WassrClient.FRIEND_TIMELINE_URL, false);
	}

	public static ArrayList<TimelineItem> getReply() throws TwitterException {
		return WassrClient.getItems(WassrClient.REPLY_URL, false);
	}

	public static ArrayList<TimelineItem> getMyPost() throws TwitterException {
		return WassrClient.getItems(WassrClient.MYPOST_URL, false);
	}

	public static ArrayList<TimelineItem> getOdai() throws TwitterException {
		return WassrClient.getItems(WassrClient.ODAI_URL, false);
	}

	public static ArrayList<TimelineItem> getChannel(String name)
			throws TwitterException {
		return WassrClient.getItems(CHANNEL_TIMELINE_URL
				.replace("[name]", name), true);
	}

	public static ArrayList<TimelineItem> getItems(String url, boolean channel)
			throws TwitterException {
		ArrayList<TimelineItem> ret = new ArrayList<TimelineItem>();
		JSONArray result;
		try {
			HttpResponse res = http.get(url, getAuthorization());
			result = res.asJSONArray();
			int j = result.length();
			SimpleDateFormat sdf = new SimpleDateFormat(
					"EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
			for (int i = 0; i < j; i++) {
				JSONObject obj = result.getJSONObject(i);
				TimelineItem ws = new WassrItem(obj);
				if (channel) {
					JSONObject ch = obj.getJSONObject("channel");
					SpannableStringBuilder sb = new SpannableStringBuilder(ch
							.getString("title"));
					sb.append(" (");
					sb.append(ch.getString("name_en"));
					ws.service = sb.append(")").toString();
					ws.id = obj.getJSONObject("user").getString("login_id");
					ws.name = obj.getJSONObject("user").getString("nick");
					ws.link = CHANNEL_PERMA_LINK.replace("[name]",
							ch.getString("name_en")).replace("[rid]", ws.rid);
					try {
						JSONObject reply = obj.getJSONObject("reply");
						ws.replyUserNick = reply.getJSONObject("user")
								.getString("nick");
						ws.replyMessage = HTMLEntity.unescape(reply
								.getString("body"));
					} catch (JSONException e) {
						// 返信なかったらスルー
					}
					try {
						ws.epoch = sdf.parse(obj.getString("created_on"))
								.getTime() / 1000;
					} catch (ParseException e) {
						ws.epoch = 0;
					}
					ws.text = HTMLEntity.unescape(obj.getString("body"));
				} else {
					ws.id = obj.getString("user_login_id");
					ws.name = obj.getJSONObject("user")
							.getString("screen_name");
					ws.link = obj.getString("link");
					ws.replyUserNick = obj.getString("reply_user_nick");
					ws.replyMessage = HTMLEntity.unescape(obj
							.getString("reply_message"));
					ws.epoch = Long.parseLong(obj.getString("epoch"));
					ws.text = HTMLEntity.unescape(obj.getString("text"));
				}
				String profile = obj.getJSONObject("user").getString(
						"profile_image_url");
				if (Wasatter.downloadWaitUrls.indexOf(profile) == -1
						&& Wasatter.images.get(profile) == null) {
					Wasatter.downloadWaitUrls.add(profile);
				}
				ws.profileImageUrl = profile;
				if ("null".equalsIgnoreCase(ws.replyMessage)) {
					ws.replyMessage = Wasatter.CONTEXT
							.getString(R.string.message_private_message);
				}
				JSONArray favorites = obj.getJSONArray("favorites");
				// お題のイイネは取得しない。
				int fav_count = favorites.length();
				for (int k = 0; k < fav_count; k++) {
					String icon_url = WassrClient.FAVORITE_ICON_URL.replace(
							"[user]", favorites.getString(k));
					ws.favorite.add(favorites.getString(k));
					if (!WassrClient.ODAI_URL.equals(url)
							&& Wasatter.downloadWaitUrls.indexOf(icon_url) == -1
							&& Wasatter.images.get(icon_url) == null) {
						Wasatter.downloadWaitUrls.add(icon_url);
					}
				}
				ws.favorited = ws.favorite.indexOf(Setting.getWassrId()) != -1;
				ret.add(ws);
			}
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
		}
		return ret;
	}

	public static ArrayList<WassrTodo> getTodo() {
		ArrayList<WassrTodo> ret = new ArrayList<WassrTodo>();
		if (!Setting.isWassrEnabled()) {
			return ret;
		}
		JSONArray result;
		try {
			result = http.post(WassrClient.TODO_URL, getAuthorization())
					.asJSONArray();
			int j = result.length();
			for (int i = 0; i < j; i++) {
				try {
					JSONObject obj = result.getJSONObject(i);
					WassrTodo ws = new WassrTodo();
					ws.rid = obj.getString("todo_rid");
					ws.body = obj.getString("body");
					ret.add(ws);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (TwitterException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		return ret;
	}

	public static ArrayList<TimelineItem> getChannelList()
			throws TwitterException {
		ArrayList<TimelineItem> ret = new ArrayList<TimelineItem>();
		if (!Setting.isWassrEnabled()) {
			return ret;
		}
		JSONArray result;
		try {
			result = http.get(WassrClient.CHANNEL_LIST_URL, getAuthorization())
					.asJSONObject().getJSONArray("channels");
			int j = result.length();
			for (int i = 0; i < j; i++) {
				try {
					JSONObject obj = result.getJSONObject(i);
					TimelineItem item = new TimelineItem();
					item.id = obj.getString("name_en");
					item.name = obj.getString("title");
					ret.add(item);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ret;
	}

	public static boolean updateTimeLine(String status) throws TwitterException {
		return WassrClient.updateTimeLine(status, null);
	}

	public static boolean updateTimeLine(String status, String rid)
			throws TwitterException {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append(UPDATE_TIMELINE_URL);
		sb.append("?source=");
		sb.append(URLEncoder.encode(Wasatter.VIA));
		sb.append("&status=");
		sb.append(URLEncoder.encode(status));
		if (rid != null) {
			sb.append("&reply_status_rid=");
			sb.append(rid);
		}
		JSONObject res;
		res = http.post(sb.toString(), getAuthorization()).asJSONObject();
		try {
			return res.getString("text") != null;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean updateChannel(String channelId, String status,
			String rid) throws TwitterException {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append(UPDATE_CHANNEL_URL.replace("[channel]", channelId));
		sb.append("&body=");
		sb.append(URLEncoder.encode(status));
		if (rid != null) {
			sb.append("&reply_channel_message_rid=");
			sb.append(rid);
		}
		JSONObject res;
		res = http.post(sb.toString(), getAuthorization()).asJSONObject();
		try {
			return res.getString("text") != null
					&& "null".equals(res.getString("error"));
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return false;
	}

	public static boolean favorite(TimelineItem item) {
		JSONObject json;
		try {
			if (!item.favorited) {
				json = http.post(FAVORITE_URL.replace("[rid]", item.rid),
						getAuthorization()).asJSONObject();

			} else {
				json = http.post(FAVORITE_DEL_URL.replace("[rid]", item.rid),
						getAuthorization()).asJSONObject();
			}
			boolean result = json.getString("status").equalsIgnoreCase("ok");
			if(result){
				if(item.favorited){
					item.favorite.remove(Setting.getWassrId());
				}else{
					item.favorite.add(Setting.getWassrId());
				}
				item.favorited = !item.favorited;
			}
			return result;
		} catch (TwitterException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String channelFavorite(TimelineItem item) {
		JSONObject json;
		try {
			json = http.post(FAVORITE_CHANNEL_URL.replace("[rid]", item.rid),
					getAuthorization()).asJSONObject();
			return json.getString("message");
		} catch (TwitterException e1) {
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "NG";
	}

	public static boolean startTodo(String rid) {
		return WassrClient.todo(rid, WassrClient.TODO_START);
	}

	public static boolean stopTodo(String rid) {
		return WassrClient.todo(rid, WassrClient.TODO_STOP);
	}

	public static boolean completeTodo(String rid) {
		return WassrClient.todo(rid, WassrClient.TODO_COMP);
	}

	public static boolean deleteTodo(String rid) {
		return WassrClient.todo(rid, WassrClient.TODO_DEL);
	}

	public static boolean todo(String rid, String mode) {
		SpannableStringBuilder sb = new SpannableStringBuilder(
				WassrClient.TODO_STATUS_URL);
		sb.append(mode);
		sb.append(".json");
		sb.append("?todo_rid=");
		sb.append(rid);
		try {
			JSONObject json = http.post(sb.toString(), getAuthorization())
					.asJSONObject();
			;
			if (WassrClient.TODO_DEL.equals(mode)) {
				return "ok.removed."
						.equalsIgnoreCase(json.getString("message"));
			}
			return "ok".equalsIgnoreCase(json.getString("message"));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return false;

	}

	public static Authorization getAuthorization() {

		return new BasicAuthorization(Setting.getWassrId(), Setting
				.getWassrPass());

	}
	*/
}
