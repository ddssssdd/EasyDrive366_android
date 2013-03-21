package cn.count.easydriver366.service;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import cn.count.easydrive366.R;
import cn.count.easydrive366.WelcomeActivity;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.HttpClient;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class GetLatestReceiver extends BroadcastReceiver implements HttpClient.IHttpCallback {
	private static final int NOTIFICATION_ID = 0;
	private Context context;

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		Toast.makeText(arg0, "Check the latest information", Toast.LENGTH_LONG)
				.show();
		this.context = arg0;
		/*
		 * Log.d("TestReceiver","intent="+arg1); String message =
		 * arg1.getStringExtra("message"); Log.d("TestReceiver",message); if
		 * (message.equals("clear")){ this.deleteNotification(); }else{
		 * this.showNotification(); }
		 */
		run();
	}

	

	private void deleteNotification() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}

	private void showNotification() {
		Notification notification = new Notification(R.drawable.ic_launcher,
				"title", System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, WelcomeActivity.class), 0);
		notification.setLatestEventInfo(context, "title", "information",
				contentIntent);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	public void run() {
		if (!AppSettings.isLogin){
			return;
		}
		HttpClient client = new HttpClient(this);
		for(int i=1;i<=AppSettings.TotalPageCount;i++){
			final String url = String.format("api/get_latest?userid=%d&keyname=%02d",AppSettings.userid,i);
			client.sendHttp(AppSettings.ServerUrl, url, i);
		
		}
	}
	
	@Override
	public void processMessage(int msgType, Object result) {
		try{
			JSONObject json = (JSONObject)result;
			if (AppTools.isSuccess(json)){
				Intent intent = new Intent();
				intent.putExtra("key", String.format("%02d", msgType));
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				final String updated_time = sdf.format(new Date());
				json.getJSONObject("result").put("updated_time", updated_time);
				intent.putExtra("json",json.getJSONObject("result").toString());
				intent.setAction("cn.count.easydrive366.components.HomeMenuItem$LatestInformationReceiver");
				context.sendBroadcast(intent);
			}
		}catch(Exception e){
			AppTools.log(e);
		}
		
	}

	@Override
	public void recordResult(int msgType, Object result) {
		// TODO Auto-generated method stub
		
	}

}
