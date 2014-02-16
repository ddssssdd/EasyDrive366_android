package cn.count.easydrive366.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.count.easydrive366.R;
import cn.count.easydrive366.BaseListViewActivity.ViewHolder;
import cn.count.easydrive366.comments.ItemCommentsActivity;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class ProviderDetailActivity extends BaseHttpActivity {
	protected List<Map<String,Object>> _list=null;
	private String code;
	private ProviderAdapter _adapter;
	private List<Album> _imageList;
	private int _index=-1;
	private ImageView _imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_provider_detail);
		this.setupPhoneButtonInVisible();
		this.setRightButtonInVisible();
		code = getIntent().getStringExtra("code");
		if (code!=null && !code.isEmpty()){
			beginHttp();
			new HttpExecuteGetTask(){

				@Override
				protected void onPostExecute(String result) {
					endHttp();
					load_view(result);
					
				}}.execute(String.format("api/get_service_info?userid=%d&code=%s", AppSettings.userid,code));
			
		}
		_imageView = (ImageView)findViewById(R.id.img_picture);
		this.addSwipeToView(_imageView);
		findViewById(R.id.layout_rating).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openRating();
				
			}
		});
	}
	private void openRating(){
		Intent intent =new Intent(this,ItemCommentsActivity.class);
		intent.putExtra("id",code);
		intent.putExtra("type", "provider");
		startActivity(intent);
	}
	private void load_view(final String result){
		JSONObject json =  AppSettings.getSuccessJSON(result);
		try{
			((TextView)findViewById(R.id.txt_name)).setText(json.getString("name"));
			((TextView)findViewById(R.id.txt_phone)).setText(json.getString("phone"));
			((TextView)findViewById(R.id.txt_address)).setText(json.getString("address"));
			((TextView)findViewById(R.id.txt_star)).setText(json.getString("star"));
			((TextView)findViewById(R.id.txt_voternum)).setText(json.getString("star_voternum"));
			((RatingBar)findViewById(R.id.rating_bar)).setRating(json.getInt("star_num"));
			JSONArray list = json.getJSONArray("goods");
			_list = new ArrayList<Map<String,Object>>();
			for(int i=0;i<list.length();i++){
				JSONObject item = list.getJSONObject(i);
				Map<String,Object> map = new HashMap<String,Object>();
				
				for(Iterator<String> it = item.keys();it.hasNext();){
					String key = it.next();
					map.put(key, item.getString(key));
					
				}
				_list.add(map);
			}
			_adapter = new ProviderAdapter(this);
			ListView lv = (ListView)findViewById(R.id.lv_items);
			lv.setAdapter(_adapter);
			_imageList = new ArrayList<Album>();
			list = json.getJSONArray("album");
			for(int i=0;i<list.length();i++){
				JSONObject item = list.getJSONObject(i);
				Album album = new Album();
				album.pic_url = item.getString("pic_url");
				album.remark = item.getString("remark");
				_imageList.add(album);
				
			}
			if (_imageList.size()>0){
				_index = 0;
				showPicture();
			}
			
		}catch(Exception e){
			log(e);
		}
	}
	private void showPicture(){
		String url = _imageList.get(_index).pic_url;
		this.loadImageFromUrl(_imageView, url);
	}
	@Override
	public void onLeftSwipe(){
		if (_imageList!=null && _imageList.size()>0){
			_index--;
	    	if (_index<0){
	    		_index = _imageList.size()-1;
	    	}
	    	showPicture();
		}
    	
    }
	@Override
    public void onRightSwipe(){
		if (_imageList!=null && _imageList.size()>0){
			_index++;
	    	if (_index>_imageList.size()-1){
	    		_index = 0;
	    	}
	    	showPicture();
		}
    }
	private class ProviderHolderView{
		public ImageView image;
		public TextView title;
		public TextView address;
		public TextView phone;
		public TextView voternum;
		public RatingBar ratingbar;
		
	}
	private class ProviderAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater = null;
		public ProviderAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			if (_list==null)
				return 0;
			else
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
			ProviderHolderView holder = null;
			if (convertView==null){
				holder = new ProviderHolderView();
				convertView = mInflater.inflate(R.layout.listitem_provider, null);
				//initListItem(holder,convertView);
				holder.image = (ImageView)convertView.findViewById(R.id.img_picture);
				holder.title = (TextView)convertView.findViewById(R.id.txt_title);
				holder.phone = (TextView)convertView.findViewById(R.id.txt_phone);
				holder.address  = (TextView)convertView.findViewById(R.id.txt_address);
				holder.voternum = (TextView)convertView.findViewById(R.id.txt_voternum);
				holder.ratingbar =(RatingBar)convertView.findViewById(R.id.rating_bar);
				convertView.setTag(holder);
			}else{
				holder = (ProviderHolderView)convertView.getTag();
			}
			
			//{id=1, price=¥200, buyers=100人购买, name=[青岛]油猴精致保养\n洗车护理套装, stand_price=¥1000, miles=20KM, pic_url=http://m.yijia366.com/images/3.png, discount=2折}
			Map<String,Object> info = _list.get(position);
			//setupListItem(holder,info);
			//com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(holder.image, info.get("pic_url").toString());
			loadImageFromUrl(holder.image,info.get("pic_url").toString());
			holder.title.setText(info.get("name").toString());
			holder.address.setText(info.get("buyers").toString());
			holder.phone.setText(info.get("price").toString());
			holder.voternum.setText("");
			holder.ratingbar.setRating(4);
			return convertView;
		}
		
	}
	private class Album{
		public String pic_url;
		public String remark;
	}
}
