package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.Collections;
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
		this.setupLeftButton();
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
				 {"type_0":{"���ڵ�·��ͨ�¹��������ε��⳥�޶�":[{"price":"11,000Ԫ","name":"�����˲��⳥�޶�"},{"price":"10,00Ԫ","name":"ҽ�Ʒ����⳥�޶�"},{"price":"100Ԫ","name":"�Ʋ���ʧ�⳥�޶�"}],"���ڵ�·��ͨ�¹��������ε��⳥�޶�":[{"price":"110,000Ԫ","name":"�����˲��⳥�޶�"},{"price":"10,000Ԫ","name":"ҽ�Ʒ����⳥�޶�"},{"price":"2,000Ԫ","name":"�Ʋ���ʧ�⳥�޶�"}]},"type_1":{"�ϸ����ؼ�����":[{"price":"��������-10%","name":"��һ�����δ���������ε�·��ͨ�¹�"},{"price":"��������-20%","name":"���������δ���������ε�·��ͨ�¹�"},{"price":"��������-30%","name":"��������δ���������ε�·��ͨ�¹�"}],"�¸����ؼ�����":[{"price":"��������0%","name":"��һ����ȷ���һ�������β��漰�����ĵ�·��ͨ�¹�"},{"price":"��������10%","name":"��һ����ȷ������������β��漰�����ĵ�·��ͨ�¹�"},{"price":"��������30%","name":"��һ����ȷ��������ε�·��ͨ�����¹�"}]},"type_2":{"Ħ�г�":[{"price":"80Ԫ","name":"Ħ�г�50CC������"},{"price":"120Ԫ","name":"Ħ�г�50CC-250CC������"},{"price":"400Ԫ","name":"Ħ�г�250CC���ϼ�������"}],"��Ӫҵ�ͳ�":[{"price":"1,000Ԫ","name":"��ҵ��Ӫҵ��6������"},{"price":"1,130Ԫ","name":"��ҵ��Ӫҵ��6-10��"},{"price":"1,220Ԫ","name":"��ҵ��Ӫҵ��10-20��"},{"price":"1,270Ԫ","name":"��ҵ��Ӫҵ��20������"},{"price":"950Ԫ","name":"��ط�Ӫҵ��6������"},{"price":"1,070Ԫ","name":"��ط�Ӫҵ��6-10��"},{"price":"1,140Ԫ","name":"��ط�Ӫҵ��10-20��"},{"price":"1,320Ԫ","name":"��ط�Ӫҵ��20������"}],"��ͥ���ó�":[{"price":"950Ԫ","name":"��ͥ������6������"},{"price":"1100Ԫ","name":"��ͥ������6������"}],"Ӫҵ�ͳ�":[{"price":"1,800Ԫ","name":"Ӫҵ��������6������"},{"price":"2,360Ԫ","name":"Ӫҵ��������6-10��"},{"price":"2,400Ԫ","name":"Ӫҵ��������10-20��"},{"price":"2,560Ԫ","name":"Ӫҵ��������20-36��"},{"price":"3,530Ԫ","name":"Ӫҵ��������36������"},{"price":"2,250Ԫ","name":"Ӫҵ���й���6-10��"},{"price":"2,520Ԫ","name":"Ӫҵ���й���10-20��"},{"price":"3,020Ԫ","name":"Ӫҵ���й���20-36��"},{"price":"3,140Ԫ","name":"Ӫҵ���й���36������"},{"price":"2,350Ԫ","name":"Ӫҵ��·����6-10��"},{"price":"2,620Ԫ","name":"Ӫҵ��·����10-20��"},{"price":"3,420Ԫ","name":"Ӫҵ��·����20-36��"},{"price":"4,690Ԫ","name":"Ӫҵ��·����36������"}],"���ֳ�":[{"price":"3,710Ԫ","name":"���ֳ�һ"},{"price":"2,430Ԫ","name":"���ֳ���"},{"price":"1,080Ԫ","name":"���ֳ���"},{"price":"3,980Ԫ","name":"���ֳ���"}],"������":[{"price":" ","name":"�������������[2007]53��ʵ�е��������"}],"Ӫҵ����":[{"price":"1,850Ԫ","name":"Ӫҵ����2������"},{"price":"3,070Ԫ","name":"Ӫҵ����2-5��"},{"price":"3,450Ԫ","name":"Ӫҵ����5-10��"},{"price":"4,480Ԫ","name":"Ӫҵ����10������"}],"��Ӫҵ����":[{"price":"1,200Ԫ","name":"��Ӫҵ����2������"},{"price":"1,470Ԫ","name":"��Ӫҵ����2-5��"},{"price":"1,650Ԫ","name":"��Ӫҵ����5-10��"},{"price":"2,220Ԫ","name":"��Ӫҵ����10������"}]}}
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
				List<String> keys = new ArrayList<String>();
				for(Iterator<String> it =_result.keys();it.hasNext();){
					keys.add(it.next());
				}
				Collections.sort(keys);
				for(int j=0;j<keys.size();j++){
					String key= keys.get(j);
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
