package com.handgold.pjdc.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2015/8/31.
 */
public class VersionUtil {
    static int[] sLocalVer = null;
    static String sLocalName = null;
    private static final String DOT = ".";

    private static String mVersionName = "";
    private static int mVersionCode = 0;

    public VersionUtil() {
    }

    public static Ver getVerFromStr(String version) {
        if(version.matches("\\d{1,}\\.\\d{1,}\\.\\d{1,}")) {
            Ver ver = new Ver();
            int dotPos = version.indexOf(".");
            byte prevPos = 0;
            ver.mMajor = Integer.valueOf(version.substring(prevPos, dotPos)).intValue();
            int prevPos1 = dotPos + 1;
            dotPos = version.indexOf(".", prevPos1);
            ver.mMinor = Integer.valueOf(version.substring(prevPos1, dotPos)).intValue();
            prevPos1 = dotPos + 1;
            ver.mBuild = Integer.valueOf(version.substring(prevPos1)).intValue();
            return ver;
        } else {
            return null;
        }
    }

    public static Ver getLocalVer(Context c) {
        Ver v = new Ver();
        int[] ver = getLocal(c);
        v.mMajor = ver[0];
        v.mMinor = ver[1];
        v.mBuild = ver[2];
        return v;
    }

    public static String getLocalName(Context c) {
        if(sLocalName != null) {
            return sLocalName;
        } else {
            loadLoaclVer(c);
            return sLocalName;
        }
    }

    public static int[] getLocal(Context c) {
        if(sLocalVer != null) {
            return sLocalVer;
        } else {
            loadLoaclVer(c);
            return sLocalVer;
        }
    }

    static void loadLoaclVer(Context c) {
        try {
            sLocalName = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException var4) {
            throw new RuntimeException("Local Ver Package Error");
        }

        if(sLocalName == null) {
            throw new RuntimeException("Local Ver VersionName Not Exist");
        } else {
            int pos = sLocalName.indexOf(45);
            if(pos != -1) {
                sLocalName = sLocalName.substring(0, pos);
            }

            String[] verStr = sLocalName.split("\\.");
            if(verStr.length != 3) {
                throw new RuntimeException("Local Ver VersionName Error");
            } else {
                sLocalVer = new int[3];

                try {
                    for(int e = 0; e < 3; ++e) {
                        sLocalVer[e] = Integer.parseInt(verStr[e]);
                    }

                } catch (NumberFormatException var5) {
                    throw new RuntimeException("Local Ver VersionName Error");
                }
            }
        }
    }

    public static void setVersionName(String verName)
    {
        if (verName != null)
            mVersionName = new String(verName);
    }

    public static void setVersionCode(int verCode)
    {
        mVersionCode = verCode;
    }

    public static String getVersionName()
    {
        return mVersionName;
    }

    public static int getVersionCode(){
        return mVersionCode;
    }

    public static boolean isDevelopVersion()
    {
        final String developTag = "snapshot";

        if (mVersionName == null || mVersionName.length() == 0)
            return true;
        else
            return mVersionName.toLowerCase().indexOf(developTag) >= 0;
    }
}
