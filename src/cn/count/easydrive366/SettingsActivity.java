package cn.count.easydrive366;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;

public class SettingsActivity extends BaseHttpActivity {
	private EditText password1;
	private EditText password2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modules_settings_activity);
		this.setupLeftButton();
		this.setupRightButton();
		this.setupPhoneButtonInVisible();
		this.init_view();
		
	}
	private void init_view(){
		password1 = (EditText)findViewById(R.id.edt_password);
		password2 = (EditText)findViewById(R.id.edt_repassword);
		findViewById(R.id.btn_change_password).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				changePassword();
				
			}});
		findViewById(R.id.row_choose1).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setup_maintain();
				
			}});
		findViewById(R.id.row_choose2).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setup_driver();
				
			}});
		findViewById(R.id.row_choose3).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setup_car_registration();
				
			}});
		findViewById(R.id.btn_logout).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				logout();
				
			}});
		
		
	}
	private void changePassword(){
		this.showDialog("change password, i am waiting interface");
		
	}
	private void setup_maintain(){
		Intent intent =new Intent(this,MaintainEditActivity.class);
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
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void setup_driver(){
		Intent intent = new Intent(this,DriverLicenseEditActivity.class);
		JSONObject result = new JSONObject();
		try{
			
			result.put("name", "");
			result.put("init_date", "1990-01-01");
			result.put("number", "");
			result.put("car_type", "C1");
			
		}catch(Exception e){
			
		}
		result = this.loadWithKey("driver_license", result);
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void setup_car_registration(){
		Intent intent = new Intent(this,CarRegistrationEditActivity.class);
		JSONObject result = new JSONObject();
		try{
			
			result.put("engine_no", "");
			result.put("vin", "");
			result.put("registration_date", "1990-01-01");
			result.put("plate_no", this.getResources().getString(R.string.default_plate_no));
			
		}catch(Exception e){
			
		}
		result = this.loadWithKey("car_registration", result);
		intent.putExtra("data", result.toString());
		startActivityForResult(intent,0);
	}
	private void logout(){
		AppSettings.logout(this);
		Intent intent = new Intent(this,WelcomeActivity.class);
		startActivity(intent);
	}
}
