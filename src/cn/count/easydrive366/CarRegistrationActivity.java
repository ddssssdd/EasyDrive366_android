package cn.count.easydrive366;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;

public class CarRegistrationActivity extends BaseListViewActivity {
	private JSONObject _result;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_carregistration_activity);
		restoreFromLocal(1);
		this.get(AppSettings.url_get_car_registration(), 1);
	}
	@Override
	protected void initData(Object result, int msgType) {
		try{
			if (msgType==1){
				_result= (((JSONObject)result).getJSONObject("result")).getJSONObject("data");
			}
		}catch(Exception e){
			this.log(e);
		}

	}

	@Override
	protected void initView() {
		try{
			
			((TextView)findViewById(R.id.txt_carregistration_brand)).setText(_result.getString("brand"));
			((TextView)findViewById(R.id.txt_carregistration_car_type  )).setText(_result.getString("car_type"));
			((TextView)findViewById(R.id.txt_carregistration_car_typename  )).setText(_result.getString("car_typename"));
			((TextView)findViewById(R.id.txt_carregistration_engine_no  )).setText(_result.getString("engine_no"));
			((TextView)findViewById(R.id.txt_carregistration_issue_date  )).setText(_result.getString("issue_date"));
			((TextView)findViewById(R.id.txt_carregistration_model  )).setText(_result.getString("model"));
			((TextView)findViewById(R.id.txt_carregistration_plate_no  )).setText(_result.getString("plate_no"));
			((TextView)findViewById(R.id.txt_carregistration_registration_date  )).setText(_result.getString("registration_date"));
			((TextView)findViewById(R.id.txt_carregistration_untreated_fine  )).setText(_result.getString("untreated_fine"));
			((TextView)findViewById(R.id.txt_carregistration_untreated_mark  )).setText(_result.getString("untreated_mark"));
			((TextView)findViewById(R.id.txt_carregistration_untreated_number  )).setText(_result.getString("untreated_number"));
			((TextView)findViewById(R.id.txt_carregistration_vin  )).setText(_result.getString("vin"));
			this.setupRightButton();
			/*
			((Button)findViewById(R.id.btn_carregistration_edit)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CarRegistrationActivity.this,CarRegistrationEditActivity.class);
					intent.putExtra("data", _result.toString());
					startActivityForResult(intent,0);
					
				}});
				*/
			((Button)findViewById(R.id.btn_carregistration_search)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(CarRegistrationActivity.this,CarRegistrationEditActivity.class);
					intent.putExtra("data", _result.toString());
					startActivityForResult(intent,0);
					
				}});
		}catch(Exception e){
			log(e);
		}
		

	}
	@Override
	protected void onRightButtonPress(){
		Intent intent = new Intent(CarRegistrationActivity.this,CarRegistrationEditActivity.class);
		intent.putExtra("data", _result.toString());
		startActivityForResult(intent,0);
	}
	@Override
	protected void setupListItem(ViewHolder holder, Map<String, Object> info) {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==0 && resultCode==RESULT_OK){
			Bundle extras = data.getExtras();
			try {
				this.processMessage(1, new JSONObject(extras.getString("result")));
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
	}
}
