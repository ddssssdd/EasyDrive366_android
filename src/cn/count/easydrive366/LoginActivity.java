package cn.count.easydrive366;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;

public class LoginActivity extends BaseHttpActivity {
	private Button btnLogin;
	private Button btnSignup;
	private EditText edtUsername;
	private EditText edtPassword;
	private CheckBox chbRememberPassword;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		this._isHideTitleBar = false;
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modules_login_activity);
		btnLogin = (Button)findViewById(R.id.btn_login_login);
	
		edtUsername = (EditText)findViewById(R.id.edt_login_username);
		edtPassword = (EditText)findViewById(R.id.edt_login_passwrod);
		chbRememberPassword = (CheckBox)findViewById(R.id.chb_remember_password);
		SharedPreferences pref = this.getPreferences(MODE_PRIVATE);	
		
		edtUsername.setText(pref.getString("username", ""));
		chbRememberPassword.setChecked(pref.getBoolean("remember_password", false));
		if (chbRememberPassword.isChecked()){
			edtPassword.setText(pref.getString("password", ""));
		}
		btnLogin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				login();
				
			}});
		
	}
	private void saveLogin(){
		SharedPreferences pref = this.getPreferences(MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putString("username", edtUsername.getText().toString());
		editor.putString("password", edtPassword.getText().toString());
		editor.putBoolean("remember_password", chbRememberPassword.isChecked());
		try{
			JSONObject login = new JSONObject();
			login.put("username", edtUsername.getText().toString());
			login.put("password", edtPassword.getText().toString());
			login.put("remember_password", chbRememberPassword.isChecked());
			String logins = pref.getString("logins", "");
			JSONArray login_list;
			if (logins.equals("")){
				login_list = new JSONArray();
			}else{
				login_list = new JSONArray(logins);
			}
			int index = login_list.length();
			for(int i=0;i<index;i++){
				JSONObject item = login_list.getJSONObject(i);
				if (item.getString("username").trim().equals(edtUsername.getText().toString().trim())){
					index = i;
					break;
				}
			}
			login_list.put(index, login);
			editor.putString("logins", login_list.toString());
			
		}catch(Exception e){
			log(e);
		}
		editor.commit();
	}
	
	private void login(){
		String username= edtUsername.getText().toString();
		String password = edtPassword.getText().toString();
		if (username.equals("")){
			this.showMessage(getResources().getString(R.string.username_not_empty), null);
			return;
		}
		if (password.equals("") ){
			this.showMessage(getResources().getString(R.string.password_not_empty), null);
			return;
		}
		String url =String.format("api/login?username=%s&password=%s",username,password);
		this.get(url, 1);
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		super.processMessage(msgType, result);
		if (this.isSuccess(result)){
			AppSettings.login((JSONObject) result,this);
			saveLogin();
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
	

}
