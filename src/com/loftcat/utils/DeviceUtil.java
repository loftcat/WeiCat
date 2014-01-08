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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DeviceUtil {
	/**
	 * @MethodName: dip2px
	 * @Description:根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 * @Author: heb
	 * @CreateDate: 2012/10/09
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @MethodName: px2dip
	 * @Description:根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * @Author: heb
	 * @CreateDate: 2012/10/09
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	  public static void overridePendingTransition(Activity activity, int enter_anim, int exit_anim) {
	       	activity.overridePendingTransition(enter_anim, exit_anim);
	    }

	    public static Bitmap getThumbnail(ContentResolver cr, long origId, int kind, Options options) {
	       	return MediaStore.Images.Thumbnails.getThumbnail(cr,origId,kind, options);
	    }
	    
	    public static void recreate(Activity activity) {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            activity.recreate();
	        }
	    }

	    @SuppressLint("NewApi")
		public static void setLayerType(View view, int layerType, Paint paint) {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	            view.setLayerType(layerType, paint);
	        }
	    }

		public static void setUiOptions(Window window, int uiOptions) {
	        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	            window.setUiOptions(uiOptions);
	        }
	    }
}
