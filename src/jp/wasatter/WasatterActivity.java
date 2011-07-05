/**
 * 
 */
package jp.wasatter;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * @author takuji
 *
 */
public class WasatterActivity extends FragmentActivity {

	public Wasatter app() {
		return (Wasatter) getApplication();
	}
	
	public Toast toast(int text_id) {
		return app().toast(text_id);
	}
	
	public Toast toast(String text) {
		return app().toast(text);
	}
	
	public Toast longToast(int text_id) {
		return app().longToast(text_id);
	}
	
	public Toast longToast(String text) {
		return app().longToast(text);
	}
}
