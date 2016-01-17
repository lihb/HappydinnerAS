package com.handgold.pjdc.ui.Pay;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.action.SingleOkHttpClient;
import com.handgold.pjdc.activity.PayActivity;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.Constant;
import com.handgold.pjdc.entitiy.Order;
import com.handgold.pjdc.entitiy.WeChatReqData;
import com.handgold.pjdc.entitiy.WeChatResData;
import com.handgold.pjdc.util.CommonUtils;
import com.handgold.pjdc.util.WeChatUtil;
import com.umeng.analytics.MobclickAgent;
import retrofit.*;
import retrofit.http.Body;
import retrofit.http.POST;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类说明：
 *
 * @author Administrator
 * @version 1.0
 * @date 2015/6/16
 */

public class PayRightWeChatFragment extends Fragment {

    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
    private final static String DEFAULT_ENCODING = "UTF-8";


    @InjectView(R.id.pay_price_tv)
    TextView payPriceTv;

    @InjectView(R.id.pay_choice_tv)
    TextView payChoiceTv;

    @InjectView(R.id.pay_info_step1_img)
    ImageView payInfoStep1Img;

    @InjectView(R.id.pay_info_step1_tv)
    TextView payInfoStep1Tv;

    @InjectView(R.id.pay_info_step2_img)
    ImageView payInfoStep2Img;

    @InjectView(R.id.pay_info_step2_tv)
    TextView payInfoStep2Tv;

    private Order mOrder = null;

    /**
     * 微信的获取二维码链接接口
     */
    public interface WeChat {
        @POST("/pay/unifiedorder")
        Call<WeChatResData> getQRCode(@Body WeChatReqData weChatEntity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.pay_right_fragment, null);
        ButterKnife.inject(this, view);
        Bundle bundle = getArguments();
        float price = bundle.getFloat("price");
        float temp = CommonUtils.round(price, 2, BigDecimal.ROUND_HALF_UP);
        payPriceTv.setText("金额：" + temp + "元");
        payChoiceTv.setText("微信支付");
        payInfoStep1Tv.setText("1.打开手机微信应用\n   点击扫一扫功能");
        payInfoStep2Tv.setText("2.扫描该二维码，成功后\n     按照提示输入密码");
        return view;
    }


    public void onResume() {
        super.onResume();
        Log.i("PayRightWeChatFragment", "onResume");
        //获取二维码
        generateAndPostData();
        MobclickAgent.onPageStart("PayRightWeChatFragment"); //统计页面
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PayRightWeChatFragment");
    }

