package cn.count.easydriver366.base;

public class HomeMenu {
	public String name;
	public String description;
	public String lastUpdate;
	public String key;
	public String phone;
	public String company;
	public Class activityClass;
	public HomeMenu(final String n,final String k,Class aClass){
		this.name= n;
		this.key =k;
		this.activityClass = aClass;
	}
}
