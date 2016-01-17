package com.handgold.pjdc.base;

/**
 * 保持应用运行的状态变量，退出是应该被清空
 */
public class MyAppContext {


	/** 清除状态变量 */
	public static void clear() {
		// 全局配置，即使注销也不清除
		// newConfigs = null;
		has_login_private_zoom = false;
		private_zoom_key = "";
	}

/*	*//** 配置信息 *//*
	public static ConfigList newConfigs = null;

	*//**
	 * 用户信息
	 *//*
	public static UserInfo userInfo = new UserInfo();
	
	*//**
     * 用户扩展信息
     *//*
    public static UserInfoExt userInfoExt = new UserInfoExt();
    
	*//** 版本升级信息 *//*
	public static ClientVersionCheck mClientVersionCheck;*/

	/**
	 * 渠道id，在application初始化
	 */
	public static String CLIENT_VERSION = "";

	/** 渠道id，在application初始化 */
	public static String CHANNEL_ID = "";
	/**
	 * 屏幕高宽
	 */
	public static int screenW, screenH;
	/** 屏幕的逻辑密度 */
	public static float screenDensity = 1.0f;
	/**
	 * Http响应头返回的时间串，成功登录后赋值，作为基准时间，在退出时清空
	 */
	public static String base_http_resp_date = null;
	/**
	 * 当前机器总共运行时间，成功登录后赋值，退出时清0
	 */
	public static long base_elapsed_time = 0L;
	/**
	 * 从第三方应用分享到天翼云的缓存目录
	 */
	public static String share_pending_path = null;
	/**
	 * 判断是登录过私密空间
	 */
	public static boolean has_login_private_zoom = false;
	/**
	 * 私密空间的key
	 */
	public static String private_zoom_key = "";

	/** 流量统计 *//*
	public static DataFlowBean dataFlow = new DataFlowBean();

	*//** 活动消息 *//*
	public static List<UserMsg> noticeMessage = null;
	*//** 群空间消息 *//*
	public static List<UserMsg> groupMessage = null;
	*//** 分享消息 *//*
	public static List<UserMsg> shareMessage = null;*/

}
