package cn.count.easydrive366.baidumap;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import cn.count.easydrive366.R;
import cn.count.easydriver366.base.BaseHttpActivity;

public class ShowLocationActivity extends BaseHttpActivity {
	private BMapManager _mapManager = null;
	private MapView _mapView = null;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// init map step1
		_mapManager = new BMapManager(getApplication());
		_mapManager.init("30d50073a606ac3ce0b7f8a187e8248b", null);

		setContentView(R.layout.modules_map_showlocation_activity);
		/*
		 * this.setRightButtonInVisible(); this.setupLeftButton();
		 * this.setupPhoneButtonInVisible();
		 */

		// init map step2
		_mapView = (MapView) findViewById(R.id.bmapsView);
		_mapView.setBuiltInZoomControls(true);
		MapController mapController = _mapView.getController();
		GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mapController.setCenter(point);// 设置地图中心点
		mapController.setZoom(12);// 设置地图zoom级别
		
		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
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
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		else 
			Log.d("LocSDK3", "locClient is null or not started");
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
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
			} 
	 
			Log.d("EasyDrive366", sb.toString());
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
}

