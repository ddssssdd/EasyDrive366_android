package cn.count.easydrive366.insurance;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import cn.count.easydrive366.R;
import cn.count.easydrive366.components.InsuranceDetailItem;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public abstract class BuyInsuranceStep5 extends BaseHttpActivity {
	private String data;
	private boolean is_com;
	private String total;
	private String total_nocomm;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buyinsurance_step5);
		this.setupTitle("在线购买保险", "第五步");
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
		
		
		String url = String.format("ins/carins_check?userid=%d&is_comm=%d",
				AppSettings.userid,is_com?"1":"0");
				
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
			Intent intent = new Intent(this,BuyInsuranceStep6.class);
			intent.putExtra("data", result);
			startActivity(intent);
		}
	}
	private TextView txt_section0;
	private TextView txt_section1;
	private TextView txt_section2;
	private CheckBox chb_com;
	private TableLayout table_biz;
	private TextView txt_com_com;
	private TextView txt_com_tax;
	private TextView txt_com_total;
	private TextView txt_total;
	private void init_view(){
		txt_section0 = (TextView)findViewById(R.id.txt_section0);
		txt_section1 = (TextView)findViewById(R.id.txt_section1);
		txt_section2 = (TextView)findViewById(R.id.txt_section2);
		chb_com = (CheckBox)findViewById(R.id.chb_com);
		txt_com_com = (TextView)findViewById(R.id.txt_com_com);
		txt_com_tax = (TextView)findViewById(R.id.txt_com_tax);
		txt_com_total = (TextView)findViewById(R.id.txt_com_total);
		txt_total = (TextView)findViewById(R.id.txt_total);
		table_biz = (TableLayout)findViewById(R.id.table_biz);
		
		
		
		data  = this.getIntent().getStringExtra("data");
		
		
		try{
			JSONObject json = new JSONObject(data).getJSONObject("result");
			JSONObject biz = json.getJSONObject("biz");
			JSONObject com = json.getJSONObject("com");
			is_com =com.getInt("is_com")==1;
			txt_section0.setText(biz.getString("title"));
			txt_section1.setText(com.getString("title"));
			txt_section2.setText(json.getString("price_content"));
			chb_com.setChecked(is_com);
			txt_com_com.setText(com.getString("com"));
			txt_com_tax.setText(com.getString("tax"));
			txt_com_total.setText(com.getString("com_total"));
			total = json.getString("total");
			total_nocomm = json.getString("total_nocomm");
			txt_total.setText(is_com?total:total_nocomm);
			
			JSONArray bizList= biz.getJSONArray("list");
			//table_biz.removeAllViews();
			for(int i=0;i<bizList.length();i++){
				JSONObject item = bizList.getJSONObject(i);
				TableRow row = new TableRow(this);
				InsuranceDetailItem detail = new InsuranceDetailItem(this,null);
				detail.setData(item.getString("insu_name"),item.getString("amount"),item.getString("fee"),item.getString("no_excuse"));
				row.addView(detail);
				table_biz.addView(row);
			}
			chb_com.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					is_com = isChecked;
					txt_total.setText(is_com?total:total_nocomm);
				}});
		}catch(Exception e){
			log(e);
		}
	}
}
