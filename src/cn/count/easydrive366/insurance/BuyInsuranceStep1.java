package cn.count.easydrive366.insurance;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.AppSettings;
import cn.count.easydriver366.base.BaseHttpActivity;
import cn.count.easydriver366.base.HttpExecuteGetTask;

public class BuyInsuranceStep1 extends BaseHttpActivity {
	private WebView _webView;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modules_browser_activity);
		this.setupTitle("在线购买保险", "第一步");
		
		_webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = _webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		_webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		/*
		_webView.addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				mHandler.post(new Runnable() {
					public void run() {
						_webView.loadUrl("javascript:wave()");
					}
				});
			}
		}, "Browser");
		*/
		
		//_webView.loadUrl(url);
		load_data();
		 
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && _webView.canGoBack()) {       
            _webView.goBack();       
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }
	private void load_data(){
		beginHttp();
		String url = String.format("ins/carins_intro?userid=%d", AppSettings.userid);
		new HttpExecuteGetTask(){

			@Override
			protected void onPostExecute(String result) {
				endHttp();
				process_data(result);
				
			}}.execute(url);
	}
	private void process_data(final String result){
		try{
			JSONObject json =AppSettings.getSuccessJSON(result,this);
			if (json!=null){
				_webView.loadUrl(json.getString("web_url"));
			}
		}
		catch(Exception e){
			log(e);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add("下一步");
		//MenuInflater inflater = this.getMenuInflater();
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this,BuyInsuranceStep2.class));
		return super.onOptionsItemSelected(item);
	}
	

}