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
		txtBind = (TextView)findViewById(R.id.txt_bindCellphone);
		txtVersion = (TextView)findViewById(R.id.txt_version);
		txtVersion.setText(String.format("V%s >", AppSettings.version));
		
		this.logoutButton = (Button)findViewById(R.id.btn_logout);
		logoutButton.setText(String.format("注销-%s", AppSettings.username));
		findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				logout();
				
			}});
		
		this.get(AppSettings.url_get_user_phone(), 1);
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
					
					if (json.getJSONObject("result").getString("status").equals("02")){
						this.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								txtBind.setText(getString(R.string.unbind));
								
							}});
						
						_isbind = 0;
					}
					_cellphone= json.getJSONObject("result").getString("phone");
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
		Intent intent = new Intent(this,WelcomeActivity.class);
		startActivity(intent);
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
}
