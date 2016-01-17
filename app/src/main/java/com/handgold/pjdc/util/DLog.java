/**
 * 
 */
package com.handgold.pjdc.util;

import android.util.Log;
import com.handgold.pjdc.services.DinnerPathManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 日志信息输出类
 * 
 * <p>该类可自动或手动配置不同等级日志在发布模式下是否允许输出，
 * 并使用android.util.Log输出日志内容</p>
 *
 */
public final class DLog {
	/**
	 * 输出所有log
	 */
	public static final int LEVEL_VERBOSE = 1;
	public static final int LEVEL_DEBUG = 2;
	public static final int LEVEL_INFO = 3;
	public static final int LEVEL_WARN = 4;
	public static final int LEVEL_ERROR = 5;
	/**
	 * 不输出log
	 */
	public static final int LEVEL_NONE = 6;
	
	private static int mLevel = LEVEL_NONE;
	
	/**
	 * 
	 * @param debug true输出所有日志，false不输出任何日志。
	 */
	public static void setInDebugMode(boolean debug) {
		if(debug){
			mLevel = LEVEL_VERBOSE;
		}else{
			mLevel = LEVEL_NONE;
		}
	}
	/**
	 *  设置日志级别
	 * @param level {@link DLog#LEVEL_VERBOSE}、{@link DLog#LEVEL_DEBUG}......
	 */
	public static void setLogLevel(int level){
		mLevel = level;
	}
	
	public static int d(String tag, String msg) {
		if(mLevel <= LEVEL_DEBUG) {
			return Log.d(tag, msg);
		}
		return 0;
	}
	public static int d(String tag, String msg, Throwable tr) {
		if(mLevel <= LEVEL_DEBUG) {
			return Log.d(tag, msg, tr);
		}
		return 0;
	}
	public static int i(String tag, String msg) {
		if(mLevel <= LEVEL_INFO) {
			return Log.i(tag, msg);
		}
		return 0;
	}
	public static int i(String tag, String msg, Throwable tr) {
		if(mLevel <= LEVEL_INFO) {
			return Log.i(tag, msg, tr);
		}
		return 0;
	}
	public static int v(String tag, String msg) {
		if(mLevel <= LEVEL_VERBOSE) {
			return Log.v(tag, msg);
		}
		return 0;
	}
	public static int v(String tag, String msg, Throwable tr) {
		if(mLevel <= LEVEL_VERBOSE) {
			return Log.v(tag, msg, tr);
		}
		return 0;
	}
	public static int w(String tag, String msg) {
		if(mLevel <= LEVEL_WARN) {
			return Log.w(tag, msg);
		}
		return 0;
	}
	public static int w(String tag, String msg, Throwable tr) {
		if(mLevel <= LEVEL_WARN) {
			return Log.w(tag, msg, tr);
		}
		return 0;
	}
	public static int e(String tag, String msg) {
		if(mLevel <= LEVEL_ERROR) {
			return Log.e(tag, msg);
		}
		return 0;
	}
	public static int e(String tag, String msg, Throwable tr) {
		if(mLevel <= LEVEL_ERROR) {
			return Log.e(tag, msg, tr);
		}
		return 0;
	}
	public static void write2File(String tag, String msg){
		write2File(tag, msg, TimeUtils.getNowDateShort()+"_log.txt");
	}
	
	
	private static boolean mbNeedWrite2File = false;
	
	public static void write2File(String tag, String msg,String name){
		if(mbNeedWrite2File){
			
			String fileDir = DinnerPathManager.get().getLogPath();
			String fileName = name;
			
			File file = new File(fileDir+fileName);
			FileWriter fw = null;
			try {
				fw = new FileWriter(file,true);
				StringBuffer sb = new StringBuffer();
				sb.append(TimeUtils.getNowDateLongest());
				sb.append("	");
				sb.append(tag);
				sb.append("	");
				sb.append(msg);
				sb.append("\n\t");
				fw.write(sb.toString());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	public static void setLogW2FInRelease(boolean b) {
		mbNeedWrite2File = b;
	}
	public static boolean isW2FEnabledInRelease() {
		return mbNeedWrite2File;
	}
}
