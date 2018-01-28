package com.example.cjz.mymap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;

public class MainActivity extends Activity {
    private MapView mv;
    private BaiduMap map;
    private LocationClient locationClient;
    private BDAbstractLocationListener myLocationListener = new BDAbstractLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();
            String coorType = bdLocation.getCoorType();
            int locType = bdLocation.getLocType();

            String addrStr = bdLocation.getAddrStr();


            Log.e("test", "Located  " + latitude + "  " + longitude + "   " + coorType + "  " + locType + "  " + addrStr);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.bmapView);
        map = mv.getMap();
        map.getUiSettings().setOverlookingGesturesEnabled(false);
//        map.setMapType(BaiduMap.MAP_TYPE_SATELLITE);

        map.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                map.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(22.5, 114.0)));
                LatLngBounds build = new LatLngBounds.Builder().include(new LatLng(22.5, 114.0)).include(new LatLng(22.5, 116.0)).include(new LatLng(24.5, 116.0)).include(new LatLng(24.5, 114.0)).build();
                map.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(build));
                int[] colors = new int[100 * 100];
                for(int i = 0; i < 10000; ++i){
                    colors[i] = 0x33f60000;
                }
                Bitmap bitmap = Bitmap.createBitmap(colors, 100, 100, Bitmap.Config.ARGB_8888);
                OverlayOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(bitmap)).position(new LatLng(23.5, 115.0)).draggable(true);
                Marker marker = (Marker) map.addOverlay(options);
                map.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDrag(Marker marker) {
                        Log.e("test", "onMarkerDrag");
                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Log.e("test", "onMarkerDragEnd");
                    }

                    @Override
                    public void onMarkerDragStart(Marker marker) {
                        Log.e("test", "onMarkerDragStart");
                    }
                });
                locationClient.start();

            }
        });

        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(myLocationListener);
        LocationClientOption locationOptions = new LocationClientOption();
        locationOptions.setOpenGps(true);
        locationOptions.setCoorType("bd09ll");
        locationOptions.setIsNeedAddress(true);
//        locationOptions.setScanSpan(2000);
        locationOptions.setLocationNotify(true);
        locationOptions.disableCache(true);
        locationOptions.setOpenAutoNotifyMode();
        int autoNotifyMinDistance = locationOptions.getAutoNotifyMinDistance();
        Log.e("test", autoNotifyMinDistance + "");
//        locationOptions.
        locationClient.setLocOption(locationOptions);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv.onDestroy();
        locationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.onPause();
    }
}
