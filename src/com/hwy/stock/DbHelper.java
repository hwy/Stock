package com.hwy.stock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE Stock (stock_id INTEGER PRIMARY KEY AUTOINCREMENT,stock_no TEXT, qty INTEGER DEFAULT 0, price REAL DEFAULT 0, date TEXT)";

		String sq2 = "CREATE TABLE Record (record_id INTEGER PRIMARY KEY AUTOINCREMENT,stock_no TEXT, bstype INTEGER DEFAULT 0, bsqty INTEGER DEFAULT 0, bprice REAL DEFAULT 0, sprice REAL DEFAULT 0, bsdate TEXT)";
	
		db.execSQL(sql);
		db.execSQL(sq2);

		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}