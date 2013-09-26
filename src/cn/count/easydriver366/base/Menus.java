package cn.count.easydriver366.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import cn.count.easydrive366.BusinessInsuranceActivity;
import cn.count.easydrive366.CarRegistrationActivity;
import cn.count.easydrive366.CarServiceActivity;
import cn.count.easydrive366.CompulsoryInsuranceActivity;
import cn.count.easydrive366.DriverLicenseActivity;
import cn.count.easydrive366.HelpCallActivity;
import cn.count.easydrive366.InformationActivity;
import cn.count.easydrive366.InsuranceProcessListActivity;
import cn.count.easydrive366.MaintainActivity;
import cn.count.easydrive366.R;
import cn.count.easydrive366.RescueActivity;
import cn.count.easydrive366.TaxForCarShipActivity;

public class Menus {
	private Activity _activity;
	private List<HomeMenu> menus;
	public Menus(Activity activity){
		_activity = activity;
		initMenuItems();
	}
	private void initMenuItems(){
		menus = new ArrayList<HomeMenu>();
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_01),"01",InformationActivity.class));
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_02),"02",HelpCallActivity.class));
		//menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_03),"03",RescueActivity.class));
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_04),"04",MaintainActivity.class));	
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_06),"06",CarRegistrationActivity.class));
		
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_12),"12",CarServiceActivity.class));
		
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_05),"05",DriverLicenseActivity.class));
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_07),"07",TaxForCarShipActivity.class));
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_08),"08",CompulsoryInsuranceActivity.class));
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_09),"09",BusinessInsuranceActivity.class));	
		menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_10),"10",InsuranceProcessListActivity.class));
		//menus.add(new HomeMenu(_activity.getResources().getString(R.string.key_11),"11",null));
	}
	public List<HomeMenu> getMenus(){
		return menus;
	}
	public HomeMenu findMenuByKey(final String key){
		for(HomeMenu item : menus){
			if (item.key.equals(key)){
				return item;
			}
		}
		return null;
	}
	public Class findMenuItemClassByKey(final String key){
		HomeMenu menu= findMenuByKey(key);
		if (menu!=null){
			return menu.activityClass;
		}else{
			return null;
		}
	}
	
}
