package com.handgold.pjdc.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单类
 * Created by lihb on 15/5/13.
 */
public class Order implements Parcelable {



    // 4种状态，未提交， 已提交，未付款，已付款
    public enum OrderStatus {
        NOTSUBMIT, SUBMITED, NOTPAY, PAYED
    }

    private String orderId;

    private ArrayList<MenuItemInfo> menuList = new ArrayList<>();

    private OrderStatus  status;

    private float totalPrice;

    // 订单提交时间
    private String orderTime;

    // 订单桌号或者房间号码
    private String deskId;

    public Order() {
       /* this.orderId = UUID.randomUUID().toString();
        this.menuList = new ArrayList<Menu>();
        this.status = OrderStatus.NOTSUBMIT;*/
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getOrderId() {
        return orderId;
    }

    /**
     *  增加一个菜品
     * @param menu
     */
    public void addMenu(MenuItemInfo menu) {
        if (menuList.contains(menu)) {
            int index = menuList.indexOf(menu);
            MenuItemInfo menu1 = menuList.get(index);
            menu1.count++;
        }else {
            menuList.add(menu);
            menu.count++;
        }
        totalPrice += menu.getActualPrice();
    }

    /**
     * 减少一个菜品
     * @param menu
     */
    public void delMenu(MenuItemInfo menu) {
        if (menuList.contains(menu)) {
            int index = menuList.indexOf(menu);
            MenuItemInfo menu1 = menuList.get(index);
            menu1.count--;
            if (menu1.count == 0) {
                menuList.remove(menu1);
            }
            totalPrice -= menu.getActualPrice();
        }
    }

    /**
     * 返回订单总价格
     * @return
     */
    public float getTotalPrice() {
        float res = 0.0f;
        for (MenuItemInfo menu : menuList) {
            res += menu.count * menu.getActualPrice();
        }
        return res;
    }

    /**
     * 返回订单总等菜品数量
     * @return
     */
    public int getSize() {
        int count = 0;
        for (MenuItemInfo menu : menuList) {
            count += menu.getCount();
        }
        return count;
    }

    /**
     * 设置订单状态
     * @param status
     */
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setMenuList(ArrayList<MenuItemInfo> menuList) {
        this.menuList = menuList;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTime() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return orderTime;
    }

    /**
     * 返回菜品信息
     *
     * @return menuList
     */
    public List<MenuItemInfo> getMenuList() {
        return menuList;
    }


    /**
     * 清空所以菜品
     *
     * @return
     */
    public void clear() {
        for (MenuItemInfo menu : menuList) {
            menu.setCount(0);
        }
        menuList.clear();
        totalPrice = 0.0f;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", menuList=" + menuList +
                ", status=" + status +
                ", totalPrice=" + totalPrice +
                ", orderTime=" + orderTime +
                ", deskId=" + deskId +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderId);
        dest.writeString(orderTime);
        dest.writeString(deskId);
        dest.writeList(menuList);
        dest.writeFloat(totalPrice);
        dest.writeValue(status);
    }

    public static final Parcelable.Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            Order order = new Order();
            order.setOrderId(source.readString());
            order.setOrderTime(source.readString());
            order.setDeskId(source.readString());
            order.setMenuList(source.readArrayList(MenuItemInfo.class.getClassLoader()));
            order.setTotalPrice(source.readFloat());
            order.setStatus((OrderStatus) source.readValue(null));

            return order;
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    /*public static void main(String[] args) {
        ArrayList<Menu> dataList = new ArrayList<Menu>();
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setMenuList(dataList);
        order.setStatus(OrderStatus.NOTSUBMIT);

        com.happydinner.entitiy.Menu lurouMenu = new com.happydinner.entitiy.Menu("卤肉", null, null, 14.5f, "好吃看的见－lurou", 1, 0, 1);
        com.happydinner.entitiy.Menu lurouMenu2 = new com.happydinner.entitiy.Menu("卤肉", null, null, 14.5f, "好吃看的见－lurou", 1, 0, 1);
        order.addMenu(lurouMenu);
        order.addMenu(lurouMenu2);
        order.delMenu(lurouMenu);
        order.delMenu(lurouMenu2);
    }*/
}
