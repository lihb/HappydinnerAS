package com.handgold.pjdc.ui.Pay;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class PayLeftFragment extends Fragment {


    private final int ZHIFUBAO = 0;
    private final int WECHAT = 1;

    LinearLayout zhifubaoLayout = null;
    LinearLayout wechatLayout = null;

    ImageView zhifubaoImg = null;
    ImageView wechatImg = null;

    TextView zhifubaoText = null;
    TextView wechatText = null;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;
    private PayRightZhiFuBaoFragment payRightZhiFuBaoFragment;
    private PayRightWeChatFragment payRightWeChatFragment;
    private PayLeftFragment payLeftFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pay_left_fragment, null);
        zhifubaoLayout = (LinearLayout) view.findViewById(R.id.layout_zhifubao);
        wechatLayout = (LinearLayout) view.findViewById(R.id.layout_wechat);
        zhifubaoImg = (ImageView) zhifubaoLayout.findViewById(R.id.pay_icon_iv);
        zhifubaoText = (TextView) zhifubaoLayout.findViewById(R.id.pay_desc_tv);

        wechatImg = (ImageView) wechatLayout.findViewById(R.id.pay_icon_iv);
        wechatText = (TextView) wechatLayout.findViewById(R.id.pay_desc_tv);

        zhifubaoImg.setImageResource(R.drawable.icon_zhifubao);
        zhifubaoText.setText("支付宝");

        wechatImg.setImageResource(R.drawable.icon_wechat);
        wechatText.setText("微信");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        zhifubaoLayout.setSelected(true);
        zhifubaoLayout.setOnClickListener(mOnclickListener);
        wechatLayout.setOnClickListener(mOnclickListener);
        mFragmentManager = getFragmentManager();
    }


    private View.OnClickListener mOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mTransaction = mFragmentManager.beginTransaction();
//            payLeftFragment = new PayLeftFragment();
//            mTransaction.replace(R.id.left_frag, payLeftFragment, "pay_left_frag");
            Intent intent = getActivity().getIntent();
            float price = intent.getFloatExtra("totalPrice", 0f);
            if (v == zhifubaoLayout) {
                Log.i("PayLeftFragment", "zhifubao layout click!!!");
                zhifubaoLayout.setSelected(true);
                wechatLayout.setSelected(false);
                Bundle bundle = new Bundle();
                bundle.putFloat("price", price);
                payRightZhiFuBaoFragment = new PayRightZhiFuBaoFragment();
                payRightZhiFuBaoFragment.setArguments(bundle);
                mTransaction.replace(R.id.right_frag, payRightZhiFuBaoFragment, "pay_right_zhifubao_frag").commit();
            }else {
                Log.i("PayLeftFragment", "weixin layout click!!!");
                wechatLayout.setSelected(true);
                zhifubaoLayout.setSelected(false);
                Bundle bundle = new Bundle();
                bundle.putFloat("price", price);
                payRightWeChatFragment = new PayRightWeChatFragment();
                payRightWeChatFragment.setArguments(bundle);
                mTransaction.replace(R.id.right_frag, payRightWeChatFragment, "pay_right_wechat_frag").commit();
            }
        }
    };

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("PayLeftFragment"); //统计页面
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayLeftFragment");
    }
}
