package cn.count.easydrive366.signup;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;

public class Step1Activity extends BaseHttpActivity {
	private Button btnNext;
	
	private EditText edtCar_no;
	private EditText edtId_no;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		this._isHideTitleBar = true;
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modules_step1_activity);
		this.setupLeftButton();
		this.setRightButtonInVisible();
		this.setupPhoneButtonInVisible();
		btnNext =(Button)findViewById(R.id.btn_ok);
		edtCar_no = (EditText)findViewById(R.id.edt_car_no);
		edtId_no = (EditText)findViewById(R.id.edt_id_no);
		edtCar_no.setText("鲁");
		btnNext.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				nextStep();
				
			}});
	}
	
	@Override
	public void processMessage(int msgType, final Object result) {
		super.processMessage(msgType, result);
		if (this.isSuccess(result)){
			try{
				Intent intent =new Intent(this,Step2Activity.class);
				JSONObject json = (JSONObject)result;
				AppSettings.userid = json.getJSONObject("result").getInt("userid");
				intent.putExtra("vin", json.getJSONObject("result").getString("vin"));
				intent.putExtra("engine_no", json.getJSONObject("result").getString("engine_no"));
				intent.putExtra("registration_date", json.getJSONObject("result").getString("registration_date"));
				startActivity(intent);
				finish();
			}catch(Exception e){
				log(e);
			}
			
			
		}else{
			String message;
			try {
				message = ((JSONObject)result).getString("message");
				this.showMessage(message, null);
			} catch (JSONException e) {
			
				log(e);
			}
			
		}
	}
	private void nextStep(){
		String car_no= edtCar_no.getText().toString();
		String id_no = edtId_no.getText().toString();
		
		if (car_no.equals("")){
			this.showMessage("请输入车牌号码！", null);
			return;
		}
		if (!id_no.equals("") ){
			
			if ( id_no.length()!=18 || !personIdValidation(id_no)){
				this.showMessage(this.getResources().getString(R.string.id_is_wrong), null);
				return;
			}
		}
		
		String url =String.format("api/wizardstep1?userid=%d&car_id=%s&license_id=%s",AppSettings.userid,car_no.toUpperCase(),id_no);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtCar_no.getWindowToken(), 0);
		this.get(url, 1);
		
	}
}
