package com.handgold.pjdc.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Build;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.handgold.pjdc.base.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean isShortCutAdded(Activity act, String shortcutName) {
        boolean isInstallShortcut = false;

        final ContentResolver cr = act.getContentResolver();
        // 2.2以上系统是”com.android.launcher2.settings”,其他的为"com.android.launcher.settings"

        final String AUTHORITY = "com.android.launcher.settings";
        final String AUTHORITY2 = "com.android.launcher2.settings";

        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");

        Cursor c =
                cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                        new String[]{shortcutName}, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        } else {
            CONTENT_URI = Uri.parse("content://" + AUTHORITY2 + "/favorites?notify=true");

            c =
                    cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                            new String[]{shortcutName}, null);
            if (c != null && c.getCount() > 0) {
                isInstallShortcut = true;
            }
        }

        return isInstallShortcut;
    }

    // 是否第一次使用信息
    public static int getFirstUse(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("FirstUseGuideVC", 0);
    }

    // 保存第一次使用信息
    public static void saveFirstUse(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt("FirstUseGuideVC", Constant.FIRST_USE_GUIDE_VERSION_CODE);
        editor.commit();
    }

    /**
     * 保存首次成功登陆
     */
    public static void saveIsFirstLogined(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsFirstLogined", true);
        editor.commit();
    }

    /**
     * 是否首次成功登陆
     */
    public static boolean getIsFirstLogined(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsFirstLogined", false);
    }

    /**
     * 保存打开文档
     */
    public static void saveIsFirstOpenDoc(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsFirstOpenDoc", false);
        editor.commit();
    }

    /**
     * 是否首次打开文档
     */
    public static boolean getIsFirstOpenDoc(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsFirstOpenDoc", true);
    }

    // 保存已成功登陆
    public static void saveLogined(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("HasLogined", true);
        editor.commit();
    }

    // 是否有成功登陆
    public static boolean hasLogined(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("HasLogined", false);
    }

    // 保存快捷方式已创建信息
    public static void saveHasShortcut(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("HasShortcut", true);
        editor.commit();
    }

    // 得到快捷方式是否创建
    public static boolean hasShortcut(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("HasShortcut", false);
    }

    // 保存“NEW”标识出现次数
    public static void saveNewAmount(Context context, int i) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt("NewAppearAmount", i);
        editor.commit();
    }

    // 得到“NEW”标识已经出现的次数
    public static int getNewAmount(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("NewAppearAmount", 0);
    }

    // 保存首次运行程序的时间信息
    public static void saveFirstRun(Context context, Long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("FirstRunTime", time);
        editor.commit();
    }

    // 得到首次运行程序的时间信息
    public static Long getFirstRun(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("FirstRunTime", 0);
    }

    // 保存最近一次登录的时间信息
    public static void saveLastLogin(Context context, Long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("LastLoginTime", time);
        editor.commit();
    }

    // 得到最近一次登录的时间信息
    public static Long getLastLogin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastLoginTime", 0);
    }

    /**
     * 保存个人动态最近获取的时间
     */
    public static void saveCloudDynamicTime(Context context, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("LastCloudDynamicTime", time);
        editor.commit();
    }

    /**
     * 得到个人动态最近获取的时间
     */
    public static long getCloudDynamicTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastCloudDynamicTime", -1);
    }

    /**
     * 保存群空间动态最近获取的时间
     */
    public static void saveGroupDynamicTime(Context context, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("LastGroupDynamicTime", time);
        editor.commit();
    }

    /**
     * 得到群空间列表最近获取的时间
     */
    public static long getGroupSpaceListTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastGroupSpaceListTime", -1);
    }

    /**
     * 保存群空间列表最近获取的时间
     */
    public static void saveGroupSpaceListTime(Context context, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("LastGroupSpaceListTime", time);
        editor.commit();
    }

    /**
     * 得到群空间动态最近获取的时间
     */
    public static long getGroupDynamicTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastGroupDynamicTime", -1);
    }

    /**
     * 保存心标文件列表最近获取的时间
     */
    public static void saveStarFileListTime(Context context, long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("LastStarFileList", time);
        editor.commit();
    }

    /**
     * 得到心标文件列表最近获取的时间
     */
    public static long getStarFileListTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastStarFileList", -1);
    }


    /**
     * 获取新功能使用信息
     *
     * @param context
     * @param key     新功能key
     * @return true 已经使用过，false还未使用过
     */
    public static boolean getNewFunction(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("new_function", Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    // 保存帐号信息
    public static void setUserLoginMessage(Context context, String name, String password) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("UserName", name);
        // 加密密码
        String encPassword = SimpleDES.getEncString(password);
        editor.putString("PassWord", encPassword);
        editor.commit();
    }

    // 保存是否自动登录
    public static void setAutoLoginMessage(Context context, boolean isAuto) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsAutoLogin", isAuto);
        editor.commit();
    }

    public static boolean getAutoLoginMessage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsAutoLogin", false);
    }

    /**
     * 获取经过加密的用户名，加密算法云通讯系统提供(只用于通讯录备份)
     *
     * @param context
     * @return
     */
    public static String getEncryptedUserName(Context context) {
        String userName = SharePreferencesUtils.getLastLoginUserName(context);
        return new DES().encryptDES(userName);
    }

    // 得到登录密码
    public static String getPassWord(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String password = SimpleDES.getDecString(preferences.getString("PassWord", ""));
        return password;
    }

    /**
     * 过时，使用getPassWord(Context context)
     */
    @Deprecated
    public static String getUnEncryptedPassWord(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("PassWord", "");
    }

    // 》=2.7版本密码密文保存，2.6升级到2.7之后,2.6之前的密码需要加密处理，通过此标志判断是否加密
    public static void setEncryptedPassword(Context context, boolean isEncrypted) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isEnctypted", isEncrypted);
        editor.commit();
    }

    // 密码是否已加密
    public static boolean isEncryptedPassWord(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("isEnctypted", false);
    }

    // 清除帐号信息
    public static void clearUserLoginMessage(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("PassWord", "");
        editor.commit();
    }

    // 保存本地通讯录更新时间
    public static void saveLocalUpdate(Context context, String time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("localUpdate", time);
        editor.commit();
    }

    // 获取本地通讯录更新时间
    public static String getLocalUpdate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("localUpdate", "");
    }

    /**
     * 保存云转码播放提示
     *
     * @param context
     * @param istell
     */
    public static void saveVideoTranscode(Context context, boolean istell) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("vtranscode", istell);
        editor.commit();
    }

    /**
     * 是否提示云转码播放
     *
     * @param context
     */
    public static boolean getVideoTranscode(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("vtranscode", false);
    }

    /**
     * 保存云转码播放提示
     *
     * @param context
     * @param istell
     */
    public static void saveDocumentTranscode(Context context, boolean istell) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("dtranscode", istell);
        editor.commit();
    }

    /**
     * 保存即拍即传不再弹出分享提示
     */
    public static void saveShowSharePhoto(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("ShowSharePhoto", false);
        editor.commit();
    }

    /**
     * 即拍即传是否弹出分享提示
     */
    public static boolean getShowSharePhoto(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ShowSharePhoto", true);
    }

    /*--------------服务条款声明-----------------*/
    public static boolean getShowStatement(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ShowStatement", true);
    }

    /**
     * 保存是否要继续显示服务条款页面
     */
    public static void saveShowStatement(Context context, boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("ShowStatement", isShow);
        editor.commit();
    }

    /*--------------3G网络切换提示-----------------*/
    public static boolean getShowChanged3G(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ShowChanged3G", true);
    }

    /**
     * 保存是否要继续3G网络切换提示
     */
    public static void saveShowChanged3G(Context context, boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("ShowChanged3G", isShow);
        editor.commit();
    }

    /*--------------DLNA新功能弹框提示-----------------*/
    public static boolean getShowDlnaMain(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        // return preferences.getBoolean("ShowDlnaMain", true);
        return false;
    }

    /**
     * 保存是否要继续DLNA新功能弹框提示
     */
    public static void saveShowDlnaMain(Context context, boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("ShowDlnaMain", isShow);
        editor.commit();
    }

    /*--------------文件排序新功能pop提示-----------------*/
    public static boolean getShowNewSort(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("ShowNewSort", true);
    }

    /**
     * 保存是否要显示文件排序新功能pop提示
     */
    public static void saveShowNewSort(Context context, boolean isShow) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("ShowNewSort", isShow);
        editor.commit();
    }

    /*--------------------------------*/

    /**
     * 今天是否有检测过版本更新
     */
    public static String getCheckUpdateDay(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("CheckUpdateDay", "");
    }

    public static void setCheckUpdateDay(Context context, String today) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("CheckUpdateDay", today);
        editor.commit();
    }

    /**
     * 不再提醒更新的版本
     */
    public static String getNotNotifyUpdateVersion(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("NotNotifyUpdateVersion", "");
    }

    /**
     * 保存不再提醒更新的版本
     */
    public static void setNotNotifyUpdateVersion(Context context, String version) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("NotNotifyUpdateVersion", version);
        editor.commit();
    }

    /**
     * 修改文件权限
     *
     * @param filePath 要修改的文件全路径
     * @param mode     权限模式，如“777”；
     */
    public static void chMod(String filePath, String mode) {
        try {
            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            if (TextUtils.isEmpty(mode)) {
                mode = "777";
            }
            String command = "chmod " + mode + " " + filePath;
            DLog.i("chMod", "command = " + command);
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (Exception e) {
            DLog.i("chMod", "chmod fail!!!!");
            e.printStackTrace();
        }
    }

    /**
     * 判断是否存在sd卡
     *
     * @return true :存在 false: 不存在
     */
    public static boolean isSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static String getRootSDCard() {
        if (isSDCard()) {
            return android.os.Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }

    /**
     * 文件夹、文件排序
     *
     * @param files
     */
    public static void orderFiles(File[] files, List<File> fileSet) {
        ArrayList<File> fileList = new ArrayList<File>();
        ArrayList<File> folderList = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().charAt(0) == '.') {
                continue;
            }
            if (files[i].isDirectory()) {
                folderList.add(files[i]);
            } else {
                fileList.add(files[i]);
            }
        }
        Comparator comp = new Mycomparator();
        Collections.sort(folderList, comp);
        Collections.sort(fileList, comp);

        for (File file : folderList) {
            fileSet.add(file);
        }
        for (File file : fileList) {
            fileSet.add(file);
        }
        folderList = null;
        fileList = null;

    }

    /**
     * 文件夹排序
     *
     * @param files
     */
    public static void orderFolders(File[] files, List<File> fileSet) {
        ArrayList<File> folderList = new ArrayList<File>();
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().charAt(0) == '.') {
                continue;
            }
            if (files[i].isDirectory()) {
                folderList.add(files[i]);
            }
        }
        Comparator comp = new Mycomparator();
        Collections.sort(folderList, comp);

        for (File file : folderList) {
            fileSet.add(file);
        }
        folderList = null;

    }

    public static class Mycomparator implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            File mfile = (File) lhs;
            File nfile = (File) rhs;
            Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
            return cmp.compare(mfile.getName(), nfile.getName());
        }
    }

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            DLog.d("commonutils", "service:" + serviceList.get(i).service.getClassName());
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 获取是否提示相册备份的标志--直接弹出形式
     *
     * @param context 上下文
     * @return true提示，false不提示。
     */
    public static boolean shouldPromptAlbumBackup(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Constant.CONTROL_PROMPT_ALBUMBACK_ID != preferences.getInt("promptId1", -1);
    }

    /**
     * 保存是否提示相册备份的标志--直接弹出形式
     *
     * @param context 上下文
     */
    public static void savePromptAlbumBackup(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt("promptId1", Constant.CONTROL_PROMPT_ALBUMBACK_ID);
        editor.commit();
    }

    /**
     * 获取是否提示相册备份的标志--状态栏提示
     *
     * @param context 上下文
     * @return true提示，false不提示。
     */
    public static boolean shouldPromptAlbumBackupbyNotification(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("shouldPromptAlbumBackup1", true);
    }

    /**
     * 保存是否提示相册备份的标志--状态栏提示
     *
     * @param context    上下文
     * @param isPrompted true提示，false不提示
     */
    public static void savePromptAlbumBackupbyNotification(Context context, boolean isPrompted) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("shouldPromptAlbumBackup1", isPrompted);
        editor.commit();
    }

    /**
     * @param context
     * @return 根据系统的版本返回，<=2.1返回“/sdcard”,>2.1返回“/mnt”或者“/storage”
     */
    public static String getStorageRootPath(Context context) {
        String rootPath = "";
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ECLAIR_MR1) {
            return "/sdcard";
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ECLAIR_MR1 &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return "/mnt";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            StorageManager storageMgr = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
            String[] volumePaths = (String[]) ReflectionHelper.invokeMethod(storageMgr, "getVolumePaths", null);
            if (volumePaths != null && volumePaths.length > 0) {
                String path = volumePaths[0];
                int firstIndex = path.indexOf("/") + 1;
                rootPath = path.substring(0, path.indexOf("/", firstIndex));
                System.out.println("rootPath" + rootPath);
            }
        }
        return rootPath;
    }

    /**
     * 判断是否是电话号码
     *
     * @param input 电话号码
     * @return 通过正则表示判断regularExpression ="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
     */
    public static boolean validatePhoneNumber(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        String regularExpression = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
        return input.matches(regularExpression);
    }

    /**
     * <p>
     * 是否为手机号，判断与{@link #validatePhoneNumber(String)}不同。
     * </p>
     * 11位数字，1开头
     *
     * @param input
     * @return
     */
    public static boolean isPhoneNumber(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        return input.startsWith("1") && input.length() == 11 && input.matches("[0-9]*");
    }

    /**
     * 判断是否是邮箱
     *
     * @param input 邮箱
     * @return 通过正则表示判断regularExpression =
     * "^[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$"
     * ;
     */
    public static boolean validateEmail(String input) {
        if (TextUtils.isEmpty(input)) {
            return false;
        }
        String regularExpression =
                "^[_a-z\\d\\-\\./]+@[_a-z\\d\\-]+(\\.[_a-z\\d\\-]+)*(\\.(info|biz|com|edu|gov|net|am|bz|cn|cx|hk|jp|tw|vc|vn))$";
        return input.matches(regularExpression);
    }

    /**
     * 由于前一版本路径有错的影响，需要把../elcoud的数据迁移到../ecloud中
     *
     * @param context 上下文
     * @return 如果已经迁移返回true，否则返回false。
     */
    public static boolean isRemovedElcoudData(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("isRemovedElcoudData", false);
    }

    /**
     * 设置是否已经迁移数据，需要把../elcoud的数据迁移到../ecloud中
     *
     * @param context   上下文
     * @param isRemoved 是否已经迁移标志
     */
    public static void setElcoudDataRemoved(Context context, boolean isRemoved) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("isRemovedElcoudData", isRemoved);
        editor.commit();
    }

    /**
     * 返回上次提示3G网络的时间
     *
     * @param context 上下文
     * @return 上次提示3G网络的时间
     */
    public static long getLastToastTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("LastToastTime", 0);
    }



    /**
     * 保存上次登录方式
     *
     * @param context      上下文
     * @param loginChannel 登录方式
     */
    public static void setLastLoginChannel(Context context, int loginChannel) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt("LastLoginChannel", loginChannel);
        editor.commit();
    }

    public static String getEmailWithoutSuffix(String email) {
        if (TextUtils.isEmpty(email)) {
            return "";
        }
        String retVal = "";
        if (email.contains("@")) {
            retVal = email.substring(0, email.lastIndexOf("@"));
        } else {
            retVal = email;
        }
        return retVal;
    }

    /**
     * 如果input没有@后缀，则添加后缀intput+“@189.cn”
     *
     * @param input
     * @return
     */
    public static String getEmailWithSuffix(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        String retVal = "";
        if (input.contains("@")) {
            retVal = input;
        } else {
            retVal = input + "@189.cn";
        }
        return retVal;
    }

    public static void toastDiyView(Activity act, View diyView, int duration, int gravity, int xOffset, int yOffset) {
        Toast diyToast = new Toast(act);
        diyToast.setView(diyView);
        diyToast.setDuration(duration);
        diyToast.setGravity(gravity, xOffset, yOffset);
        diyToast.show();
    }

    public static void toastText(Activity act, String text) {
        Toast.makeText(act, text, Toast.LENGTH_SHORT).show();
    }

    public static void sendSms(final String addr, final String msg, final PendingIntent pendingIntent,
                               final PendingIntent deliveryIntent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SmsManager smsMgr = SmsManager.getDefault();
                    smsMgr.sendTextMessage(addr, null, msg, pendingIntent, deliveryIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, 80, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean isApkInstalled(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(pkgName);

        return null != intent;
    }

    /**
     * 获取是否使用imsi登录开关状态
     */
    public static boolean getIsAllowImsiLogin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsAllowImsiLogin", true);
    }

    /**
     * 设置是否使用imsi登录的开关状态
     */
    public static void saveIsAllowImsiLogin(Context context, boolean allowed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsAllowImsiLogin", allowed);
        editor.commit();
    }

    /**
     * 获取是否使用ctaccount登录开关状态
     */
    public static boolean getIsAllowCTAccountLogin(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("IsAllowCTAccountLogin", true);
    }

    /**
     * 设置是否使用ctaccount登录的开关状态
     */
    public static void saveIsAllowCTAccountLogin(Context context, boolean allowed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsAllowCTAccountLogin", allowed);
        editor.commit();
    }

    /**
     * 获取是否开启手势密码状态
     */
    public static boolean getIsEnableGesture(Context context) {
        if (null == context)
            return false;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (null == preferences)
            return false;
        return preferences.getBoolean("IsEnableGesture", false);
    }

    /**
     * 设置是否开启手势密码开关状态
     */
    public static void saveIsEnableGesture(Context context, boolean allowed) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean("IsEnableGesture", allowed);
        editor.commit();
    }


    /**
     * 移除插件Plugin
     *
     * @param pkgName apk包名
     */
    public static void removePlugin(String pkgName) {
        // PluginManager.getDefault().removePlugin(pkgName);
    }


    /**
     * 是否对启动后未登录过的用户增加照片备份提示 ， 获取上次提示时间
     *
     * @param context
     * @return 上次提示的时间戳，默认返回0
     */
    public static Long getBackupNotifyForUnloginTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong("BackupNotifyForUnloginTime", 0);
    }

    /**
     * 是否对启动后未登录过的用户增加照片备份提示 ， 保存上次提示时间
     *
     * @param context
     * @param timestamp 时间戳
     */
    public static void saveBackupNotifyForUnloginTime(Context context, Long timestamp) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong("BackupNotifyForUnloginTime", timestamp);
        editor.commit();
    }

    /**
     * 是否对启动后未登录过的用户增加照片备份提示 ， 获取已经提示次数
     */
    public static int getBackupNotifyForUnloginNum(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("BackupNotifyForUnloginNum", 0);
    }

    /**
     * 是否对启动后未登录过的用户增加照片备份提示 ， 保存已经提示次数
     */
    public static void saveBackupNotifyForUnloginNum(Context context, int num) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt("BackupNotifyForUnloginNum", num);
        editor.commit();
    }

    /* 手势密码获取 */
    public static String getGesturePassWord(Context mContext) {
        SharedPreferences settings = mContext.getSharedPreferences(mContext.getPackageName(), 0);
        return settings.getString("gesturepassword", "");
    }

    /* 手势密码保存 */
    public static boolean resetGesturePassWord(Context mContext, String password) {
        Editor editor = mContext.getSharedPreferences(mContext.getPackageName(), 0).edit();
        editor.putString("gesturepassword", password);
        return editor.commit();
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }

    /**
     * 是否支持myid
     *
     * @return
     */
    public static boolean isSupportMyId(Context context) {
        // if(true){
        // return false;
        // }
        try {
            Class.forName("com.gsta.MYIDApp.MYIDApp");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取通知栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 剔除字符串中一些特殊字符 ：\n 回车( )，\t 水平制表符( )，\s 空格(\u0008)，\r 换行( )
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 设置浮点数的显示方式
     *
     * @param value float数据
     * @param scale 精度位数（保留的小数位数）
     * @param mode  精度取值方式
     * @return 精度计算后的数据
     */
    public static float round(float value, int scale, int mode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, mode);
        float f = bd.floatValue();
        bd = null;
        return f;

    }

    public static String formatTimeLength(int timeLength) {
        int minutes = timeLength / 60;
        int seconds = timeLength % 60;
        int hours = minutes / 60;
        if (hours > 0) {
            minutes = minutes % 60;
        }
        int days = hours / 24;
        if (days > 0) {
            hours = hours % 24;
        }

        StringBuffer stringBuffer = new StringBuffer();
        if (days > 0) {
            stringBuffer.append(String.format("%02d", days) + "天");
        }
        if (hours > 0 || stringBuffer.length() > 0) {
            stringBuffer.append(String.format("%02d", hours) + "小时");
        }
        stringBuffer.append(String.format("%02d", minutes) + "分");
        stringBuffer.append(String.format("%02d", seconds) + "秒");
        return stringBuffer.toString();
    }

}
