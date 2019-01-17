package com.blks.bdloc;

import android.app.Notification;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.blks.antrscapp.R;

import static com.blks.utils.LoginUtils.isNetwork;

/**
 * 定位管理
 * Created by limh on 2018/2/27.
 */

public class FULocationService {

    private LocationClient client = null;
    private FULocationListener locationListener;
//    private Object objLock = new Object();
    private boolean canPlay = true;
    public BDLocation bdLocation;

    private CountDownTimer downTimer;
    private MediaPlayer mediaPlayer;
    private Context mContext;

//    private LinkedList<LocationEntity> locationList = new LinkedList<LocationEntity>(); // 存放历史定位结果的链表，最大存放当前结果的前5次定位结果
    /***
     *
     * @param locationContext
     */
    public FULocationService(Context locationContext){
        mContext = locationContext;
        if(client == null){
            client = new LocationClient(locationContext);
            client.setLocOption(FULocationClientOption.getLocOption());
            locationListener = new FULocationListener(){
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    super.onReceiveLocation(bdLocation);

//                    if (bdLocation != null && (bdLocation.getLocType() == 161 || bdLocation.getLocType() == 66)) {
//                        Algorithm(bdLocation);
//                    } else {
                    FULocationService.this.bdLocation = bdLocation;
//                    }

                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    if (downTimer != null) {
                        downTimer.onFinish();
                    }
                }

                @Override
                public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {
                    super.onLocDiagnosticMessage(locType, diagnosticType, diagnosticMessage);
                    if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
                        client.restart();
                    }

                    if (downTimer == null) {
                        downTimer = new CountDownTimer(2 * 60 * 1000, 30 * 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                if (isNetwork) {
                                    downTimer.onFinish();
                                }
                            }

                            @Override
                            public void onFinish() {
                                canPlay = true;
                            }
                        };
                    }

                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(mContext, R.raw.gps_error);
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if (!isNetwork && mp != null) {
                                    mp.start();
                                }
                            }
                        });
                    }

                    if (canPlay && !mediaPlayer.isPlaying()) {
                        canPlay = false;
                        downTimer.start();
                        mediaPlayer.start();
                    }

                }
            };
            client.registerLocationListener(locationListener);
        }
    }

    /***
     *
     * @param listener
     * @return
     */
    public boolean registerListener(BDAbstractLocationListener listener){
        boolean isSuccess = false;
        if(listener != null){
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    public void unregisterListener(BDAbstractLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }

    public void unregisterListener() {
        if (locationListener != null) {
            client.unRegisterLocationListener(locationListener);
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(client.isStarted())
                client.stop();
            client.setLocOption(option);
        }
        return isSuccess;
    }

    /**
     *
     * @return DIYOption 自定义Option设置
     */
    public LocationClientOption getOption(){
        return FULocationClientOption.getLocOption();
    }

    public void start(){
        if(client != null && !client.isStarted())
            client.start();
    }
    public void stop(){
        if(client != null && client.isStarted())
            client.stop();
    }

    public void reStart(){
        if (client != null)
            client.restart();
    }

    public boolean isStart() {
        return client.isStarted();
    }

    public boolean requestHotSpotState(){
        return client.requestHotSpotState();
    }
    // 0：离线定位请求成功 1:service没有启动 2：无监听函数 6：两次请求时间太短,-1:locationClient==null
    public int getRequestLocation(){
        if(client != null && client.isStarted())
            client.requestLocation();
        return -1;
    }
    //关闭后台定位（true：通知栏消失；false：通知栏可手动划除）
    public void disableLocInForeground() {
        if (client != null){
            client.disableLocInForeground(true);
        }
    }
    //开启后台定位
    public void enableLocInForeground(Notification notification) {
        if (client != null){
            client.enableLocInForeground(3, notification);
        }
    }


//    /**
//     * 封装定位结果和时间的实体类
//     *
//     * @author baidu
//     *
//     */
//    class LocationEntity {
//        BDLocation location;
//        long time;
//    }
//
//    /***
//     * 平滑策略代码实现方法，主要通过对新定位和历史定位结果进行速度评分，
//     * 来判断新定位结果的抖动幅度，如果超过经验值，则判定为过大抖动，进行平滑处理,若速度过快，
//     * 则推测有可能是由于运动速度本身造成的，则不进行低速平滑处理 ╭(●｀∀´●)╯
//     *
//     * @param location
//     * @return Bundle
//     */
//    private Bundle Algorithm(BDLocation location) {
//        Bundle locData = new Bundle();
//        double curSpeed = 0;
//        if (locationList.isEmpty() || locationList.size() < 2) {
//            LocationEntity temp = new LocationEntity();
//            temp.location = location;
//            temp.time = System.currentTimeMillis();
//            locData.putInt("iscalculate", 0);
//            locationList.add(temp);
//        } else {
//            if (locationList.size() > 5)
//                locationList.removeFirst();
//            double score = 0;
//            for (int i = 0; i < locationList.size(); ++i) {
//                LatLng lastPoint = new LatLng(locationList.get(i).location.getLatitude(),
//                        locationList.get(i).location.getLongitude());
//                LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
//                double distance = DistanceUtil.getDistance(lastPoint, curPoint);
//                curSpeed = distance / (System.currentTimeMillis() - locationList.get(i).time) / 1000;
//                score += curSpeed * Utils.EARTH_WEIGHT[i];
//            }
//            if (score > 0.00000999 && score < 0.00005) { // 经验值,开发者可根据业务自行调整，也可以不使用这种算法
//                location.setLongitude(
//                        (locationList.get(locationList.size() - 1).location.getLongitude() + location.getLongitude())
//                                / 2);
//                location.setLatitude(
//                        (locationList.get(locationList.size() - 1).location.getLatitude() + location.getLatitude())
//                                / 2);
//                locData.putInt("iscalculate", 1);
//            } else {
//                locData.putInt("iscalculate", 0);
//            }
//            LocationEntity newLocation = new LocationEntity();
//            newLocation.location = location;
//            newLocation.time = System.currentTimeMillis();
//            locationList.add(newLocation);
//
//        }
//
//        FULocationService.this.bdLocation = location;
//        return locData;
//    }
}
