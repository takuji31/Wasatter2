package jp.wasatter.ui;

import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.WasatterActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingWassrAccountActivity extends WasatterActivity {
	public static final String ID = "wassr_id";
	public static final String PASS = "wassr_pass";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_wassr_account);
		// IDとパスワードを入力
		final EditText id = (EditText) findViewById(R.id.id);
		final EditText password = (EditText) findViewById(R.id.password);
		final Wasatter app = app();
		id.setText(app.getPref(ID, ""));
		password.setText(app.getPref(PASS, ""));
		Intent intent = getIntent();
		Uri uri = intent.getData();
		final String  from = uri != null ? uri.getQueryParameter("from") : "";

		// パスワードを表示のチェックボックスにイベントを割り当てる
		CheckBox showPassword = (CheckBox) findViewById(R.id.showPassword);
		showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {


			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// チェックされていればパスワードを表示する。
				password.setInputType(isChecked ? InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
						: InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);

			}
		});

		// 保存とキャンセルボタンにイベントを割り当てる
		Button saveButton = (Button) findViewById(R.id.saveButton);
		Button cancelButton = (Button) findViewById(R.id.cancelButton);
		saveButton.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				String strId = id.getText().toString().trim();
				String strPass = password.getText().toString().trim();
				boolean valid = !("".equals(strId) ^ "".equals(strPass));
				boolean input = !"".equals(strId) && !"".equals(strPass);
				if (!valid) {
					app.toast(R.string.please_input_id_and_pass_or_cancel).show();
				}else{
					app.setPref(ID, strId);
					app.setPref(PASS, strPass);
					app.toast(R.string.setting_saved).show();
					if("wizard".equals(from)) {
						Intent intent = new Intent();
						//ID PASS共に設定されてたら設定完了
						intent.putExtra("set_wassr", input);
						intent.putExtra("clear_wassr", !input);
						//戻ります
						startActivity(intent);
					}
					finish();
				}
			}
		});
		cancelButton.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				// キャンセルボタンが押されたら前の画面に戻る
				finish();
			}
		});
	}
}
