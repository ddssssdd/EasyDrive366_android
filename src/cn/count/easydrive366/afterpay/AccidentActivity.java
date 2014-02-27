package cn.count.easydrive366.afterpay;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class AccidentActivity extends BaseHttpActivity {
	private String data;
	private String order_id;
	private EditText edt_name;
	private EditText edt_cellphone;
	private TextView txt_job;
	private EditText edt_idcard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_afterpay_accident);
		this.setupLeftButton();
		getActionBar().setTitle("保险信息");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		init_view();
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,100,0,"完成").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		if (item.getItemId()==100)
			doSave();	
		return super.onOptionsItemSelected(item);
	}
	private void doSave(){
		if (edt_name.getText().toString().trim().isEmpty()){
			this.showMessage("姓名不能为空！",null);
			return;
		}
		if (edt_idcard.getText().toString().trim().isEmpty()){
			this.showMessage("身份证号码不能为空！",null);
			return;
		}
		if (edt_cellphone.getText().toString().trim().isEmpty()){
			this.showMessage("手机不能为空！",null);
			return;
		}
		
		String url = String.format("order/order_ins_accident?userid=%d&orderid=%s&name=%s&phone=%s&type=%s&idcard=%s",
				AppSettings.userid,
				order_id,
				edt_name.getText().toString(),
				edt_cellphone.getText().toString(),
				txt_job.getText().toString(),
				edt_idcard.getText().toString());
				
		beginHttp();
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				endHttp();
				JSONObject json = AppSettings.getSuccessJSON(result,AccidentActivity.this);
				if (json!=null){
					Intent intent = new Intent(AccidentActivity.this,FinishedActivity.class);
					intent.putExtra("data", json.toString());
					startActivity(intent);
					AccidentActivity.this.finish();
				}
				
				
			}}.execute(url);
	}
	private String[] types;
	private int index=-1;
	private void init_view(){
		edt_name = (EditText)findViewById(R.id.edt_name);
		edt_cellphone = (EditText)findViewById(R.id.edt_cellphone);
		edt_idcard = (EditText)findViewById(R.id.edt_idcard);
		txt_job = (TextView)findViewById(R.id.txt_job);
		data = getIntent().getStringExtra("data");
		try{
			JSONObject json = new JSONObject(data);
			
			order_id  = json.getString("order_id");
			edt_name.setText(json.getString("name"));
			edt_cellphone.setText(json.getString("phone"));
			
			edt_idcard.setText(json.getString("idcard"));
			JSONArray list = json.getJSONArray("list_type");
			final String current_type = json.getString("type_name");
			types = new String[list.length()];
			for(int i=0;i<list.length();i++){
				types[i] = list.getJSONObject(i).getString("label");
				if (current_type.equals(list.getJSONObject(i).getString("label"))){
					index =i;
				}
			}
			findViewById(R.id.row_job).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					choose_job();
					
				}});
		}catch(Exception e){
			log(e);
		}
	}
	private void choose_job(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		
		builder.setSingleChoiceItems(types, index, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				index = which;
				
			}
		});
		builder.setPositiveButton(this.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				txt_job.setText(types[index]);
			}
		});
		builder.setNegativeButton(this.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
		builder.show();
	}
}
