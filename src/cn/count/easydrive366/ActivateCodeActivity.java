package cn.count.easydrive366;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;

public class ActivateCodeActivity extends BaseHttpActivity {
	
	private EditText edtCode;
	private String _number;
	private String _code;
	private String _activate_date;
	private String _valid_date;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_activatecode_activity);
		this.setRightButtonInVisible();
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		edtCode = (EditText)findViewById(R.id.edt_code);
		
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activate_code();
				
			}
		});
	}
	private void activate_code(){
		
		if (edtCode.getText().toString().trim().length()==0){
			this.showMessage("请输入激活码！", null);
			return;
		}
		String url =String.format("api/add_activate_code?userid=%d&code=%s", AppSettings.userid,
				edtCode.getText().toString());
		this.get(url, 1);
	}
	private void show_activate_code(){
		Intent intent = new Intent(this,ActivateCodeShowActivity.class);
		intent.putExtra("number", _number);
		intent.putExtra("code", _code);
		intent.putExtra("activate_date",_activate_date);
		intent.putExtra("valid_date",_valid_date);
		startActivity(intent);
	}
	@Override
	public void processMessage(int msgType, final Object result) {

		if (AppTools.isSuccess(result)){
			try{
				JSONObject json = (JSONObject)result;
				_number = json.getJSONObject("result").getString("number");
				_code = json.getJSONObject("result").getString("code");
				_activate_date = json.getJSONObject("result").getString("activate_date");
				_valid_date = json.getJSONObject("result").getString("valid_date");
				
			}catch(Exception e){
				log(e);
			}
			
			this.showMessage("您的账户已经激活。", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					show_activate_code();
					
				}
			});
		}else{
			try{
				String message = ((JSONObject)result).getString("message");
				this.showMessage(message,null);
			}catch(Exception e){
				log(e);
			}
		}
		
	}
}
