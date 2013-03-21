package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;


public class CompulsoryInsuranceActivity extends BaseHttpActivity {
	private JSONObject _result;
	private JSONObject _type0;
	private JSONObject _type1;
	private JSONObject _type2; 
	private ExtAdapter _adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_compulsoryinsurance_activity);
		this.setRightButtonInVisible();
		ExpandableListView lv = (ExpandableListView)findViewById(R.id.expandableListView_compulsory_insurance);
		_adapter = new ExtAdapter(this);
		lv.setAdapter(_adapter);
		restoreFromLocal(1);
		this.get(AppSettings.url_get_compulsory_details(), 1);
		
		Button type0 =(Button)findViewById(R.id.btn_compulsory_insurance_0); 
		type0.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				_adapter.setData(_type0);
				
			}
			
		});
		Button type1 =(Button)findViewById(R.id.btn_compulsory_insurance_1); 
		type1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				_adapter.setData(_type1);
				
			}
			
		});
		Button type2 =(Button)findViewById(R.id.btn_compulsory_insurance_2); 
		type2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				_adapter.setData(_type2);
				
			}
			
		});
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		try{
			if (msgType==1){
				_result=( ((JSONObject)result).getJSONObject("result")).getJSONObject("data");
				/*
				 {"type_0":{"机动车在道路交通事故中无责任的赔偿限额":[{"price":"11,000元","name":"死亡伤残赔偿限额"},{"price":"10,00元","name":"医疗费用赔偿限额"},{"price":"100元","name":"财产损失赔偿限额"}],"机动车在道路交通事故中有责任的赔偿限额":[{"price":"110,000元","name":"死亡伤残赔偿限额"},{"price":"10,000元","name":"医疗费用赔偿限额"},{"price":"2,000元","name":"财产损失赔偿限额"}]},"type_1":{"上浮因素及比率":[{"price":"浮动比率-10%","name":"上一个年度未发生有责任道路交通事故"},{"price":"浮动比率-20%","name":"上两个年度未发生有责任道路交通事故"},{"price":"浮动比率-30%","name":"上三个年度未发生有责任道路交通事故"}],"下浮因素及比率":[{"price":"浮动比率0%","name":"上一个年度发生一次有责任不涉及死亡的道路交通事故"},{"price":"浮动比率10%","name":"上一个年度发生两次有责任不涉及死亡的道路交通事故"},{"price":"浮动比率30%","name":"上一个年度发生有责任道路交通死亡事故"}]},"type_2":{"摩托车":[{"price":"80元","name":"摩托车50CC及以下"},{"price":"120元","name":"摩托车50CC-250CC（含）"},{"price":"400元","name":"摩托车250CC以上及侧三轮"}],"非营业客车":[{"price":"1,000元","name":"企业非营业汽车6座以下"},{"price":"1,130元","name":"企业非营业汽车6-10座"},{"price":"1,220元","name":"企业非营业汽车10-20座"},{"price":"1,270元","name":"企业非营业汽车20座以上"},{"price":"950元","name":"机关非营业汽车6座以下"},{"price":"1,070元","name":"机关非营业汽车6-10座"},{"price":"1,140元","name":"机关非营业汽车10-20座"},{"price":"1,320元","name":"机关非营业汽车20座以上"}],"家庭自用车":[{"price":"950元","name":"家庭自用汽车6座以下"},{"price":"1100元","name":"家庭自用汽车6座及以上"}],"营业客车":[{"price":"1,800元","name":"营业出租租赁6座以下"},{"price":"2,360元","name":"营业出租租赁6-10座"},{"price":"2,400元","name":"营业出租租赁10-20座"},{"price":"2,560元","name":"营业出租租赁20-36座"},{"price":"3,530元","name":"营业出租租赁36座以上"},{"price":"2,250元","name":"营业城市公交6-10座"},{"price":"2,520元","name":"营业城市公交10-20座"},{"price":"3,020元","name":"营业城市公交20-36座"},{"price":"3,140元","name":"营业城市公交36座以上"},{"price":"2,350元","name":"营业公路客运6-10座"},{"price":"2,620元","name":"营业公路客运10-20座"},{"price":"3,420元","name":"营业公路客运20-36座"},{"price":"4,690元","name":"营业公路客运36座以上"}],"特种车":[{"price":"3,710元","name":"特种车一"},{"price":"2,430元","name":"特种车二"},{"price":"1,080元","name":"特种车三"},{"price":"3,980元","name":"特种车四"}],"拖拉机":[{"price":" ","name":"拖拉机，按保监产险[2007]53号实行地区差别费率"}],"营业货车":[{"price":"1,850元","name":"营业货车2吨以下"},{"price":"3,070元","name":"营业货车2-5吨"},{"price":"3,450元","name":"营业货车5-10吨"},{"price":"4,480元","name":"营业货车10吨以上"}],"非营业货车":[{"price":"1,200元","name":"非营业货车2吨以下"},{"price":"1,470元","name":"非营业货车2-5吨"},{"price":"1,650元","name":"非营业货车5-10吨"},{"price":"2,220元","name":"非营业货车10吨以上"}]}}
				 */
				_type0 = _result.getJSONObject("type_0");
				_type1 = _result.getJSONObject("type_1");
				_type2 = _result.getJSONObject("type_2");
				this.runOnUiThread(new Runnable(){

					@Override
					public void run() {
						_adapter.setData(_type0);
						
					}});
				
			}
		}catch(Exception e){
			this.log(e);
		}

	}
	public class ExtAdapter extends BaseExpandableListAdapter{
		private LayoutInflater mInflater = null;
		private List<String> groups = new ArrayList<String>();
		private List<ArrayList<JSONObject>> items = new ArrayList<ArrayList<JSONObject>>();
	
		private JSONObject _result;
		public void setData(JSONObject obj){
			_result = obj;
			groups.clear();
			items.clear();
			try{
				for(Iterator<String> it =_result.keys();it.hasNext();){
					String key= it.next();
					groups.add(key);
					JSONArray list = _result.getJSONArray(key);
					ArrayList<JSONObject> target = new ArrayList<JSONObject>();
					
					for(int i=0;i<list.length();i++){
						target.add(list.getJSONObject(i));
					}
					items.add(target);
				}
				this.notifyDataSetChanged();
			}catch(Exception e){
				log(e);
			}
			
		}
		public ExtAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getGroupCount() {
			
			return groups.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			
			return items.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			
			return groups.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			
			return items.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
		
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.group_section, null);
			TextView section = (TextView)convertView.findViewById(R.id.txt_group_section);
			section.setText(groups.get(groupPosition));
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = mInflater.inflate(R.layout.group_item, null);
			TextView item = (TextView)convertView.findViewById(R.id.txt_group_item);
			TextView price = (TextView)convertView.findViewById(R.id.txt_group_price);
			try{
				item.setText(items.get(groupPosition).get(childPosition).getString("name"));
				price.setText(items.get(groupPosition).get(childPosition).getString("price"));
			}catch(Exception e){
				log(e);
			}
			
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			
			return false;
		}
		
	}

	

}
