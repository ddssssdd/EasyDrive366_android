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
		this._isHideTitleBar = false;
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modules_signup_activity);
		
		btnNext =(Button)findViewById(R.id.btn_ok);
		edtCar_no = (EditText)findViewById(R.id.edt_car_no);
		edtId_no = (EditText)findViewById(R.id.edt_id_no);
		
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
			/*
			Bundle bundle =new Bundle();
			bundle.putString("result",result.toString());
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
			*/
			finish();
			
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
			this.showMessage(getResources().getString(R.string.username_not_empty), null);
			return;
		}
		if (id_no.equals("") ){
			this.showMessage(getResources().getString(R.string.password_not_empty), null);
			return;
		}
		
		String url =String.format("api/initstep1?userid=-1&car_id=%s&license_id=%s",car_no,id_no);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtCar_no.getWindowToken(), 0);
		this.get(url, 1);
		
	}
}
