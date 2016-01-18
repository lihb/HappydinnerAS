package com.handgold.pjdc.action;

import com.handgold.pjdc.entitiy.GameListEntity;
import com.handgold.pjdc.entitiy.GameType;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.MenuListEntity;
import com.handgold.pjdc.entitiy.MovieListEntity;
import com.handgold.pjdc.entitiy.MovieType;
import com.handgold.pjdc.entitiy.OrderPayInfo;
import com.handgold.pjdc.entitiy.PayState;
import com.handgold.pjdc.entitiy.PlaceOrderInfo;
import com.handgold.pjdc.entitiy.RoomTableInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lihb on 16/1/12.
 */
public interface ApiManager {


    /**
     * 获取所有菜品列表
     *
     * @param deviceid 设备号
     * @return
     */
    @GET("menu/list")
    Observable<MenuListEntity> getMenuList(@Query("deviceid") String deviceid);


    /**
     * 获取所有电影列表
     * @param restaurantId 餐厅号
     * @return
     */
    @GET("movie/list")
    Observable<MovieListEntity> getMovieList(@Query("restaurantId") String restaurantId);


    /**
     * 获取所有游戏列表
     * @param restaurantId 餐厅号
     * @return
     */
    @GET("game/list")
    Observable<GameListEntity> getGameList(@Query("restaurantId") String restaurantId);


    /**
     * 根据设备号读取已下单的清单列表以及其他基本信息
     *
     * @param deviceid 设备号
     * @return
     */
    @GET("order/getorderinfo")
    Observable<RoomTableInfo> getOrderInfo(@Query("deviceid") String deviceid);

    class PlaceOrderParas {
        public String deviceid;
        public String menuListJson;

    }

    /**
     * 根据设备号读取已下单的清单列表以及其他基本信息
     *
     * @param placeOrderParas
     * @return
     */
    @POST("order/placeorder")
    Observable<PlaceOrderInfo> getPlaceOrder(@Body PlaceOrderParas placeOrderParas);


    /**
     * 根据设备号读取已下单的清单列表以及其他基本信息
     *
     * @param deviceid 设备号
     * @return
     */
    @GET("order/buyorder")
    Observable<OrderPayInfo> getBuyOrder(@Query("deviceid") String deviceid);


    /**
     * 根据设备号读取已下单的清单列表以及其他基本信息
     *
     * @param deviceid 设备号
     * @return
     */
    @GET("pay/checkresult")
    Observable<PayState> checkresult(@Query("deviceid") String deviceid);




}
