package com.handgold.pjdc.ui.Order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.activity.VideoListActivity;
import com.handgold.pjdc.activity.VideoPlayer2Activity;
import com.handgold.pjdc.base.ApplicationEx;
import com.handgold.pjdc.base.MenuTypeEnum;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;

/**
 * Created by lihb on 15/5/21.
 */
public class OrderRightFragment extends android.support.v4.app.Fragment {

    private static int NUM_ITEMS;

    private static ListView orderRightLv;

    private static Order mOrder;

    private static List<MenuItemInfo> orderMenuList;

    private static SortedMap<Integer, List<MenuItemInfo>> sortedMap;

    private static RefreshLeftFragListener mRefreshLeftFragListener;

    private static OrderRightFragAdapter mAdapter;

    private ViewPager viewPager;

    private static StatePagerAdapter statePagerAdapter;

    private PagerTabStrip pagerTabStrip;

    private List<String> titleList;

    private static int flagToAdapter;

    public static final String ORDERRIGHTFRAGMENT = "OrderRightFragment";

    private static Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortedMap = (SortedMap<Integer, List<MenuItemInfo>>) ((ApplicationEx) getActivity().getApplication()).receiveInternalActivityParam("allMenuList");
        //构造viewpager的标题
        Iterator it = sortedMap.keySet().iterator();
        titleList = new ArrayList<String>();
        while (it.hasNext()) {
            int type = (Integer) it.next() - 1;
            if (type == MenuTypeEnum.RECOMMEND.ordinal()) {
                titleList.add("主厨推荐");
            }/* else if (type == MenuTypeEnum.MEALSET.ordinal()) {
                titleList.add("套餐");
            }*/ else if (type == MenuTypeEnum.DRINK.ordinal()) {
                titleList.add("小吃");
            } else {
                titleList.add("其他");
            }
        }
        NUM_ITEMS = titleList.size();
        mContext = getActivity();
        flagToAdapter = getArguments().getInt(ORDERRIGHTFRAGMENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rightView = inflater.inflate(R.layout.order_right_fragment, container, false);

        viewPager = (ViewPager) rightView.findViewById(R.id.viewpager);
        pagerTabStrip = (PagerTabStrip) rightView.findViewById(R.id.pagertabstrip);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.orange));
        pagerTabStrip.setDrawFullUnderline(false);
        pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.win_bar_bg));
        pagerTabStrip.setTextSpacing(50);
        statePagerAdapter = new StatePagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(statePagerAdapter);

        return rightView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mRefreshLeftFragListener = (RefreshLeftFragListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + "must implement RefreshLeftFragListener");
        }
    }


    /**
     * 回调接口，传递数据到activity，用来刷新左边的fragment
     */
    public interface RefreshLeftFragListener {
        void changeDataToLeft(Order order);
    }


    public class StatePagerAdapter extends FragmentStatePagerAdapter {

        public StatePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.getInstances(position);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        /**
         * 刷新本viewpager的页面事件一定要调用的方法
         * 
         * @param object
         * @return
         */
        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    public static class ArrayListFragment extends android.support.v4.app.Fragment {

        int num;

        static ArrayListFragment getInstances(int num) {
            ArrayListFragment listFragment = new ArrayListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("num", num);
            listFragment.setArguments(bundle);
            return listFragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            num = (getArguments() != null ? getArguments().getInt("num") : 1);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.order_right_frag_pager_list, null);
            orderRightLv = (ListView) view.findViewById(R.id.order_right_lv);
            mOrder = (Order) ((ApplicationEx) getActivity().getApplication()).receiveInternalActivityParam("order");
            List<MenuItemInfo> tmpList = sortedMap.get(num + 1);


            mAdapter = new OrderRightFragAdapter(getActivity(), new AdapterCallBack(), flagToAdapter);
//            mAdapter.setData(mOrder.getMenuList());
            mAdapter.setData(tmpList);
            orderRightLv.setAdapter(mAdapter);
            return view;
        }

        @Override
        public void onPause() {
            super.onPause();
        }

    }

    public static class AdapterCallBack implements OrderRightFragAdapter.OrderRightFragListener {

        @Override
        public void onItemClicked(Object itemData) {
            if (flagToAdapter == VideoListActivity.VIDEOLISTACTIVITY) {
                MenuItemInfo menu = (MenuItemInfo) itemData;
                Intent intent = new Intent(mContext, VideoPlayer2Activity.class);
                menu.setVideo("http://download.cloud.189.cn/v5/downloadFile.action?downloadRequest=1_266BEB5F2F53474145C6EBE33E9A75D592251F2581CFE66ED934BC80674F070BA6790DA91C37DD2867779B6A435B6E040ED7928D6EFEB456A463C8E6238E8DA431473E7443FCC8025B64223A6700BF64EDD9FFDFEEA7447A59FC024F4CE7979319CFCCF6F79641E0E10945F7D23B60F7557901BF94E0BF88DFACD44EF40A4A4D0E77B882");
                intent.putExtra("videoUrl", menu.getVideo());
                intent.putExtra("fileName", menu.getName());
                mContext.startActivity(intent);
            }

        }

        @Override
        public void onAddMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            mOrder.addMenu(menu);
            statePagerAdapter.notifyDataSetChanged();
            mRefreshLeftFragListener.changeDataToLeft(mOrder);
            mAdapter.notifyDataSetChanged();

        }

        @Override
        public void onSubMenuClicked(Object itemData) {
            MenuItemInfo menu = (MenuItemInfo) itemData;
            mOrder.delMenu(menu);
            statePagerAdapter.notifyDataSetChanged();
            mRefreshLeftFragListener.changeDataToLeft(mOrder);
            mAdapter.notifyDataSetChanged();
        }

    }

}
