package cn.count.easydrive366;

import java.util.List;
import java.util.Timer;

import cn.count.easydrive366.components.HomeMenuItem;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.CheckUpdate;
import cn.count.easydriver366.base.HomeMenu;
import cn.count.easydriver366.base.Menus;
import cn.count.easydriver366.service.BackendService;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	private TableLayout _tableLayout;
	private List<HomeMenu> menus;
	private boolean _userWantQuit=false;
	private Timer _quitTimer;
	private ProgressDialog _dialog;
	PullToRefreshScrollView mPullRefreshScrollView;
	ScrollView mScrollView;
	View view;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
		view = inflater.inflate(R.layout.moudles_home_activity,container,false);
		
		AppSettings.restore_login_from_device(this.getActivity());
		startBackendService();
		setupMenu();
		String rightButtonTitle ="登录";
		if (AppSettings.isLogin){
			rightButtonTitle=this.getResources().getString(R.string.menu_settings);
		}
		((Button)view.findViewById(R.id.title_set_bn)).setText(rightButtonTitle);
		
		view.findViewById(R.id.title_set_bn).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				settingsButtonPress();
				
			}
			
		});
		view.findViewById(R.id.img_navigationbar_logo).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				logoClicked();
				
			}
			
		});
		
		mPullRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				new GetDataTask().execute();
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mPullRefreshScrollView.getWindowToken(), 0);
		new CheckUpdate(this.getActivity(),false);
		return view;
		//this.addSwipeToView(this.mPullRefreshScrollView);
		//com.koushikdutta.urlimageviewhelper.UrlImageViewHelper.setUrlDrawable(imageView, url);
	}
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		private boolean _needSleep=false;
		public GetDataTask(){
			super();
		}
		public GetDataTask(boolean needSleep){
			super();
			_needSleep = needSleep;
		}
		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			if (_needSleep){
				try {
					Thread.sleep(2000);
					
				} catch (InterruptedException e) {
				}
			}
			
			
			Intent intent = new Intent("cn.count.easydriver366.service.GetLatestReceiverr");
			intent.putExtra("isInApp", true);
			HomeFragment.this.getActivity().sendBroadcast(intent);
			
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here
			_dialog.dismiss();
			
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute(){
			_dialog = new ProgressDialog(HomeFragment.this.getActivity());
			_dialog.setMessage(getResources().getString(R.string.app_loading));
			_dialog.show();
		}
		 
	}
	private void startBackendService(){
		if (!isServiceRunning()){
			Intent service = new Intent(HomeFragment.this.getActivity(),BackendService.class);
			HomeFragment.this.getActivity().startService(service);
		}
		
		
	}
	private boolean isServiceRunning(){
		boolean result = false;
		ActivityManager mActivityManager = (ActivityManager)this.getActivity().getSystemService(HomeFragment.this.getActivity().ACTIVITY_SERVICE);
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
		View v = view.findViewById(R.id.btn_phone);
		if (v != null) {
			v.setVisibility(View.GONE);
		}
		
		_tableLayout = (TableLayout)view.findViewById(R.id.tablelout_in_home_activity);
		initMenuItems();
		fillMenu();
		new GetDataTask(true).execute();
		
	}
	private void addTableRow(HomeMenu menu){
		TableRow tr = new TableRow(this.getActivity());
		HomeMenuItem item = new HomeMenuItem(this.getActivity(),null);
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
		menus = (new Menus(this.getActivity())).getMenus();
	}
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_welcome, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//this.logout();
		Intent intent = new Intent(this,SettingsActivity.class);
		startActivity(intent);
		return true;
	}
	*/
	public void logout(){
		AppSettings.logout(this.getActivity());
		Intent intent = new Intent(HomeFragment.this.getActivity(),WelcomeActivity.class);
		startActivity(intent);
		
	}
	private void settingsButtonPress(){
		
		
		
		if (AppSettings.isLogin){
			Intent intent = new Intent(this.getActivity(),SettingsActivity.class);
			
			startActivityForResult(intent,1);
		}else{
			Intent intent = new Intent(this.getActivity(),WelcomeActivity.class);
			startActivityForResult(intent,2);
		}
		
		
		
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		String rightButtonTitle ="登录";
		if (AppSettings.isLogin){
			rightButtonTitle=this.getResources().getString(R.string.menu_settings);
		}
		((Button)view.findViewById(R.id.title_set_bn)).setText(rightButtonTitle);
		new GetDataTask().execute();
		
	   
	  }
	private void showDialog(int title, CharSequence message) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
	    builder.setTitle(title);
	    builder.setMessage(message);
	    builder.setPositiveButton("OK", null);
	    builder.show();
	  }
	
	private void logoClicked()
	{
		
	}
	
}
