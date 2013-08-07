package cn.count.easydrive366;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.CheckUpdate;

public class SettingsActivity extends BaseHttpActivity {
	
	private Button logoutButton;
	private TextView txtVersion;
	private TextView txtBind;
	private TextView txtCellphone;
	private TextView txtActivate_code;
	private boolean _isActivate=false;
	private int _isbind =1;
	private String _cellphone;
	private int BINDCELLPHONE = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modules_settings_activity);
		this.setupLeftButton();
		this.setRightButtonInVisible();
		this.setupPhoneButtonInVisible();
		this.init_view();
		this.setBarTitle(this.getResources().getString(R.string.menu_settings));
		
	}
	private void init_view(){
		
		findViewById(R.id.row_choose4).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				changePassword();
				
			}});
		
		
		
		findViewById(R.id.row_choose1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
//				setup_maintain();
				get(AppSettings.url_for_get_maintain_record(), 2);
				
			}});
		findViewById(R.id.row_choose2).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
//				setup_driver();
				get(AppSettings.url_get_driver_license(),3);
				
			}});
		findViewById(R.id.row_choose3).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
//				setup_car_registration();
				get(AppSettings.url_get_car_registration(),4);
				
			}});
		
		findViewById(R.id.row_choose_cellphone).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//start activity bindcellphone
				Intent intent = new Intent(SettingsActivity.this,BindCellphoneActivity.class);
				intent.putExtra("phone", _cellphone);
				intent.putExtra("isbind", _isbind);
				startActivityForResult(intent,BINDCELLPHONE);
				
			}});
		
		findViewById(R.id.row_choose_check_version).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				new CheckUpdate(SettingsActivity.this,true);
				
			}});
		findViewById(R.id.row_choose_findpassword).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				reset_password();
				
			}});
		findViewById(R.id.row_choose_user_feedback).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				user_feedback();
				
			}});
		findViewById(R.id.row_activate_code).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				check_activate_code();
				
			}});
		txtBind = (TextView)findViewById(R.id.txt_bindCellphone);
		txtVersion = (TextView)findViewById(R.id.txt_version);
		txtCellphone = (TextView)findViewById(R.id.img_choose_cellphone);
		txtActivate_code = (TextView)findViewById(R.id.txt_activate_code);
		txtVersion.setText(String.format("V%s >", AppSettings.version));
		
		this.logoutButton = (Button)findViewById(R.id.btn_logout);
		logoutButton.setText(String.format("注销-%s", AppSettings.username));
		findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				logout();
				
			}});
		
		this.get(AppSettings.url_get_user_phone(), 1);
		this.get(AppSettings.url_get_activate_code(), 11,"");
	}
	private void changePassword(){
		Intent intent = new Intent(this,PasswordResetActivity.class);
		startActivity(intent);
	}
	private void find_password(){
		this.get(String.format("api/sms_reset_pwd?userid=%d", AppSettings.userid), 5);
	}
	@Override
	public void processMessage(int msgType, final Object result) {

		if (msgType==1){
			if (AppTools.isSuccess(result)){
				try{
					JSONObject json = (JSONObject)result;
					_cellphone= json.getJSONObject("result").getString("phone");
					if (json.getJSONObject("result").getString("status").equals("02")){
						this.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								txtBind.setText(getString(R.string.unbind));
								txtCellphone.setText(String.format("%s >", _cellphone));
							}});
						
						_isbind = 0;
					}
					
				}catch(Exception e){
					log(e);
				}
			}
			
		}else if (msgType==11){
			if (AppTools.isSuccess(result)){
				try{
					this.runOnUiThread(new Runnable(){

						@Override
						public void run() {
							txtActivate_code.setText(getResources().getString(R.string.has_activate_code));
						}});
					
					this._isActivate = true;
					
				}catch(Exception e){
					log(e);
				}
			}
			
			
		}else if (msgType==2){
			//maintain
			try{
				JSONObject json = (JSONObject)result;
				this.setup_maintain(json.getJSONObject("result").getJSONObject("data"));
			}catch(Exception e){
				log(e);
			}
			
		}else if (msgType==3){
			try{
				JSONObject json = (JSONObject)result;
				this.setup_driver(json.getJSONObject("result").getJSONObject("data"));
			}catch(Exception e){
				log(e);
			}
		}else if (msgType==4){
			try{
				JSONObject json = (JSONObject)result;
				this.setup_car_registration(json.getJSONObject("result").getJSONObject("data"));
			}catch(Exception e){
				log(e);
			}
		}else if (msgType==5){
			try{
				if (AppTools.isSuccess(result)){
					this.showMessage(((JSONObject)result).getString("result"), null);
					changePassword();
				}
			}catch(Exception e){
				log(e);
			}
		}
		
	}
	private void check_activate_code()
	{
		Intent intent;
		if (this._isActivate){
			intent = new Intent(this,ActivateCodeShowActivity.class);
		}else{
			intent = new Intent(this,ActivateCodeActivity.class);
		}
		startActivity(intent);
	}
	private void setup_maintain(final JSONObject result){
		Intent intent =new Intent(this,MaintainEditActivity.class);
		/*
		JSONObject result = new JSONObject();
		try{
			
			result.put("average_mileage", "30");
			result.put("prev_distance", "0");
			result.put("prev_date", "");
			result.put("max_distance", "5000");
			result.put("max_time", "6");
		}catch(Exception e){
			
		}
		result = this.loadWithKey("maintain", result);
		*/
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void setup_driver(final JSONObject result){
		Intent intent = new Intent(this,DriverLicenseEditActivity.class);
		/*
		JSONObject result = new JSONObject();
		try{
			
			result.put("name", "");
			result.put("init_date", "1990-01-01");
			result.put("number", "");
			result.put("car_type", "C1");
			
		}catch(Exception e){
			
		}
		result = this.loadWithKey("driver_license", result);
		*/
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void setup_car_registration(final JSONObject result){
		Intent intent = new Intent(this,CarRegistrationEditActivity.class);
		/*
		JSONObject result = new JSONObject();
		try{
			
			result.put("engine_no", "");
			result.put("vin", "");
			result.put("registration_date", "1990-01-01");
			result.put("plate_no", this.getResources().getString(R.string.default_plate_no));
			
		}catch(Exception e){
			
		}
		result = this.loadWithKey("car_registration", result);
		*/
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void logout(){
		
		AppSettings.logout(this);
		/*
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		*/
		setResult(-100,null);
		this.finish();
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==BINDCELLPHONE && resultCode==RESULT_OK){
			this.get(AppSettings.url_get_user_phone(), 1);
			/*
			Bundle extras = data.getExtras();
			this._isbind = extras.getInt("isbind");
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					txtBind.setText(getString(_isbind==0?R.string.unbind:R.string.bind));
					
				}});
				*/
		}
	}
	private void reset_password(){
		if (this._isbind==1){
			this.confirm("找回密码操作需要绑定手机，请先绑定手机。", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(SettingsActivity.this,BindCellphoneActivity.class);
					intent.putExtra("phone", _cellphone);
					intent.putExtra("isbind", _isbind);
					startActivityForResult(intent,BINDCELLPHONE);
					
				}
			});
		}else{
			this.confirm("找回密码操作将向您绑定手机发送随机初始密码（短信免费），请确认要找回密码？", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					find_password();
					
				}
			});
		}
	}
	private void user_feedback(){
		Intent intent = new Intent(SettingsActivity.this,UserFeedbackActivity.class);
		intent.putExtra("phone", _cellphone);
		intent.putExtra("isbind", _isbind);
		startActivity(intent);
	}
}
