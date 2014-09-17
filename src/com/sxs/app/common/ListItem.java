package com.sxs.app.common;

import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-21
 * Time: 下午11:22
 * To change this template use File | Settings | File Templates.
 */
public class ListItem {

    private  int imageType;

    private TextView textView;

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }
}
