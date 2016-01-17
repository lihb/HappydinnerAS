package com.handgold.pjdc.ui.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.MenuItemInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2015/10/29.
 */
public class FoodRightAdapter  extends BaseAdapter {

    private ArrayList<MenuItemInfo> mMenuList = new ArrayList<MenuItemInfo>();
    private Context mContext;
    private LayoutInflater mInflater = null;
    private OnFoodRightAdapterListener mListener;

    public final static int OPER_ADD = 0;
    public final static int OPER_SUB = 1;
    public final static int OPER_NONE = -1;

    public FoodRightAdapter(ArrayList<MenuItemInfo> mMenuList, Context mContext, OnFoodRightAdapterListener listener) {
        this.mMenuList = mMenuList;
        this.mContext = mContext;
        this.mListener = listener;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void setOnListWorkerListener(OnFoodRightAdapterListener listener) {
        this.mListener = listener;
    }

    public void setData(ArrayList<MenuItemInfo> menuList) {
        mMenuList = menuList;
    }

    @Override
    public int getCount() {
        return mMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewholder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.menu_item_detail, null);
            viewholder = new ViewHolder();
            viewholder.menuNameIv = (ImageView) convertView.findViewById(R.id.menu_name_iv);
            viewholder.menuNameTitle = (TextView) convertView.findViewById(R.id.menu_name_title);
            viewholder.menuNamePrice = (TextView) convertView.findViewById(R.id.menu_name_price);
            viewholder.relativeLayoutOperation = (RelativeLayout) convertView.findViewById(R.id.relative_operation);
            viewholder.imageViewAdd = (ImageView) convertView.findViewById(R.id.imagview_add);
            viewholder.imageViewSub = (ImageView) convertView.findViewById(R.id.imagview_sub);
            viewholder.menuCount = (TextView) convertView.findViewById(R.id.text_count);
            viewholder.menuDetail = (TextView) convertView.findViewById(R.id.menu_detail);
            viewholder.linearLayoutMenuDetail = (LinearLayout) convertView.findViewById(R.id.menu_detail_linearLayout);
            viewholder.linearLayoutOperationBg = (LinearLayout) convertView.findViewById(R.id.linearLayout_operation_bg);
            //为了解决在xml中设置layout_marginBottom失效，而在代码中设置padding
            viewholder.linearLayoutMenuDetail.setPadding(0,0,0, mContext.getResources().getDimensionPixelSize(R.dimen.menu_item_margin));
            viewholder.linearLayoutOperationBg.setPadding(0,0,0,mContext.getResources().getDimensionPixelSize(R.dimen.menu_item_margin));
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }

        final MenuItemInfo menu = (MenuItemInfo) getItem(position);

        showSubOper(viewholder, menu.getCount(), OPER_NONE);


        Glide.with(mContext)
                .load(menu.getImgUrl())
                .placeholder(R.drawable.fijitribe)
                .into(viewholder.menuNameIv);

        viewholder.menuNameTitle.setText(menu.getName());
        viewholder.menuNamePrice.setText("¥" + menu.getPrice());
        viewholder.menuCount.setText("" + menu.getCount());

        //增加数目
        final ViewHolder tempViewholder = viewholder;
        viewholder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAddMenuClicked(menu, v);
                    showSubOper(tempViewholder, menu.getCount(), OPER_ADD);
                }
            }
        });
        // 减少数目
        viewholder.imageViewSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSubMenuClicked(menu);
                    showSubOper(tempViewholder, menu.getCount(), OPER_SUB);
                }
            }
        });

        // 查看详情
        viewholder.menuDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onGotoLookDesc(v, menu);
                }
            }
        });
        return convertView;
    }

    private void showSubOper(final ViewHolder holder, int count, int oper) {
        if (count > 0) {
            holder.imageViewSub.setVisibility(View.VISIBLE);
            if (count == 1 && oper == OPER_ADD) { // 减号出现动画
                RotateAnimation rotateAnimation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(300);
                TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f,
                        TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0);
                translateAnimation.setDuration(300);
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(rotateAnimation);
                set.addAnimation(translateAnimation);
                holder.imageViewSub.startAnimation(set);
            }

            holder.relativeLayoutOperation.setBackgroundResource(R.drawable.button_exclude_bg2);
            holder.menuCount.setVisibility(View.VISIBLE);
            holder.menuCount.setText("" + count);
        } else {
            if (oper == OPER_SUB) {
                // 减号消失动画
                if (holder.imageViewSub.getAnimation() != null) {
                    return;
                }
                RotateAnimation rotateAnimation = new RotateAnimation(360, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnimation.setDuration(300);
                TranslateAnimation translateAnimation = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.5f,
                        TranslateAnimation.RELATIVE_TO_SELF, 0, TranslateAnimation.RELATIVE_TO_SELF, 0);
                translateAnimation.setDuration(300);
                AnimationSet set = new AnimationSet(false);
                set.addAnimation(rotateAnimation);
                set.addAnimation(translateAnimation);
                set.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        holder.imageViewSub.setVisibility(View.GONE);
                        holder.relativeLayoutOperation.setBackground(null);
                        holder.menuCount.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                holder.imageViewSub.startAnimation(set);
            }else {
                holder.imageViewSub.setVisibility(View.GONE);
                holder.relativeLayoutOperation.setBackground(null);
                holder.menuCount.setVisibility(View.GONE);
            }
        }
    }

    private class ViewHolder {
        ImageView menuNameIv;
        TextView menuNameTitle;
        TextView menuNamePrice;
        RelativeLayout relativeLayoutOperation;
        ImageView imageViewAdd;
        ImageView imageViewSub;
        TextView menuCount;
        TextView menuDetail;
        LinearLayout linearLayoutMenuDetail;
        LinearLayout linearLayoutOperationBg;
    }

    public interface OnFoodRightAdapterListener {
        /**
         * 当条目选中状态改变时调用
         */
        void onItemClicked(Object itemData, int index);

        /**
         * 加菜
         */
        void onAddMenuClicked(Object itemData, View view);

        /**
         * 减菜
         */
        void onSubMenuClicked(Object itemData);

        /**
         * 添加到订单
         *
         * @param itemData
         */
        void onAddToOrderClicked(Object itemData);

        /**
         * 查看详情
         */
        void onGotoLookDesc(View view, Object itemData);
    }
}
