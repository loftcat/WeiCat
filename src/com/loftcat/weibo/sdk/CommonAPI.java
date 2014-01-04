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
package com.loftcat.weibo.sdk;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.RequestListener;
/**
 * 此类封装了公共服务的接口，详情见<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E5.85.AC.E5.85.B1.E6.9C.8D.E5.8A.A1">公共服务接口</a>
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class CommonAPI extends WeiboAPI {
	public CommonAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "/common";

	/**
	 * 获取城市列表
	 * 
	 * @param province
	 * @param capital
	 * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
	 * @param listener
	 */
	public void getCity( String province, CAPITAL capital,String language, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("province", province);
	      if(null!=capital){
	          params.add("capital", capital.name().toLowerCase());
	      }
		params.add("language", language);
		request( SERVER_URL_PRIX + "/get_city.json", params, HTTPMETHOD_GET, listener);
	}
		
	/**
     * 获取国家列表
     * @param capital 国家的首字母，a-z，可为空代表返回全部，默认为全部。
     * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
     */
    public void getCountry(CAPITAL capital,String language, RequestListener listener) {
        WeiboParameters params = new WeiboParameters();
      if(null!=capital){
          params.add("capital", capital.name().toLowerCase());
      }
        params.add("language", language);
        request( SERVER_URL_PRIX + "/get_country.json", params, HTTPMETHOD_GET, listener);
    }
	
	/**
	 * 获取时区配置表
	 * 
	 * @param language 返回的语言版本，zh-cn：简体中文、zh-tw：繁体中文、english：英文，默认为zh-cn。
	 * @param listener
	 */
	public void getTimezone( String language, RequestListener listener) {
		WeiboParameters params = new WeiboParameters();
		params.add("language", language);
		request( SERVER_URL_PRIX + "/get_timezone.json", params, HTTPMETHOD_GET, listener);
	}
}
