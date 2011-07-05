/**
 * 
 */
package jp.wasatter.ui;

import jp.wasatter.R;
import jp.wasatter.task.GetTimeLine;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author takuji
 *
 */
public class HomeTimelineFragment extends TimelineFragment {

	public ListView timeline;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.timeline_main, container);
		timeline = (ListView) v.findViewById(R.id.timeline);
		GetTimeLine task = new GetTimeLine(app(), timeline);
		task.execute();
		
		return v;
	}
}
