package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.List;



import cn.count.easydrive366.components.HomeMenuItem;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.HomeMenu;
import cn.count.easydriver366.service.BackendService;
import cn.count.easydriver366.service.GetLatestReceiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;

public class HomeActivity extends Activity {
	private TableLayout _tableLayout;
	private List<HomeMenu> menus;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moudles_home_activity);
		startBackendService();
		setupMenu();
		findViewById(R.id.title_set_bn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				settingsButtonPress();
				
			}
			
		});
	}
	private void startBackendService(){
		if (!isServiceRunning()){
			Intent service = new Intent(this,BackendService.class);
			this.startService(service);
		}
		
		GetLatestReceiver receiver = new GetLatestReceiver();
		receiver.run();
			
		
	}
	private boolean isServiceRunning(){
		boolean result = false;
		ActivityManager mActivityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList=mActivityManager.getRunningServices(50);
		final String serviceName =  "cn.count.easydriver366.service.BackendService";
		for(int i=0;i<mServiceList.size();i++){
			if (mServiceList.get(i).service.getClassName().equals(serviceName)){
				result = true;
				break;
			}
		}
		return result;
	}
	private void setupMenu(){
		_tableLayout = (TableLayout)findViewById(R.id.tablelout_in_home_activity);
		initMenuItems();
		fillMenu();
		
	}
	private void addTableRow(HomeMenu menu){
		TableRow tr = new TableRow(this);
		HomeMenuItem item = new HomeMenuItem(this,null);
		item.setData(menu);
		tr.addView(item);
		_tableLayout.addView(tr);
	}
	private void fillMenu(){
		for(int i=0;i<menus.size();i++){
			HomeMenu menu = menus.get(i);
			addTableRow(menu);
		}
	}
	
	private void initMenuItems(){
		menus = new ArrayList<HomeMenu>();
		menus.add(new HomeMenu("01","01",InformationActivity.class));
		menus.add(new HomeMenu("02","02",HelpCallActivity.class));
		menus.add(new HomeMenu("03","03",RescueActivity.class));
		menus.add(new HomeMenu("04","04",MaintainActivity.class));
		menus.add(new HomeMenu("05","05",DriverLicenseActivity.class));
		menus.add(new HomeMenu("06","06",CarRegistrationActivity.class));
		menus.add(new HomeMenu("07","07",TaxForCarShipActivity.class));
		menus.add(new HomeMenu("08","08",CompulsoryInsuranceActivity.class));
		menus.add(new HomeMenu("09","09",BusinessInsuranceActivity.class));
		
		menus.add(new HomeMenu("10","10",null));
		menus.add(new HomeMenu("11","11",null));
	}
	@Override
	public void onBackPressed() {
		//nothing;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		this.logout();
		Intent intent = new Intent(this,WelcomeActivity.class);
		startActivity(intent);
		return true;
	}
	public void logout(){
		SharedPreferences prefs =getSharedPreferences(AppSettings.AppTile, MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString("username","");
		editor.putInt("userid", 0);
		editor.putBoolean("islogin", false);
		editor.commit();
	}
	private void settingsButtonPress(){
		Intent intent = new Intent();
		intent.putExtra("key", "01");
		intent.putExtra("description","dd");
		intent.putExtra("updated_time", "aa");
		intent.setAction("cn.count.easydrive366.components.HomeMenuItem$LatestInformationReceiver");
		this.sendBroadcast(intent);
	}
	
}
