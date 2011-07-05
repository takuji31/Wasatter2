package jp.wasatter.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WasatterDBOpenHelper extends SQLiteOpenHelper {

	WasatterDBOpenHelper(Context context) {
        super(context, "wasatter.db", null, 1);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Open schema sql file and get DDL string
	 * @return DDL string
	 */
	private String getDDL() {
		
		return null;
	}

}
