package cn.count.easydrive366.components;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.count.easydrive366.R;
import cn.count.easydriver366.base.HomeMenu;

public class HomeMenuItem extends LinearLayout {
	private LayoutInflater _inflater =null;
	private Context _context;
	private HomeMenu _menuItem;
	private TextView _title;
	private TextView _description;
	public HomeMenuItem(Context context,AttributeSet attrs){
		super(context,attrs);
		_inflater = LayoutInflater.from(context);
		_context = context;
		_inflater.inflate(R.layout.home_menu_item, this);
		_title = (TextView)findViewById(R.id.listitem_title);
		_description =(TextView)findViewById(R.id.listitem_content);
		this.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				clickHandler();
				
			}
			
		});
	}
	public void setData(HomeMenu menuItem){
		_menuItem = menuItem;
		_title.setText(_menuItem.name);
		_description.setText(_menuItem.description);
	}
	private void clickHandler(){
		if ((_menuItem!=null) && (_menuItem.activityClass!=null)){
			
			Intent intent=new Intent(_context,_menuItem.activityClass);
			_context.startActivity(intent);
		}
	}
}
