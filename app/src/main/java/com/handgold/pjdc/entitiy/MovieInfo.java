package com.handgold.pjdc.entitiy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MovieInfo implements Parcelable {
    private final String TAG = "MovieInfo";
    /**
     * 影片名字
     */
    public String name;

    /**
     * 影片url
     */
    public String imgUrl;

    /**
     * 影片所属分类
     */
    public int playtype;


    /**
     * 下载地址
     */
    public String downloadUrl;

    public void setName(String name) {
        this.name = name;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setPlaytype(int playtype) {
        this.playtype = playtype;
    }

    public int getPlaytype() {
        return playtype;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public MovieInfo(String name, String picUrl, int type, String downloadUrl) {
        this.name = name;
        this.imgUrl = picUrl;
        this.playtype = type;
        this.downloadUrl = downloadUrl;
    }


    public MovieInfo(JSONObject jobj) {
        // TODO Auto-generated constructor stub
        try {
            name = jobj.optString("name");
            imgUrl = jobj.optString("imgUrl");
            downloadUrl = jobj.optString("downloadUrl");
            playtype = jobj.optInt("playtype");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "MovieInfo json init exception!");
        }
    }

    public MovieInfo(String jsonInfo) {
        // TODO Auto-generated constructor stub
        try {
            JSONObject jobj = new JSONObject(jsonInfo);
            name = jobj.optString("name");
            imgUrl = jobj.optString("imgUrl");
            downloadUrl = jobj.optString("downloadUrl");
            playtype = jobj.optInt("playtype");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "MovieInfo json init exception!");
        }
    }

    public String toJsonString() {
        String str = "";
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("name", name);
            jobj.put("imgUrl", imgUrl);
            jobj.put("playtype", playtype);
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
        dest.writeInt(playtype);
        dest.writeString(downloadUrl);

    }
    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            MovieInfo movieInfo = new MovieInfo(source.readString());
            movieInfo.setName(source.readString());
            movieInfo.setImgUrl(source.readString());
            movieInfo.setPlaytype(source.readInt());
            movieInfo.setDownloadUrl(source.readString());

            return movieInfo;
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