    private void generateAndPostData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //获取订单数据
        mOrder = (Order) ((ApplicationEx) (getActivity()).getApplication()).receiveInternalActivityParam("order");
        if (mOrder != null) {
            String appid = Constant.APP_ID;
            String mch_id = Constant.MCH_ID;
            String device_info = "WEB";
            String nonce_str = WeChatUtil.getRandomStringByLength(32);
            String body = "测试！！";
            String detail = "";
            String out_trade_no = WeChatUtil.getRandomStringByLength(19) + System.currentTimeMillis();
//            int total_fee = (int) (mOrder.getTotalPrice() * 100);
            int total_fee = (1);
            String spbill_create_ip = WeChatUtil.getLocalIp();
            String time_start = sdf.format(new Date());
            String time_expire = "";
            String goods_tag = "";
            String notify_url = Constant.NOTIFY_URL;
            String trade_type = "NATIVE";
            String product_id = "";
            WeChatReqData weChatEntity = new WeChatReqData(appid, mch_id, device_info, nonce_str, body,
                    detail, out_trade_no, total_fee, spbill_create_ip, time_start,
                    time_expire, goods_tag, notify_url, trade_type, product_id);

            postDataByRetrofit(weChatEntity);

            //解决XStream对出现双下划线的bug
//            XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
////            Log.i("PayRightWeChatFragment", "路径===" + WeChatReqData.class.getCanonicalName());
////            String path = WeChatReqData.class.getCanonicalName();
            //将要提交给API的数据对象转换成XML格式数据Post给API
//            String postDataXML = xStreamForRequestPostData.toXML(weChatEntity);
            // 子线程发送请求
//            new WeChatAsyncTask().execute(postDataXML);
        }

    }

    /**
     * 法一 使用Retrofit发送数据到微信接口，获取二维码链接
     * @param weChatEntity
     */
    private void postDataByRetrofit(WeChatReqData weChatEntity) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mch.weixin.qq.com")
                .client(SingleOkHttpClient.getInstance())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();
        WeChat weChat = retrofit.create(WeChat.class);
        Call<WeChatResData> call = weChat.getQRCode(weChatEntity);
        call.enqueue(new Callback<WeChatResData>() {
            @Override
            public void onResponse(Response<WeChatResData> response, Retrofit retrofit) {
                Log.e("PayRightWeChatFragment", response.body().getAppid());
                generateQRBitmap(response.body());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("PayRightWeChatFragment", "调用获取微信支付二维码接口失败。。。。");
            }
        });

    }

    /**
     * 法二 使用AsyncTask和URLConnection发送数据到微信接口，获取二维码链接
     */
    private class WeChatAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            try {

                URL url = new URL(Constant.UNIFY_PAY_API);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(CONNECT_TIMEOUT);
                conn.setRequestProperty("Content-Type", "text/xml");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);

                //write parameters
                writer.write(params[0]);
                Log.e("PayRightWeChatFragment", "params[0] = " + params[0]);
                writer.flush();

                // Get the response
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                writer.close();
                reader.close();

                //Output the response
                result = sb.toString();
                Log.e("PayRightWeChatFragment", "result = " + result);
            } catch (UnsupportedEncodingException | MalformedURLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            //将从API返回的XML数据映射到Java对象
            WeChatResData weChatResData = (WeChatResData) WeChatUtil.getObjectFromXML(res, WeChatResData.class);

            generateQRBitmap(weChatResData);

        }
    }

    /**
     * 根据微信返回的结果生成二维码图片，或者提示错误信息。
     * @param weChatResData
     */
    private void generateQRBitmap(WeChatResData weChatResData) {
        if (weChatResData == null || weChatResData.getReturn_code() == null) {
            Log.e("PayRightWeChatFragment", "【支付失败】支付请求逻辑错误，请仔细检测传过去的每一个参数是否合法，或是看API能否被正常访问");
            CommonUtils.toastText(getActivity(), getString(R.string.get_wechat_link_fail));
            return;
        }

        if (TextUtils.equals(weChatResData.getReturn_code(), "FAIL")) {
            Log.e("PayRightWeChatFragment", "【支付失败】支付API系统返回失败，请检测Post给API的数据是否规范合法");
            CommonUtils.toastText(getActivity(), getString(R.string.get_wechat_link_fail));
            return;
        }

        if (TextUtils.equals(weChatResData.getReturn_code(), "SUCCESS")) {

            Log.i("PayRightWeChatFragment", "支付API系统成功返回数据");
            if (TextUtils.equals(weChatResData.getResult_code(), "SUCCESS")) {
                String code_url = weChatResData.getCode_url();
                Log.i("PayRightWeChatFragment", "code_url = "+ code_url);
                Log.i("PayRightWeChatFragment", "trade_type = "+ weChatResData.getTrade_type());
                Log.i("PayRightWeChatFragment", "prepay_id = "+ weChatResData.getPrepay_id());
//                Glide.with(this).load("http://goo.gl/gEgYUd").into(payInfoStep1Img);
                ((PayActivity) getActivity()).generateQrImg(payInfoStep2Img, code_url);

            }else {
                //获取错误码
                String errorCode = weChatResData.getErr_code();
                //获取错误描述
                String errorCodeDes = weChatResData.getErr_code_des();

                Log.e("PayRightWeChatFragment", "errorCode = "+ errorCode);
                Log.e("PayRightWeChatFragment", "errorCodeDes = "+ errorCodeDes);
                CommonUtils.toastText(getActivity(), getString(R.string.get_wechat_link_fail));
            }
        }

    }

}
