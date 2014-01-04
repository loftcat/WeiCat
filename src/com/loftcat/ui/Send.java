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
package com.loftcat.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.loftcat.utils.BaseActivity;
import com.loftcat.utils.ImageUtils;
import com.loftcat.utils.LogCenter;
import com.loftcat.utils.SoundCenter;
import com.loftcat.weibo.sdk.CommentsAPI;
import com.loftcat.weibo.sdk.StatusesAPI;
import com.loftcat.weibo.sdk.WeiboAPI.COMMENTS_TYPE;
import com.samsung.spensdk.example.basiceditor.SPen_Example_BasicEditor;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class Send extends BaseActivity {
	public static String TAG = "Send";

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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	private CheckBox checkBox;
	private Button submit;
	private EditText editText;
	private ImageButton send_image;
	private ImageButton send_phone;
	private ImageView send_image_show;
	private TextView at_textview;
	private ImageView send_image_show_x;

	@Override
	public void initView() {
		setContentView(R.layout.send);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		checkBox = (CheckBox) findViewById(R.id.send_checkbox);
		submit = (Button) findViewById(R.id.send_publish);
		at_textview = (TextView) findViewById(R.id.at);
		send_image_show = (ImageView) findViewById(R.id.send_image_show);
		editText = (EditText) findViewById(R.id.send_text);
		send_image = (ImageButton) findViewById(R.id.send_image);
		send_phone = (ImageButton) findViewById(R.id.send_photo);
		send_image_show_x = (ImageView) findViewById(R.id.send_image_show_x);
		soundCenter = new SoundCenter(getApplicationContext(), true);
	}

	private SoundCenter soundCenter;

	private Bitmap bitmap;
	private long status_id;
	private long userID;

	private long reply_id;

	private String mode;
	private String msg = "";

	private StatusesAPI statusesAPI;
	private String name = "";
	private String at;

	private CommentsAPI commentsAPI;

	@Override
	public void initLogic() {
		userID = getIntent().getLongExtra("userID", 0);
		status_id = getIntent().getLongExtra("status_id", 0);
		reply_id = getIntent().getLongExtra("reply_id", 0);
		name = getIntent().getStringExtra("userName");
		msg = getIntent().getStringExtra("msg");


		mode = getIntent().getStringExtra("mode");

		if (mode != null && mode.equals("forward")) {
			checkBox.setText("同时评论该微博");
			statusesAPI = new StatusesAPI(utility.getAccessToken());
			send_image.setVisibility(View.GONE);
			send_phone.setVisibility(View.GONE);
		} else if (mode != null
				&& (mode.equals("comment") || mode.equals("reply"))) {
			checkBox.setText("是否评论给原微博");
			commentsAPI = new CommentsAPI(utility.getAccessToken());
			send_image.setVisibility(View.GONE);
			send_phone.setVisibility(View.GONE);
		} else if (mode != null && mode.equals("new")) {
			at = getIntent().getStringExtra("at");
			if (at != null) {
				String msg = "@" + at + " ";
				editText.setText(msg);
				editText.requestFocus();
				editText.setSelection(msg.length());
			}
			checkBox.setVisibility(View.GONE);
			statusesAPI = new StatusesAPI(utility.getAccessToken());
			String path = getIntent().getStringExtra("path");
			if (path != null && !path.equals("")) {
				send_image_show.setImageBitmap(ImageUtils.getSmallImage(path,
						64, 64));
				thumbnail = "sdcard/loftcat/share/"
						+ System.currentTimeMillis() + ".png";
				try {
					ImageUtils.createImageThumbnail(this, path, thumbnail, 480,
							50);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				send_image_show.setVisibility(View.VISIBLE);
				send_image_show_x.setVisibility(View.VISIBLE);
				hasPic = true;
				send_image_show_x
						.setOnClickListener(new ImageView.OnClickListener() {

							@Override
							public void onClick(View v) {
								hasPic = false;
								send_image_show.setVisibility(View.GONE);
								send_image_show_x.setVisibility(View.GONE);
								thumbnail = null;
							}
						});
			}
		}
	}

	@Override
	public void initListener() {
		submit.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (mode.equals("comment")) {
					String msg = editText.getText().toString();
					commentsAPI.create(msg, status_id, checkBox.isChecked(),
							new RequestListener() {

								@Override
								public void onIOException(IOException arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onError(WeiboException arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onComplete(final String arg0) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(Send.this, "发布成功",
													Toast.LENGTH_LONG).show();
											soundCenter.show();
										}
									});
								}
							});
					Toast.makeText(Send.this, "消息进入发送队列~", Toast.LENGTH_LONG)
							.show();
					finish();

				} else if (mode.equals("forward")) {
					if (bitmap == null) {
						String msg = editText.getText().toString();

						statusesAPI.repost(status_id, msg,
								checkBox.isChecked() ? COMMENTS_TYPE.BOTH
										: COMMENTS_TYPE.NONE,
								new RequestListener() {

									@Override
									public void onIOException(IOException arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onError(WeiboException arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onComplete(final String arg0) {
										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(Send.this,
														"发布成功",
														Toast.LENGTH_LONG)
														.show();
												soundCenter.show();

											}
										});
									}
								});
						Toast.makeText(Send.this, "消息进入发送队列~",
								Toast.LENGTH_LONG).show();
						finish();
					} else {

					}
				} else if (mode.equals("new")) {
					if (!hasPic) {
						statusesAPI.update(editText.getText().toString(),
								"0.0", "0.0", new RequestListener() {

									@Override
									public void onIOException(IOException arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onError(WeiboException arg0) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onComplete(final String arg0) {
										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(Send.this,
														"发布成功",
														Toast.LENGTH_LONG)
														.show();
												soundCenter.show();

											}
										});
									}
								});
					} else {
						statusesAPI.upload(editText.getText().toString(),
								thumbnail, "0.0", "0.0", new RequestListener() {

									@Override
									public void onIOException(
											final IOException arg0) {

										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(
														getApplicationContext(),
														arg0.toString(),
														Toast.LENGTH_LONG)
														.show();
												Log.e("error", arg0.toString());
											}
										});
									}

									@Override
									public void onError(
											final WeiboException arg0) {
										//
										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(
														getApplicationContext(),
														arg0.toString(),
														Toast.LENGTH_LONG)
														.show();
												Log.e("error", arg0.toString());
											}
										});
									}

									@Override
									public void onComplete(String arg0) {
										runOnUiThread(new Runnable() {

											@Override
											public void run() {
												Toast.makeText(Send.this,
														"发布成功",
														Toast.LENGTH_LONG)
														.show();
												soundCenter.show();

											}
										});
									}
								});
					}
					Toast.makeText(Send.this, "消息进入发送队列~", Toast.LENGTH_LONG)
							.show();
					finish();
				} else if (mode.equals("reply")) {
					Log.d("RESULT", "reply");
					commentsAPI.reply(reply_id, status_id, editText.getText()
							.toString(), true, checkBox.isChecked(),
							new RequestListener() {

								@Override
								public void onIOException(IOException arg0) {
									Log.d("RESULT", arg0.toString());

								}

								@Override
								public void onError(WeiboException arg0) {
									Log.d("RESULT", arg0.toString());

								}

								@Override
								public void onComplete(final String arg0) {
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(Send.this, "发布成功",
													Toast.LENGTH_LONG).show();
											soundCenter.show();

										}
									});
								}
							});
					Toast.makeText(Send.this, "消息进入发送队列~", Toast.LENGTH_LONG)
							.show();
					finish();
				}

			}
		});
		send_image.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);

			}
		});
		send_phone.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, RESULT_PHOTO);
			}
		});
		at_textview.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AppConfig.INTENT_ACTION_AT);
				intent.putExtra("userID", userID);
				intent.putExtra("mode", "add");
				startActivityForResult(intent, RESULT_LOAD_AT);
			}
		});
	}

	static int RESULT_LOAD_IMAGE = 1;
	static int RESULT_PHOTO = 2;
	public static int RESULT_LOAD_AT = 0;

	boolean hasPic = false;
	String picturePath;
	String thumbnail;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& data != null) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			picturePath = cursor.getString(columnIndex);
			cursor.close();

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Send.this);
					builder.setTitle("提示");
					builder.setMessage("是否对图像进行编辑？");
					builder.setPositiveButton("编辑",
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									System.gc();
									Intent intent = new Intent();
									intent.setClass(Send.this,
											SPen_Example_BasicEditor.class);
									intent.putExtra("path", picturePath);
									intent.putExtra("mode", "edit");
									// startActivity(intent);
									startActivityForResult(
											intent,
											ImageUtils.REQUEST_CODE_GETIMAGE_EDIT);

								}
							});
					builder.setNegativeButton("取消",
							new AlertDialog.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									send_image_show.setImageBitmap(ImageUtils
											.getSmallImage(picturePath, 64, 64));
									thumbnail = "sdcard/loftcat/"
											+ System.currentTimeMillis()
											+ ".jpg";
									try {
										ImageUtils.createImageThumbnail(
												Send.this, picturePath,
												thumbnail, 640, 100);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									send_image_show.setVisibility(View.VISIBLE);
									send_image_show_x
											.setVisibility(View.VISIBLE);
									hasPic = true;
									send_image_show_x
											.setOnClickListener(new ImageView.OnClickListener() {

												@Override
												public void onClick(View v) {
													hasPic = false;
													send_image_show
															.setVisibility(View.GONE);
													send_image_show_x
															.setVisibility(View.GONE);
													thumbnail = null;
												}
											});

								}
							});
					builder.show();
				}
			});

		} else if (requestCode == RESULT_PHOTO && resultCode == RESULT_OK
				&& data != null) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				return;
			}

			Bundle bundle = data.getExtras();
			Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
			FileOutputStream b = null;
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/" + AppConfig.PIC_FILE_DERECTORY + "/");
			if (!file.exists()) {
				file.mkdirs();// 创建文件夹
			}
			Date now = new Date();
			Long time = now.getTime();
			picturePath = file.getPath() + time + ".JPEG";
			try {
				b = new FileOutputStream(picturePath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					b.flush();
					b.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			send_image_show.setImageBitmap(ImageUtils.getSmallImage(
					picturePath, 64, 64));// 将图片显示在ImageView里
			thumbnail = "sdcard/loftcat/" + System.currentTimeMillis() + ".jpg";
			try {
				ImageUtils.createImageThumbnail(this, picturePath, thumbnail,
						640, 100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send_image_show.setVisibility(View.VISIBLE);
			send_image_show_x.setVisibility(View.VISIBLE);
			hasPic = true;
			send_image_show_x
					.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							hasPic = false;
							send_image_show.setVisibility(View.GONE);
							send_image_show_x.setVisibility(View.GONE);
							thumbnail = null;
						}
					});
		} else if (requestCode == RESULT_LOAD_AT && resultCode == RESULT_OK
				&& data != null) {
			Log.d("RESULT", "RESULT_LOAD_AT");
			String msg = data.getStringExtra("at");
			// editText.setText(editText.getText().toString() + " @" + msg +
			// " ");
			editText.append(" @" + msg + " ");
		} else if (requestCode == ImageUtils.REQUEST_CODE_GETIMAGE_EDIT
				&& resultCode == ImageUtils.REQUEST_CODE_GETIMAGE_EDIT
				&& data != null) {
			picturePath = data.getStringExtra("path");
			send_image_show.setImageBitmap(ImageUtils.getSmallImage(
					picturePath, 64, 64));
			thumbnail = "sdcard/loftcat/" + System.currentTimeMillis() + ".jpg";
			try {
				ImageUtils.createImageThumbnail(Send.this, picturePath,
						thumbnail, 640, 100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			send_image_show.setVisibility(View.VISIBLE);
			send_image_show_x.setVisibility(View.VISIBLE);
			hasPic = true;
			send_image_show_x
					.setOnClickListener(new ImageView.OnClickListener() {

						@Override
						public void onClick(View v) {
							hasPic = false;
							send_image_show.setVisibility(View.GONE);
							send_image_show_x.setVisibility(View.GONE);
							thumbnail = null;
						}
					});

		}

	}
}
