package cn.count.easydriver366.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class BaseHttpActivity extends Activity implements HttpClient.IHttpCallback {
	private static String BaseHttpClientTAG="BaseHttpActivity";
	private HttpClient httpClient;
	protected HttpClient getHttpClient(){
		if (httpClient==null){
			httpClient =new HttpClient(this);
		}
		return httpClient;
	}
	public void get(String actionAndParameters,final int returnType){
		this.getHttpClient().requestServer(actionAndParameters, returnType);
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		
		Log.e(BaseHttpClientTAG, result.toString());
	}
	public void restoreFromLocal(final int msgType){
		String result = this.getOfflineResult(msgType);
		if (result!=null && !result.isEmpty()){
			try {
				this.processMessage(msgType, new JSONObject(result));
			} catch (JSONException e) {
				log(e);
			}
		}
	}
	public String getOfflineResult(final int msgType){
		SharedPreferences prefs =this.getPreferences(MODE_PRIVATE);
		String result = prefs.getString(getKey(msgType), "");
		return result;
	}
	private String getKey(final int msgType){
		return String.format("json_%d_by_userid_%d",msgType,AppSettings.userid);
	}
	@Override
	public void recordResult(final int msgType,final Object result){
		if (this.isSuccess(result)){
			SharedPreferences prefs =this.getPreferences(MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString(getKey(msgType), result.toString());
			editor.commit();
		}
		
		
	}
	public boolean isSuccess(final Object jsonobj){
		return AppTools.isSuccess(jsonobj);
	}
	protected void log(Exception e){
		Log.e(BaseHttpClientTAG, e.getMessage());
	}
	protected void showMessage(final String info,final DialogInterface.OnClickListener okListener){
		final Context context = this;
		this.runOnUiThread(new Runnable(){

			@Override
			public void run() {
				new AlertDialog.Builder(context)
				.setTitle("Warnning")
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(info)
				.setPositiveButton("OK",okListener)
				.setNegativeButton("Cancel", null).show();
			}});
		
	}
	protected void showDialog(String str) {
		 new AlertDialog.Builder(this)
	         .setMessage(str)
	         .show();
	    }
	    
}
