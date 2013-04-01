package cn.count.easydriver366.base;

import org.json.JSONException;
import org.json.JSONObject;
import cn.count.easydrive366.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BaseHttpActivity extends Activity implements
		HttpClient.IHttpCallback {
	private static String BaseHttpClientTAG = "BaseHttpActivity";
	protected HttpClient httpClient;
	// protected ImageButton _rightButton;
	protected Button _rightButton;
	protected String _company;
	protected String _phone;
	protected boolean _isHideTitleBar = true;
	protected ProgressDialog _dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (_isHideTitleBar){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

	}

	protected HttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = new HttpClient(this);
		}
		return httpClient;
	}
	public void get(String actionAndParameters, final int returnType) {
		
		this.get(actionAndParameters, returnType,this.getResources().getString(R.string.app_loading));
	}

	public void get(String actionAndParameters, final int returnType,final String hint) {
		if (!this.isOnline()){
			this.restoreFromLocal(returnType);
			return;
		}
		if (!hint.equals("")){
			_dialog = new ProgressDialog(this);
			_dialog.setMessage(hint);
			_dialog.show();
		}
		
		this.getHttpClient().requestServer(actionAndParameters, returnType);
	}

	@Override
	public void processMessage(int msgType, final Object result) {

		Log.e(BaseHttpClientTAG, result.toString());
	}

	protected void setupCompanyAndPhone(final Object json) {
		final JSONObject result = (JSONObject) json;
		try {
			if (!result.getJSONObject("result").isNull("company")){
				_company = result.getJSONObject("result").getString("company");
				_phone = result.getJSONObject("result").getString("phone");
				this.setupPhoneButton();
			}
			
		} catch (Exception e) {
			log(e);
		}
	}

	public void restoreFromLocal(final int msgType) {
		if (this.isOnline()) {
			return;
		}
		String result = this.getOfflineResult(msgType);
		if (!result.equals("")) {
			try {
				this.setupCompanyAndPhone(new JSONObject(result));	
				this.processMessage(msgType, new JSONObject(result));

			} catch (JSONException e) {
				log(e);
			}
		}
	}

	public String getOfflineResult(final int msgType) {

		SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
		String result = prefs.getString(getKey(msgType), "");
		return result;

	}

	private String getKey(final int msgType) {
		return String.format("json_%d_by_userid_%d", msgType,
				AppSettings.userid);
	}

	@Override
	public void recordResult(final int msgType, final Object result) {
		if (this.isSuccess(result)) {
			SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(getKey(msgType), result.toString());
			editor.commit();
			this.setupCompanyAndPhone(result);
		}
		if (_dialog!=null){
			_dialog.dismiss();
			_dialog = null;
		}
	}

	public boolean isSuccess(final Object jsonobj) {
		return AppTools.isSuccess(jsonobj);
	}

	protected void log(Exception e) {
		Log.e(BaseHttpClientTAG, e.getMessage());
	}

	protected void showMessage(final String info,
		final DialogInterface.OnClickListener okListener) {
		final Context context = this;
		
		this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new AlertDialog.Builder(context).setTitle(getResources().getString(R.string.hint))
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setMessage(info).setPositiveButton(getResources().getString(R.string.ok), okListener).show();
						//.setNegativeButton(getResources().getString(R.string.cancel), null).show();
			}
		});

	}

	protected void showDialog(String str) {
		new AlertDialog.Builder(this).setMessage(str).show();
	}

	protected void onRightButtonPress() {

	}

	protected void setBarTitle(final String title) {
		TextView txt = (TextView) findViewById(R.id.txt_navigation_bar_title);
		if (txt != null) {
			txt.setText(title);
		}
	}

	protected void setRightButtonInVisible() {
		_rightButton = (Button) findViewById(R.id.title_set_bn);
		if (_rightButton != null) {
			_rightButton.setVisibility(View.GONE);
		}
	}

	protected void setupRightButton() {
		this.setupRightButtonWithText(getResources().getString(R.string.edit));
	}

	protected void setupRightButtonWithText(final String buttonText) {
		_rightButton = (Button) findViewById(R.id.title_set_bn);
		if (_rightButton != null) {
			_rightButton.setText(buttonText);
			_rightButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onRightButtonPress();

				}

			});
		}

	}

	protected void onLeftButtonPress() {
		finish();
	}

	protected void setupLeftButton() {
		View v = findViewById(R.id.img_navigationbar_logo);
		if (v != null) {
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onLeftButtonPress();

				}
			});
		}
	}
	protected void setupPhoneButtonInVisible(){
		View v = findViewById(R.id.btn_phone);
		if (v != null) {
			v.setVisibility(View.GONE);
		}
	}
	protected void setupPhoneButton() {
		final View v = findViewById(R.id.btn_phone);
		if (v != null) {
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					((Button)v).setText(String.format("拨号：%s", _phone));
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (_phone!=null && !_phone.equals("")){
								Uri uri =Uri.parse(String.format("tel:%s",_phone)); 
								
								Intent it = new Intent(Intent.ACTION_VIEW,uri); 
								startActivity(it); 
							}

						}
					});
					
					
				}
				
			});
			
		}
	}

	protected boolean isOnline() {
		return NetworkUtils.getNetworkState(this) > 0;
	}

	protected void makeCall(final String phone) {
		startActivity(AppTools.getPhoneAction(phone));
	}
}
