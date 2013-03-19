package cn.count.easydriver366.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class AppSettings {
//	static public String ServerUrl = "http://10.4.30.190:7000/pm/";
	static public String ServerUrl ="http://124.135.63.250:21000/pm/";
	static public String AppTile="cn.count.EasyDrive366";
	static public int userid=65;
	static public String username;
	static public boolean isLogin= false;
	static public JSONArray driver_type_list;
	static public String url_for_get_news()
	{
		
	    return String.format("api/get_news?userid=%d",userid);
	}
	static public String url_for_get_helpcalls()
	{
	    return String.format("api/get_helps?userid=%d",userid);
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
		return "api/get_compulsory_details?userid=0";
	}
	static public String url_get_taxforcarship(){
		return "api/get_taxforcarship?userid=0";
	}
	
	
	static public void login(JSONObject result) {
		try {
			userid = result.getJSONObject("result").getInt("id");
			username = result.getJSONObject("result").getString("username");
			isLogin = true;
		} catch (JSONException e) {
			AppTools.log(e);
		}
	}
	
	
}
