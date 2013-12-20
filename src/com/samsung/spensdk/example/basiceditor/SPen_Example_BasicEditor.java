package com.samsung.spensdk.example.basiceditor;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.loftcat.R;
import com.loftcat.app.AppConfig;
import com.samsung.samm.common.SObjectImage;
import com.samsung.samm.common.SObjectStroke;
import com.samsung.samm.common.SOptionSCanvas;
import com.samsung.spen.settings.SettingFillingInfo;
import com.samsung.spen.settings.SettingStrokeInfo;
import com.samsung.spen.settings.SettingTextInfo;
import com.samsung.spensdk.SCanvasConstants;
import com.samsung.spensdk.SCanvasView;
import com.samsung.spensdk.applistener.ColorPickerColorChangeListener;
import com.samsung.spensdk.applistener.HistoryUpdateListener;
import com.samsung.spensdk.applistener.SCanvasInitializeListener;
import com.samsung.spensdk.applistener.SCanvasModeChangedListener;
import com.samsung.spensdk.applistener.SPenHoverListener;
import com.samsung.spensdk.example.tools.PreferencesOfSAMMOption;
import com.samsung.spensdk.example.tools.SPenSDKUtils;
import com.samsung.spensdk.example.tools.ToolListActivity;

public class SPen_Example_BasicEditor extends Activity {

	private final String TAG = "SPenSDK Sample";

	// ==============================
	// Intent Parameters
	// ==============================

	public final static String KEY_IMAGE_SAVE_PATH = "SavePath";
	public final static String KEY_IMAGE_SRC_PATH = "FilePathOrigin";

	public final static int COME_FROM_SWEET = 0;
	public final static int COME_FROM_IMAGEDIALOG = 1;
	public final static int COME_FROM_ZOOMIMAGEDIALOG = 2;

	private int come_source;
	// ==============================
	// Application Identifier Setting
	// "SDK Sample Application 1.0"
	// ==============================
	private final String APPLICATION_ID_NAME = "SDK Sample Application";
	private final int APPLICATION_ID_VERSION_MAJOR = 2;
	private final int APPLICATION_ID_VERSION_MINOR = 2;
	private final String APPLICATION_ID_VERSION_PATCHNAME = "Debug";

	// ==============================
	// Menu
	// ==============================
	private final int MENU_FILE_GROUP = 2000;
	private final int MENU_FILE_1 = 2001;
	private final int MENU_FILE_2 = 2002;

	private final int MENU_DATA_GROUP = 3000;
	private final int MENU_DATA_1 = 3001;
	private final int MENU_DATA_2 = 3002;

	// ==============================
	// Activity Request code
	// ==============================
	private final int REQUEST_CODE_INSERT_IMAGE_OBJECT = 100;

	// ==============================
	// Variables
	// ==============================
	Context mContext = null;

	private String mSrcImageFilePath = null;
	private Rect mSrcImageRect = null;
	private String saveFileName = null;
	private FrameLayout mLayoutContainer;
	private RelativeLayout mCanvasContainer;
	private SCanvasView mSCanvas;
	private ImageView mPenBtn;
	private ImageView mEraserBtn;
	private ImageView mTextBtn;
	private ImageView mFillingBtn;
	private ImageView mInsertBtn;
	private ImageView mColorPickerBtn;
	private ImageView mUndoBtn;
	private ImageView mRedoBtn;

	private String mTempAMSFolderPath = null;

