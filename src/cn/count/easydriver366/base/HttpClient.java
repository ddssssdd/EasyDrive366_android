package cn.count.easydriver366.base;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

public class HttpClient {
	private IHttpCallback mCallback;

	
	public HttpClient(IHttpCallback callback){
		mCallback = callback;
	}
	
	
	
	public void requestServer(final String urlParams,final int returnType){
		sendHttp(AppSettings.ServerUrl,urlParams,returnType);
	}
	public void sendHttp(final String serverUrl,final String urlParams,final int msgType){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				String url =serverUrl +urlParams+"&timestamp="+String.valueOf(System.currentTimeMillis());
				
			
				HttpGet httpGet = new HttpGet(url);
				DefaultHttpClient client = new DefaultHttpClient();
				try{
				
					HttpResponse response = client.execute(httpGet);
					if (response.getStatusLine().getStatusCode()==200){
						HttpEntity entity = response.getEntity();
						InputStream inputStream = entity.getContent();
						ByteArrayOutputStream content = new ByteArrayOutputStream();
						int readBytes =0;
						byte[] sBuffer = new byte[512];
						while ((readBytes=inputStream.read(sBuffer))!=-1){
							content.write(sBuffer, 0, readBytes);
						}
						String result = new String(content.toByteArray());
						
						processResponse(msgType,result);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					client.getConnectionManager().shutdown();
				}
				
			}
			
		});
		t.start();
		
	}
	
	public void postHttp(final String serverUrl,final String urlParams,final List<NameValuePair> params,final int msgType){
		Thread t = new Thread(new Runnable(){

			@Override
			public void run() {
				String url =serverUrl +urlParams;
				HttpPost http = new HttpPost(url);
				DefaultHttpClient client = new DefaultHttpClient();
				try{
					params.add(new BasicNameValuePair("timestamp",String.valueOf(System.currentTimeMillis())));
					http.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
					HttpResponse response = client.execute(http);
					if (response.getStatusLine().getStatusCode()==200){
						HttpEntity entity = response.getEntity();
						InputStream inputStream = entity.getContent();
						ByteArrayOutputStream content = new ByteArrayOutputStream();
						int readBytes =0;
						byte[] sBuffer = new byte[512];
						while ((readBytes=inputStream.read(sBuffer))!=-1){
							content.write(sBuffer, 0, readBytes);
						}
						String result = new String(content.toByteArray());
						processResponse(msgType,result);
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					client.getConnectionManager().shutdown();
				}
				
			}
			
		});
		t.start();
		
	}
	private void processResponse(final int msgType,final String result){
		try{
			if (mCallback!=null){
				Object resultObj=null;
				if (result.startsWith("[")){
					resultObj=new JSONArray(result);
				}else{
					resultObj = new JSONObject(result);
				}
				mCallback.processMessage(msgType,resultObj);
				mCallback.recordResult(msgType, resultObj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void dispose(){
		mCallback = null;
		
	}
	
	public interface IHttpCallback{
		void processMessage(final int msgType,final Object result);
		void recordResult(final int msgType,final Object result);
	}
}
