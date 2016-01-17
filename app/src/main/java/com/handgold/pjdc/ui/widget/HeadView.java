package com.handgold.pjdc.ui.widget;

import android.app.Activity;
import android.view.View;
import android.widget.*;
import com.handgold.pjdc.R;

public class HeadView {

    public FrameLayout headerRootContainer;

    public LinearLayout headerDefaultMenu;

    public LinearLayout h_header;

    public RelativeLayout h_left_rlyt;

    public LinearLayout h_tab_llyt;

    public ImageView h_left, h_right;

    public ImageView h_right_bg;

    public ImageView h_left_new_msg_iv, h_right_new_msg_iv;

    public TextView h_title;

    public View h_bottom_line;

    public RelativeLayout h_transfer_rlyt;

    public LinearLayout h_transfer_msg_llyt;

    public TextView h_transfer_msg_tv;

    public FrameLayout h_more_rlyt;

    public LinearLayout h_right_tv_llyt;

    public TextView h_right_tv;

    public TextView h_left_tv;

    public ImageView h_title_img;

    public HeadView(Activity act){
        init(act);
    }

    public HeadView(View contentView){
        init(contentView);
    }

    private void init(Activity act) {
        headerRootContainer = (FrameLayout) act.findViewById(R.id.header_root_container);
        headerDefaultMenu = (LinearLayout) act.findViewById(R.id.header_default_menu);
        h_header = (LinearLayout) act.findViewById(R.id.cloud_head);
        h_left_rlyt = (RelativeLayout) act.findViewById(R.id.head_left_rlyt);
        h_left = (ImageView) act.findViewById(R.id.head_left);
        h_title_img = (ImageView) act.findViewById(R.id.head_title_img);
//        h_tab_llyt = (LinearLayout) act.findViewById(R.id.head_tab_llyt);
//        h_right = (ImageView) act.findViewById(R.id.head_right);
//        h_right_bg = (ImageView) act.findViewById(R.id.head_right_bg);
//        h_left_new_msg_iv = (ImageView) act.findViewById(R.id.head_left_new_msg_iv);
//        h_right_new_msg_iv=(ImageView)act.findViewById(R.id.head_right_new_msg_iv);
        h_title = (TextView) act.findViewById(R.id.head_title);
        h_bottom_line = act.findViewById(R.id.head_bottom_line);
        h_left_tv = (TextView) act.findViewById(R.id.head_left_tv);

//        h_transfer_rlyt = (RelativeLayout) act.findViewById(R.id.head_transfer_rlyt);
//        h_transfer_msg_llyt = (LinearLayout) act.findViewById(R.id.head_transfer_msg_llyt);
//        h_transfer_msg_tv = (TextView) act.findViewById(R.id.head_transfer_msg_tv);
//        h_more_rlyt = (FrameLayout) act.findViewById(R.id.head_right_flyt);
        h_right_tv_llyt = (LinearLayout) act.findViewById(R.id.head_right_tv_layout);
        h_right_tv = (TextView) act.findViewById(R.id.head_right_tv);

    }

    private void init(View contentView) {
        headerRootContainer = (FrameLayout) contentView.findViewById(R.id.header_root_container);
        headerDefaultMenu = (LinearLayout) contentView.findViewById(R.id.header_default_menu);
        h_header = (LinearLayout) contentView.findViewById(R.id.cloud_head);
        h_left_rlyt = (RelativeLayout) contentView.findViewById(R.id.head_left_rlyt);
        h_left = (ImageView) contentView.findViewById(R.id.head_left);
        h_title_img = (ImageView) contentView.findViewById(R.id.head_title_img);
//        h_tab_llyt = (LinearLayout) contentView.findViewById(R.id.head_tab_llyt);
//        h_right = (ImageView) contentView.findViewById(R.id.head_right);
//        h_right_bg = (ImageView) contentView.findViewById(R.id.head_right_bg);
//        h_left_new_msg_iv = (ImageView) contentView.findViewById(R.id.head_left_new_msg_iv);
//        h_right_new_msg_iv=(ImageView)contentView.findViewById(R.id.head_right_new_msg_iv);
        h_title = (TextView) contentView.findViewById(R.id.head_title);
        h_bottom_line = contentView.findViewById(R.id.head_bottom_line);
        h_left_tv = (TextView) contentView.findViewById(R.id.head_left_tv);

//        h_transfer_rlyt = (RelativeLayout) contentView.findViewById(R.id.head_transfer_rlyt);
//        h_transfer_msg_llyt = (LinearLayout) contentView.findViewById(R.id.head_transfer_msg_llyt);
//        h_transfer_msg_tv = (TextView) contentView.findViewById(R.id.head_transfer_msg_tv);
//        h_more_rlyt = (FrameLayout) contentView.findViewById(R.id.head_right_flyt);
        h_right_tv_llyt = (LinearLayout) contentView.findViewById(R.id.head_right_tv_layout);
        h_right_tv = (TextView) contentView.findViewById(R.id.head_right_tv);
        
    }

    public void setHeadBgColor(int color) {
        h_header.setBackgroundColor(color);
    }
}
