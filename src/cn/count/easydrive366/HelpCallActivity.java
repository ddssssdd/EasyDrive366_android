package cn.count.easydrive366;

import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;


public class HelpCallActivity extends BaseListViewActivity {
	private String _companyname;
	private String _phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_helpcall_activity);
		this.setRightButtonInVisible();
		resource_listview_id = R.id.moudles_helpcall_listview;
		resource_listitem_id = R.layout.module_listitem;
		restoreFromLocal(1);
		this.get(AppSettings.url_for_get_helpcalls(), 1);
		
	}
	
	protected void initData(Object result,int msgType){
		try{
			JSONArray list = ((JSONObject)result).getJSONObject("result").getJSONArray("data");
			_companyname = ((JSONObject)result).getJSONObject("result").getString("company");
			 _phone =((JSONObject)result).getJSONObject("result").getString("phone");
			initList(list);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void initView(){
		ListView lv = (ListView)findViewById(resource_listview_id);
		MyAdapter adapter =new MyAdapter(HelpCallActivity.this);
		lv.setAdapter(adapter);
		TextView txtCompany = (TextView)findViewById(R.id.txt_helpcall_company);
		txtCompany.setText(_companyname);
	}
	@Override
	protected void setupListItem(ViewHolder holder,Map<String,Object> info){
		holder.title.setText(info.get("name").toString());
		holder.detail.setText(info.get("description").toString());
		holder.action.setText(info.get("price").toString());
	}
	@Override
	protected void initListItem(ViewHolder holder,View convertView){
		holder.title = (TextView)convertView.findViewById(R.id.txt_listitem_detail_title);
		holder.detail = (TextView)convertView.findViewById(R.id.txt_listitem_detail_detail);
		holder.action = (TextView)convertView.findViewById(R.id.txt_listitem_detail_right);
		convertView.setTag(holder);
	}

}
