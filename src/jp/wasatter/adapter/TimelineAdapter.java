package jp.wasatter.adapter;

import java.util.ArrayList;

import twitter4j.Status;
import jp.wasatter.R;
import jp.wasatter.item.TimelineItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimelineAdapter extends ArrayAdapter<TimelineItem>  {

	private ArrayList<TimelineItem> list;
	private LayoutInflater inflater;

	public TimelineAdapter(Context context, int textViewResourceId,
			ArrayList<TimelineItem> list) {
		super(context, textViewResourceId, list);
		this.list = list;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			// 受け取ったビューがnullなら新しくビューを生成
			view = this.inflater.inflate(R.layout.timeline_row, null);
		}
		TimelineItem item = list.get(position);
		TextView statusText = (TextView) view.findViewById(R.id.statusText);
		TextView screenName = (TextView) view.findViewById(R.id.screenName);
		statusText.setText(item.text());
		screenName.setText(item.screenName());
		return view;
	}
}