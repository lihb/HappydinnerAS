package com.handgold.pjdc.entitiy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/2.
 *
 * name 				string			游戏名称
 * imgUrl				string			游戏封面
 * launchInfo      	    string			启动参数
 * packageName			string			游戏包名
 * downloadUrl			string			下载地址
 */
public class GameInfo implements Parcelable {
    private final String TAG = "GameInfo";
    /**
     * 游戏名字
     */
    public String name;

    /**
     * 游戏图片url
     */
    public String imgUrl;

    public String launchInfo;

    public String packageName;

    public String downloadUrl;


    public String getLaunchInfo() {
        return launchInfo;
    }

    public void setLaunchInfo(String launchInfo) {
        this.launchInfo = launchInfo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public GameInfo(String name, String imgUrl, String launchInfo, String packageName, String downloadUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.launchInfo = launchInfo;
        this.packageName = packageName;
        this.downloadUrl = downloadUrl;
    }

    public GameInfo(JSONObject jobj) {
        // TODO Auto-generated constructor stub
        try {
            name = jobj.optString("name");
            imgUrl = jobj.optString("imgUrl");
            launchInfo = jobj.optString("launchInfo");
            packageName = jobj.optString("packageName");
            downloadUrl = jobj.optString("downloadUrl");

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "GameInfo json init exception!");
        }
    }

    public GameInfo(String jsonInfo) {
        // TODO Auto-generated constructor stub
        try {
            JSONObject jobj = new JSONObject(jsonInfo);
            name = jobj.optString("name");
            imgUrl = jobj.optString("imgUrl");
            launchInfo = jobj.optString("launchInfo");
            packageName = jobj.optString("packageName");
            downloadUrl = jobj.optString("downloadUrl");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "GameInfo json init exception!");
        }
    }

    public String toJsonString() {
        String str = "";
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("name", name);
            jobj.put("imgUrl", imgUrl);
            jobj.put("launchInfo", launchInfo);
            jobj.put("packageName", packageName);
            jobj.put("downloadUrl", downloadUrl);
            str = jobj.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imgUrl);
        dest.writeString(launchInfo);
        dest.writeString(packageName);
        dest.writeString(downloadUrl);

    }
    public static final Parcelable.Creator<GameInfo> CREATOR = new Creator<GameInfo>() {
        @Override
        public GameInfo createFromParcel(Parcel source) {
            GameInfo gameInfo = new GameInfo(source.readString());
            gameInfo.setName(source.readString());
            gameInfo.setImgUrl(source.readString());
            gameInfo.setLaunchInfo(source.readString());
            gameInfo.setPackageName(source.readString());
            gameInfo.setDownloadUrl(source.readString());

            return gameInfo;
        }

        @Override
        public GameInfo[] newArray(int size) {
            return new GameInfo[size];
        }
    };
}
