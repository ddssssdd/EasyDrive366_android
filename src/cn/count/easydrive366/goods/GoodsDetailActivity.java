package cn.count.easydrive366.goods;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.count.easydrive366.R;
import cn.count.easydrive366.comments.ItemCommentsActivity;
import cn.count.easydrive366.order.NewOrderActivity;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class GoodsDetailActivity extends BaseHttpActivity {
	private int _goods_id;
	private TextView txtBuyer;
	private TextView txtDiscount;
	private TextView txtStand_price;
	private TextView txtPrice;
	private TextView txtDescription;
	private TextView txtRate;
	private TextView txtVoternum;
	private TextView txtIndex;
	private ImageView imgPicture;
	private Button btnButy;
	private RatingBar rateBar;
	private JSONObject _json;
	private JSONArray _albums;
	private int _index = 0;
	private String _id;
	private MenuItem _menuFavor;
	
	private IWXAPI api;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_goods_detail);
		
		api = WXAPIFactory.createWXAPI(this, AppSettings.WEIXIN_ID);
    	api.registerApp(AppSettings.WEIXIN_ID);
		_goods_id = getIntent().getIntExtra("id", 0);
		if (_goods_id>0){
			beginHttp();
			new HttpExecuteGetTask(){

				@Override
				protected void onPostExecute(String result) {
					endHttp();
					load_view(result);
					
				}}.execute(String.format("goods/get_goods_info?userid=%d&id=%d", AppSettings.userid,_goods_id));
			
		}
		findViewById(R.id.layout_rating).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openRating();
				
			}
		});
		findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnBuyPressed();
				
			}
		});
		txtBuyer = (TextView)findViewById(R.id.txt_buyer);
		txtDiscount = (TextView)findViewById(R.id.txt_discount);
		txtStand_price = (TextView)findViewById(R.id.txt_stand_price);
		txtPrice = (TextView)findViewById(R.id.txt_price);
		txtDescription = (TextView)findViewById(R.id.txt_description);
		txtRate = (TextView)findViewById(R.id.txt_rate);
		txtIndex = (TextView)findViewById(R.id.txt_index);
		txtVoternum = (TextView)findViewById(R.id.txt_voternum);
		imgPicture = (ImageView)findViewById(R.id.img_picture);
		rateBar = (RatingBar)findViewById(R.id.rate_bar);
	}
	private void load_view(final String result){
		_json = AppSettings.getSuccessJSON(result);
		
		try{
			_id = _json.getString("id");
			txtBuyer.setText(_json.getString("buyer"));
			txtDiscount.setText(_json.getString("discount"));
			txtStand_price.setText(_json.getString("stand_price"));
			txtStand_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
			txtPrice.setText(_json.getString("price"));
			txtDescription.setText(_json.getString("description"));
			txtRate.setText(_json.getString("star"));
			txtVoternum.setText(_json.getString("star_voternum"));
			rateBar.setRating(3/*_json.getInt("star")*/);
			_albums = _json.getJSONArray("album");
			
			_index = 0;
			showPicture();
			this.addSwipeToView(imgPicture);
		}catch(Exception e){
			log(e);
		}
	}
	private void btnBuyPressed(){
		
		Intent intent = new Intent(
				this,
				NewOrderActivity.class);
		intent.putExtra("id", _id);
		startActivity(intent);
	}
	private void openRating(){
		Intent intent =new Intent(this,ItemCommentsActivity.class);
		intent.putExtra("id",String.valueOf(_goods_id));
		intent.putExtra("type", "goods");
		startActivity(intent);
	}
	private void showPicture(){
		if (_albums!=null && _albums.length()>0){
			try{
				
				txtIndex.setText(String.format("%d/%d", _index+1,_albums.length()));
				String url = _albums.getJSONObject(_index).getString("pic_url");
				com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(imgPicture, url);
			}catch(Exception e){
				log(e);
			}
		}
		
	}
	@Override
	public void onLeftSwipe(){
    	_index--;
    	if (_index<0)
    		_index = _albums.length()-1;
    	showPicture();
    }
	@Override
    public void onRightSwipe(){
    	_index++;
    	if (_index>_albums.length()-1)
    		_index=0;
    	showPicture();
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
		switch(item.getItemId()){
		case R.id.action_favor:
			break;
		case R.id.action_share_weixin:
			doShare();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void doShare(){
		WXTextObject text = new WXTextObject();
		text.text="Share ...";
		WXMediaMessage msg = new WXMediaMessage();
		msg.mediaObject = text;
		msg.description = "Share something";
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene =  SendMessageToWX.Req.WXSceneTimeline ;//SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
}
