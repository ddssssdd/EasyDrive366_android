package cn.count.easydrive366;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;

public class UserFeedbackActivity extends BaseHttpActivity {
	private EditText edtContent;
	private EditText edtContact;
	private TextView txtLeft;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_userfeedback_activity);
		this.setRightButtonInVisible();
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		
		edtContent = (EditText)findViewById(R.id.edt_content);
		edtContact = (EditText)findViewById(R.id.edt_contact);
		Intent intent = this.getIntent();
		int isbind = intent.getIntExtra("isbind", -1);
		if (isbind==0){
			String phone = intent.getStringExtra("phone");
			edtContact.setText(phone);
		}
		txtLeft = (TextView)findViewById(R.id.txt_left);
		txtLeft.setText(String.format("还可以输入%d个字.",200-edtContent.getText().toString().length()));
		edtContent.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				txtLeft.setText(String.format("还可以输入%d个字.",200-arg0.toString().length()));
				
				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}});
		findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				send_userfeedback();
				
			}
		});
	}
	private void send_userfeedback(){
		if (edtContent.getText().toString().trim().length()==0){
			this.showMessage("请输入用户反馈信息", null);
			return;
		}
		if (edtContact.getText().toString().trim().length()==0){
			this.showMessage("请输入联系方式", null);
			return;
		}
		String url =String.format("api/add_feeback?userid=%d&communication=%s&content=%s", AppSettings.userid,
				edtContact.getText().toString(),edtContent.getText().toString());
		this.get(url, 1);
	}
	@Override
	public void processMessage(int msgType, final Object result) {

		if (AppTools.isSuccess(result)){
			this.showMessage("您的反馈信息已经提交。", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
					
				}
			});
		}
		
	}
}
