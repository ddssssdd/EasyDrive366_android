package cn.count.easydrive366.order;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

public class OrderDetailActivity extends BaseHttpActivity {
	private String order_id;
	private String order_status;
	private String order_status_name;
	private boolean is_exform;
	private String next_form;
	private boolean is_upload;
	private int order_count;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_orderdetail_activity);
		this.setBarTitle("Order Detail");
		this.setupLeftButton();
		
		this.setupRightButtonWithText("编辑");
		Intent intent= getIntent();
		order_id = intent.getStringExtra("order_id");
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
		JSONObject json = AppSettings.getSuccessJSON(result,this);
		if (json!=null){
			try{
				order_status = json.getString("order_status");
				order_status_name = json.getString("order_status_name");
				next_form = json.getString("next_form");
				is_upload = json.getInt("is_upload")==1;
				is_exform = json.getInt("is_exform")==1;
				if (!is_exform){
					this.rightTopMenu.setVisible(false);
				}
				order_count = 0;
				if (json.getJSONArray("goods").length()>0){
					JSONObject  goods = json.getJSONArray("goods").getJSONObject(0);
					order_count += goods.getInt("quantity");
					//set goods
					((TextView)findViewById(R.id.txt_title)).setText(goods.getString("name"));
					((TextView)findViewById(R.id.txt_description)).setText(goods.getString("description"));
					((TextView)findViewById(R.id.txt_stand_price)).setText(goods.getString("stand_price"));
					((TextView)findViewById(R.id.txt_discount)).setText(goods.getString("discount"));
					((TextView)findViewById(R.id.txt_price)).setText(goods.getString("price"));
					((TextView)findViewById(R.id.txt_buyer)).setText(goods.getString("buyer"));
					ImageView img = (ImageView)findViewById(R.id.img_picture);
					AppTools.loadImageFromUrl(img, goods.getString("pic_url"));
					
				}
				//sets order informaiton
				((TextView)findViewById(R.id.txt_order_id)).setText(json.getString("name"));
				((TextView)findViewById(R.id.txt_order_time)).setText(json.getString("order_time"));
				((TextView)findViewById(R.id.txt_count)).setText(String.format("%d", order_count));
				((TextView)findViewById(R.id.txt_order_total)).setText(json.getString("order_total"));
				if (order_status.equals("notpay")){
					findViewById(R.id.layout_contents).setVisibility(View.GONE);
					findViewById(R.id.layout_accident).setVisibility(View.GONE);
					findViewById(R.id.layout_finished).setVisibility(View.GONE);
					findViewById(R.id.layout_address).setVisibility(View.GONE);
					findViewById(R.id.layout_shouldpay).setVisibility(View.GONE);
					findViewById(R.id.layout_pay).setVisibility(View.GONE);
					findViewById(R.id.layout_upload).setVisibility(View.GONE);
					findViewById(R.id.btn_pay).setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							doPay();
							
						}});
				}else{
					findViewById(R.id.btn_pay).setVisibility(View.GONE);
					if (is_upload){
						findViewById(R.id.layout_upload).setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								doUpload();
								
							}});
					}else{
						findViewById(R.id.layout_upload).setVisibility(View.GONE);
					}
					if (next_form.equals("address")){
						findViewById(R.id.layout_contents).setVisibility(View.GONE);
						findViewById(R.id.layout_accident).setVisibility(View.GONE);
						findViewById(R.id.layout_finished).setVisibility(View.GONE);
						((TextView)findViewById(R.id.txt_shipping)).setText(json.getString("shipping"));
						((TextView)findViewById(R.id.txt_name)).setText(json.getString("name"));
						((TextView)findViewById(R.id.txt_phone)).setText(json.getString("phone"));
						((TextView)findViewById(R.id.txt_address)).setText(json.getString("address"));
					}else if (next_form.equals("ins_contents")){
						findViewById(R.id.layout_address).setVisibility(View.GONE);
						findViewById(R.id.layout_accident).setVisibility(View.GONE);
						findViewById(R.id.layout_finished).setVisibility(View.GONE);
						((TextView)findViewById(R.id.txt_idcard)).setText(json.getString("idcard"));
						((TextView)findViewById(R.id.txt_name)).setText(json.getString("name"));
						((TextView)findViewById(R.id.txt_phone)).setText(json.getString("phone"));
						((TextView)findViewById(R.id.txt_address)).setText(json.getString("address"));
					}else if (next_form.equals("ins_accident")){
						findViewById(R.id.layout_contents).setVisibility(View.GONE);
						findViewById(R.id.layout_address).setVisibility(View.GONE);
						findViewById(R.id.layout_finished).setVisibility(View.GONE);
						((TextView)findViewById(R.id.txt_idcard)).setText(json.getString("idcard"));
						((TextView)findViewById(R.id.txt_name)).setText(json.getString("name"));
						((TextView)findViewById(R.id.txt_phone)).setText(json.getString("phone"));
						((TextView)findViewById(R.id.txt_typename)).setText(json.getString("typename"));
					}else if (next_form.equals("finished")){
						findViewById(R.id.layout_contents).setVisibility(View.GONE);
						findViewById(R.id.layout_accident).setVisibility(View.GONE);
						findViewById(R.id.layout_address).setVisibility(View.GONE);
						((TextView)findViewById(R.id.txt_content)).setText(json.getString("content"));
						
					}
					//should pay
					((TextView)findViewById(R.id.txt_discount)).setText(json.getString("discount"));
					((TextView)findViewById(R.id.txt_bounds)).setText(json.getString("bounds"));
					((TextView)findViewById(R.id.txt_order_pay)).setText(json.getString("order_pay"));
					//pay
					JSONArray pays = json.getJSONArray("pay");
					TableLayout tblPays = (TableLayout)findViewById(R.id.tb_pays);
					for(int i=0;i<pays.length();i++){
						final JSONObject pay = pays.getJSONObject(i);
						int index;
						if (i==0)
							index=0;
						else if (i==pays.length()-1)
							index=-1;
						else
							index =1;
						TableRow tr = new TableRow(this);
						PayPayItem payItem = new PayPayItem(this,null);
						payItem.setData(pay.getString("bank_name"), pay.getString("account"), index);
						tr.addView(payItem);
						tblPays.addView(tr);
						tr.setTag(pay);
						tr.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								
							}});
					}
					
				}
				
				
			}catch(Exception e){
				log(e);
			}
		}
		
	}
	private void doPay(){
		
	}
	private void doUpload(){
		
	}
}

