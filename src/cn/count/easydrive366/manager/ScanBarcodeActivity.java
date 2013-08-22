package cn.count.easydrive366.manager;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.count.easydrive366.R;

import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;

public class ScanBarcodeActivity extends BaseHttpActivity {
	private Button button;
	private EditText editText;
	private ImageView imageView;
	private BitMatrix matrix;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modules_manager_scanbarcode_activity);
		button = (Button) findViewById(R.id.button);
		imageView = (ImageView) findViewById(R.id.code_img);
		editText = (EditText) findViewById(R.id.code_edit);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openCamera();
			}
		});
		findViewById(R.id.button_encode).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String str = editText.getText().toString();
				Bitmap bitmap = null;
				try {
					if (str != null && !"".equals(str)) {
						bitmap = CreateCode(str);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		});
	}

	private void openCamera() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.initiateScan();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode,
				resultCode, intent);
		if (result != null) {
			String contents = result.getContents();
			if (contents != null) {
				showDialog(R.string.result_succeeded, result.toString());
				editText.setText(contents);
			} else {
				showDialog(R.string.result_failed,
						getString(R.string.result_failed_why));
			}
		}
	}

	private void showDialog(int title, CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.show();
	}

	private Bitmap CreateCode(String content) throws WriterException,
			UnsupportedEncodingException {

		QRCodeWriter writer = new QRCodeWriter();

		//
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

		matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,
				300, 300, hints);

		int width = matrix.getWidth();
		int hight = matrix.getHeight();

		int[] pixels = new int[width * hight];
		for (int y = 0; y < hight; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		Bitmap map = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
		map.setPixels(pixels, 0, width, 0, 0, width, hight);

		return map;

	}
}
