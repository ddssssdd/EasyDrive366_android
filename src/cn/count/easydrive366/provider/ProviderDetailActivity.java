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
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.count.easydrive366.R;
import cn.count.easydrive366.BaseListViewActivity.ViewHolder;
import cn.count.easydrive366.baidumap.ShowLocationActivity;
import cn.count.easydrive366.comments.ItemCommentsActivity;
import cn.count.easydrive366.goods.GoodsDetailActivity;
import cn.count.easydrive366.goods.GoodsListFragment;
import cn.count.easydrive366.order.NewOrderActivity;
import cn.count.easydrive366.share.ShareController;
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
	private TextView txtIndex;
	private ShareController _share;
	private MenuItem _menuFavor;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_provider_detail);
		_share = new ShareController(this);
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
		findViewById(R.id.layout_phone).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openPhone();
				
			}
		});
		findViewById(R.id.layout_address).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openAddress();
				
			}
		});
		txtIndex = (TextView)findViewById(R.id.txt_index);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.share_menu, menu);
	      _menuFavor = menu.findItem(R.id.action_favor);
	      return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (R.id.action_favor==item.getItemId()){
			
		}else{
			_share.share(item.getItemId());
		}
		
		return super.onOptionsItemSelected(item);
	}
	private void openRating(){
		Intent intent =new Intent(this,ItemCommentsActivity.class);
		intent.putExtra("id",code);
		intent.putExtra("type", "provider");
		startActivity(intent);
	}
	private void openPhone(){
		Uri uri =Uri.parse(String.format("tel:%s",_phone)); 
		
		Intent it = new Intent(Intent.ACTION_VIEW,uri); 
		startActivity(it); 
	}
	private void openAddress(){
		Intent intent = new Intent(this,ShowLocationActivity.class);
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
			_phone =json.getString("phone");
			_share.setContent(json);
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
			lv.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id2) {
					if (_list != null) {

						Map<String, Object> map = _list.get( position);
						int id = Integer.parseInt(map.get("id").toString());
						Intent intent = new Intent(ProviderDetailActivity.this,
								GoodsDetailActivity.class);
						intent.putExtra("id", id);
						startActivity(intent);

					}
					
				}});
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
		this.loadImageFromUrl(_imageView, url,R.drawable.default_640x234);
		txtIndex.setText(String.format("%d/%d", _index+1,_imageList.size()));
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
		public TextView title;
		public TextView detail;
		public TextView action;
		public CheckBox selected;
		public ImageView image;
		public TextView detail2;
		public TextView detail3;
		public TextView detail4;
		public TextView detail5;
		public RatingBar ratingbar;
		public Button button1;
		public Button btnDelete;
		public ImageButton imgButton;
		
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
				convertView = mInflater.inflate(R.layout.listitem_goods, null);
				/*
				holder.image = (ImageView)convertView.findViewById(R.id.img_picture);
				holder.title = (TextView)convertView.findViewById(R.id.txt_title);
				holder.phone = (TextView)convertView.findViewById(R.id.txt_phone);
				holder.address  = (TextView)convertView.findViewById(R.id.txt_address);
				*/
				holder.title = (TextView) convertView.findViewById(R.id.txt_title);
				holder.detail2 = (TextView) convertView.findViewById(R.id.txt_price);
				holder.detail3 = (TextView) convertView.findViewById(R.id.txt_stand_price);
				holder.detail4 = (TextView) convertView.findViewById(R.id.txt_discount);
				holder.detail5 = (TextView) convertView.findViewById(R.id.txt_buyer);
				holder.detail =(TextView) convertView.findViewById(R.id.txt_description);
				holder.image = (ImageView) convertView.findViewById(R.id.img_picture);
				holder.button1 = (Button) convertView.findViewById(R.id.btn_buy);
				convertView.setTag(holder);

				holder.button1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String id = (String) v.getTag();
						Intent intent = new Intent(
								ProviderDetailActivity.this,
								NewOrderActivity.class);
						intent.putExtra("id", id);
						startActivity(intent);
					}
				});
				convertView.setTag(holder);
			}else{
				holder = (ProviderHolderView)convertView.getTag();
			}
			
			//{id=1, price=¥200, buyers=100人购买, name=[青岛]油猴精致保养\n洗车护理套装, stand_price=¥1000, miles=20KM, pic_url=http://m.yijia366.com/images/3.png, discount=2折}
			Map<String,Object> info = _list.get(position);
			//setupListItem(holder,info);
			//com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(holder.image, info.get("pic_url").toString());
			/*
			loadImageFromUrl(holder.image,info.get("pic_url").toString());
			holder.title.setText(info.get("name").toString());
			holder.address.setText(info.get("buyer").toString());
			holder.phone.setText(info.get("price").toString());
			*/
			holder.title.setText(info.get("name").toString());
			holder.detail.setText(info.get("description").toString());
			holder.detail2.setText(info.get("price").toString());
			holder.detail3.setText(info.get("stand_price").toString());
			holder.detail4.setText(info.get("discount").toString());
			holder.detail5.setText(info.get("buyer").toString());
			com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(
					holder.image, info.get("pic_url").toString());
			holder.button1.setTag(info.get("id"));
			return convertView;
		}
		
	}
	private class Album{
		public String pic_url;
		public String remark;
	}
}
