package com.handgold.pjdc.action;

import java.io.InputStream;

/**
 * Created by wangqiming on 15/10/2.
 */
public class StreamWrapper {
    public InputStream inputStream;
    public String name;
    public String contentType;

    public StreamWrapper(InputStream inputStream, String name, String contentType) {
        this.inputStream = inputStream;
        this.name = name;
        this.contentType = contentType;
    }
}
