package cn.count.easydrive366;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.count.easydrive366.article.ArticleListFragment;
import cn.count.easydrive366.goods.GoodsListFragment;
import cn.count.easydrive366.provider.ProviderListFragment;
import cn.count.easydrive366.user.SettingsFragment;



import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static MainActivity instance = null;  
    private ViewPager mTabPager;//声明对象  
    private ImageView _imgHome, _imgGoods, _imgProvider, _imgArticle,_imgSettings;  
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private HomeFragment _home;
    private GoodsListFragment _goods;
    private ProviderListFragment _provider;
    private ArticleListFragment _article;
    private SettingsFragment _settings;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_activity);  
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 启动activity时不自动弹出软键盘  
        
        instance = this;  
        //this.getActionBar().setDisplayShowTitleEnabled(true);
        //this.setTitle("EasyDrive366");
        //this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        
        mTabPager = (ViewPager) findViewById(R.id.tabpager);  
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());  
        mTabPager.setOffscreenPageLimit(5);
        
        _imgHome = (ImageView) findViewById(R.id.img_home);  
        _imgGoods = (ImageView) findViewById(R.id.img_goods);  
        _imgProvider = (ImageView) findViewById(R.id.img_provider);  
        _imgArticle = (ImageView) findViewById(R.id.img_article);  
        _imgSettings  = (ImageView) findViewById(R.id.img_settings);
        
  
        findViewById(R.id.layout_home).setOnClickListener(new MyOnClickListener(0));  
        findViewById(R.id.layout_goods).setOnClickListener(new MyOnClickListener(1));  
        findViewById(R.id.layout_provider).setOnClickListener(new MyOnClickListener(2));  
        findViewById(R.id.layout_article).setOnClickListener(new MyOnClickListener(3));
        findViewById(R.id.layout_settings).setOnClickListener(new MyOnClickListener(4));
  
        
        mSectionsPagerAdapter= new SectionsPagerAdapter(
				getSupportFragmentManager());
        mTabPager.setAdapter(mSectionsPagerAdapter);
        
        
    }  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main_menu, menu);
      return true;
    } 
    
	/** 
     * 头标点击监听 
     */  
    public class MyOnClickListener implements View.OnClickListener {  
        private int index = 0;  
  
        public MyOnClickListener(int i) {  
            index = i;  
        }  
  
        public void onClick(View v) {  
        	
            mTabPager.setCurrentItem(index);  
            Log.d("Tab", String.format("Select=%d", index));
        }  
    };  
  
    /* 
     * 页卡切换监听(原作者:D.Winter) 
     */  
    public class MyOnPageChangeListener implements OnPageChangeListener {  
        public void onPageSelected(int arg0) {  
            
            switch (arg0) {  
            case 0:  
                _imgHome.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_weixin_pressed));  
                _imgGoods.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_address_normal)); 
                _imgProvider.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_find_frd_normal)); 
                _imgArticle.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                _imgSettings.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                break;
            case 1:  
            	_imgHome.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_weixin_normal));  
                _imgGoods.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_address_pressed)); 
                _imgProvider.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_find_frd_normal)); 
                _imgArticle.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                _imgSettings.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                
                break;  
            case 2:  
            	_imgHome.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_weixin_normal));  
                _imgGoods.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_address_normal)); 
                _imgProvider.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_find_frd_pressed)); 
                _imgArticle.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                _imgSettings.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal));  
                
                break;  
            case 3:  
            	_imgHome.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_weixin_normal));  
                _imgGoods.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_address_normal)); 
                _imgProvider.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_find_frd_normal)); 
                _imgArticle.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_pressed)); 
                _imgSettings.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                
                break; 
            case 4:  
            	_imgHome.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_weixin_normal));  
                _imgGoods.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_address_normal)); 
                _imgProvider.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_find_frd_normal)); 
                _imgArticle.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_normal)); 
                _imgSettings.setImageDrawable(getResources().getDrawable(  
                        R.drawable.tab_settings_pressed)); 
                
                break; 
            }  
            
          
        }

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
			
		}   
    }
    /**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d("Tab", String.format("Wanted=%d", position));
			if (position==0){
				if (_home==null){
					_home = new HomeFragment();
				}
				return _home;
			}else if (position ==1){
				if (_goods==null){
					_goods = new GoodsListFragment();
				}
				return _goods;
			}else if (position ==2){
				if (_provider==null){
					_provider = new ProviderListFragment();
				}
				return _provider;
			}else if (position ==3){
				if (_article==null){
					_article = new ArticleListFragment();
				}
				return _article;
			}else if (position ==4){
				if (_settings==null){
					_settings = new SettingsFragment();
				}
				return _settings;
			}else{
				ComingSoonFragment f = new ComingSoonFragment();
				return f;
			}
			
		}

		@Override
		public int getCount() {
			
			return 5;
		}
		/*
		@Override
		public int getItemPosition(Object object) {
			return this.POSITION_NONE;
			
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			return "Title";
			
		}
		*/
	}
	
}
