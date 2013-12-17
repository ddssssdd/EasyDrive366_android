package cn.count.easydrive366.goods;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.count.easydrive366.R;
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
	private ImageView imgPicture;
	private RatingBar rateBar;
	private JSONObject _json;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_goods_detail);
		this.setupPhoneButtonInVisible();
		this.setupRightButtonWithText("buy");
		_goods_id = getIntent().getIntExtra("id", 0);
		if (_goods_id>0){
			new HttpExecuteGetTask(){

				@Override
				protected void onPostExecute(String result) {
					load_view(result);
					
				}}.execute(String.format("goods/get_goods_info?userid=%d&id=%d", AppSettings.userid,_goods_id));
			
		}
	}
	private void load_view(final String result){
		_json = AppSettings.getSuccessJSON(result);
		txtBuyer = (TextView)findViewById(R.id.txt_buyer);
		txtDiscount = (TextView)findViewById(R.id.txt_discount);
		txtStand_price = (TextView)findViewById(R.id.txt_stand_price);
		txtPrice = (TextView)findViewById(R.id.txt_price);
		txtDescription = (TextView)findViewById(R.id.txt_description);
		txtRate = (TextView)findViewById(R.id.txt_rate);
		txtVoternum = (TextView)findViewById(R.id.txt_voternum);
		imgPicture = (ImageView)findViewById(R.id.img_picture);
		rateBar = (RatingBar)findViewById(R.id.rate_bar);
		try{
			txtBuyer.setText(_json.getString("buyer"));
			txtDiscount.setText(_json.getString("discount"));
			txtStand_price.setText(_json.getString("stand_price"));
			txtPrice.setText(_json.getString("price"));
			txtDescription.setText(_json.getString("description"));
			txtRate.setText(_json.getString("star"));
			txtVoternum.setText(_json.getString("star_voternum"));
			JSONArray album = _json.getJSONArray("album");
			String url = album.getJSONObject(0).getString("pic_url");
			com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(imgPicture, url);
			
		}catch(Exception e){
			log(e);
		}
	}

}
