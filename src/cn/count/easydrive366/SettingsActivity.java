package cn.count.easydrive366;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;

public class SettingsActivity extends BaseHttpActivity {
	
	private Button logoutButton;
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
		this.logoutButton = (Button)findViewById(R.id.btn_logout);
		logoutButton.setText(String.format("注销-%s", AppSettings.username));
		findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				logout();
				
			}});
		
		
	}
	private void changePassword(){
		Intent intent = new Intent(this,PasswordResetActivity.class);
		startActivity(intent);
		
		
	}
	@Override
	public void processMessage(int msgType, final Object result) {

		if (msgType==1){
			
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
}
