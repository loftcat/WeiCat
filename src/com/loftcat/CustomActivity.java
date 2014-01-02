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
package com.loftcat;

import com.baidu.android.pushservice.PushConstants;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

/** 
 * 
 * 自定义的显示通知的Activity
 *
 */
public class CustomActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
		setContentView(resource.getIdentifier("custom_activity", "layout", pkgName));

		TextView titleView = (TextView) this.findViewById(resource.getIdentifier("title", "id", pkgName));
		TextView contentView = (TextView) this.findViewById(resource.getIdentifier("content", "id", pkgName));
		
		Intent intent = getIntent();
        String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
        String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);
        
        titleView.setText(title);
        contentView.setText(content);
	}
}
