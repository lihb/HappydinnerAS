package com.handgold.pjdc.ui.Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.activity.VideoListActivity;
import com.handgold.pjdc.entitiy.MenuItemInfo;

import java.util.ArrayList;
import java.util.List;

public class OrderRightFragAdapter extends BaseAdapter {

    private List<MenuItemInfo> mData = new ArrayList<MenuItemInfo>();

    private Context mContext;

    private OrderRightFragListener mOrderRightFragListener;

    private int fromWhichActivity;

    public OrderRightFragAdapter(Context context, OrderRightFragListener onOrderRightFragListener, int flag) {
        mContext = context;
        mOrderRightFragListener = onOrderRightFragListener;
        fromWhichActivity = flag;
    }

    public void setData(List<MenuItemInfo> data) {
        mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int pos) {
        return mData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.order_right_item, null);
            ViewHolder viewHolder = new ViewHolder(rowView);
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        final MenuItemInfo menu = mData.get(position);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOrderRightFragListener != null) {
                    mOrderRightFragListener.onItemClicked(menu);
                }
            }
        });

        holder.orderMenuNameTv.setText(menu.getName());
        // todo 设置菜品图片
        holder.orderMenuMateriasTv.setText(menu.getInfo());
        holder.orderMenuPriceTv1.setText("" + menu.getPrice());

        // 视频界面则隐藏加减数字按钮
        if (fromWhichActivity == VideoListActivity.VIDEOLISTACTIVITY) {
            holder.orderMenuCountLl.setVisibility(View.GONE);
        }

        holder.menuCountConfirmTv1.setText("" + menu.count);

        holder.menuCountSubTv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOrderRightFragListener != null) {
                    mOrderRightFragListener.onSubMenuClicked(menu);
                }

            }
        });

        holder.menuCountAddTv1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mOrderRightFragListener != null) {
                    mOrderRightFragListener.onAddMenuClicked(menu);
                }
            }
        });

        return rowView;
    }

    public interface OrderRightFragListener {
        /**
         * 当条目选中状态改变时调用
         */
        void onItemClicked(Object itemData);

        /**
         * 加菜
         */
        void onAddMenuClicked(Object itemData);

        /**
         * 减菜
         */
        void onSubMenuClicked(Object itemData);

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'order_right_item.xml' for easy to
     * all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    class ViewHolder {
        @InjectView(R.id.order_menu_name_iv)
        ImageView orderMenuNameIv;

        @InjectView(R.id.order_menu_name_tv)
        TextView orderMenuNameTv;

        @InjectView(R.id.order_menu_materias_tv)
        TextView orderMenuMateriasTv;

        @InjectView(R.id.order_menu_price_tv1)
        TextView orderMenuPriceTv1;

        @InjectView(R.id.order_right_rl)
        RelativeLayout orderRightRl;

        @InjectView(R.id.menu_count_sub_tv1)
        TextView menuCountSubTv1;

        @InjectView(R.id.menu_count_confirm_tv1)
        TextView menuCountConfirmTv1;

        @InjectView(R.id.menu_count_add_tv1)
        TextView menuCountAddTv1;

        @InjectView(R.id.order_menu_count_ll)
        LinearLayout orderMenuCountLl;

        ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }
}
