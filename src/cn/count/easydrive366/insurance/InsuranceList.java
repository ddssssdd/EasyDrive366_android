package cn.count.easydrive366.insurance;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.count.easydrive366.BaseListViewActivity;
import cn.count.easydrive366.R;
import cn.count.easydrive366.BaseListViewActivity.ViewHolder;
import cn.count.easydrive366.goods.GoodsDetailActivity;
import cn.count.easydrive366.goods.GoodsListActivity;
import cn.count.easydrive366.order.NewOrderActivity;
import cn.count.easydriver366.base.AppSettings;

public class InsuranceList extends BaseListViewActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modules_goodslist);
		this.setupPhoneButtonInVisible();
		this.setupLeftButton();
		this.resource_listview_id = R.id.modules_information_listview;
		//this.resource_listitem_id = R.layout.module_listitem;
		this.resource_listitem_id = R.layout.listitem_insurance;
		
		
		reload_data();
		this.setupPullToRefresh();
	}
	@Override
	protected void reload_data(){
		
		this.get(String.format("ins/list_quote?userid=%d", AppSettings.userid), 1);
		
	}
	@Override
	protected void initData(Object result,int msgType){
		try{
			
			JSONArray list = ((JSONObject)result).getJSONArray("result");
			
			this.initList(list);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	protected void onListItemClick(final View view,final long index){
		
		if (_list!=null){
			
			Map<String,Object> map = _list.get((int) index);
			String id =map.get("id").toString();
			Intent intent = new Intent(this,BuyInsuranceStep2.class);
			intent.putExtra("id", id);
			startActivity(intent);
			
		}
	}
	
	@Override
	protected void setupListItem(ViewHolder holder,Map<String,Object> info){
		holder.title.setText(info.get("car_id").toString());
		holder.detail2.setText(info.get("price_time").toString());
		holder.detail3.setText(info.get("status_name").toString());
	}
	@Override
	protected void initListItem(ViewHolder holder,View convertView){
		holder.title = (TextView)convertView.findViewById(R.id.txt_car_id);
		holder.detail2 = (TextView) convertView.findViewById(R.id.txt_price_time);
		holder.detail3 = (TextView) convertView.findViewById(R.id.txt_status_name);
		
	}

}
