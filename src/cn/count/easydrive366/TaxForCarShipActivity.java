package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.AppSettings;


public class TaxForCarShipActivity extends BaseHttpActivity {
	private JSONObject _result;
	private ExtAdapter _adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_taxforcarship_activity);
		this.get(AppSettings.url_get_taxforcarship(), 1);
		ExpandableListView lv = (ExpandableListView)findViewById(R.id.expandableListView1);
		_adapter = new ExtAdapter(this);
		lv.setAdapter(_adapter);
	}
	@Override
	public void processMessage(int msgType, final Object result) {
		try{
			_result= ((JSONObject)result).getJSONObject("result");
			_result = _result.getJSONObject("data");
			Log.d("Http", _result.toString());
			this.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					_adapter.setData(_result);
					
				}
				
			});
			

		}catch(Exception e){
			log(e);
		}
		
	}

	
	public class ExtAdapter extends BaseExpandableListAdapter{
		private LayoutInflater mInflater = null;
		private List<String> groups = new ArrayList<String>();
		private List<String> marks = new ArrayList<String>();
		private List<ArrayList<JSONObject>> items = new ArrayList<ArrayList<JSONObject>>();
	
		public ExtAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
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
					marks.add(_result.getJSONObject(key).getString("marks"));
					JSONArray list = _result.getJSONObject(key).getJSONArray("list");
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
				item.setText(items.get(groupPosition).get(childPosition).getString("description"));
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
