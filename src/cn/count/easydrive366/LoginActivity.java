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

public class LoginActivity extends BaseHttpActivity {
	private Button btnLogin;
	private Button btnSignup;
	private EditText edtUsername;
	private EditText edtPassword;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_login_activity);
		btnLogin = (Button)findViewById(R.id.btn_login_login);
		btnSignup =(Button)findViewById(R.id.btn_login_Signup);
		edtUsername = (EditText)findViewById(R.id.edt_login_username);
		edtPassword = (EditText)findViewById(R.id.edt_login_passwrod);
		edtUsername.setText("aaa");
		edtPassword.setText("12");
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
		String username= edtUsername.getText().toString();
		String password = edtPassword.getText().toString();
		if (username.equals("")){
			this.showMessage("username can not be empty", null);
			return;
		}
		if (password.equals("")){
			this.showMessage("password can not be empty", null);
			return;
		}
		String url =String.format("api/login?username=%s&password=%s",username,password);
		this.get(url, 1);
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
				// TODO Auto-generated catch block
				log(e);
			}
			
		}
	}
	private void signup(){
		Intent intent = new Intent(this,SignupActivity.class);
		startActivity(intent);
		this.finish();
		
	}

}
