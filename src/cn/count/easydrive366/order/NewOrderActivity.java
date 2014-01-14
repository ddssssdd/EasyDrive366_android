package cn.count.easydrive366.order;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Button;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class NewOrderActivity extends BaseHttpActivity {
	private TableLayout tblItems;
	private String product_id;
	private String order_id;
	private String order_total;
	private Map<String,Integer> _map= new HashMap<String,Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_neworder_activity);
		this.setBarTitle("Order");
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		this.setRightButtonInVisible();
		Intent intent= getIntent();
		product_id = intent.getStringExtra("id");
		load_data();
	}
	private void load_data(){
		String url;
		if (product_id!=null && !product_id.isEmpty()){
			url = String.format("order/order_new?userid=%d&goodsid=%s", AppSettings.userid,product_id);
		}else{
			order_id = getIntent().getStringExtra("order_id");
			url = String.format("order/order_edit?userid=%d&orderid=%s", AppSettings.userid,order_id);
		}
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				load_view(result);
				
			}}.execute(url);
	}
	private void load_view(final String result){
		tblItems = (TableLayout)findViewById(R.id.tb_items);
		tblItems.removeAllViews();
		JSONObject json = AppSettings.getSuccessJSON(result);
		try{
			order_id = json.getString("order_id");
			order_total = json.getString("order_total");
			JSONArray goods = json.getJSONArray("goods");
			for(int i=0;i<goods.length();i++){
				TableRow tr = new TableRow(this);
				NewOrderItem item = new NewOrderItem(this, null);
				item.setData(goods.getJSONObject(i));
				item.changed = new IOrderQuantityChanged(){

					@Override
					public void changed(String id, int quantity) {
						_map.put(id, quantity);
						
					}};
				tr.addView(item);
				tblItems.addView(tr);
			}
			findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					submit();
					
				}});
		}catch(Exception e){
			log(e);
		}
	}
	private void submit(){
		StringBuilder sb = new StringBuilder();
		for(String key :_map.keySet()){
			sb.append(String.format("goodsid=%s&quantity=%d&", key,_map.get(key)));
		}
	
		String url = String.format("order/order_save?userid=%d&%sorderid=%s", AppSettings.userid,sb.toString(),order_id);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				processResult(result);
				
			}}.execute(url);
		
	}
	private void processResult(final String result){
		try{
			JSONObject json = new JSONObject(result);
			if (AppSettings.isSuccessJSON(json)){
				Intent intent = new Intent(this,PayActivity.class);
				intent.putExtra("json", result);
				startActivity(intent);
				
			}
		}catch(Exception e){
			log(e);
		}
	
	}
}
