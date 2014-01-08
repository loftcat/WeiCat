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

import com.loftcat.R;
import com.loftcat.weibo.vo.BackgroundVo;

public class CacheManager {
	
	private static CacheManager cacheManager;
	
	public static CacheManager getInstance(){
		if (cacheManager ==null) {
			cacheManager = new CacheManager();
		}
		return cacheManager;
	}
	
	
	public static BackGroundCache getBackGroundCache(){
		
		return BackGroundCache.getInstance();
	}
	
	
	public static BackgroundVo[] backgrounds = {
		new BackgroundVo(R.drawable.background_0small,
				R.drawable.background_0),
		new BackgroundVo(R.drawable.background_1small,
				R.drawable.background_1),
		new BackgroundVo(R.drawable.background_3small,
				R.drawable.background_3),
		new BackgroundVo(R.drawable.background_4small,
				R.drawable.background_4),
		new BackgroundVo(R.drawable.background_5small,
				R.drawable.background_5),
		new BackgroundVo(R.drawable.background_6small,
				R.drawable.background_6),
		new BackgroundVo(R.drawable.background_7small,
				R.drawable.background_7),
		new BackgroundVo(R.drawable.background_8small,
				R.drawable.background_8) };
public static BackgroundVo background;
	
}
