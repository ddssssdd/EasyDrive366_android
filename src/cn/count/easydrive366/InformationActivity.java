package cn.count.easydrive366;


import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;


import android.widget.TextView;




import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.HomeMenu;
import cn.count.easydriver366.base.Menus;


public class InformationActivity extends BaseListViewActivity {
	
	private ProgressDialog _dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_information_activity);
		this.setupLeftButton();
		
		this.setRightButtonInVisible();
		this.resource_listview_id = R.id.modules_information_listview;
		this.resource_listitem_id = R.layout.module_listitem;
		restoreFromLocal(1);
		
		reload_data();
		this.setupPullToRefresh();
	}
	@Override
	protected void reload_data(){
		this.get(AppSettings.url_for_get_news(), 1);
	}
	@Override
	protected void initData(Object result,int msgType){
		try{
			//this.setupCompanyAndPhone(result);
			result = ((JSONObject)result).getJSONObject("result");
			JSONArray obj = ((JSONObject)result).getJSONArray("data");		
			this.initList(obj);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	protected void onListItemClick(final View view,final long index){
		//System.out.println("click");
		if (_list!=null){
			Map<String,Object> map = _list.get((int) index);
			final String action = map.get("action").toString();
			final String url = map.get("url").toString();
			if (!url.equals("")){
				//open browser;
				Intent intent = new Intent(this,BrowserActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);
			}else if (!action.equals("01")){
				
				Menus menus = new Menus(this);
				//Class<?> intentClass = menus.findMenuItemClassByKey(action);
				HomeMenu item = menus.findMenuByKey(action);
				if (item!=null){
					Intent intent = new Intent(this,item.activityClass);
					intent.putExtra("title", item.name);
					startActivity(intent);
				}
			}
			
		}
	}
	@Override
	protected void setupListItem(ViewHolder holder,Map<String,Object> info){
		holder.title.setText(info.get("fmt_createDate").toString());
		holder.detail.setText(info.get("description").toString());
		//holder.action.setText(info.get("action").toString());
		holder.action.setText("");
	}
	@Override
	protected void initListItem(ViewHolder holder,View convertView){
		holder.title = (TextView)convertView.findViewById(R.id.txt_listitem_detail_title);
		holder.detail = (TextView)convertView.findViewById(R.id.txt_listitem_detail_detail);
		holder.action = (TextView)convertView.findViewById(R.id.txt_listitem_detail_right);
		convertView.setTag(holder);
	}
	
}
