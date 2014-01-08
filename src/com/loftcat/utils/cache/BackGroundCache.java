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
package com.loftcat.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.weibo.vo.BackgroundVo;

public class BackGroundCache {
	private static BackGroundCache backGroundCache;
	
	public static BackGroundCache getInstance(){
		if (backGroundCache==null) {
			backGroundCache = new BackGroundCache();
		}
		return backGroundCache;
	}
	
	
	public  void saveBackgroundId(int id, Context _context) {
		SharedPreferences sp = (_context).getSharedPreferences(
				AppConfig.PREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("Backgroundid", id);
		editor.commit();
	}

	public  int getBackgroundId(Context _context) {
		SharedPreferences sp = _context.getSharedPreferences(
				AppConfig.PREFERENCES_NAME, 0);
		return sp.getInt("Backgroundid", 0);
	}
	

	
}
