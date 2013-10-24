package cn.count.easydrive366.card;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.BaseHttpActivity;

public class AddCardStep2Activity extends BaseHttpActivity {
	private String number;
	private EditText edtName;
	private EditText edtIdentity;
	private EditText edtCell;
	private EditText edtAddress;
	private EditText edtBf_name;
	private EditText edtBf_identity;
	private CheckBox chkBf;
	private TableRow row_bf_name;
	private TableRow row_bf_identity;
	private TableRow row_bf_default;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_addcardstep2_activity);
		this.setupRightButtonWithText("保存");
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		number = this.getIntent().getStringExtra("number");
		
		initData();
		
		
				
	}
	private void initData()
	{
		edtName = (EditText)findViewById(R.id.edt_name);
		edtIdentity = (EditText)findViewById(R.id.edt_identity);
		edtCell = (EditText)findViewById(R.id.edt_cell);
		edtAddress = (EditText)findViewById(R.id.edt_address);
		edtBf_name = (EditText)findViewById(R.id.edt_bf_name);
		edtBf_identity = (EditText)findViewById(R.id.edt_bf_identity);
		chkBf = (CheckBox)findViewById(R.id.chk_bf);
		row_bf_name = (TableRow)findViewById(R.id.row_bf_name);
		row_bf_identity = (TableRow)findViewById(R.id.row_bf_identity);
		row_bf_default = (TableRow)findViewById(R.id.row_bf_default);
		row_bf_default.setVisibility(View.VISIBLE);
		row_bf_name.setVisibility(View.GONE);
		row_bf_identity.setVisibility(View.GONE);
		chkBf.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked){
					row_bf_default.setVisibility(View.GONE);
					row_bf_name.setVisibility(View.VISIBLE);
					row_bf_identity.setVisibility(View.VISIBLE);
				}else{
					row_bf_default.setVisibility(View.VISIBLE);
					row_bf_name.setVisibility(View.GONE);
					row_bf_identity.setVisibility(View.GONE);
				}
				
			}});
		
	}
	@Override
	public void processMessage(int msgType, final Object result) {

		if (msgType==1){
			try{
				if (AppTools.isSuccess(result)){
					Intent intent = new Intent(this,CardViewActivity.class);
					startActivity(intent);
				}
				
			}catch(Exception e){
				log(e);
			}
		}
		
	}
	@Override
	protected void onRightButtonPress() {
		this.get(AppSettings.add_inscard_step2(number, this.edtName.getText().toString(), this.edtIdentity.getText().toString(), this.edtCell.getText().toString(), edtAddress.getText().toString(), chkBf.isChecked(), edtBf_name.getText().toString(), edtBf_identity.getText().toString()), 1);
	}
}
