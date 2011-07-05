/**
 *
 */
package jp.wasatter.ui;

import twitter4j.Twitter;
import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.WasatterActivity;
import jp.wasatter.task.GetTwitterOAuthRequestUrl;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * @author takuji
 *
 */
public class SetupWizardActivity extends WasatterActivity {

	public CheckBox wassrSet;
	public CheckBox twitterSet;
	public Button buttonComplete;
	public Button wassr;
	public Button twitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_wizard);
		final Wasatter app = app();

		wassrSet = (CheckBox) findViewById(R.id.wassr_set);
		twitterSet = (CheckBox) findViewById(R.id.twitter_set);
		buttonComplete = (Button) findViewById(R.id.button_complete);
		wassr = (Button) findViewById(R.id.wassr);
		twitter = (Button) findViewById(R.id.twitter);

		if(app.getPref("twitter_access_token",null) != null) {
			twitterSet.setChecked(true);
		}

		wassr.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_DEFAULT);
		        intent.setData(Uri.parse("wasatter://account/wassr?from_wizard=true"));
				startActivity(intent);
			}
		});
		twitter.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				GetTwitterOAuthRequestUrl task = new GetTwitterOAuthRequestUrl(SetupWizardActivity.this);
				Twitter auth = app.twitter;
				auth.setOAuthAccessToken(null);
				task.execute();
			}
		});

		buttonComplete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if ( !wassrSet.isChecked() && !twitterSet.isChecked() ) {
					//何も設定されてなかったらエラー表示
					app.toast(app.getText(R.string.no_service_added).toString()).show();
				}else{
					//TODO メイン画面へ遷移
					app.setPref("setup_completed_ver_1",true);
					SetupWizardActivity.this.finish();
				}
			}
		});

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Bundle bundle = intent.getExtras();
		boolean set_twitter = false;
		boolean set_wassr = false;
		boolean clear_wassr = false;
		if(bundle != null) {
			set_twitter = bundle.getBoolean("set_twitter");
			set_wassr = bundle.getBoolean("set_wassr");
			clear_wassr = bundle.getBoolean("clear_wassr");
		}
		if(set_twitter) {
			twitterSet.setChecked(true);
		}
		if(set_wassr) {
			wassrSet.setChecked(true);
		}
		if(clear_wassr) {
			wassrSet.setChecked(false);
		}
	}
}
