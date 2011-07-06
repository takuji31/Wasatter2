package jp.wasatter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import jp.wasatter.R;
import jp.wasatter.Wasatter;
import jp.wasatter.WasatterFragment;
import jp.wasatter.item.TimelineItem;
import jp.wasatter.item.WassrItem;
import jp.wasatter.util.ImageDownloadHelper;
import jp.wasatter.util.Setting;

public class DetailFragment extends WasatterFragment {

	protected TimelineItem item;
	public static String ADD_WASSR = "イイネ！する";
	public static String DEL_WASSR = "イイネ！を消す";
	public static String ADD_TWITTER = "お気に入りに追加する";
	public static String DEL_TWITTER = "お気に入りから削除する";
	public Button favoriteButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.detail, container, false);

		favoriteButton = (Button) v.findViewById(R.id.button_favorite);
		TimelineItem item = app().selectedItem;
		this.item = item;
		if (item != null) {
			// サービス名/チャンネル名をセット
			TextView service_name = (TextView) v
					.findViewById(R.id.service_name);
			service_name.setText(item.serviceName());
			// ニックネームをセット
			TextView screen_name = (TextView) v
					.findViewById(R.id.screen_name);
			screen_name.setText(item.screenName());
			// 本文をセット
			TextView status = (TextView) v.findViewById(R.id.status);
			status.setText(item.text());
			// 画像をセット
			ImageView icon = (ImageView) v.findViewById(R.id.icon);
			Bitmap bmp = ImageDownloadHelper.getWithCache(item.profileImageUrl());
			if (bmp != null && app().getPref(Setting.LOAD_IMAGE, false)) {
				icon.setImageBitmap(bmp);
				icon.setVisibility(View.VISIBLE);
			} else {
				icon.setImageResource(R.drawable.tlicon_default);
				//icon.setVisibility(View.GONE);
			}
			// 返信であるかどうか判定
			if (item.replyUserNick() != null && !item.replyUserNick().equals("null")) {
				TextView reply_message = (TextView) v
						.findViewById(R.id.reply_text);
				SpannableStringBuilder sb = new SpannableStringBuilder("by ");
				sb.append(item.replyUserNick());
				TextView reply_user_name = (TextView) v
						.findViewById(R.id.reply_user_name);
				reply_user_name.setText(sb.toString());
				if (item.replyUserMessage() != null) {
					SpannableStringBuilder sb2 = new SpannableStringBuilder(
							"> ");
					sb2.append(item.replyUserMessage());
					reply_message.setText(sb2.toString());
				}

			} else {
				LinearLayout layout_reply = (LinearLayout) v
						.findViewById(R.id.layout_reply);
				layout_reply.setVisibility(View.GONE);
			}
			// OpenPermalinkボタンにイベント割り当て
			Button button_open_link = (Button) v
					.findViewById(R.id.button_open_link);
			button_open_link.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自動生成されたメソッド・スタブ
					String permalink = DetailFragment.this.item.permaLink();
					Intent intent_parmalink = new Intent(Intent.ACTION_VIEW,
							Uri.parse(permalink));
					startActivity(intent_parmalink);
				}
			});
			// Open URLボタンにイベント割り当て
			Button button_open_url = (Button) v
					.findViewById(R.id.button_open_url);
			button_open_url.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自動生成されたメソッド・スタブ
					String text = DetailFragment.this.item.text();
					/*String url = Wasatter.getUrl(text);
					if (!"".equals(url)) {
						Intent intent_url = new Intent(Intent.ACTION_VIEW, Uri
								.parse(url));
						startActivity(intent_url);
					} else { */
						AlertDialog.Builder ad = new AlertDialog.Builder(
								DetailFragment.this.getActivity());
						// ad.setTitle(R.string.notice_title_no_url);
						ad.setMessage(R.string.dialog_url_not_found);
						ad.setPositiveButton("OK", null);
						ad.show();
					//}
				}
			});
			/*
			// Favoriteボタンにイベント割り当て
			Button button_favorite = (Button) v
					.findViewById(R.id.button_favorite);
			if (Wasatter.SERVICE_TWITTER.equals(item.service)) {
				button_favorite.setText(ADD_TWITTER);
			} else if (item.favorite != null
					&& item.favorite.indexOf(Setting.getWassrId()) != -1) {
				button_favorite.setText(DEL_WASSR);
			} else {
				button_favorite.setText(ADD_WASSR);
			}
			button_favorite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new TaskToggleFavorite(Detail.this)
							.execute(Detail.this.item);
				}
			});
			// Replyボタンにイベント割り当て
			Button button_reply = (Button) v.findViewById(R.id.reply_button);
			button_reply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自動生成されたメソッド・スタブ
					Intent intent_reply = new Intent(Detail.this,
							Update.class);
					intent_reply.putExtra(Wasatter.REPLY,
							Detail.this.item);
					startActivity(intent_reply);
				}
			});
			*/
		}
		return v;
	}
}
