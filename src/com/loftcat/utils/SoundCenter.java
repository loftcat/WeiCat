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

import com.loftcat.R;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundCenter {
	private boolean isSound;
	private MediaPlayer mPlayer;

	public SoundCenter(Context context, boolean isSound) {

		this.isSound = isSound;

		mPlayer = MediaPlayer.create(context, R.raw.newdatatoast);
		mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// mp.release();
			}
		});

	}

	public void show() {
		mPlayer.start();
	}

	public void release() {
		mPlayer.release();
	}
}
