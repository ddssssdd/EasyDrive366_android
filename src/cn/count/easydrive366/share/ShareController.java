package cn.count.easydrive366.share;



import org.json.JSONObject;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ShareController {
	private Context _context;
	private String _title;
	private String _description;
	private String _url;
	private IWXAPI api;
	public ShareController(Context context){
		_context = context;
		api = WXAPIFactory.createWXAPI(_context, AppSettings.WEIXIN_ID);
    	api.registerApp(AppSettings.WEIXIN_ID);
	}
	public void setContent(final String title,final String description,final String url){
		_title = title;
		_description = description;
		_url = url;
	}
	public void setContent(final JSONObject json){
		try{
			setContent(json.getString("share_title"), json.getString("share_intro"),json.getString("share_url"));
		}catch(Exception e){
			Log.e("Parse JSON Error", e.getMessage());
		}
	}
	public void share(final int menuId){
		switch(menuId){
		case R.id.action_share_weixin:
			share_weixin(false);
			break;
		case R.id.action_share_friends:
			share_weixin(true);
		case R.id.action_share_weibo:
			share_weibo();
			break;
		case R.id.action_share_text:
			share_text();
			break;
		case R.id.action_share_email:
			share_email();
			break;
		}
	}
	public void share(final int menuId,final String title,final String description,final String url){
		setContent(title,description,url);
		share(menuId);
		
	}
	public void share(final int menuId,final JSONObject json){
		setContent(json);
		share(menuId);
	}
	private void share_weixin(boolean isFriends){
		/*
		WXTextObject text = new WXTextObject();
		text.text="Share ...";
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = text;
		msg.description = "Share something";
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene =isFriends?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;//  SendMessageToWX.Req.WXSceneTimeline ;//SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
		*/
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = _url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = _title;
		msg.description = _description;
		Bitmap thumb = BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "webpage" +String.valueOf( System.currentTimeMillis());
		req.message = msg;
		req.scene = isFriends ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
	
	private void share_weibo(){
		
	}
	private void share_email(){
		
	}
	private void share_text(){
		Intent smsIntent = new Intent(Intent.ACTION_SEND);
		smsIntent.putExtra("sms_body", String.format("%s - %s(%s)",_title,_description,_url));
		_context.startActivity(smsIntent);
				
	}
}
