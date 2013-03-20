package cn.count.easydriver366.base;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public final class AppTools {
	public static void log(Exception e){
		Log.e("Application Log", e.getMessage());
	}
	public static boolean isSuccess(final Object jsonobj){
		boolean result= false;
		if (jsonobj==null){
			return result;
		}
		if (jsonobj instanceof JSONObject){
			try{
				final String status = ((JSONObject)jsonobj).getString("status");
				result = status.equals("success");
			}catch(Exception e){
				log(e);
			}
			 
		}else if (jsonobj instanceof JSONArray){
			try{
				result =  ((JSONArray)jsonobj).length()>0;
			}catch(Exception e){
				log(e);
			}
		}
		return result;
	}
}
