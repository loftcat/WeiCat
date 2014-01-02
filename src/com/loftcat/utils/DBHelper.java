/*
 * Copyright (c) 2013 HeBin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loftcat.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库类
 * 
 * @author HeBin
 * 
 * @version 1.0
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String ACCOUNT_DATABASE_NAME = "loftcat_account.db";
	private static final int ACCOUNT_DATABASE_VERSION = 4;

	public DBHelper(Context context) {
		super(context, ACCOUNT_DATABASE_NAME, null, ACCOUNT_DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE IF NOT EXISTS account"
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT," + " id VARCHAR,"
				+ "name VARCHAR," + "userInfo VARCHAR," + "token VARCHAR,"
				+ " expires_in VARCHAR)");

		// db.execSQL("CREATE TABLE " + constraint + "'NOTE' (" + //
		// "'_id' INTEGER PRIMARY KEY ," + // 0: id
		// "'TEXT' TEXT NOT NULL ," + // 1: text
		// "'COMMENT' TEXT," + // 2: comment
		// "'DATE' INTEGER);"); // 3: date
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS account");
		onCreate(db);

	}

}
