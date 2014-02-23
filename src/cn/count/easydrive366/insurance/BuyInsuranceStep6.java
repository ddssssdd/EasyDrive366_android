package cn.count.easydrive366.insurance;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.count.easydrive366.R;
import cn.count.easydrive366.components.KeyValueItem;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class BuyInsuranceStep6 extends BaseHttpActivity {
	private String data;
	private boolean useDiscount=true;
	private String order_pay;
	private String order_pay2;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buyinsurance_step6);
		this.setupTitle("在线购买保险", "第六步");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init_view();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add("下一步").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		doSave();
		return super.onOptionsItemSelected(item);
	}
	private void doSave(){
		
		
		String url = String.format("ins/carins_total?userid=%d",
				AppSettings.userid);
				
		beginHttp();
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				endHttp();
				process_result(result);
				
			}}.execute(url);
			
	}
	private void process_result(final String result){
		
		if (AppSettings.isSuccessJSON(result, this)){
			Intent intent = new Intent(this,BuyInsuranceStep5.class);
			intent.putExtra("data", result);
			startActivity(intent);
		}
	}
	private TextView txt_pay;
	private void init_view(){
		data  = this.getIntent().getStringExtra("data");
		try{
			JSONObject json = new JSONObject(data).getJSONObject("result");
			order_pay = json.getString("order_pay");
			order_pay2 = json.getString("order_pay_2");
			((TextView)findViewById(R.id.txt_order_total)).setText(json.getString("order_total"));
			((TextView)findViewById(R.id.txt_bounds)).setText(json.getString("bounds"));
			txt_pay =(TextView)findViewById(R.id.txt_order_pay); 
			txt_pay.setText(useDiscount?order_pay:order_pay2);
			CheckBox chb = (CheckBox)findViewById(R.id.chb_usediscount);
			(chb).setChecked(useDiscount);
			chb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					useDiscount = isChecked;
					txt_pay.setText(useDiscount?order_pay:order_pay2);
				}});
			TableLayout table = (TableLayout)findViewById(R.id.table_pay);
			JSONArray list = json.getJSONArray("pay");
			for(int i=0;i<list.length();i++){
				JSONObject pay = list.getJSONObject(i);
				final String bank_id = pay.getString("back_id"); 
				TableRow tr = new TableRow(this);
				KeyValueItem kv = new KeyValueItem(this,null);
				kv.setData(pay.getString("bank_name"), pay.getString("account"));
				tr.addView(kv);
				table.addView(tr);
				tr.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						order_pay_by(bank_id);
						
					}});
			}
			
			
		}catch(Exception e){
			log(e);
		}
	}
	private void order_pay_by(String bank_id){
		//dothing pay and after pay;
	}
}
