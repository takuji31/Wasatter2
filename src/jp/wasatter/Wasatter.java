/**
 *
 */
package jp.wasatter;

import java.io.File;

import jp.wasatter.client.WassrClient;
import jp.wasatter.item.TimelineItem;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.widget.Toast;

/**
 * @author takuji
 *
 */
public class Wasatter extends Application {

	public static final String CONSUMER_KEY = "qXHqlKcN3GH07WY77yftyA";
	public static final String CONSUMER_SECRET = "6NPOnrPaUOBd0SuOyZO7fjvKim2JURD75CHVbP6OfM";
	public static final String OAUTH_CALLBACK_URL = "wasatter://callback.twitter";
	public static final int PREF_VERSION = 1;
	public static final String VIA = "Wasatter";
	
	/**
	 * Timeline type
	 */
	public static final int TIMELINE_FRIEND  = 1;
	public static final int TIMELINE_REPLY   = 2;
	public static final int TIMELINE_MYPOST  = 3;
	public static final int TIMELINE_ODAI    = 4;
	public static final int TIMELINE_MESSAGE = 5;

	public RequestToken request_token;
	public Twitter twitter;
	public SharedPreferences pref;
	public WassrClient wassr;
	
	public TimelineItem selectedItem;

	@Override
	public void onCreate() {
		super.onCreate();
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (getPref("pref_version", 0) == 0) {
			// Ver1からの設定データーは全部消す
			clearPref();
			// キャッシュとかDBも消すよ
			deleteDatabase("imagestore.db");
			SpannableStringBuilder sb = new SpannableStringBuilder();
			sb.append(getFilesDir().getParentFile().getAbsolutePath());
			sb.append("/imagecache/");
			deleteFileRecursive(new File(sb.toString()));
			setPref("pref_version", PREF_VERSION);
		}
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Wasatter.CONSUMER_KEY,
				Wasatter.CONSUMER_SECRET);
		String token = getPref("twitter_access_token", null);
		String tokenSecret = getPref("twitter_access_token_secret", null);
		if (token != null && tokenSecret != null) {
			setTwitterToken(token, tokenSecret);
		}
		wassr = new WassrClient(this);
	}

	public void setTwitterToken(String token, String tokenSecret) {
		twitter.setOAuthAccessToken(new AccessToken(token, tokenSecret));
	}

	/**
	 * Show short toast
	 *
	 * @param id
	 *            text_id
	 * @return
	 */
	public Toast toast(int id) {
		return Toast.makeText(this, getText(id).toString(), Toast.LENGTH_SHORT);
	}

	/**
	 * Show short toast
	 *
	 * @param text
	 */
	public Toast toast(String text) {
		return Toast.makeText(this, text, Toast.LENGTH_SHORT);
	}

	/**
	 * Show long toast
	 *
	 * @param text
	 */
	public Toast longToast(String text) {
		return Toast.makeText(this, text, Toast.LENGTH_LONG);
	}

	/**
	 * Show long toast
	 *
	 * @param id
	 *            text_id
	 * @return Toast
	 */
	public Toast longToast(int id) {
		return Toast.makeText(this, getText(id).toString(), Toast.LENGTH_LONG);
	}

	/**
	 * 設定を取得するメソッド
	 *
	 * @param key
	 *            キー
	 * @param def
	 *            デフォルト値
	 * @return 設定値
	 */
	public String getPref(String key, String def) {
		return pref.getString(key, def);
	}

	public int getPref(String key, int def) {
		return pref.getInt(key, def);
	}

	public boolean getPref(String key, boolean def) {
		return pref.getBoolean(key, def);
	}

	public long getPref(String key, long def) {
		return pref.getLong(key, def);
	}

	/**
	 * 設定を代入するメソッド
	 *
	 * @param key
	 *            キー
	 * @param value
	 *            値
	 */
	public void setPref(String key, String value) {
		pref.edit().putString(key, value).commit();
	}

	public void setPref(String key, int value) {
		pref.edit().putInt(key, value).commit();
	}

	public void setPref(String key, boolean value) {
		pref.edit().putBoolean(key, value).commit();
	}

	public void setPref(String key, long value) {
		pref.edit().putLong(key, value).commit();
	}

	/**
	 * 設定を全部クリアするメソッド ※デバッグ時と初期セットアップ時以外は使用すべきでない。
	 */
	public void clearPref() {
		pref.edit().clear().commit();
	}
	
	public static boolean deleteFileRecursive(File dirOrFile) {
		if (dirOrFile.isDirectory()) {//ディレクトリの場合
			String[] children = dirOrFile.list();//ディレクトリにあるすべてのファイルを処理する
			for (int i=0; i<children.length; i++) {
				boolean success = deleteFileRecursive(new File(dirOrFile, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dirOrFile.delete();
	}
}
