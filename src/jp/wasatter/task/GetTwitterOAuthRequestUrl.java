package jp.wasatter.task;

import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.WasatterActivity;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;

public class GetTwitterOAuthRequestUrl extends AsyncTask<Void, TwitterException, RequestToken>{

	private WasatterActivity currentActivity;
	private ProgressDialog dialog;

	public GetTwitterOAuthRequestUrl(WasatterActivity currentActvity) {
		this.currentActivity = currentActvity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		//プログレスダイアログを表示する
		dialog = new ProgressDialog(currentActivity);
		dialog.setMessage(currentActivity.getText(R.string.please_wait));
		dialog.setIndeterminate(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				//ダイアログを閉じたらこのタスクをキャンセル
				GetTwitterOAuthRequestUrl.this.cancel(true);
			}
		});
		dialog.show();
	}

	@Override
	protected RequestToken doInBackground(Void... params) {
		Wasatter app = currentActivity.app();
		Twitter twitter = app.twitter;
		try {
			//認証URL取得
			return twitter.getOAuthRequestToken(Wasatter.OAUTH_CALLBACK_URL);
		} catch (TwitterException e) {
			e.printStackTrace();
			publishProgress(e);
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(TwitterException... e) {
		super.onProgressUpdate(e);
		TwitterException e1 = e[0];
		if(e1.getStatusCode() == 503) {
			currentActivity.toast(R.string.communication_failure_cause_twitter).show();
		} else {
			currentActivity.toast(R.string.communication_failure).show();
		}
	}

	@Override
	protected void onPostExecute(RequestToken result) {
		super.onPostExecute(result);
		//ダイアログを閉じる
		dialog.dismiss();
		//取得に成功した場合は認証用URLを開く
		if(result != null) {
			Uri twitterAuthUri = Uri.parse(result.getAuthorizationURL());
			Intent gotoTwitterAuth = new Intent(Intent.ACTION_VIEW,twitterAuthUri);
			currentActivity.app().request_token = result;
			currentActivity.startActivity(gotoTwitterAuth);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		currentActivity.app().toast(R.string.cancelled).show();
	}

}
