package jp.wasatter;

import jp.wasatter.ui.HomeActivity;
import jp.wasatter.ui.SetupWizardActivity;
import android.content.Intent;
import android.os.Bundle;

public class ActivityMain extends WasatterActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( !app().getPref("setup_completed_ver_1", false) ) {
        	//初期セットアップウィザードの起動
	        Intent intent = new Intent(this,SetupWizardActivity.class);
	        startActivity(intent);
	        finish();
        } else {
        	//メイン画面の起動
	        Intent intent = new Intent(this,HomeActivity.class);
	        startActivity(intent);
	        finish();
        }
    }
}