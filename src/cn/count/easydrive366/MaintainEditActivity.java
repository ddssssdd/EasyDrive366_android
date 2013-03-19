package cn.count.easydrive366;


import org.json.JSONException;
import org.json.JSONObject;


import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.HttpClient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.EditText;

public class MaintainEditActivity extends Activity implements HttpClient.IHttpCallback{
	private JSONObject _result;
	private String _average_mileage;
	private String _pre_distance;
	private String _prev_date;
	private String _max_distance;
	private String _max_time;
	private HttpClient _http;
/*	private String _current_date;
	private String _current_miles;
	private String _current_distance;
*/
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_edit_maintain_activity);
		_http = new HttpClient(this);
		Intent intent = this.getIntent();
		try {
			_result = new JSONObject(intent.getStringExtra("data"));
			initView();
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
	}
	private void initView(){
		try{
			
			_average_mileage = _result.getString("average_mileage");
			_pre_distance = _result.getString("prev_distance");
			_prev_date = _result.getString("prev_date");
			_max_distance = _result.getString("max_distance");
			_max_time = _result.getString("max_time");
			/*
			_current_date = _result.getString("current_date");
			_current_miles = _result.getString("current_miles");
			_current_distance = _result.getString("current_distance");
			*/
			((EditText)findViewById(R.id.edt_maintain_average_mileage)).setText(_average_mileage);
			((EditText)findViewById(R.id.edt_maintain_pre_distance)).setText(_pre_distance);
			((EditText)findViewById(R.id.edt_maintain_pre_date)).setText(_prev_date);
			((EditText)findViewById(R.id.edt_maintain_max_distance)).setText(_max_distance);
			((EditText)findViewById(R.id.edt_maintain_max_time)).setText(_max_time);
			/*
			((EditText)findViewById(R.id.edt_maintain_current_date)).setText(_current_date);
			((EditText)findViewById(R.id.edt_maintain_current_miles)).setText(_current_miles);
			((EditText)findViewById(R.id.edt_maintain_current_distance)).setText(_current_distance);
			*/
			((Button)findViewById(R.id.btn_modules_edit_maintain_save)).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					save();
					
				}});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void save(){
		String url = String.format("api/add_maintain_record?user_id=%d&max_distance=%s&max_time=%s&prev_date=%s&prev_distance=%s&average_mileage=%s",
				AppSettings.userid,
				((EditText)findViewById(R.id.edt_maintain_max_distance)).getText(),
				((EditText)findViewById(R.id.edt_maintain_max_time)).getText(),
				((EditText)findViewById(R.id.edt_maintain_pre_date)).getText(),
				((EditText)findViewById(R.id.edt_maintain_pre_distance)).getText(),
				((EditText)findViewById(R.id.edt_maintain_average_mileage)).getText()
				);
		_http.requestServer(url, 1);
		
	}
	@Override
	public void processMessage(int msgType, Object result) {
		Log.d("Http",result.toString());
		
		Bundle bundle =new Bundle();
		bundle.putString("result",result.toString());
		Intent intent = new Intent();
		intent.putExtras(bundle);
		setResult(RESULT_OK,intent);
		finish();
		
	}
	@Override
	public void recordResult(int msgType, Object result) {
		// TODO Auto-generated method stub
		
	}
}
