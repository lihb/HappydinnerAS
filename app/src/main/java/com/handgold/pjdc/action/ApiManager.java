package com.handgold.pjdc.action;

import com.handgold.pjdc.entitiy.GameType;
import com.handgold.pjdc.entitiy.MenuListEntity;
import com.handgold.pjdc.entitiy.MovieType;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by lihb on 16/1/12.
 */
public interface ApiManager {

    @GET("menu/list")
    Observable<MenuListEntity> getMenuList(@Query("deviceid") String deviceid);

    @GET("movie/list")
    Observable<List<MovieType>> getMovieList(@Query("restaurantId") String restaurantId);

    @GET("game/list")
    Observable<List<GameType>> getGameList(@Query("restaurantId") String restaurantId);
}
