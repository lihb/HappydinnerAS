package com.handgold.pjdc.action;

import com.handgold.pjdc.util.VersionUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlList {
	private static UrlList 		mInstance = null;
	
	public static synchronized UrlList instance() 
	{
		if(mInstance==null) {
			mInstance = new UrlList();
		}
		return mInstance;
	}
	
	private String urlPrefix() {
		if (VersionUtil.isDevelopVersion())
		{
			return "https://dev-wewatch.yy.com/1";
		}
		else
		{
			return "https://wewatch.yy.com/1";
		}
    }
    private String urlSuffix() {
    	Map<String, String> paramMap = new HashMap<String, String>();
    	paramMap.put("platform", "1");
		String model = android.os.Build.MODEL;
		if(model != null) {
			paramMap.put("device", model);
		}
		else {
			paramMap.put("device", "unknown android phone");
		}
			
        String ver = android.os.Build.VERSION.RELEASE;
        if(ver != null) {
        	paramMap.put("os_ver", ver);
        }
        else {
        	paramMap.put("os_ver", "unknown");
		}
        
    	paramMap.put("app_id", "wewatch");
		paramMap.put("app_ver", VersionUtil.getVersionName());
    	paramMap.put("language", "zh_cn");
//		paramMap.put("uid", ""+DataManager.getInstance().getCurrentUserId());
        try {
			return map2EncodeUrl(paramMap);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		
    }
    
    public String  urlCreate(String urlBody, String urlParam) {
    	if (urlParam != null) {
    		return urlPrefix()+urlBody+"?"+urlSuffix()+urlParam; 
		}
    	else {
    		return urlPrefix() + urlBody + "?" + urlSuffix(); 
		}
    	
    }

    public String map2EncodeUrl(Map<String, String> paramsMap) throws UnsupportedEncodingException {
    	
    	StringBuffer parameterBuffer = new StringBuffer();
        if (paramsMap != null) {
            Iterator<String> iterator = paramsMap.keySet().iterator();
            String key = null;
            String value = null;
            while (iterator.hasNext()) {
                key = iterator.next();
                if (paramsMap.get(key) != null) {
                    value = paramsMap.get(key);
                } else {
                    value = "";
                }
                
                parameterBuffer.append(key).append("=").append(URLEncoder.encode(value, "UTF-8"));
                if (iterator.hasNext()) {
                    parameterBuffer.append("&");
                }
            }
        }
        
        return parameterBuffer.toString();
        
	}
}
