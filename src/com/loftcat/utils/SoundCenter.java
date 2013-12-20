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
