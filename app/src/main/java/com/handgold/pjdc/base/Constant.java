package com.handgold.pjdc.base;


/**
 * @author Administrator
 *
 */
public class Constant {

	/**
	 * appSecret
	 */
	public static final String TYY_APP_SECRET = "rx8C3Zgh";

	/**
	 * 客户端类型
     */
	public final static String CLIENT_TYPE = "TELEANDROID";
	/**网络类型--所有网络*/
	public final static int NETWORK_TYPE_ALL = 1;
	/**网络类型--wifi*/
	public final static int NETWORK_TYPE_ONLY_WIFI = 0;
	
	// 检查首次使用引导界面所需的版本号
	public final static int FIRST_USE_GUIDE_VERSION_CODE = 59;
	/** 控制某些提示的显示的id号 */
	public static final int CONTROL_PROMPT_ALBUMBACK_ID = 60;

    //——————————————————————————————————————————————————微信支付——————————————————————————————

	// 公众号ID
	public static final String APP_ID = "wx8b8f5cc4dc48ff84";

	// 商户号
	public static final  String MCH_ID = "1292957201";

	// 用来生成签名的key
	public static final String WECHAT_KEY = "d89852607452a741984f28caa4612a49";

	// 回调地址
	public static final  String NOTIFY_URL = "  http://120.55.162.6/api/pay/wxcallback";

	// 统一下单 api
	public static final String UNIFY_PAY_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	//——————————————————————————————————————————————————微信支付——————————————————————————————

	public static class ServerRes{
		public static final int SVR_RES_SUCCESS = 0;
		public static final int SVR_RES_SERVER_FAULT = 10001;
		public static final int SVR_RES_PARAM_MISS = 20001;
		public static final int SVR_RES_PARAM_FORM_FAULT = 20002;
		public static final int SVR_RES_TICKET_INVALID = 20003;
		public static final int SVR_RES_SMSCODE_UPPERLIM = 20101;
		public static final int SVR_RES_NAME_USED = 20102;
		public static final int SVR_RES_USER_NOT_EXIST = 20103;
		public static final int SVR_RES_SMSCODE_NOT_EXIST = 20104;
		public static final int SVR_RES_SMSCODE_NOT_EQUAL = 20105;
		public static final int SVR_RES_SMSCODE_INVALID = 20106;
		public static final int SVR_RES_FAULT_UPPER_LIM = 20107;
		public static final int SVR_RES_TOKEN_INVALID = 20108;
		public static final int SVR_RES_ACCOUNT_PASSWD_WRONG = 20109;
		public static final int SVR_RES_NO_LIVE_PERMISSION = 20201;
		public static final int SVR_RES_LIVE_UPPER_LIM = 20202;
		public static final int SVR_RES_DUPLICATE_LIVE = 20203;
		public static final int SVR_RES_NO_SESSION = 20204;
		public static final int SVR_RES_LIVE_OFFLINE = 20205;
		public static final int SVR_RES_NO_ACCESS_PERMISSION = 20206;
		public static final int SVR_RES_AUDIENCE_UPPER_LIM = 20207;
		public static final int SVR_RES_DUPLICATE_JOIN = 20208;
		public static final int SVR_RES_NOT_IN_SESSION = 20209;
		// 已经在PC登录直播
		public static final int SVR_RES_LOGIN_PC = 20110;
		public static final int SVR_RES_NO_RESPONSE = -1;

	}



}
