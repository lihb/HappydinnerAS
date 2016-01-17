package com.handgold.pjdc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RequestParams {
    public static final String URLENCODED = "application/x-www-form-urlencoded";
    public static final String DATA = "multipart/form-data";
    public static final String JSON = "application/json";
    public static final String XML = "text/xml";

    private String mHostUrl = "";
    private String mHostIp = "";
    private String mPostType = URLENCODED;
    private Map<String, String> mUrlParams = new HashMap<String, String>();
    private Map<String, StreamWrapper> mStreamParams = new HashMap<String, StreamWrapper>();

    public void setHost(String hostUrl, String hostIp) {
        mHostUrl = hostUrl;
        mHostIp = hostIp;
    }

    public String getHostUrl() {
        return mHostUrl;
    }

    public String getHostIp() {
        return mHostIp;
    }

    public void setPostType(String type){
        mPostType = type;
    }

    public String getPostType(){
        return mPostType;
    }

    public Map<String, String> getUrlParams() {
        return mUrlParams;
    }

    public Map<String, StreamWrapper> getStreamParams() {
        return mStreamParams;
    }

    public void put(String key, String value) {
        mUrlParams.put(key, value);
    }

    public void put(String key, File file) throws FileNotFoundException {
        if (file == null) {
            return;
        }
        String name = file.getName();
        put(key, new FileInputStream(file), name, URLConnection.guessContentTypeFromName(name));
    }

    public void put(String key, InputStream inputStream, String name, String contentType) {
        mStreamParams.put(key, new StreamWrapper(inputStream, name, contentType));
    }
}
