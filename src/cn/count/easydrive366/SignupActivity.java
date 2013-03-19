package cn.count.easydrive366;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;

public class SignupActivity extends BaseHttpActivity {
	private Button btnLogin;
	private Button btnSignup;
	private EditText edtUsername;
	private EditText edtPassword;
	private EditText edtRePassword;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_signup_activity);
		btnLogin = (Button)findViewById(R.id.btn_signup_login);
		btnSignup =(Button)findViewById(R.id.btn_signup_signup);
		edtUsername = (EditText)findViewById(R.id.edt_signup_username);
		edtPassword = (EditText)findViewById(R.id.edt_signup_passwrod);
		edtRePassword = (EditText)findViewById(R.id.edt_signup_repasswrod);
		btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				login();
				
			}});
		btnSignup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				signup();
				
			}});
	}
	private void login(){
		Intent intent = new Intent(this,SignupActivity.class);
		startActivity(intent);
		this.finish();
		
		
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		super.processMessage(msgType, result);
		if (this.isSuccess(result)){
			AppSettings.login((JSONObject) result);
			Bundle bundle =new Bundle();
			bundle.putString("result",result.toString());
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK,intent);
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
	private void signup(){
		String username= edtUsername.getText().toString();
		String password = edtPassword.getText().toString();
		String repassword = edtRePassword.getText().toString();
		if (username.equals("")){
			this.showMessage("username can not be empty", null);
			return;
		}
		if (password.equals("")){
			this.showMessage("password can not be empty", null);
			return;
		}
		if (!password.equals(repassword)){
			this.showMessage("password not match", null);
			return;
		}
		String url =String.format("api/signup?username=%s&password=%s",username,password);
		this.get(url, 1);
		
	}

}
