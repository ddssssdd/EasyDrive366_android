package cn.count.easydrive366;

import org.json.JSONException;
import org.json.JSONObject;

import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;


public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		if (this.isLogin()){
			this.goHome();
		}else{
			findViewById(R.id.btn_welcome_login).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
					startActivityForResult(intent,1);
					
				}
				
			});
			findViewById(R.id.btn_welcome_signup).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent intent =new Intent(WelcomeActivity.this,SignupActivity.class);
					startActivityForResult(intent,2);
					
				}
				
			});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode==1 || requestCode==2) && resultCode==RESULT_OK){
			Bundle extras = data.getExtras();
			try {
				this.processMessage(requestCode, new JSONObject(extras.getString("result")));
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
	}
	private void processMessage(final int msgType,final JSONObject result){
		try{
			AppSettings.userid = result.getJSONObject("result").getInt("id");
			AppSettings.username= result.getJSONObject("result").getString("username");
			AppSettings.isLogin= true;
			this.saveUserLogin(AppSettings.userid,AppSettings.username);
			this.goHome();
		}catch(Exception e){
			AppTools.log(e);
		}
	}
	private void goHome(){
		Intent intent = new Intent(this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	private boolean isLogin(){
		SharedPreferences prefs =getSharedPreferences(AppSettings.AppTile, MODE_PRIVATE);
		boolean  result = prefs.getBoolean("islogin", false);
		if (result){
			AppSettings.userid = prefs.getInt("userid", 0);
			AppSettings.username= prefs.getString("username", "");
			AppSettings.isLogin = prefs.getBoolean("islogin", false);
			result = (AppSettings.userid>0) && (!AppSettings.username.equals("")) && AppSettings.isLogin;
		}
		return result;
	}
	private void saveUserLogin(final int userid,final String username){
		SharedPreferences prefs =getSharedPreferences(AppSettings.AppTile, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("username",username);
		editor.putInt("userid", userid);
		editor.putBoolean("islogin", true);
		editor.commit();
	}
	@Override
	public void onBackPressed() {
		//nothing;
	}
}

