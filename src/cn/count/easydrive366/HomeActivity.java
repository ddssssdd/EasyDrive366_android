package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;





import cn.count.easydrive366.components.HomeMenuItem;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.AppTools;
import cn.count.easydriver366.base.HomeMenu;
import cn.count.easydriver366.base.Menus;
import cn.count.easydriver366.service.BackendService;
import cn.count.easydriver366.service.GetLatestReceiver;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private TableLayout _tableLayout;
	private List<HomeMenu> menus;
	private boolean _userWantQuit=false;
	private Timer _quitTimer;
	
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.moudles_home_activity);
		AppSettings.restore_login_from_device(this);
		startBackendService();
		setupMenu();
		
		((Button)findViewById(R.id.title_set_bn)).setText("注销");
		
		findViewById(R.id.title_set_bn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				settingsButtonPress();
				
			}
			
		});
		
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		
	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			/*
			try {
				Thread.sleep(4000);
				
			} catch (InterruptedException e) {
			}
			*/
			Intent intent = new Intent("cn.count.easydriver366.service.GetLatestReceiverr");
			intent.putExtra("isInApp", true);
			sendBroadcast(intent);
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
	private void startBackendService(){
		if (!isServiceRunning()){
			Intent service = new Intent(this,BackendService.class);
			this.startService(service);
		}
		
		
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
		View v = findViewById(R.id.btn_phone);
		if (v != null) {
			v.setVisibility(View.GONE);
		}
		
		_tableLayout = (TableLayout)findViewById(R.id.tablelout_in_home_activity);
		initMenuItems();
		fillMenu();
		new GetDataTask().execute();
		
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
		menus = (new Menus(this)).getMenus();
	}
	@Override
	public void onBackPressed() {
		if (_userWantQuit){
			this.finish();
			System.exit(0);
		}else{
			Toast.makeText(this, this.getResources().getString(R.string.exit_question), Toast.LENGTH_LONG).show();
			_userWantQuit = true;
			if (_quitTimer==null){
				_quitTimer = new Timer();
			}
			TimerTask task = new TimerTask(){

				@Override
				public void run() {
					_userWantQuit = false;
					
				};
			};
			_quitTimer.schedule(task, 2000);

							
		}
		
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
		
		return true;
	}
	public void logout(){
		AppSettings.logout(this);
		Intent intent = new Intent(this,WelcomeActivity.class);
		startActivity(intent);
		
	}
	private void settingsButtonPress(){
		
		//startActivity(AppTools.getPhoneAction("18963023080"));
		//startActivity(AppTools.getBrowserAction("http://www.baidu.com"));
		new AlertDialog.Builder(this).setTitle(R.string.app_name)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setMessage(String.format(getResources().getString(R.string.quit_question),AppSettings.username)).setPositiveButton(this.getResources().getString(R.string.ok), new  DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				logout();
				
			}})
		.setNegativeButton(this.getResources().getString(R.string.cancel), null).show();
	}
	
}
