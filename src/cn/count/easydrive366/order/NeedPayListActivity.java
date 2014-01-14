package cn.count.easydrive366.order;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.count.easydrive366.BaseListViewActivity;
import cn.count.easydrive366.R;

import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.HttpExecuteGetTask;


public class NeedPayListActivity extends BaseListViewActivity{
	private JSONArray results;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_goodslist);
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		this.resource_listview_id = R.id.modules_information_listview;
		
		this.resource_listitem_id = R.layout.listitem_needpay;
		restoreFromLocal(1);
		
		reload_data();
		this.setupPullToRefresh();
	}
	@Override
	protected void reload_data(){
		this.get(String.format("order/order_list?userid=%d&status=notpay", AppSettings.userid), 1);
		
	}
	@Override
	protected void initData(Object result,int msgType){
		try{
			
			JSONArray list = ((JSONObject)result).getJSONArray("result");
			results = new JSONArray();
			for(int i=0;i<list.length();i++){
				JSONObject item = list.getJSONObject(i);
				JSONObject good = item.getJSONArray("goods").getJSONObject(0);
				good.put("order_id", item.getString("order_id"));
				good.put("order_total", item.getString("order_total"));
				results.put(i,good);
			}
			
			this.initList(results);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	protected void onListItemClick(final View view,final long index){
		
		if (_list!=null){
			
			Map<String,Object> map = _list.get((int) index);
			String order_id = map.get("order_id").toString();
			
			Intent intent = new Intent(this,OrderDetailActivity.class);
			intent.putExtra("order_id", order_id);
			startActivity(intent);
			
		}
	}
	
	@Override
	protected void setupListItem(ViewHolder holder,Map<String,Object> info){
		holder.title.setText(info.get("name").toString());
		holder.detail.setText(info.get("description").toString());
		holder.detail3.setText(info.get("order_total").toString());
		holder.detail4.setText(info.get("quantity").toString());
		
		com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(holder.image, info.get("pic_url").toString());
		holder.button1.setTag(info.get("order_id"));
		holder.btnDelete.setTag(info.get("order_id"));
		
	}
	@Override
	protected void initListItem(ViewHolder holder,View convertView){
		holder.title = (TextView)convertView.findViewById(R.id.txt_name);
		holder.detail = (TextView)convertView.findViewById(R.id.txt_description);
		holder.detail3 = (TextView)convertView.findViewById(R.id.txt_total);
		holder.detail4 = (TextView)convertView.findViewById(R.id.txt_quantity);
		
		holder.image = (ImageView)convertView.findViewById(R.id.img_picture);
		holder.button1 =(Button)convertView.findViewById(R.id.btn_buy);
		holder.btnDelete =(Button)convertView.findViewById(R.id.btn_delete);
		holder.btnDelete.setVisibility(View.GONE);
		convertView.setTag(holder);
		
		holder.button1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String id =(String)v.getTag();
				Intent intent = new Intent(NeedPayListActivity.this, NewOrderActivity.class);
				intent.putExtra("order_id", id);
				startActivity(intent);
			}});
		holder.btnDelete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String id =(String)v.getTag();
				deleteOrder(id);
			}});

		convertView.setOnTouchListener(new OnSwipeTouchListener(){

			@Override
			public void onSwipeRight(View v) {
				ViewHolder holder = (ViewHolder)v.getTag();
				String id = (String)holder.button1.getTag();
				holder.btnDelete.setVisibility(View.VISIBLE);
				
			}

			@Override
			public void onSwipeLeft(View v) {
				ViewHolder holder = (ViewHolder)v.getTag();
				String id = (String)holder.button1.getTag();
				holder.btnDelete.setVisibility(View.GONE);
				
			}
			
			
		});
	}
	private void deleteOrder(final String order_id){
		String url = String.format("order/order_del?userid=%d&orderid=%s", AppSettings.userid,order_id);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				processDeleteResult(result,order_id);
				
			}}.execute(url);
		
	}
	private void processDeleteResult(final String result,final String order_id){
		try{
			JSONObject json = new JSONObject(result);
			if (AppSettings.isSuccessJSON(json)){
				for(Map<String,Object> map : _list){
					if (map.get("order_id").toString().equals(order_id)){
						_list.remove(map);
						break;
					}
				}
				_adapter.notifyDataSetChanged();
			}
		}catch(Exception e){
			log(e);
		}
	}
	public abstract class OnSwipeTouchListener implements OnTouchListener {
		private View sender;
	    private final GestureDetector gestureDetector = new GestureDetector(NeedPayListActivity.this,new GestureListener());

	    public boolean onTouch(final View view, final MotionEvent motionEvent) {
	    	sender = view;
	        return gestureDetector.onTouchEvent(motionEvent);
	    }

	    
	    private final class GestureListener extends SimpleOnGestureListener implements OnGestureListener {

	        private static final int SWIPE_THRESHOLD = 100;
	        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

	        @Override
	        public boolean onDown(MotionEvent e) {
	            return true;
	        }

	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            boolean result = false;
	            try {
	                float diffY = e2.getY() - e1.getY();
	                float diffX = e2.getX() - e1.getX();
	                if (Math.abs(diffX) > Math.abs(diffY)) {
	                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffX > 0) {
	                            onSwipeRight(sender);
	                        } else {
	                            onSwipeLeft(sender);
	                        }
	                    }
	                } else {
	                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffY > 0) {
	                            onSwipeBottom(sender);
	                        } else {
	                            onSwipeTop(sender);
	                        }
	                    }
	                }
	            } catch (Exception exception) {
	                exception.printStackTrace();
	            }
	            return result;
	        }
	    }
		abstract public void onSwipeRight(final View v) ;

	    abstract public void onSwipeLeft(final View v); 

	    public void onSwipeTop(final View v) {
	    }

	    public void onSwipeBottom(final View v) {
	    }
	    
	}
	

}
