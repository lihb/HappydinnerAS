package com.handgold.pjdc.util;

import android.annotation.SuppressLint;

/**
 * Created by Administrator on 2015/8/31.
 */
public class Ver {
    public int mMajor;
    public int mMinor;
    public int mBuild;

    public Ver() {
    }

    public boolean bigThan(Ver v) {
        return this.mMajor > v.mMajor || this.mMajor == v.mMajor && this.mMinor > v.mMinor || this.mMajor == v.mMajor && this.mMinor == v.mMinor && this.mBuild > v.mBuild;
    }

    public boolean smallThan(Ver v) {
        return this.mMajor < v.mMajor || this.mMajor == v.mMajor && this.mMinor < v.mMinor || this.mMajor == v.mMajor && this.mMinor == v.mMinor && this.mBuild < v.mBuild;
    }

    public boolean equals(Object o) {
        if(o == null) {
            return false;
        } else if(!(o instanceof Ver)) {
            return false;
        } else {
            Ver v = (Ver)o;
            return this.mMajor == v.mMajor && this.mMinor == v.mMinor && this.mBuild == v.mBuild;
        }
    }

    public int hashCode() {
        return this.mMajor * 100 + this.mMajor * 10 + this.mBuild;
    }

    @SuppressLint({"DefaultLocale"})
    public String toString() {
        return String.format("%d.%d.%d", Integer.valueOf(this.mMajor), Integer.valueOf(this.mMinor), Integer.valueOf(this.mBuild));
    }
}
