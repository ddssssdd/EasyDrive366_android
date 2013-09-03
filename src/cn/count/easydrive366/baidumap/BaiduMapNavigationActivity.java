package cn.count.easydrive366.baidumap;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import cn.count.easydrive366.R;

import cn.count.easydrive366.baidumap.ShowLocationActivity.ShopLocation;
import cn.count.easydrive366.baidumap.ShowLocationActivity.ShopOverlay;
import cn.count.easydriver366.base.BaseHttpActivity;

public class BaiduMapNavigationActivity extends BaseHttpActivity {
	private BMapManager _mapManager = null;
	private MapView _mapView = null;
	public LocationClient mLocationClient = null;
	private LocationData locData = null;
	private MyLocationOverlay myLocationOverlay = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	private boolean isFirstLoc = false;
	
	private Button button =null;
	private MapView.LayoutParams layoutParam = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init map step1
		_mapManager = new BMapManager(getApplication());
		_mapManager.init("30d50073a606ac3ce0b7f8a187e8248b", null);

		setContentView(R.layout.modules_map_showlocation_activity);
		
		this.setRightButtonInVisible(); 
		this.setupLeftButton();
		this.setupPhoneButtonInVisible();
		 

		// init map step2
		_mapView = (MapView) findViewById(R.id.bmapsView);
		_mapView.setBuiltInZoomControls(true);
		MapController mapController = _mapView.getController();
		GeoPoint point = new GeoPoint((int) (37.10052 * 1E6),
				(int) (120.404586 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mapController.setCenter(point);// 设置地图中心点
		mapController.setZoom(16);// 设置地图zoom级别
		
		
		button = new Button(this);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				_mapView.removeView(button);
				
			}
			
		});
		
		locData = new LocationData();
		myLocationOverlay = new MyLocationOverlay(_mapView);
		myLocationOverlay.setData(locData);
		_mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		_mapView.refresh();
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
		mLocationClient.setAK("30d50073a606ac3ce0b7f8a187e8248b");
		mLocationClient.registerLocationListener( myListener );    //注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");//返回的定位结果包含地址信息
		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);//禁止启用缓存定位
		option.setPoiNumber(5);	//最多返回POI个数	
		option.setPoiDistance(1000); //poi查询距离		
		option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else 
			Log.d("LocSDK3", "locClient is null or not started");
		addOverlay();
	}
	private void addOverlay()
	{
		ShopOverlay overlay = new ShopOverlay(this.getResources().getDrawable(R.drawable.icon_gcoding),_mapView);
		String name = getIntent().getStringExtra("name");
		String description = getIntent().getStringExtra("description");
		double latitude = getIntent().getDoubleExtra("latitude", 0);
		double longtitude = getIntent().getDoubleExtra("longtitude", 0);
		GeoPoint p = new GeoPoint((int)(latitude*1e6),(int)(longtitude*1e6));
		OverlayItem item = new OverlayItem(p,name,description);
		overlay.addItem(item);
		_mapView.getOverlays().add(overlay);
		_mapView.refresh();
		
	}

	@Override
	protected void onDestroy() {
		_mapView.destroy();
		if (_mapManager != null) {
			_mapManager.destroy();
			_mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		_mapView.onPause();
		if (_mapManager != null) {
			_mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		_mapView.onResume();
		if (_mapManager != null) {
			_mapManager.start();
		}
		super.onResume();
	}
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			myLocationOverlay.setData(locData);
			_mapView.refresh();
			if (!isFirstLoc){
				_mapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),(int)(locData.longitude*1e6)));
				isFirstLoc = true;
				
			}
			
		}
	
		public void onReceivePoi(BDLocation poiLocation) {
				if (poiLocation == null){
					return ;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Poi time : ");
				sb.append(poiLocation.getTime());
				sb.append("\nerror code : ");
				sb.append(poiLocation.getLocType());
				sb.append("\nlatitude : ");
				sb.append(poiLocation.getLatitude());
				sb.append("\nlontitude : ");
				sb.append(poiLocation.getLongitude());
				sb.append("\nradius : ");
				sb.append(poiLocation.getRadius());
				if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation){
					sb.append("\naddr : ");
					sb.append(poiLocation.getAddrStr());
				} 
				if(poiLocation.hasPoi()){
					sb.append("\nPoi:");
					sb.append(poiLocation.getPoi());
				}else{				
					sb.append("noPoi information");
				}
				Log.d("EasyDrive366", sb.toString());
			}

	}
	public class ShopOverlay extends ItemizedOverlay<OverlayItem>{

		public ShopOverlay(Drawable mark, MapView mapView) {
			super(mark, mapView);			
		}
		
		@Override
		protected boolean onTap(int index) {
			OverlayItem item = getItem(index);
			_mapView.removeView(button);
			button.setText(item.getTitle()+"\r\n"+item.getSnippet());
			button.setTag(index);
			GeoPoint pt = item.getPoint();//new GeoPoint ((int)(mLat5*1E6),(int)(mLon5*1E6));
	         //创建布局参数
	         layoutParam  = new MapView.LayoutParams(
	               //控件宽,继承自ViewGroup.LayoutParams
	               MapView.LayoutParams.WRAP_CONTENT,
	                //控件高,继承自ViewGroup.LayoutParams
	               MapView.LayoutParams.WRAP_CONTENT,
	               //使控件固定在某个地理位置
	                pt,
	                0,
	                -32,
	               //控件对齐方式
	                 MapView.LayoutParams.BOTTOM_CENTER);
	         //添加View到MapView中
	         _mapView.addView(button,layoutParam);
			return super.onTap(index);
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mapView) {
			
			_mapView.removeView(button);
			return super.onTap(pt, mapView);
		}
		
		
	}
}
