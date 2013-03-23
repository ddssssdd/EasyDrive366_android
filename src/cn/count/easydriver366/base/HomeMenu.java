package cn.count.easydriver366.base;

public class HomeMenu {
	public String name;
	public String description;
	public String lastUpdate;
	public String key;
	public String phone;
	public String company;
	public Class activityClass;
	public HomeMenu(final String name,final String key,Class aClass){
		this.name= name;
		this.key =key;
		this.activityClass = aClass;
	}
}
