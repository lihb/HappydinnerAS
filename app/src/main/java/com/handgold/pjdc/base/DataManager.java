package com.handgold.pjdc.base;

import com.handgold.pjdc.entitiy.GameType;
import com.handgold.pjdc.entitiy.MenuType;
import com.handgold.pjdc.entitiy.MovieType;
import com.handgold.pjdc.entitiy.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lihb on 16/1/23.
 */
public class DataManager {

    public static List<MenuType> menuTypelist = new ArrayList<>();

    public static List<MovieType> movieTypeList = new ArrayList<>();

    public static List<GameType> gameTypeList = new ArrayList<>();

    public static Order order = new Order();

    // 下单时间
    public static String timestamp = "";

    // 桌号
    public static int table_number = -100;

    // 订单号
    public static String order_pay_id = "";

}
