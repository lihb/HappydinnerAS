package com.handgold.pjdc.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.handgold.pjdc.R;
import com.handgold.pjdc.base.BaseActivity;

/**
 * Created by Administrator on 2015/11/24.
 */
public class MapShowActivity extends BaseActivity {

    private TextView mTextLocDesc = null;

    private BaiduMap mBaiduMap = null;

    private MapView mMapView = null;

    private LocationClient mLocationClient;
    private BDLocationListener myListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        mTextLocDesc = (TextView) findViewById(R.id.text_desc);
        //初始化百度地图
        initMapView();

        //初始化LocationClient类,推荐用getApplicationConext获取全进程有效的context
        mLocationClient = new LocationClient(getApplicationContext());

        //定位相关设置
        setLocOption(mLocationClient);

        //定位回调函数
        myListener = new MyLocationListenner();

        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        //开启定位
        mLocationClient.start();
    }

    //初始化百度地图
    private void initMapView() {
        mMapView = (MapView) findViewById(R.id.baidumapView);
        mBaiduMap = mMapView.getMap();
        // 设置地图的缩放级别
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
    }

    //设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等
    private void setLocOption(LocationClient mLocClient) {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //是否打开gps
        option.setAddrType("all");//设置是否要返回地址信息，String 值为 all时，表示返回地址信息。其他值都表示不返回地址信息。
        option.setCoorType("gcj02");//返回的定位结果是百度经纬度,默认值gcj02

        //当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
        //当不设此项，或者所设的整数值小于1000（ms）时，采用一次定位模式。
        option.setScanSpan(1000);//设置发起定位请求的间隔时间为5000ms

        option.disableCache(true);//true表示禁用缓存定位，false表示启用缓存定位。

        mLocClient.setLocOption(option);
    }

    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                Toast.makeText(getApplicationContext(), "获取位置信息失败", Toast.LENGTH_LONG).show();
                return;
            }
            if (location.getAddrStr() == null) {
                Toast.makeText(getApplicationContext(), "获取位置信息失败", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(), "定位成功：" + location.getAddrStr(), Toast.LENGTH_LONG).show();
                mTextLocDesc.setText(location.getAddrStr());
                showMyLocationOnMap(location);
            }

            //定位完成后关闭定位
            mLocationClient.stop();

            //取消监听函数。
            mLocationClient.unRegisterLocationListener(myListener);

        }


    }

    /**
     * 根据传入的经纬度在地图上显示
     *
     */
    private void showMyLocationOnMap(BDLocation location) {
        // map view 销毁后不在处理新接收的位置
        if (location == null || mMapView == null)
            return;
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(location.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(location.getLatitude())
                .longitude(location.getLongitude()).build();
        mBaiduMap.setMyLocationData(locData);

        LatLng ll = new LatLng(location.getLatitude(),
                location.getLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        //构建Marker图标
        BitmapDescriptor pin_1 = BitmapDescriptorFactory
                .fromResource(R.drawable.pin_1);
        BitmapDescriptor pin_2 = BitmapDescriptorFactory
                .fromResource(R.drawable.pin_2);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option1 = new MarkerOptions()
                .position(ll)
                .icon(pin_1);
        OverlayOptions option2 = new MarkerOptions()
                .position(ll)
                .icon(pin_2);
        //在地图上添加Marker，并显示
//        mBaiduMap.addOverlay(option1);
        mBaiduMap.addOverlay(option2);
        mBaiduMap.animateMapStatus(u);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


}
