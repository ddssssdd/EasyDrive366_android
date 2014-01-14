package cn.count.easydrive366.order;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CheckBox;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class PayActivity extends BaseHttpActivity {
	private TableLayout tblItems;
	private TableLayout tblPays;
	private CheckBox chbUse;
	private TextView txtOrderPay;
	private String order_id;
	private String json;
	private JSONObject data;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_needpay_activity);
		this.setBarTitle("Pay");
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		this.setRightButtonInVisible();
		Intent intent= getIntent();
		json = intent.getStringExtra("json");
		if (json==null || json.isEmpty()){
			order_id = intent.getStringExtra("order_id");
			load_data();
		}else{
			load_view(json);
		}
	}
	private void load_data(){
		String url = String.format("order/order_info?userid=%d&orderid=%s", AppSettings.userid,order_id);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				load_view(result);
				
			}}.execute(url);
	}
	private void setupUseDiscount(final boolean isUsed){
		try{
			txtOrderPay.setText(data.getString(isUsed?"order_pay":"order_pay_2"));
		}catch(Exception e){
			log(e);
		}
	}
	private void load_view(final String result){
		txtOrderPay=(TextView)findViewById(R.id.txt_order_pay);
		chbUse = (CheckBox)findViewById(R.id.chb_usedicount);
		chbUse.setChecked(true);
		chbUse.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setupUseDiscount(isChecked);
				
			}});
		
		data = AppSettings.getSuccessJSON(result);
		try{
			order_id = data.getString("order_id");
			((TextView)findViewById(R.id.txt_order_total)).setText(data.getString("order_total"));
			((TextView)findViewById(R.id.txt_discount)).setText(data.getString("discount"));
			((TextView)findViewById(R.id.txt_bounds)).setText(data.getString("bounds"));
			txtOrderPay.setText(data.getString("order_pay"));
			tblItems = (TableLayout)findViewById(R.id.tb_items);
			tblPays = (TableLayout)findViewById(R.id.tb_pays);
			tblItems.removeAllViews();
			tblPays.removeAllViews();
			
			JSONArray goods = data.getJSONArray("goods");
			JSONArray pays = data.getJSONArray("pay");
			for(int i=0;i<goods.length();i++){
				JSONObject item = goods.getJSONObject(i);
				TableRow tr = new TableRow(this);
				PayGoodsItem goodsItem = new PayGoodsItem(this,null);
				goodsItem.setData(item.getString("name"), item.getString("price"), item.getString("quantity"));
				tr.addView(goodsItem);
				tblItems.addView(tr);
			}
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
						JSONObject pay = (JSONObject)v.getTag();
						Log.d("JSON", pay.toString());
					}});
			}
			
		}catch(Exception e){
			log(e);
		}
	}
}
