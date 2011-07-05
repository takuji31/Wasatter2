package jp.wasatter.task;

import java.io.IOException;
import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.adapter.TimelineAdapter;
import jp.wasatter.client.TwitterClient;
import jp.wasatter.item.TimelineItem;
import android.os.AsyncTask;
import android.widget.ListView;

public class GetTimeLine extends
		AsyncTask<Void, String, ArrayList<TimelineItem>> {
	protected ListView listview;
	protected Wasatter  app;

	// コンストラクタ
	public GetTimeLine(Wasatter app,ListView list) {
		listview = list;
		this.app = app;
	}

	// バックグラウンドで実行する処理
	protected ArrayList<TimelineItem> doInBackground(Void... param) {
		try {
			return TwitterClient.getUserTimeLine(app.twitter);
		} catch (TwitterException e) {
			// TODO: handle exception
			return new ArrayList<TimelineItem>();
		}
	}

	protected void onProgressUpdate(String... values) {
	}

	// 進行中に出す処理
	protected void onPreExecute() {
	}

	// メインスレッドで実行する処理
	@Override
	protected void onPostExecute(ArrayList<TimelineItem> result) {
		// TODO 自動生成されたメソッド・スタブ
		super.onPostExecute(result);
		TimelineAdapter adapter = new TimelineAdapter(app, R.layout.timeline_row, result);
		listview.setAdapter(adapter);
	}
}
