package com.handgold.pjdc.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharePreferencesUtils {
	
	private static String getUserSettingPreferencesName(Context context){
		return getLastLoginUserName(context)+"_setting";
	}
	//***********************************保存用户的私有数据，比如是否相册备份*********************
	/*--------------------------------*/
	/** 今天是否有自动登录过 */
	public static String getAutoLoginDay(Context context){
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		return preferences.getString("AutoLoginDay", "");
	}
	
	public static void setAutoLoginDay(Context context,String today){
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("AutoLoginDay", today);
		editor.commit();
	}
	
	/**
	 * 判断是否备份相册
	 * @param context 上下文
	 * @return true应自动备份，false不应自动备份；没有设置默认返回false。
	 */
	public static boolean isAllowedBackupAlbums(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		return preferences.getBoolean("IsAutoBackup", false);
	}
	/**
	 * 记录是否自动备份相册
	 * @param context 上下文
	 * @param isAutoBackUp 是否备份
	 */
	public static void setAllowedBackupAlbums(Context context, boolean isAutoBackUp) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("IsAutoBackup", isAutoBackUp);
		editor.commit();
	}
	/**
	 * 判断是否含有某个偏好
	 * @param context 上下文
	 * @param key 
	 * @return true有，false无。
	 */
	public static boolean containPrivatePreferences(Context context, String key) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		return preferences.contains(key);
	}
	/**
	 * 判断是否已经初始化一键备份
	 * @param context 上下文
	 * @return true，已经初始化；false 未初始化，默认返回false。
	 */
	public static boolean isInitedOneKeyBackup(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		return preferences.getBoolean("isInitedOneKeyBackup", false);
	}
	/**
	 * 记录是否已经初始化一键备份
	 * @param context 上下文
	 * @param isInited 是否已经初始化
	 */
	public static void setInitedOneKeyBackup(Context context, boolean isInited) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isInitedOneKeyBackup", isInited);
		editor.commit();
	}
	/**
	 * 是否提示群空间有新动态
	 * @param context 上下文
	 * @return true提示新动态，false不提示新动态
	 */
	public static boolean isNotifyGroupDynamic(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		return preferences.getBoolean("IsNotifyGroupDynamic", true);
	}

	/**
	 * 保存是否提示群空间有新动态标识位
	 * @param context 上下文
	 * @param notify true提示新动态，false不提示新动态
	 */
	public static void setNotifyGroupDynamic(Context context, boolean notify) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), 
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("IsNotifyGroupDynamic", notify);
		editor.commit();
	}
	/**
	 * 保存自动备份的目录
	 * @param context 上下文
	 * @param dir 可能是单个目录，也可能是多个目录，“；”分割，如“/sdcard/ecloud;/sdcard/com”
	 */
	public static void setBackUpDirectory(Context context, String dir) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("AutoBackupRoot", dir);
		editor.commit();
	}
	/**
	 * 获取相册备份的目录
	 * @param context 上下文
	 * @return 可能是单个目录，也可能是多个目录，“；”分割，如“/sdcard/ecloud;/sdcard/com”
	 */
	public static String getBackUpDirectory(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		String tempDirs = preferences.getString("AutoBackupRoot","");
//		return !TextUtils.isEmpty(tempDirs) ? tempDirs : ECloudPathManager.get().getRootPath()+File.separator+"DCIM/Camera";
		return tempDirs;
	}
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isShow189Mails(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getBoolean("AllowShow189Mails", true);
	}
	/**
	 * 
	 * @param context
	 * @param allowed
	 */
	public static void setShow189Mails(Context context, boolean allowed) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("AllowShow189Mails", allowed);
		editor.commit();
	}
	/**
	 * 保存相册自动备份时的网络类型
	 * @param context 上下文
	 * @param type 0表示wifi，1表示所有网络
	 */
	public static void setAlbumBackupNetType(Context context, int type) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt("RadioSelect", type);
		editor.commit();
	}
	/**
	 * 获取相册自动备份时，设置的网络类型
	 * @param context
	 * @return 0表示wifi，1表示所有网络
	 */
	public static int getAlbumBackupNetType(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getInt("RadioSelect", 0);
	}
	/**
	 * 保存最近备份的图片对于数据库记录的id
	 * @param context 上下文
	 * @param id 已备份记录id
	 */
	public static void setIamgeLastBackUpId(Context context, Long id) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong("ImageDBCountID", id);
		editor.commit();
	}
	/**
	 * 获取最近备份的图片对应数据库的记录id
	 * @param context 上下文
	 * @return 记录id
	 */
	public static long getIamgeLastBackUpId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getLong("ImageDBCountID", 0);
	}
	/**
	 * 保存最近备份的视频对于数据库记录的id
	 * @param context 上下文
	 * @param id 已备份记录id
	 */
	public static void setVideoLastBackUpId(Context context, Long id) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong("VideoDBCountID", id);
		editor.commit();
	}
	/**
	 * 获取最近备份的视频对应数据库的记录id
	 * @param context 上下文
	 * @return 记录id
	 */
	public static long getVideoLastBackUpId(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getLong("VideoDBCountID", 0);
	}
	
	/**
	 * 保存用户最近浏览的一个收到的分享文件的分享日期
	 * @param context 上下文
	 * @param shareDate 收到的分享文件的分享日期
	 */
	public static void setLatestReceiveShareDate(Context context, String shareDate) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("LatestReceiveShareDate", shareDate);
		editor.commit();
	}
	/**
	 * 获取用户最近浏览的一个收到的分享文件的分享日期
	 * @param context 上下文
	 * @return 收到的分享文件的分享日期
	 */
	public static String getLatestReceiveShareDate(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getString("LatestReceiveShareDate", null);
	}
	
	/**
	 * 保存用户通讯录AccessToken的失效时间
	 * @param context 上下文
	 * @param expireTime AccessToken的具体失效时间 格式——yyyy-MM-dd HH:mm:ss
	 */
	public static void setContactsAccessTokenExpireTime(Context context, String expireTime) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("ContactsAccessTokenExpireTime", expireTime);
		editor.commit();
	}
	/**
	 * 获取用户通讯录AccessToken的失效时间
	 * @param context 上下文
	 * @return AccessToken的具体失效时间 格式——yyyy-MM-dd HH:mm:ss
	 */
	public static String getContactsAccessTokenExpireTime(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getString("ContactsAccessTokenExpireTime", null);
	}
	
	/**
	 * 保存用户通讯录AccessToken
	 * @param context 上下文
	 * @param accessToken AccessToken
	 */
	public static void setContactsAccessToken(Context context, String accessToken) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("ContactsAccessToken", accessToken);
		editor.commit();
	}
	/**
	 * 获取用户通讯录AccessToken
	 * @param context 上下文
	 * @return AccessToken
	 */
	public static String getContactsAccessToken(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getString("ContactsAccessToken", null);
	}
	/**
	 * 是否已经重命名手机相册名为“modle+相册”
	 * @param context
	 * @return
	 */
	public static boolean isRenamedAlbumFolder(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getBoolean("alreadyRenameAlbumFolder", false);
	}
	/**
	 * 设置是否已经重命名手机相册名为“modle+相册”
	 * @param context
	 * @param already
	 */
	public static void setAlreadyRenameAlbumFolder(Context context,boolean already){
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("alreadyRenameAlbumFolder", already);
		editor.commit();
	}
	
	/**
	 * 是否已经把本地已下载的文件加入DB
	 * @param context
	 * @return 未加入DB返回false,已加入返回true
	 */
	public static boolean isLocalFileAddedToDB(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getBoolean("LocalFileAddedToDB", false);
	}
	/**
	 * 设置是否已经把本地已下载的文件加入DB
	 * @param context
	 * @param added 未加入DB设值false,已加入设值true
	 */
	public static void setLocalFileAddedToDB(Context context,boolean added){
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("LocalFileAddedToDB", added);
		editor.commit();
	}
	
	/**是否已经提示用户送流量活动--进入首页送流量*/
	public static boolean isAlreadyPopFreeFlowTip(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getBoolean("isAlreadyPopFreeFlowTip", false);
	}
	
	/**设置是否已经提示用户送流量活动--进入首页送流量*/
	public static void setAlreadyPopFreeFlowTip(Context context, boolean showed) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isAlreadyPopFreeFlowTip", showed);
		editor.commit();
	}
	/**是否已经提示赠送T级空间*/
	public static boolean isAlreadyPopTspaceTip(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getBoolean("isAlreadyPopTspaceTip", false);
	}
	
	/**设置是否已经提示赠送T级空间*/
	public static void setAlreadyPopTspaceTip(Context context, boolean tiped) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isAlreadyPopTspaceTip", tiped);
		editor.commit();
	}

	/**
	 * 上次一键换机登录时间
	 * @param context 上下文
	 * @return 上次登录timestamp，单位毫秒
	 */
	public static long getLastLoginTimeForOneKey(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getLong("LastLoginTimeForOneKey", 0);
	}
	
	/**
	 * 设置上次登录时间
	 * @param context 上下文
	 * @param timestamp 时间戳，单位毫秒
	 */
	public static void setLastLoginTimeForOneKey(Context context, long timestamp) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong("LastLoginTimeForOneKey", timestamp);
		editor.commit();
	}
	/**
	 * 获取广告显示情况（格式为：广告id;日期;次数，如xx;2014-01-01;0）
	 * @param context 上下文
	 * @return 
	 */
	public static String getAdShowInfo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getString("AdShowInfo", "xx;2014-01-01;0");
	}
	
	/**
	 * 设置广告显示的情况
	 * @param context 上下文
	 * @param details 广告显示详情（格式为：广告id;日期;次数，如xx;2014-01-01;0）
	 */
	public static void setAdShowInfo(Context context, String details) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("AdShowInfo", details);
		editor.commit();
	}

	/**
	 * 获取广告2显示情况（格式为：广告id;日期;次数，如xx;2014-01-01;0）
	 * @param context 上下文
	 * @return
	 */
	public static String getAd2ShowInfo(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		return preferences.getString("Ad2ShowInfo", "xx;2014-01-01;0");
	}

	/**
	 * 设置广告2显示的情况
	 * @param context 上下文
	 * @param details 广告显示详情（格式为：广告id;日期;次数，如xx;2014-01-01;0）
	 */
	public static void setAd2ShowInfo(Context context, String details) {
		SharedPreferences preferences = context.getSharedPreferences(
				getUserSettingPreferencesName(context), Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("Ad2ShowInfo", details);
		editor.commit();
	}
	
	//**********************************保存用户共有数据，比如是否提示引导页******************
	/**
	 * 获取登录账号
	 * @param context 上下文
	 * @return 登录账号
	 */
	public static String getLastLoginUserName(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString("UserName", "");
	}
	
	/**
	 * 获取初次使用教程是否已经显示过
	 * @param context 上下文
	 * @return 是否已经显示过
	 * 
	 */
	public static boolean getIsFirstUseShowed(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean("FirstUseShowed", false);
	}
	/**
	 * 设置初次使用教程是否已经显示过
	 * @param context 上下文
	 * @param isShowed 是否已经显示过
	 */
	public static void setFirstUseShowed(Context context, boolean isShowed) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("FirstUseShowed", isShowed);
		editor.commit();
	}
	
	/**
	 * 获取闪屏页配置的最近更新时间串
	 * @param context 上下文
	 * @return 最近更新时间串
	 * 
	 */
	public static String getSplashLastupdated(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getString("SplashLastupdated", null);
	}
	/**
	 * 设置闪屏页配置的最近更新时间串
	 * @param context 上下文
	 * @param timeStr 最近更新时间串
	 */
	public static void setSplashLastupdated(Context context, String timeStr) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString("SplashLastupdated", timeStr);
		editor.commit();
	}
	
	/**
	 * 是否已经分离用户数据
	 * @param context 上下文
	 * @return true 没做过数据分离；false 已经做了数据分离
	 */
	public static boolean isNeedSeparateUserData(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean("NeedSeparateUserData", true);
	}
	/**
	 * 设置数据分离的标识位
	 * @param context 上下文
	 * @param needed 是否分离了数据
	 */
	public static void setNeedSeparateUserData(Context context, boolean needed) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("NeedSeparateUserData", needed);
		editor.commit();
	}

	/**
	 * 保持文件升序、降序类型
	 * @param context 上下文
	 * @param type 1表示升序，-1表示降序
	 */
	public static void setDescended(Context context, int type){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putInt("FileSortOrder", type);
		editor.commit();
	}

	/**
	 * 取启动页闪屏图片的md5
	 * @param context 上下文
	 * @return 
	 */
	public static String getSplashPicMd5(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString("SplashPicMd5", "");
	}
	/**
	 * 存启动页闪屏图片的md5
	 * @param context 上下文
	 * @param  md5
	 */
	public static void saveSplashPicMd5(Context context, String md5){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putString("SplashPicMd5", md5);
		editor.commit();
	}
	
	/**
	 * 是否第一次启动程序，add in v3.1
	 * @param context
	 * @return
	 */
	public static boolean getStartPageShowed(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean("StartPageShowed", false);
	}
	/**
	 * 保存已打开过程序   add in v3.1
	 * @param context
	 */
	public static void saveStartPageShowed(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("StartPageShowed", true);
		editor.commit();
	}
	
	/**是否第一次使用云相机*/
	public static boolean getIsFirstCamera(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean("isFirstCamera", true);
	}
	
	/**保存非第一次使用云相机*/
	public static void saveIsFirstCamera(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstCamera", false);
		editor.commit();
	}
	
	/**获取是否使用myId登录开关状态*/
	public static boolean isAllowedMyIdLogin(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean("isAllowMyIdLogin", true);
	}
	
	/**设置是否使用myId登录的开关状态*/
	public static void setAllowedMyidLogin(Context context, boolean allowed) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("isAllowMyIdLogin", allowed);
		editor.commit();
	}
	/**是否已经提示用户送流量活动--分享送流量*/
	public static boolean isAlreadyShowedFreeFlowTip(Context context) {
		if(true){
			return false;
		}
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean("isShowedFreeFlowTip", false);
	}
	
	/**设置是否已经提示用户送流量活动--分享送流量*/
	public static void setShowedFreeFlowTip(Context context, boolean showed) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("isShowedFreeFlowTip", showed);
		editor.commit();
	}
	
	/**是否已经提示用户使用4G网络*/
	public static boolean isShowedNetWorkTip(Context context) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return preferences.getBoolean("isShowedNetWorkTip", false);
	}
	
	/**设置是否已经提示用户使用4G网络*/
	public static void setShowedNetWorkTip(Context context, boolean showed) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("isShowedNetWorkTip", showed);
		editor.commit();
	}
	

	/**
	 * 登陆前引导 ("isShowInstructionBeforLogin"字段修改为"isShowInstructionBeforLogin2"，以便升级用户也能启动引导)
	 * */
	public static boolean getShowInstructionBeforLogin(Context context){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean("isShowInstructionBeforLogin2", true);
	}
	
	public static void saveShowInstructionBeforLogin(Context context, boolean isShow){
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = preferences.edit();
		editor.putBoolean("isShowInstructionBeforLogin2", isShow);
		editor.commit();
	}
	
	/**
	 * 获取流量宝提示信息
	 * @param context 上下文
	 * @return 上次提示时间，第几次@时间，如1@2014-02-02 10:00:00,默认值为空（“”）
	 */
    public static String getFlowPlayTipInfo(Context context) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return preferences.getString("FlowPlayTipInfo", "");
    }
    /**
     * 设置流量宝提示信息
     * @param context 上下文
     * @param info 格式，第几次@时间，如1@2014-02-02 10:00:00
     */
    public static void setFlowPlayTipInfo(Context context, String info) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString("FlowPlayTipInfo", info);
        editor.commit();
    }
}
