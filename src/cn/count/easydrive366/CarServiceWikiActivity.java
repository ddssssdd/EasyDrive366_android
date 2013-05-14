package cn.count.easydrive366;

import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.TextView;
import cn.count.easydrive366.BaseListViewActivity.ViewHolder;
import cn.count.easydriver366.base.AppSettings;

public class CarServiceWikiActivity extends BaseListViewActivity {
	private String _shopName;
	private String _address;
	private String _phone;
	private String _description;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modules_carservice_wiki_activity);
		
		this.setRightButtonInVisible();
		this.setupLeftButton();
		((TextView)findViewById(R.id.txt_rescue_ShopName)).setText("敬请期待");
		
	}
	@Override
	protected void initData(Object result, int msgType) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void setupListItem(ViewHolder holder, Map<String, Object> info) {
		// TODO Auto-generated method stub
		
	}
	

}
