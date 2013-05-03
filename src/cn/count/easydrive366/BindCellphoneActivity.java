package cn.count.easydrive366;

import android.content.Intent;
import android.os.Bundle;
import cn.count.easydriver366.base.BaseHttpActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BindCellphoneActivity extends BaseHttpActivity {
	private int _isbind;
	private String _cellphone;
	private Button btnBind;
	private Button btnGetCode;
	private EditText edtCellphone;
	private EditText edtCode;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modules_bindcellphone_activity);
		this.setupLeftButton();
		this.setRightButtonInVisible();
		this.setupPhoneButtonInVisible();
		Intent intent = this.getIntent();
		_isbind = intent.getIntExtra("isbind", 0);
		_cellphone = intent.getStringExtra("phone");
		this.init_view();
		
	}
	private void init_view(){
		this.setBarTitle(this.getResources().getString(_isbind==1?R.string.bind:R.string.unbind));
		btnBind = (Button)findViewById(R.id.btn_bind);
		btnGetCode=(Button)findViewById(R.id.btn_get_code);
		edtCellphone = (EditText)findViewById(R.id.edt_cellphone);
		edtCode = (EditText)findViewById(R.id.edt_code);
		edtCellphone.setText(_cellphone);
		if (_isbind==0){			
			edtCellphone.setEnabled(false);
		}
		btnBind.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				bindCellphone();
				
			}});
		btnGetCode.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				getCode();
				
			}});
	}
	private void getCode(){
		
	}
	private void bindCellphone(){
		
	}
}
