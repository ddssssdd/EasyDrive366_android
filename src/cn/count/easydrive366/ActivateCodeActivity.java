package cn.count.easydrive366;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;

public class ActivateCodeActivity extends BaseHttpActivity {
	
	private EditText edtCode;
	
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
	@Override
	public void processMessage(int msgType, final Object result) {

		if (AppTools.isSuccess(result)){
			this.showMessage("您的账户已经激活。", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					
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
