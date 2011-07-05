package jp.wasatter.ui;

import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.WasatterActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class TwitterAuthCallbackActivity extends WasatterActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Uri uri = intent.getData();
		Intent goBack = new Intent(getBaseContext(),SetupWizardActivity.class);
		try {
			String token = uri.getQueryParameter("oauth_token");
			String verifier = uri.getQueryParameter("oauth_verifier");
			//XXX null判定のために呼ぶ
			token.length();
			Wasatter app = app();
			RequestToken requestToken = app.request_token;
			AccessToken accessToken = app.twitter.getOAuthAccessToken(requestToken, verifier);
			app.setPref("twitter_access_token", accessToken.getToken());
			app.setPref("twitter_access_token_secret", accessToken.getTokenSecret());
			app.setTwitterToken(accessToken.getToken(), accessToken.getTokenSecret());
			goBack.putExtra("set_twitter", true);
			toast(R.string.setting_completed).show();
		}catch (TwitterException e) {
			//なんかおかしかった時
			e.printStackTrace();
			if ( "503".equals(e.getExceptionCode()) ) {
				toast(R.string.setting_failure_cause_twitter).show();
			} else {
				toast(R.string.setting_failure).show();
			}
			goBack.putExtra("set_twitter", false);
		}catch (Exception e) {
			//なんかおかしかった時
			e.printStackTrace();
			toast(R.string.setting_failure).show();
			goBack.putExtra("set_twitter", false);
		}
		startActivity(goBack);
		this.finish();
	}
}
