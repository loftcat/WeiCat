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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.loftcat.app.AppContext;
import com.loftcat.weibo.vo.AccountVo;
import com.loftcat.weibo.vo.AccountVoDao;
import com.loftcat.weibo.vo.BackgroundVoDao;
import com.loftcat.weibo.vo.DaoSession;
import com.loftcat.weibo.vo.UserVODao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * @author verysunny
 *
 */
public class DBManager{

	 	private static final String TAG = DBManager.class.getSimpleName();  
	    private static DBManager instance;  
	    private static Context appContext;  
	    private DaoSession mDaoSession;
	    private AccountVoDao accountVoDao;
	    private UserVODao userVODao;
	    private BackgroundVoDao backgroundVoDao;
	    
	    
	    public static DBManager getInstance(Context context) {  
	        if (instance == null) {  
	            instance = new DBManager();  
	            if (appContext == null){  
	                appContext = context.getApplicationContext();  
	            }  
	            instance.mDaoSession = AppContext.getDaoSession(context);  
	            instance.accountVoDao =  instance.mDaoSession.getAccountVoDao();
	            instance.userVODao =  instance.mDaoSession.getUserVODao();
	            instance.backgroundVoDao =  instance.mDaoSession.getBackgroundVoDao();	 
	            
	        }  
	        return instance;  
	    }  
	    /**
		 * add accounts
		 * 
		 * @param accounts
		 */
		public void addAccount(AccountVo account) {
		accountVoDao.insertOrReplace(account);
		}

		/**
		 * close database
		 */
		public void closeDB() {
		

		}

		/**
		 * delete one account
		 * 
		 * @param account
		 */
		public void deleteAccount(AccountVo account) {

			accountVoDao.delete(account);

		}

		/**
		 * delete accounts
		 * 
		 * @param accounts
		 */
		public void deleteAccounts(ArrayList<AccountVo> accounts) {
			
			for (AccountVo accountVo : accounts) {
				accountVoDao.delete(accountVo);
			}
			
			
		}

		/**
		 * query all accounts, return list
		 * 
		 * @return List<Account>
		 */
		public List<AccountVo> getAccounts() {
		
			return accountVoDao.loadAll();
		}

	

		/**
		 * update account's name
		 * 
		 * @param account
		 */
		public void updateName(AccountVo account) {
			
			
//			ContentValues cv = new ContentValues();
//			cv.put("name", account.getName());
//			mSQLiteDatabase.update("account", cv, "id = ?",
//					new String[] { account.getUid() });// ���ID���������ǳ�
//			22
			
			accountVoDao.update(account);
		}

		public void updateExpires_in(AccountVo account) {
			accountVoDao.update(account);
		}

	

		public List<String> getUids() {
			
			return uids;
			
		}

		private List<String> uids = new ArrayList<String>();
	
	
}
