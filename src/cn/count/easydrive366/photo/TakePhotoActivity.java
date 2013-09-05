package cn.count.easydrive366.photo;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;

public class TakePhotoActivity extends BaseHttpActivity {
	protected DisplayMetrics dm;
	protected static final String imgPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";
	protected static final File PHOTO_DIR = new File(imgPath);

	/* 用来标识请求照相功能的activity */
	protected static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	protected static final int PHOTO_PICKED_WITH_DATA = 3021;
	protected static final int imageScale = 3;
	private ImageView imageView;
	protected Bitmap photo;
	private double latitude=0;
	private double longtitude=0;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		String strVer=  android.os.Build.VERSION.RELEASE;
		strVer=strVer.substring(0,3).trim();
		float fv=Float.valueOf(strVer);
		if(fv>2.3) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork() // 这里可以替换为detectAll() 就包括了磁盘读写和网络I/O
				.penaltyLog() //打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() //探测SQLite数据库操作
				.penaltyLog() //打印logcat
				.penaltyDeath()
				.build());
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_takephoto_activity);
		
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		this.setupRightButtonWithText("拍照");
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imageView = (ImageView)findViewById(R.id.image);
	}
	@Override
	protected void onRightButtonPress() {
		doPickPhotoAction();
	}
	protected boolean checkSoftStage() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { // 判断是否存在SD卡
			File file = new File(imgPath);
			if (!file.exists()) {
				file.mkdir();
			}
			return true;
		} else {
			new AlertDialog.Builder(this)
					.setMessage(
							"Testing to mobile phone no memory card! Please insert the cell phone memory card and open this application.")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).show();
			return false;
		}
	}

	protected void doPickPhotoAction() {
		Context context = this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String cancel = "Back";
		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.take_photo); // 拍照
		choices[1] = getString(R.string.pick_photo); // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle(R.string.choose_photo);
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							if (checkSoftStage()) {// 判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							} else {
								showMessage("Your device does not have a SD card. Please check your device.",null);
							}
							break;

						}
						case 1:
							doPickPhotoFromGallery();// 从相册中去获取
							break;
						}
					}
				});
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}

	/**
	 * 拍照获取图片
	 * 
	 */
	protected void doTakePhoto() {
		try {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showMessage(getText(R.string.photoPickerNotFoundText).toString(),
					null);
		}
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			/*
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", dm.widthPixels / imageScale - 6);
			intent.putExtra("outputY", dm.widthPixels / imageScale - 6);
			*/
			intent.putExtra("return-data", true);
			startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			showMessage(getText(R.string.sysError).toString(),
					null);
		}
	}

	/**
	 * 销毁图片文件
	 */
	protected void destoryBimap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		destoryBimap(photo);
		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			/*
			photo = data.getParcelableExtra("data");
			if (photo.getWidth() < photo.getHeight()) {
				Matrix matrix = new Matrix();  
	            matrix.reset();  
	            matrix.setRotate(90);
	            photo = Bitmap.createBitmap(photo, 0,0, photo.getWidth(), photo.getHeight(),matrix, true); 
			}
			*/
			Bundle bundle = data.getExtras();
			photo = (Bitmap) bundle.get("data");
			break;
		}
		case CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			Bundle bundle = data.getExtras();
			photo = (Bitmap) bundle.get("data");
			break;
		}
		}
		if (photo!=null){
			imageView.setImageBitmap(photo);
			sendImageToServer();
		}
		
	}
	private void sendImageToServer(){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		try
		{
			String actionUrl = String.format("%supload/do_upload?userid=%d&x=%f&y=%f&from=android",AppSettings.ServerUrl,
					AppSettings.userid,this.latitude,this.longtitude);
			String result = getHttpClient().uploadFile(actionUrl, "userfile", baos.toByteArray());	
			JSONObject json = new JSONObject(result);
			this.showMessage("上传成功！", null);
		}
		catch(Exception e)
		{
			
		}
	}

}
