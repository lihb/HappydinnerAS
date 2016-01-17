package com.handgold.pjdc.entitiy;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/17.
 */
public class MovieInfo  extends BaseEntity implements Parcelable {
    private final String TAG = "MovieInfo";
    /**
     * 影片名字
     */
    private String name;

    /**
     * 影片url
     */
    private String movieUrl;

    /**
     * 影片所属分类
     */
    private int type;

    public void setName(String name) {
        this.name = name;
    }

    public void setMovieUrl(String movieUrl) {
        this.movieUrl = movieUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public MovieInfo(String name, String picUrl, int type) {
        this.name = name;
        this.movieUrl = picUrl;
        this.type = type;
    }


    public MovieInfo(JSONObject jobj) {
        // TODO Auto-generated constructor stub
        try {
            name = jobj.optString("name");
            movieUrl = jobj.optString("movieUrl");
            type = jobj.optInt("type");
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
            movieUrl = jobj.optString("movieUrl");
            type = jobj.optInt("type");
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
            jobj.put("movieUrl", movieUrl);
            jobj.put("type", type);
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
        dest.writeString(movieUrl);
        dest.writeInt(type);

    }
    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            MovieInfo gameInfo = new MovieInfo(source.readString());
            gameInfo.setName(source.readString());
            gameInfo.setMovieUrl(source.readString());
            gameInfo.setType(source.readInt());

            return gameInfo;
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
