package com.handgold.pjdc.base;

/**
 * 当离线状态时，再联网请求数据时，抛出此异常
 * 
 * @author denghou
 * 
 */
public class NetWorkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NetWorkException() {
		super();
	}

	public NetWorkException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetWorkException(String detailMessage) {
		super(detailMessage);
	}

	public NetWorkException(Throwable throwable) {
		super(throwable);
	}

}
