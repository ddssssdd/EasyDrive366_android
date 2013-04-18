package cn.count.easydrive366.components;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.HomeMenu;
import cn.count.easydriver366.service.GetLatestReceiver;

public class HomeMenuItem extends LinearLayout {
	private LayoutInflater _inflater =null;
	private Context _context;
	private HomeMenu _menuItem;
	private TextView _title;
	private TextView _description;
	private String _company;
	private String _phone;
	public HomeMenuItem(Context context,AttributeSet attrs){
		super(context,attrs);
		
		_inflater = LayoutInflater.from(context);
		_context = context;
		//_inflater.inflate(R.layout.home_menu_item, this);
		_inflater.inflate(R.layout.home_menu_items_one_line, this);
		_title = (TextView)findViewById(R.id.listitem_title);
		_description =(TextView)findViewById(R.id.listitem_content);
		findViewById(R.id.listitem_pic).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				makeCall();
				
			}});
		this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				clickHandler();
				
			}
			
		});
		IntentFilter filter = new IntentFilter("cn.count.easydrive366.components.HomeMenuItem$LatestInformationReceiver");
		LatestInformationReceiver br = new LatestInformationReceiver();
		context.registerReceiver(br,filter);
	}
	public void setData(HomeMenu menuItem){
		_menuItem = menuItem;
		_title.setText(_menuItem.name);
		final String oldValue = loadJson();
		if (oldValue.equals("")){
			_description.setText(_menuItem.description);
		}else{
			processData(oldValue);
		}
		
	}
	private void makeCall(){
		if (_phone!=null && !_phone.equals("")){
			Uri uri =Uri.parse(String.format("tel:%s",_phone)); 
			
			Intent it = new Intent(Intent.ACTION_VIEW,uri); 
			_context.startActivity(it); 
		}
	}
	private void clickHandler(){
		if ((_menuItem!=null) && (_menuItem.activityClass!=null)){
			
			Intent intent=new Intent(_context,_menuItem.activityClass);
			intent.putExtra("title", _menuItem.name);
			_context.startActivity(intent);
		}
	}
	private void processData(final String json){
		try{
			JSONObject result = new JSONObject(json);
			_company = result.getString("company");
			_phone = result.getString("phone");
			_description.setText(result.getString("latest"));
			
			
		}catch(Exception e){
			AppTools.log(e);
		}
	}
	private void processJson(final String key,final String json){
		if (_menuItem.key.equals(key)){
			saveJson(json);
			
			processData(json);
		}
		
	}
	private String getKey(){
		return String.format("%s_userid_%d", AppSettings.LatestNewsKey,AppSettings.userid);
	}
	private String loadJson(){
		SharedPreferences prefs =_context.getSharedPreferences(getKey(), Context.MODE_PRIVATE);
		String result = prefs.getString(_menuItem.key, "");
		return result;
	}
	private void saveJson(final String json){
		SharedPreferences prefs =_context.getSharedPreferences(getKey(), Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(_menuItem.key, json);
		editor.commit();
		
	}
	public class LatestInformationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			final String key= bundle.getString("key");
			final String json = bundle.getString("json");
			
			processJson(key,json);
			Log.i("Receiver", json);
		}
		
	}
}
