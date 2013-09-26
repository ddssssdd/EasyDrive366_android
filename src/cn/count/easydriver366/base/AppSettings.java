package cn.count.easydriver366.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public final class AppSettings {
//	static public String ServerUrl = "http://119.167.144.23:82/index.php/";
	//static public final String ServerUrl ="http://124.135.63.250:21000/pm/";
//	static public String ServerUrl = "http://next.4006678888.com:21000/index.php/";
	static public String ServerUrl = "http://m.4006678888.com:21000/index.php/";
	static public final String AppTile="cn.count.EasyDrive366";
	static public String LatestNewsKey="LatestNews";
	static public int userid=-1;
	static public int TotalPageCount=12;
	static public String username;
	static public boolean isLogin= false;
	static public JSONArray driver_type_list;
	static public int update_time=4*60*60;
	static public boolean isquiting = false;
	static public String version = "1.05";
	//static public String BAIDUMAPKEY="30d50073a606ac3ce0b7f8a187e8248b";//for debug
	static public String BAIDUMAPKEY="939cb0c01d09f55edbf15645e42a1624";//for release
	
	static public String url_for_get_news()
	{
		
	    return String.format("api/get_news?userid=%d",userid);
	}
	static public String url_pull_msg(){
		return String.format("pushapi/pull_msg?userid=%d", userid);
	}
	static public String url_for_get_helpcalls()
	{
	    return String.format("api/get_helps?userid=%d",userid);
	}
	static public String url_for_get_carservice()
	{
	    return String.format("api/get_check_helps?userid=%d",userid);
	}
	
	static public String url_for_get_carservice_vendor(final String code)
	{
	    return String.format("api/get_help_service?userid=%d&code=%s",userid,code);
	}
	static public String url_for_get_carservice_note(final String code)
	{
	    return String.format("api/get_help_note?userid=%d&code=%s",userid,code);
	}
	
	static public String url_for_rescue(){
	    return String.format("api/get_rescues?userid=%d",userid);
	}

	static public String url_for_illegallys(){
	    return String.format("api/get_illegally_list?userid=%d",userid);
	}
	static public String url_for_insurance_list(){
	    return String.format("api/get_insurance_process_list?userid=%d",userid);
	}

	static public String url_for_maintain_list(){
	    return String.format("api/get_car_maintain_list?userid=%d",userid);
	}

	static public String url_for_post_maintain_record(){
	    return String.format("api/add_maintain_record?userid=%d",userid);
	}
	static public String url_for_get_maintain_record(){
	    return String.format("api/get_maintain_record?userid=%d",userid);
	}
	static public String url_getlatest(){
	    return String.format("api/get_latest?userid=%d",userid);
	}

	static public String url_get_driver_license(){
	    return String.format("api/get_driver_license?userid=%d",userid);
	}
	static public String url_get_car_registration(){
	    return String.format("api/get_car_registration?userid=%d",userid);
	}

	static public String url_get_suggestion_insurance(){
	    return String.format("api/get_suggestion_of_insurance?userid=%d",userid);
	    
	}
	static public String url_get_license_type(){
		return "api/get_license_type?userid=0";
	}
	
	static public String url_get_business_insurance(){
		return String.format("api/get_Policys?userid=%d", userid);
	}
	
	static public String url_get_suggestion_count(){
		return String.format("api/get_count_of_suggestion?userid=%d", userid);
	}
	static public String url_get_compulsory_details(){
		return String.format("api/get_compulsory_details?userid=%d",userid);
	}
	static public String url_get_taxforcarship(){
		return String.format("api/get_taxforcarship?userid=%d",userid);
	}
	static public String url_get_user_phone(){
		return String.format("api/get_user_phone?userid=%d", userid);
	}
	static public String url_get_activate_code(){
		return String.format("api/had_activate_code?userid=%d", userid);
	}
	static public String url_add_activate_code(String code){
		return String.format("api/add_activate_code?userid=%d&code=%s", userid,code);
	}
	static public String url_user_activate_code(){
		return String.format("api/get_activate_code?userid=%d", userid);
	}
	static public String get_activate_code_list(){
		return String.format("api/get_activate_code_list?userid=%d", userid);
	}
	static public String get_business(double latitude,double longitude,String typecode){
		return String.format("api/get_position?userid=%d&x=%f&y=%f&type=%s", userid,latitude,longitude,typecode);
	}
	static public void login(JSONObject result,Context context) {
		try {
			
			userid = result.getJSONObject("result").getInt("id");
			username = result.getJSONObject("result").getString("username");
			isLogin = true;
			int update_time = 4*60*60;
			if (!result.getJSONObject("result").isNull("update_time")){
				//Log.i("Update_time",result.getJSONObject("result").getString("update_time"));
				JSONObject json = result.getJSONObject("result");
				update_time = Integer.parseInt(json.getString("update_time"));
			}
			SharedPreferences prefs =context.getSharedPreferences(AppSettings.AppTile+"_login", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("userid", userid);
			editor.putBoolean("isLogin", isLogin);
			editor.putString("username", username);
			editor.putInt("update_time", update_time);
			editor.commit();
		} catch (JSONException e) {
			AppTools.log(e);
		}
	}
	static public  void restore_login_from_device(Context context){
		SharedPreferences prefs =context.getSharedPreferences(AppSettings.AppTile+"_login", Context.MODE_PRIVATE);
		userid = prefs.getInt("userid", -1);
		isLogin = prefs.getBoolean("isLogin", false);
		username = prefs.getString("username", "");
		update_time = prefs.getInt("update_time", 4*60*60);
	}
	static public void logout(Context context){
		AppSettings.isLogin = false;
		AppSettings.userid = -1;
		SharedPreferences prefs =context.getSharedPreferences(AppSettings.AppTile+"_login", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("userid", -1);
		editor.putBoolean("isLogin", false);
		editor.putString("username", "");
		editor.putInt("update_time", 4*60*60);
		editor.commit();
	}
	
	
	
}
