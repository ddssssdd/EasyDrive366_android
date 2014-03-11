package cn.count.easydrive366.goods;

import org.json.JSONArray;
import org.json.JSONObject;


import com.sina.weibo.sdk.api.share.BaseResponse;

import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;

import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

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

import cn.count.easydrive366.R;
import cn.count.easydrive366.comments.ItemCommentsActivity;
import cn.count.easydrive366.order.NewOrderActivity;
import cn.count.easydrive366.share.FavorController;
import cn.count.easydrive366.share.ShareController;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class GoodsDetailActivity extends BaseHttpActivity implements Response {
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
	private FavorController _favor;

	private ShareController _share;
	private IWeiboShareAPI _weibo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_goods_detail);
		this.setupLeftButton();
	
		_favor = new FavorController(this);
		_goods_id = getIntent().getIntExtra("id", 0);
		_weibo = WeiboShareSDK.createWeiboAPI(this, AppSettings.SINA_WEIBO_ID);
		_share = new ShareController(this,_weibo);
		
		if (savedInstanceState != null) {
			_weibo.handleWeiboResponse(getIntent(), this);
		}
		
		if (_goods_id > 0) {
			beginHttp();
			new HttpExecuteGetTask() {

				@Override
				protected void onPostExecute(String result) {
					endHttp();
					load_view(result);

				}
			}.execute(String.format("goods/get_goods_info?userid=%d&id=%d",
					AppSettings.userid, _goods_id));

		}
		findViewById(R.id.layout_rating).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						openRating();

					}
				});
		findViewById(R.id.btn_buy).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						btnBuyPressed();

					}
				});
		txtBuyer = (TextView) findViewById(R.id.txt_buyer);
		txtDiscount = (TextView) findViewById(R.id.txt_discount);
		txtStand_price = (TextView) findViewById(R.id.txt_stand_price);
		txtPrice = (TextView) findViewById(R.id.txt_price);
		txtDescription = (TextView) findViewById(R.id.txt_description);
		txtRate = (TextView) findViewById(R.id.txt_rate);
		txtIndex = (TextView) findViewById(R.id.txt_index);
		txtVoternum = (TextView) findViewById(R.id.txt_voternum);
		imgPicture = (ImageView) findViewById(R.id.img_picture);
		rateBar = (RatingBar) findViewById(R.id.rate_bar);
	}

	private void load_view(final String result) {
		_json = AppSettings.getSuccessJSON(result,this);
		if (_json == null) return;
		try {
			_id = _json.getString("id");
			txtBuyer.setText(_json.getString("buyer"));
			txtDiscount.setText(_json.getString("discount"));
			txtStand_price.setText(_json.getString("stand_price"));
			txtStand_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			txtPrice.setText(_json.getString("price"));
			txtDescription.setText(_json.getString("description"));
			txtRate.setText(_json.getString("star"));
			txtVoternum.setText(_json.getString("star_voternum"));
			rateBar.setRating(_json.getInt("star_num"));
			_albums = _json.getJSONArray("album");
			_share.setContent(_json.getString("share_title"),
					_json.getString("share_intro"),
					_json.getString("share_url"));
			_index = 0;
			_favor.init(_json.getInt("is_favor"), _menuFavor, _id, "GDS");
			showPicture();
			this.addSwipeToView(imgPicture);
		} catch (Exception e) {
			log(e);
		}
	}

	private void btnBuyPressed() {

		Intent intent = new Intent(this, NewOrderActivity.class);
		intent.putExtra("id", _id);
		startActivity(intent);
		finish();
	}

	private void openRating() {
		Intent intent = new Intent(this, ItemCommentsActivity.class);
		intent.putExtra("id", String.valueOf(_goods_id));
		intent.putExtra("type", "goods");
		startActivity(intent);
	}

	private void showPicture() {
		if (_albums != null && _albums.length() > 0) {
			try {

				txtIndex.setText(String.format("%d/%d", _index + 1,
						_albums.length()));
				String url = _albums.getJSONObject(_index).getString("pic_url");
				com.koushikdutta.urlimageviewhelper.UrlImageViewHelper
						.setUrlDrawable(imgPicture, url);
			} catch (Exception e) {
				log(e);
			}
		}

	}

	@Override
	public void onLeftSwipe() {
		_index--;
		if (_index < 0)
			_index = _albums.length() - 1;
		showPicture();
	}

	@Override
	public void onRightSwipe() {
		_index++;
		if (_index > _albums.length() - 1)
			_index = 0;
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
		if (R.id.action_favor == item.getItemId()) {

		} else {
			
			 _share.share(item.getItemId());
		}

		return super.onOptionsItemSelected(item);
	}

	

	@Override
	protected void onNewIntent(Intent intent) {
		//super.onNewIntent(intent);

		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		_weibo.handleWeiboResponse(intent, this);
	}

	@Override
	public void onResponse(BaseResponse arg0) {
		switch (arg0.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			break;
		}

	}

}
