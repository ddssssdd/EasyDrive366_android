package cn.count.easydrive366;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;

import cn.count.easydrive366.components.ViolationDetailItem;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;

public class ViolationSearchActivity extends BaseHttpActivity {
	private TableLayout _tableLayout;
	private JSONObject _result;
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_violationsearch_activity);
		this.setRightButtonInVisible();
		this.setupLeftButton();
		restoreFromLocal(1);
		this.get(AppSettings.url_for_illegallys(), 1);
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		
		if (msgType==1){
			if (this.isSuccess(result)){
				initData((JSONObject)result);
			}
		}
	}
	private void initData(JSONObject json){
		try{
			_result = json.getJSONObject("result");
			
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					initView();
					
				}});

		}catch(Exception e){
			log(e);
		}
		
	}
	private void initView(){
		_tableLayout = (TableLayout)findViewById(R.id.table_modules_violationsearch_table);
		try{
			JSONArray list= _result.getJSONArray("data");
			for(int i=0;i<list.length();i++){
				JSONObject item = list.getJSONObject(i);
				addTableRow(item.getString("Address"),item.getString("Reason"),item.getString("Fine"),item.getString("Mark"),item.getString("OccurTime"));
			}
			
		}catch(Exception e){
			log(e);
		}
	}
	private void addTableRow(final String address,final String reason,final String fine,final String mark,final String occurTime){
		TableRow tr = new TableRow(this);
		ViolationDetailItem item = new ViolationDetailItem(this,null);
		item.setData(address,reason,fine,mark,occurTime);
		tr.addView(item);
		_tableLayout.addView(tr);
	}
	
}
