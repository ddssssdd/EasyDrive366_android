package cn.count.easydriver366.base;

import cn.count.easydrive366.components.HomeMenuItem;

public class HomeMenu {
	public String name;
	public String description;
	public String lastUpdate;
	public String key;
	public String phone;
	public String company;
	public Class activityClass;
	public HomeMenuItem menuItem;
	public int ImageId;
	public HomeMenu(final String name,final String key,Class aClass){
		this.name= name;
		this.key =key;
		this.activityClass = aClass;
		
	}
	public HomeMenu(final String name,final String key,Class aClass,final int resourceId){
		this.name= name;
		this.key =key;
		this.activityClass = aClass;
		this.ImageId = resourceId;
	}
}
