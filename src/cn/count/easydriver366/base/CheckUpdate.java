package cn.count.easydriver366.base;

import org.json.JSONObject;

import cn.count.easydrive366.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class CheckUpdate implements HttpClient.IHttpCallback {
	private Context _context;
	private HttpClient _http;
	private boolean _isSettings;
	
	public CheckUpdate(Context context,boolean isSettings){
		_context = context;
		_isSettings = isSettings;
		_http = new HttpClient(this);
		String url = String.format("api/get_ver?ver=%s", AppSettings.version);
		_http.requestServer(url, 1);
	}

	@Override
	public void processMessage(int msgType, Object result) {
		final Object json = result;
		if (AppTools.isSuccess(result)){
			Activity activity= (Activity)_context;
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					check(json);
					
			}});
		}
		
	}
	private void check(final Object result){
		try{
			JSONObject json = ((JSONObject)result).getJSONObject("result");
			if (!json.getString("ver").equals(AppSettings.version)){
				
				final String url = json.getString("android");
				new AlertDialog.Builder(_context).setTitle(_context.getResources().getString(R.string.hint))
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setMessage(json.getString("msg")).setPositiveButton(_context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = null;
						intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
						_context.startActivity(intent);
						
					}
				})
				.setNegativeButton(_context.getResources().getString(R.string.cancel), null).show();
			}else{
				if (_isSettings){
					new AlertDialog.Builder(_context).setMessage(json.getString("msg")).show();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void recordResult(int msgType, Object result) {
		// TODO Auto-generated method stub
		
	}

}
