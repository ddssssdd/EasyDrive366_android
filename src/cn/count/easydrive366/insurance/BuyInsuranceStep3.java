package cn.count.easydrive366.insurance;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;
import cn.count.easydriver366.base.AppTools.ISetDate;

public class BuyInsuranceStep3 extends BaseHttpActivity {
	private TextView txt_brand;
	private TextView txt_model;
	private TextView txt_exhause;
	private TextView txt_biz_date;
	private TextView txt_gear;
	private TextView txt_price;
	private TextView txt_com_date;
	private String data;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buyinsurance_step3);
		this.setupTitle("在线购买保险", "第三步");
		this.setupLeftButton();
		this.setupRightButtonWithText("下一步");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init_view();
		
	}
	@Override
	protected void onRightButtonPress() {
		doSave();
	}
	private void doSave(){
		if (txt_biz_date.getText().toString().trim().isEmpty()){
			this.showMessage("商业险起期不能为空！",null);
			return;
		}
		if (txt_com_date.getText().toString().trim().isEmpty()){
			this.showMessage("交强险起期不能为空！",null);
			return;
		}
		String url = String.format("ins/carins_clause?userid=%d&biz_valid=%s&com_valid=%s", 
				AppSettings.userid,
				txt_biz_date.getText().toString().trim(),
				txt_com_date.getText().toString().trim());
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
			Intent intent = new Intent(this,BuyInsuranceStep4.class);
			intent.putExtra("data", result);
			startActivity(intent);
		}
	}
	private void init_view(){
		load_parameters();
		load_view();
		load_data();
	}
	private void load_parameters(){
		data  = this.getIntent().getStringExtra("data");
	}
	
	
	private void load_view(){
		txt_brand = (TextView)findViewById(R.id.txt_brand);
		txt_model = (TextView)findViewById(R.id.txt_model);
		txt_exhause = (TextView)findViewById(R.id.txt_exhause);
		txt_biz_date = (TextView)findViewById(R.id.txt_biz_valid);
		txt_gear = (TextView)findViewById(R.id.txt_gear);
		txt_price = (TextView)findViewById(R.id.txt_price);
		txt_com_date = (TextView)findViewById(R.id.txt_com_valid);
		
		findViewById(R.id.layout_biz_valid).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AppTools.chooseDate(txt_biz_date.getText().toString(), BuyInsuranceStep3.this, new ISetDate(){

					@Override
					public void setDate(String date) {
						txt_biz_date.setText(date);
					}});
				
			}});
		findViewById(R.id.layout_com_valid).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AppTools.chooseDate(txt_com_date.getText().toString(), BuyInsuranceStep3.this, new ISetDate(){

					@Override
					public void setDate(String date) {
						txt_com_date.setText(date);
					}});
				
			}});
	}
	private void load_data(){
		JSONObject json = AppSettings.getSuccessJSON(data,this);
		if (json!=null){
			try{
				txt_brand.setText(json.getString("brand"));
				txt_model.setText(json.getString("model"));
				txt_exhause.setText(json.getString("exhause"));
				txt_biz_date.setText(json.getString("biz_valid"));
				txt_gear.setText(json.getString("passengers"));
				txt_price.setText(json.getString("price"));
				txt_com_date.setText(json.getString("com_valid"));
				
			}catch(Exception e){
				log(e);
			}
		}
	}
	

}