	private final String DEFAULT_SAVE_PATH = "loftcat/share";
	private final String DEFAULT_FILE_EXT = ".png";
	private long id;
	private String mode;
	private final int MENU_FILE_SAVE = 1000;
	private final int MENU_FILE_LOAD = 1001;
	Button submit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.editor_basic_editor);
		submit = (Button) findViewById(R.id.submit);
		id = getIntent().getLongExtra("userID", 0);
		mode = getIntent().getStringExtra("mode");

		if (mode.equals("new")) {
			submit.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String path = save();
					Intent intent = new Intent(AppConfig.INTENT_ACTION_SEND);
					intent.putExtra("path", path);
					intent.putExtra("mode", "new");
					intent.putExtra("userID", id);
					startActivity(intent);
					finish();
				}
			});
		} else {
			submit.setText("确定");
			submit.setOnClickListener(new Button.OnClickListener() {

				@Override
				public void onClick(View v) {
					SPenSDKUtils.alertActivityFinish(
							SPen_Example_BasicEditor.this, "保存编辑后的图片并返回？",
							saveFileName, mode);

				}
			});

		}

		mContext = this;

		// ------------------------------------
		// UI Setting
		// ------------------------------------
		mPenBtn = (ImageView) findViewById(R.id.penBtn);
		mPenBtn.setOnClickListener(mBtnClickListener);
		mPenBtn.setOnLongClickListener(mBtnLongClickListener);
		mEraserBtn = (ImageView) findViewById(R.id.eraseBtn);
		mEraserBtn.setOnClickListener(mBtnClickListener);
		mEraserBtn.setOnLongClickListener(mBtnLongClickListener);
		mTextBtn = (ImageView) findViewById(R.id.textBtn);
		mTextBtn.setOnClickListener(mBtnClickListener);
		mTextBtn.setOnLongClickListener(mBtnLongClickListener);
		mFillingBtn = (ImageView) findViewById(R.id.fillingBtn);
		mFillingBtn.setOnClickListener(mBtnClickListener);
		mFillingBtn.setOnLongClickListener(mBtnLongClickListener);
		mInsertBtn = (ImageView) findViewById(R.id.insertBtn);
		mInsertBtn.setOnClickListener(mInsertBtnClickListener);
		mColorPickerBtn = (ImageView) findViewById(R.id.colorPickerBtn);
		mColorPickerBtn.setOnClickListener(mColorPickerListener);

		mUndoBtn = (ImageView) findViewById(R.id.undoBtn);
		mUndoBtn.setOnClickListener(undoNredoBtnClickListener);
		mRedoBtn = (ImageView) findViewById(R.id.redoBtn);
		mRedoBtn.setOnClickListener(undoNredoBtnClickListener);

		// ------------------------------------
		// Create SCanvasView
		// ------------------------------------
		mLayoutContainer = (FrameLayout) findViewById(R.id.layout_container);
		mCanvasContainer = (RelativeLayout) findViewById(R.id.canvas_container);

		mSCanvas = new SCanvasView(mContext);
		mCanvasContainer.addView(mSCanvas);

		// Intent intent = getIntent();

		// mSrcImageFilePath = intent.getStringExtra(KEY_IMAGE_SRC_PATH);
		//
		// // If initial image exist, resize the canvas size
		// if (mSrcImageFilePath != null) {
		// mSrcImageRect = getMiniumCanvasRect(mSrcImageFilePath, 20);
		//
		// // Place SCanvasView In the Center
		// FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)
		// mCanvasContainer
		// .getLayoutParams();
		// layoutParams.width = mSrcImageRect.right - mSrcImageRect.left;
		// layoutParams.height = mSrcImageRect.bottom - mSrcImageRect.top;
		// layoutParams.gravity = Gravity.CENTER;
		// mCanvasContainer.setLayoutParams(layoutParams);
		//
		// // Set Background of layout container
		// mLayoutContainer.setBackgroundResource(R.drawable.bg_edit);
		// }

		// ------------------------------------
		// SettingView Setting
		// ------------------------------------
		// Resource Map for Layout & Locale
		HashMap<String, Integer> settingResourceMapInt = SPenSDKUtils
				.getSettingLayoutLocaleResourceMap(true, true, true, true);
		// Resource Map for Custom font path
		HashMap<String, String> settingResourceMapString = SPenSDKUtils
				.getSettingLayoutStringResourceMap(true, true, true, true);
		// Create Setting View
		mSCanvas.createSettingView(mLayoutContainer, settingResourceMapInt,
				settingResourceMapString);

		// ====================================================================================
		//
		// Set Callback Listener(Interface)
		//
		// ====================================================================================
		// ------------------------------------------------
		// SCanvas Listener
		// ------------------------------------------------
		mSCanvas.setSCanvasInitializeListener(new SCanvasInitializeListener() {
			@Override
			public void onInitialized() {
				// --------------------------------------------
				// Start SCanvasView/CanvasView Task Here
				// --------------------------------------------
				// Application Identifier Setting
				if (!mSCanvas.setAppID(APPLICATION_ID_NAME,
						APPLICATION_ID_VERSION_MAJOR,
						APPLICATION_ID_VERSION_MINOR,
						APPLICATION_ID_VERSION_PATCHNAME))
					Toast.makeText(mContext, "Fail to set App ID.",
							Toast.LENGTH_LONG).show();

				// Set Title
				if (!mSCanvas.setTitle("SPen-SDK Test"))
					Toast.makeText(mContext, "Fail to set Title.",
							Toast.LENGTH_LONG).show();

				// Update button state
				updateModeState();

				// Load the file & set Background Image
				// if (mSrcImageFilePath != null) {

				// if (SCanvasView.isSAMMFile(mSrcImageFilePath)) {
				// loadSAMMFile(mSrcImageFilePath);
				// Set the editing rect after loading
				// } else {
				// set BG Image
				// if (!mSCanvas.setBGImagePath(mSrcImageFilePath)) {
				// Toast.makeText(mContext,
				// "Fail to set Background Image Path.",
				// Toast.LENGTH_LONG).show();
				mSCanvas.setBackgroundColor(Color.WHITE);
				Log.d("RESULT", "setBackgroundImageExpress");
				// }
				// }
				// }

				// Restore last setting information
				// mSCanvas.restoreSettingViewStatus();

				saveFileName = getIntent().getStringExtra("path");
				insertImageObject(saveFileName);

				// no save bg image
				// Update button state
				// updateModeState();
			}
		});

		// ------------------------------------------------
		// History Change Listener
		// ------------------------------------------------
		mSCanvas.setHistoryUpdateListener(new HistoryUpdateListener() {
			@Override
			public void onHistoryChanged(boolean undoable, boolean redoable) {
				mUndoBtn.setEnabled(undoable);
				mRedoBtn.setEnabled(redoable);
			}
		});

		// ------------------------------------------------
		// SCanvas Mode Changed Listener
		// ------------------------------------------------
		mSCanvas.setSCanvasModeChangedListener(new SCanvasModeChangedListener() {

			@Override
			public void onModeChanged(int mode) {
				updateModeState();
			}
		});

		// ------------------------------------------------
		// Color Picker Listener
		// ------------------------------------------------
		mSCanvas.setColorPickerColorChangeListener(new ColorPickerColorChangeListener() {
			@Override
			public void onColorPickerColorChanged(int nColor) {

				int nCurMode = mSCanvas.getCanvasMode();
				if (nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
					SettingStrokeInfo strokeInfo = mSCanvas
							.getSettingViewStrokeInfo();
					if (strokeInfo != null) {
						strokeInfo.setStrokeColor(nColor);
						mSCanvas.setSettingViewStrokeInfo(strokeInfo);
					}
				} else if (nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER) {
					// do nothing
				} else if (nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_TEXT) {
					SettingTextInfo textInfo = mSCanvas
							.getSettingViewTextInfo();
					if (textInfo != null) {
						textInfo.setTextColor(nColor);
						mSCanvas.setSettingViewTextInfo(textInfo);
					}
				} else if (nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_FILLING) {
					SettingFillingInfo fillingInfo = mSCanvas
							.getSettingViewFillingInfo();
					if (fillingInfo != null) {
						fillingInfo.setFillingColor(nColor);
						mSCanvas.setSettingViewFillingInfo(fillingInfo);
					}
				}
			}
		});

		mUndoBtn.setEnabled(false);
		mRedoBtn.setEnabled(false);
		mPenBtn.setSelected(true);
		mSCanvas.setSCanvasHoverPointerStyle(SCanvasConstants.SCANVAS_HOVERPOINTER_STYLE_SPENSDK);

		mSCanvas.setSPenHoverListener(new SPenHoverListener() {

			@Override
			public void onHoverButtonUp(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				int nPreviousMode = mSCanvas.getCanvasMode();

				boolean bIncludeDefinedSetting = true;
				boolean bIncludeCustomSetting = true;
				boolean bIncludeEraserSetting = true;
				SettingStrokeInfo settingInfo = mSCanvas
						.getNextSettingViewStrokeInfo(bIncludeDefinedSetting,
								bIncludeCustomSetting, bIncludeEraserSetting);
				if (settingInfo != null) {
					if (mSCanvas.setSettingViewStrokeInfo(settingInfo)) {
						// Mode Change : Pen => Eraser
						if (nPreviousMode == SCanvasConstants.SCANVAS_MODE_INPUT_PEN
								&& settingInfo.getStrokeStyle() == SObjectStroke.SAMM_STROKE_STYLE_ERASER) {
							// Change Mode
							mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
							// Show Setting View
							if (mSCanvas
									.isSettingViewVisible(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN)) {
								mSCanvas.showSettingView(
										SCanvasConstants.SCANVAS_SETTINGVIEW_PEN,
										false);
								mSCanvas.showSettingView(
										SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER,
										true);
							}
							updateModeState();
						}
						// Mode Change : Eraser => Pen
						if (nPreviousMode == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER
								&& settingInfo.getStrokeStyle() != SObjectStroke.SAMM_STROKE_STYLE_ERASER) {
							// Change Mode
							mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
							// Show Setting View
							if (mSCanvas
									.isSettingViewVisible(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER)) {
								mSCanvas.showSettingView(
										SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER,
										false);
								mSCanvas.showSettingView(
										SCanvasConstants.SCANVAS_SETTINGVIEW_PEN,
										true);
							}
							updateModeState();
						}
					}
				}
			}

			@Override
			public void onHoverButtonDown(View view, MotionEvent event) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean onHover(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		// Caution:
		// Do NOT load file or start animation here because we don't know canvas
		// size here.
		// Start such SCanvasView Task at onInitialized() of
		// SCanvasInitializeListener
		// 若未设置以上两个颜色，则默认为黑底白字
		// LinearLayout miniLayout = (LinearLayout)
		// findViewById(R.id.miniAdLinearLayout);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Save current final setting information
		// mSCanvas.saveSettingViewStatus();

		// Release SCanvasView resources
		if (!mSCanvas.closeSCanvasView())
			Log.e(TAG, "Fail to close SCanvasView");

	}

	@Override
	public void onBackPressed() {
		// Log.d("RESULT", "saveFileName:"+saveFileName);
		SPenSDKUtils.alertActivityFinish(this, "保存编辑后的图片并返回？", saveFileName,
				mode);

	}

	private OnClickListener undoNredoBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.equals(mUndoBtn)) {
				mSCanvas.undo();
			} else if (v.equals(mRedoBtn)) {
				mSCanvas.redo();
			}
			mUndoBtn.setEnabled(mSCanvas.isUndoable());
			mRedoBtn.setEnabled(mSCanvas.isRedoable());
		}
	};

	private OnClickListener mBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int nBtnID = v.getId();
			// If the mode is not changed, open the setting view. If the mode is
			// same, close the setting view.
			if (nBtnID == mPenBtn.getId()) {
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
					mSCanvas.setSettingViewSizeOption(
							SCanvasConstants.SCANVAS_SETTINGVIEW_PEN,
							SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_EXT);
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, false);
					updateModeState();
				}
			} else if (nBtnID == mEraserBtn.getId()) {
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER) {
					mSCanvas.setSettingViewSizeOption(
							SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER,
							SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, false);
					updateModeState();
				}
			} else if (nBtnID == mTextBtn.getId()) {
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_TEXT) {
					mSCanvas.setSettingViewSizeOption(
							SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT,
							SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, false);
					updateModeState();
					Toast.makeText(mContext, "Tap Canvas to insert Text",
							Toast.LENGTH_SHORT).show();
				}
			} else if (nBtnID == mFillingBtn.getId()) {
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_FILLING) {
					mSCanvas.setSettingViewSizeOption(
							SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING,
							SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_NORMAL);
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_FILLING);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING, false);
					updateModeState();
					Toast.makeText(mContext, "Tap Canvas to fill color",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	};

	private OnLongClickListener mBtnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {

			int nBtnID = v.getId();
			// If the mode is not changed, open the setting view. If the mode is
			// same, close the setting view.
			if (nBtnID == mPenBtn.getId()) {
				mSCanvas.setSettingViewSizeOption(
						SCanvasConstants.SCANVAS_SETTINGVIEW_PEN,
						SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_MINI);
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_PEN) {
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_PEN);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_PEN, true);
					updateModeState();
				}
				return true;
			} else if (nBtnID == mEraserBtn.getId()) {
				mSCanvas.setSettingViewSizeOption(
						SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER,
						SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_MINI);
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER) {
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_ERASER, true);
					updateModeState();
				}
				return true;
			} else if (nBtnID == mTextBtn.getId()) {
				mSCanvas.setSettingViewSizeOption(
						SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT,
						SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_MINI);
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_TEXT) {
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_TEXT, true);
					updateModeState();
					Toast.makeText(mContext, "Tap Canvas to insert Text",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			} else if (nBtnID == mFillingBtn.getId()) {
				mSCanvas.setSettingViewSizeOption(
						SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING,
						SCanvasConstants.SCANVAS_SETTINGVIEW_SIZE_MINI);
				if (mSCanvas.getCanvasMode() == SCanvasConstants.SCANVAS_MODE_INPUT_FILLING) {
					mSCanvas.toggleShowSettingView(SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING);
				} else {
					mSCanvas.setCanvasMode(SCanvasConstants.SCANVAS_MODE_INPUT_FILLING);
					mSCanvas.showSettingView(
							SCanvasConstants.SCANVAS_SETTINGVIEW_FILLING, true);
					updateModeState();
					Toast.makeText(mContext, "Tap Canvas to fill color",
							Toast.LENGTH_SHORT).show();
				}
				return true;
			}

			return false;
		}
	};

	// insert image
	private OnClickListener mInsertBtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.equals(mInsertBtn)) {
				callGalleryForInputImage(REQUEST_CODE_INSERT_IMAGE_OBJECT);
			}
		}
	};

	// color picker mode
	private OnClickListener mColorPickerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.equals(mColorPickerBtn)) {
				// Toggle
				boolean bIsColorPickerMode = !mSCanvas.isColorPickerMode();
				mSCanvas.setColorPickerMode(bIsColorPickerMode);
				mColorPickerBtn.setSelected(bIsColorPickerMode);
			}
		}
	};

	// Update tool button
	private void updateModeState() {
		int nCurMode = mSCanvas.getCanvasMode();
		mPenBtn.setSelected(nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_PEN);
		mEraserBtn
				.setSelected(nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
		mTextBtn.setSelected(nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_TEXT);
		mFillingBtn
				.setSelected(nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_FILLING);

		// Reset color picker tool when Eraser Mode
		if (nCurMode == SCanvasConstants.SCANVAS_MODE_INPUT_ERASER)
			mSCanvas.setColorPickerMode(false);
		mColorPickerBtn
				.setEnabled(nCurMode != SCanvasConstants.SCANVAS_MODE_INPUT_ERASER);
		mColorPickerBtn.setSelected(mSCanvas.isColorPickerMode());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (data == null)
				return;

			if (requestCode == REQUEST_CODE_INSERT_IMAGE_OBJECT) {
				Uri imageFileUri = data.getData();
				String imagePath = SPenSDKUtils.getRealPathFromURI(this,
						imageFileUri);
				insertImageObject(imagePath);
			}
		}
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		super.onMenuOpened(featureId, menu);

		if (menu == null)
			return true;

		return true;
	}

	// Call Gallery
	private void callGalleryForInputImage(int nRequestCode) {
		try {
			Intent galleryIntent;
			galleryIntent = new Intent();
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			galleryIntent.setType("image/*");
			galleryIntent.setClassName("com.cooliris.media",
					"com.cooliris.media.Gallery");
			startActivityForResult(galleryIntent, nRequestCode);
		} catch (ActivityNotFoundException e) {
			Intent galleryIntent;
			galleryIntent = new Intent();
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			galleryIntent.setType("image/*");
			startActivityForResult(galleryIntent, nRequestCode);
			e.printStackTrace();
		}
	}

	// Get the minimum image scaled rect which is fit to current screen
	Rect getMiniumCanvasRect(String strImagePath, int nMargin) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		int nScreenWidth = displayMetrics.widthPixels - nMargin * 2;
		int nScreenHeight = displayMetrics.heightPixels - nMargin * 2;

		// Make more small for screen rotation T.T
		if (nScreenWidth < nScreenHeight)
			nScreenHeight = nScreenWidth;
		else
			nScreenWidth = nScreenHeight;

		int nImageWidth = nScreenWidth;
		int nImageHeight = nScreenHeight;
		if (strImagePath != null) {
			BitmapFactory.Options opts = SPenSDKUtils
					.getBitmapSize(strImagePath);
			nImageWidth = opts.outWidth;
			nImageHeight = opts.outHeight;
		}

		float fResizeWidth = (float) nScreenWidth / nImageWidth;
		float fResizeHeight = (float) nScreenHeight / nImageHeight;
		float fResizeRatio;

		// Fit to Height
		if (fResizeWidth > fResizeHeight) {
			fResizeRatio = fResizeHeight;
		}
		// Fit to Width
		else {
			fResizeRatio = fResizeWidth;
		}

		return new Rect(0, 0, (int) (nImageWidth * fResizeRatio),
				(int) (nImageHeight * fResizeRatio));
	}

	// Load SAMM file
	boolean loadSAMMFile(String strFileName) {
		if (mSCanvas.isAnimationMode()) {
			// It must be not animation mode.
		} else {
			// set progress dialog
			mSCanvas.setProgressDialogSetting("Loading",
					"Please wait while loading...",
					ProgressDialog.STYLE_HORIZONTAL, false);

			// canvas option setting
			SOptionSCanvas canvasOption = mSCanvas.getOption();
			if (canvasOption == null)
				return false;
			canvasOption.mSAMMOption
					.setConvertCanvasSizeOption(PreferencesOfSAMMOption
							.getPreferenceLoadCanvasSize(mContext));
			canvasOption.mSAMMOption
					.setConvertCanvasHorizontalAlignOption(PreferencesOfSAMMOption
							.getPreferenceLoadCanvasHAlign(mContext));
			canvasOption.mSAMMOption
					.setConvertCanvasVerticalAlignOption(PreferencesOfSAMMOption
							.getPreferenceLoadCanvasVAlign(mContext));
			// option setting
			mSCanvas.setOption(canvasOption);

			// show progress for loading data
			if (mSCanvas.loadSAMMFile(strFileName, true, true, true)) {
				// Loading Result can be get by callback function
			} else {
				Toast.makeText(this,
						"Load AMS File(" + strFileName + ") Fail!",
						Toast.LENGTH_LONG).show();
				return false;
			}
		}
		return true;
	}

	// insert Image Object
	boolean insertImageObject(String imagePath) {
		// Check Valid Image File
		if (!SPenSDKUtils.isValidImagePath(imagePath)) {
			Toast.makeText(this, "Invalid image path or web image",
					Toast.LENGTH_LONG).show();
			return false;
		}

		RectF rectF = getDefaultImageRect(imagePath);
		SObjectImage sImageObject = new SObjectImage();
		sImageObject.setRect(rectF);
		sImageObject.setImagePath(imagePath);

		// canvas option setting
		SOptionSCanvas canvasOption = mSCanvas.getOption();
		if (canvasOption == null)
			return false;
		canvasOption.mSAMMOption.setContentsQuality(PreferencesOfSAMMOption
				.getPreferenceSaveImageQuality(mContext));
		// option setting
		mSCanvas.setOption(canvasOption);

		if (mSCanvas.insertSAMMImage(sImageObject, true)) {
			// Toast.makeText(this, "Insert image file("+ imagePath
			// +") Success!", Toast.LENGTH_SHORT).show();
			return true;
		} else {
			Toast.makeText(this, "Insert image file(" + imagePath + ") Fail!",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

	// get default image rect
	RectF getDefaultImageRect(String strImagePath) {
		// Rect Region : Consider image real size
		BitmapFactory.Options opts = SPenSDKUtils.getBitmapSize(strImagePath);
		int nImageWidth = opts.outWidth;
		int nImageHeight = opts.outHeight;
		int nScreenWidth = mSCanvas.getWidth();
		int nScreenHeight = mSCanvas.getHeight();
		int nBoxRadius = (nScreenWidth > nScreenHeight) ? nScreenHeight / 4
				: nScreenWidth / 4;
		int nCenterX = nScreenWidth / 2;
		int nCenterY = nScreenHeight / 2;
		if (nImageWidth > nImageHeight)
			return new RectF(nCenterX - nBoxRadius, nCenterY
					- (nBoxRadius * nImageHeight / nImageWidth), nCenterX
					+ nBoxRadius, nCenterY
					+ (nBoxRadius * nImageHeight / nImageWidth));
		else
			return new RectF(nCenterX
					- (nBoxRadius * nImageWidth / nImageHeight), nCenterY
					- nBoxRadius, nCenterX
					+ (nBoxRadius * nImageWidth / nImageHeight), nCenterY
					+ nBoxRadius);
	}

	// public boolean onCreateOptionsMenu(Menu menu) {
	// menu.add(MENU_FILE_SAVE, MENU_FILE_SAVE, 1, "Save");
	// menu.add(MENU_FILE_LOAD, MENU_FILE_LOAD, 2, "Cancel");
	//
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// super.onOptionsItemSelected(item);
	//
	// switch (item.getItemId()) {
	//
	// case MENU_FILE_SAVE: { // -------------------------------
	// // layout setting
	// // -------------------------------
	// // check canvas drawing empty
	//
	// LayoutInflater factory = LayoutInflater.from(this);
	// final View textEntryView = factory.inflate(
	// R.layout.alert_dialog_get_text, null);
	// TextView textTitle = (TextView) textEntryView
	// .findViewById(R.id.textTitle);
	// textTitle.setText("Enter filename to save (default: *.png)");
	// AlertDialog dlg = new AlertDialog.Builder(this)
	// .setTitle("Save As")
	// .setView(textEntryView)
	// .setPositiveButton("Save",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// EditText et = (EditText) textEntryView
	// .findViewById(R.id.text);
	// String strFileName = et.getText()
	// .toString();
	//
	// // check file name length, invalid
	// // characters, overwrite, extension, etc.
	// if (strFileName == null)
	// return;
	//
	// if (strFileName.length() <= 0) {
	// Toast.makeText(mContext,
	// "Enter file name to save",
	// Toast.LENGTH_LONG).show();
	// return;
	// }
	// if (!SPenSDKUtils
	// .isValidSaveName(strFileName)) {
	// Toast.makeText(
	// mContext,
	// "Invalid character to save! Save file name : "
	// + strFileName,
	// Toast.LENGTH_LONG).show();
	// return;
	// }
	//
	// int nExtIndex = strFileName
	// .lastIndexOf(".");
	// if (nExtIndex == -1)
	// strFileName += DEFAULT_FILE_EXT;
	// else {
	// String strExt = strFileName
	// .substring(nExtIndex + 1);
	// if (strExt == null)
	// strFileName += DEFAULT_FILE_EXT;
	// else {
	// if (strExt
	// .compareToIgnoreCase("png") != 0
	// && strExt
	// .compareToIgnoreCase("jpg") != 0) {
	// strFileName += DEFAULT_FILE_EXT;
	// }
	// }
	// }
	//
	// mTempAMSFolderPath = Environment
	// .getExternalStorageDirectory()
	// .getAbsolutePath();
	//
	// saveFileName = mTempAMSFolderPath + "/"
	// + DEFAULT_SAVE_PATH + "/"
	// + strFileName;
	// File files = new File(mTempAMSFolderPath
	// + "/" + DEFAULT_SAVE_PATH);
	//
	// if (!files.exists()) {
	//
	// files.mkdirs();
	// }
	// checkSameSaveFileName(saveFileName);
	// }
	// })
	// .setNegativeButton("Cancel",
	// new DialogInterface.OnClickListener() {
	// public void onClick(DialogInterface dialog,
	// int whichButton) {
	// /* User clicked cancel so do some stuff */
	// }
	// }).create();
	// dlg.getWindow()
	// .setSoftInputMode(
	// WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
	// | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	// dlg.show();
	// }
	// break;
	// case MENU_FILE_LOAD: {
	// // Intent intent = new Intent(this, ToolListActivity.class);
	// // String [] exts = new String [] { "jpg", "png", "ams" }; // file
	// // extension
	// // intent.putExtra(ToolListActivity.EXTRA_LIST_PATH,
	// // mTempAMSFolderPath);
	// // intent.putExtra(ToolListActivity.EXTRA_FILE_EXT_ARRAY, exts);
	// // intent.putExtra(ToolListActivity.EXTRA_SEARCH_ONLY_SAMM_FILE,
	// // true);
	// // startActivityForResult(intent, REQUEST_CODE_FILE_SELECT);
	// finish();
	// }
	//
	// }
	// return true;
	// }

	public String save() {
		String strFileName = String.valueOf(new Date().getTime());
		Log.d("RESULT", "strFileName:" + strFileName);
		// check file name length, invalid
		// characters, overwrite, extension, etc.
		if (strFileName == null)
			return null;

		if (strFileName.length() <= 0) {
			Toast.makeText(mContext, "Enter file name to save",
					Toast.LENGTH_LONG).show();
			return null;
		}
		if (!SPenSDKUtils.isValidSaveName(strFileName)) {
			Toast.makeText(
					mContext,
					"Invalid character to save! Save file name : "
							+ strFileName, Toast.LENGTH_LONG).show();
			return null;
		}

		int nExtIndex = strFileName.lastIndexOf(".");
		if (nExtIndex == -1)
			strFileName += DEFAULT_FILE_EXT;
		else {
			String strExt = strFileName.substring(nExtIndex + 1);
			if (strExt == null)
				strFileName += DEFAULT_FILE_EXT;
			else {
				if (strExt.compareToIgnoreCase("png") != 0
						&& strExt.compareToIgnoreCase("jpg") != 0) {
					strFileName += DEFAULT_FILE_EXT;
				}
			}
		}

		mTempAMSFolderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		saveFileName = mTempAMSFolderPath + "/" + DEFAULT_SAVE_PATH + "/"
				+ strFileName;
		File files = new File(mTempAMSFolderPath + "/" + DEFAULT_SAVE_PATH);

		if (!files.exists()) {

			files.mkdirs();
		}
		checkSameSaveFileName(saveFileName);
		return saveFileName;
	}

	private void checkSameSaveFileName(final String saveFileName) {

		File fSaveFile = new File(saveFileName);
		if (fSaveFile.exists()) {
			AlertDialog dlg = new AlertDialog.Builder(this)
					.setTitle("Same file name exists! Overwrite?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									saveSAMMFile(saveFileName, true);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* User clicked cancel so do some stuff */
								}
							}).create();
			dlg.show();
		} else {
			saveSAMMFile(saveFileName, true);
		}
	}

	boolean saveSAMMFile(String strFileName, boolean bShowSuccessLog) {
		if (mSCanvas.saveSAMMFile(strFileName)) {
			if (bShowSuccessLog) {
				Toast.makeText(this,
						"Save AMS File(" + strFileName + ") Success!",
						Toast.LENGTH_LONG).show();
			}
			return true;
		} else {
			Toast.makeText(this, "Save AMS File(" + strFileName + ") Fail!",
					Toast.LENGTH_LONG).show();
			return false;
		}
	}

}
