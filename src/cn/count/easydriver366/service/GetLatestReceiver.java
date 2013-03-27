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
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class GetLatestReceiver extends BroadcastReceiver implements HttpClient.IHttpCallback {
	private static final int NOTIFICATION_ID = 0;
	private Context context;

	@Override
	public void onReceive(Context arg0, Intent arg1) {

		//Toast.makeText(arg0, "Check the latest information", Toast.LENGTH_LONG).show();
		this.context = arg0;
		
		run();
	}

	

	private void deleteNotification() {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
	}
	/*
	private void showNotification(final String title,final String content){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(content);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, WelcomeActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(WelcomeActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());
	}
	*/
	private void showNotification(final String title,final String content) {
		Notification notification = new Notification(R.drawable.ic_launcher,
				title, System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, WelcomeActivity.class), 0);
		notification.setLatestEventInfo(context, title, content,
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
				this.showNotification(json.getJSONObject("result").getString("company"), json.getJSONObject("result").getString("latest"));
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
