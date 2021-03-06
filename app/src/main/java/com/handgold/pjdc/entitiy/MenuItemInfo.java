package com.handgold.pjdc.entitiy;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 菜品类
 * Created by lihb on 15/5/13.
 * name						string			菜品名称
 imgUrl						string			菜品图片
 video						string 			菜品介绍视频
 info						string			菜品介绍文字
 price						float			菜品价格
 discount					float			折扣信息

 */
public class MenuItemInfo implements Parcelable, Cloneable {

    // 菜品名称
    public String name;

    // 菜品介绍图片
    public String imgUrl;

    // 菜品介绍视频
    public String video;

    // 菜品价格
    public float price;

    // 菜品介绍
    public String info;

    // 菜品优惠信息
    public float discount = 1;

    // 菜品是否被厨房处理,未上菜和已上菜
//    private int cooked; // 0--未上菜；1-－已上菜

    // 菜品所属分类
//    private int playtype;

    // 该菜品被点了几份
    public int count = 0 ;

//    public String restaurantName;

    public MenuItemInfo(String name) {
        this.name = name;
    }

    /**
     * 增加clone方法，使其深复制
     * @return
     * @throws CloneNotSupportedException
     */
    @Override
    protected MenuItemInfo clone() {
        MenuItemInfo object = null;
        try {
            object = (MenuItemInfo)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuItemInfo menu = (MenuItemInfo) o;

       /* if (Float.compare(menu.discount, discount) != 0) return false;
        if (Float.compare(menu.price, price) != 0) return false;
        if (playtype != menu.playtype) return false;
        if (!imgUrl.equals(menu.imgUrl)) return false;
        if (!info.equals(menu.info)) return false;
        if (!video.equals(menu.video)) return false;*/
        return name.equals(menu.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
      /*  result = 31 * result + imgUrl.hashCode();
        result = 31 * result + video.hashCode();
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + info.hashCode();
        result = 31 * result + (discount != +0.0f ? Float.floatToIntBits(discount) : 0);
        result = 31 * result + playtype;*/
        return result;
    }

    public MenuItemInfo(String name, String imgUrl, String video, float price, String info, float discount/*, int cooked, int playtype, String restaurantName*/) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.video = video;
        this.price = price;
        this.info = info;
        this.discount = discount;
//        this.cooked = cooked;
//        this.playtype = playtype;
//        this.restaurantName = restaurantName;
    }

    /**
     * 获取折扣后等真实价格
     * @return
     */
    public float getActualPrice() {
        if (discount == 0) {
            discount = 1;
        }
        return discount * price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

//    public int isCooked() {
//        return cooked;
//    }
//
//    public void setCooked(int cooked) {
//        this.cooked = cooked;
//    }
//
//    public int getPlaytype() {
//        return playtype;
//    }
//
//    public void setPlaytype(int playtype) {
//        this.playtype = playtype;
//    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

//    public String getRestaurantName() {
//        return restaurantName;
//    }
//
//    public void setRestaurantName(String restaurantName) {
//        this.restaurantName = restaurantName;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(video);
        dest.writeFloat(price);
        dest.writeString(info);
        dest.writeFloat(discount);
//        dest.writeInt(cooked);
//        dest.writeInt(playtype);
        dest.writeInt(count);
    }

    public static final Parcelable.Creator<MenuItemInfo> CREATOR = new Creator<MenuItemInfo>() {
        @Override
        public MenuItemInfo createFromParcel(Parcel source) {
            MenuItemInfo menu = new MenuItemInfo(source.readString());
            menu.setImgUrl(source.readString());
            menu.setVideo(source.readString());
            menu.setPrice(source.readFloat());
            menu.setInfo(source.readString());
            menu.setDiscount(source.readFloat());
//            menu.setCooked(source.readInt());
//            menu.setPlaytype(source.readInt());
            menu.setCount(source.readInt());
            return menu;
        }

        @Override
        public MenuItemInfo[] newArray(int size) {
            return new MenuItemInfo[size];
        }
    };
}
