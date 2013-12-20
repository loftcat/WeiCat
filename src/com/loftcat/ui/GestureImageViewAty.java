package com.loftcat.ui;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.ImageUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.polites.android.GestureImageView;
import com.weibo.sdk.android.WeiboException;

public class GestureImageViewAty extends BaseActivity {

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	GestureImageView gestureImageView;
	String path;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	Button button;
	Long id;
	File file;

	@Override
	public void initView() {
		setContentView(R.layout.gesture_imageview);
		button = (Button) findViewById(R.id.submit);
		gestureImageView = (GestureImageView) findViewById(R.id.gestureImageView);
		path = getIntent().getStringExtra("path");
		id = getIntent().getLongExtra("userID", 0);
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading)
				.showImageForEmptyUri(R.drawable.loading).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(0)).build();
		imageLoader.displayImage(path, gestureImageView, options);
		file = imageLoader.getDiscCache().get(path);
		Log.d("RESULT", "file:" + file.getAbsolutePath());

		button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				imageLoader.clearMemoryCache();
				Intent intent2 = new Intent(AppConfig.INTENT_ACTION_SPEN_EDITOR);
				intent2.putExtra("mode", "new");
				intent2.putExtra("userID", id);
				intent2.putExtra("path", file.getAbsolutePath());
				startActivity(intent2);
				System.gc();
			}
		});

	}

	@Override
	public void initLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
