/**
 * 
 */
package jp.wasatter.ui;

import jp.wasatter.R;
import jp.wasatter.item.TimelineItem;
import jp.wasatter.task.GetTimeLine;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
		View v = inflater.inflate(R.layout.timeline_main, container, false);
		timeline = (ListView) v.findViewById(R.id.timeline);
		GetTimeLine task = new GetTimeLine(app(), timeline);
		task.execute();
		timeline.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ListView list = (ListView) parent;
				app().selectedItem = (TimelineItem) list.getAdapter().getItem(position);
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				DetailFragment  f = (DetailFragment) fm.findFragmentByTag(TAG_RIGHT_COLUMN);
				if (f == null) {
					ft.add(R.id.rightColumn, new DetailFragment(),TAG_RIGHT_COLUMN);
				} else {
					ft.replace(R.id.rightColumn, new DetailFragment());
				}
				ft.addToBackStack(null);
				ft.commit();
			}
		});
		
		return v;
	}
}
