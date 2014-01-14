package cn.count.easydrive366.order;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class OrderDetailActivity extends BaseHttpActivity {
	private String order_id;
	private String order_status;
	private String order_status_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_orderdetail_activity);
		this.setBarTitle("Order Detail");
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		this.setRightButtonInVisible();
		Intent intent= getIntent();
		order_id = intent.getStringExtra("id");
		load_data();
	}
	private void load_data(){
		String url = String.format("order/order_detail?userid=%d&orderid=%s", AppSettings.userid,order_id);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				load_view(result);
				
			}}.execute(url);
		
	}
	private void load_view(final String result){
		JSONObject json = AppSettings.getSuccessJSON(result);
		try{
			
		}catch(Exception e){
			log(e);
		}
	}
}

