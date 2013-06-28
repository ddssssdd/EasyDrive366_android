package cn.count.easydrive366;

import cn.count.easydriver366.base.BaseHttpActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowserActivity extends BaseHttpActivity {
	private WebView _webView;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.modules_browser_activity);
		this.setupLeftButton();
		
		_webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = _webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		_webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		_webView.addJavascriptInterface(new Object() {
			public void clickOnAndroid() {
				mHandler.post(new Runnable() {
					public void run() {
						_webView.loadUrl("javascript:wave()");
					}
				});
			}
		}, "Browser");
		Intent intent = this.getIntent();
		String url = intent.getStringExtra("url");
		if (url==null){
			url = "http://www.yijia366.com";
		}else{
			if (!url.startsWith("http://")){
				url = "http://"+url;
			}
		}
		_webView.loadUrl(url);
		if (intent.getStringExtra("title")!=null){
			this.setBarTitle(intent.getStringExtra("title"));
			this.setupRightButtonWithText("评论");
		}else{
			this.setRightButtonInVisible();
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && _webView.canGoBack()) {       
            _webView.goBack();       
                   return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }
	@Override 
	protected void onRightButtonPress() {
		Intent comments = new Intent(this,ArticleCommentsActivity.class);
		Intent intent =this.getIntent();
		comments.putExtra("id", intent.getStringExtra("id"));
		comments.putExtra("column_id", intent.getStringExtra("column_id"));
		comments.putExtra("title", intent.getStringExtra("title"));
		this.startActivity(comments);
	}
}
