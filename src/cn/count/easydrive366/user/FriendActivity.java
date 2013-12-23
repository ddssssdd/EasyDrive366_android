package cn.count.easydrive366.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.count.easydrive366.R;

import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class FriendActivity extends BaseHttpActivity {
	private Button btn_weixin;
	private TextView txt_ontent;
	private TextView txt_invite_code;
	private ListView lv_items;
	private FriendListAdapter _adapter;
	private List<Friend> _list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_userfriend);
		this.setBarTitle("friends");
		this.setupPhoneButtonInVisible();
		this.setRightButtonInVisible();
		this.setupLeftButton();
		init_view();
		load_data();
	}
	private void init_view(){
		btn_weixin = (Button)findViewById(R.id.btn_weixin);
		txt_ontent = (TextView)findViewById(R.id.txt_content);
		lv_items=(ListView)findViewById(R.id.lv_items);
		txt_invite_code =(TextView)findViewById(R.id.txt_invite_code);
		btn_weixin.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				doWeixin();
				
			}});
	}
	private void doWeixin(){
		
	}
	private void load_data(){
		String url = String.format("bound/get_my_friends?userid=%d", AppSettings.userid);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				load_view(result);
				
			}}.execute(url);
		
	}
	private void load_view(final String result){
		JSONObject json = AppSettings.getSuccessJSON(result);
		try{
			txt_invite_code.setText(json.getString("my_invite"));
			txt_ontent.setText(json.getString("content"));
			JSONArray tempList = json.getJSONArray("friends");
			_list = new ArrayList<Friend>();
			for(int i=0;i<tempList.length();i++){
				JSONObject item = tempList.getJSONObject(i);
				Friend friend = new Friend();
				friend.date = item.getString("registerDate");
				friend.name = item.getString("name");
				friend.memo = item.getString("is_bounds");
				
				_list.add(friend);
			}
			if (_adapter==null){
				_adapter = new FriendListAdapter(this);
				lv_items.setAdapter(_adapter);
				
			}else{
				_adapter.notifyDataSetChanged();
			}
		}catch(Exception e){
			log(e);
		}
	}
	private class Friend{
		public String name;
		public String date;
		public String memo;
	}
	private class ViewHolder{
		public TextView txtName;
		public TextView txtDate;
		public TextView txtMemo;
	}
	private class FriendListAdapter extends BaseAdapter{
		private LayoutInflater mLayoutInflater;
		public FriendListAdapter(Context context){
		
			mLayoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			
			return _list.size();
		}

		@Override
		public Object getItem(int position) {
			
			return position;
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView==null){
				holder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.listitem_boundlist, null);
				holder.txtDate = (TextView)convertView.findViewById(R.id.txtBound);
				holder.txtName = (TextView)convertView.findViewById(R.id.txtDate);
				holder.txtMemo = (TextView)convertView.findViewById(R.id.txtMemo);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			Friend bound = _list.get(position);
			holder.txtDate.setText(bound.date);
			holder.txtName.setText(bound.name);
			holder.txtMemo.setText(bound.memo);
			return convertView;
		}
		
	}
}
