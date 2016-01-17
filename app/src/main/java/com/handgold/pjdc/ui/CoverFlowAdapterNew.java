package com.handgold.pjdc.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.handgold.pjdc.R;
import com.handgold.pjdc.entitiy.GameInfo;
import com.handgold.pjdc.entitiy.MenuItemInfo;
import com.handgold.pjdc.entitiy.MovieInfo;
import com.handgold.pjdc.ui.widget.RoundedImageView;
import com.handgold.pjdc.ui.widget.XCRoundRectImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CoverFlowAdapterNew extends BaseAdapter {

	public static final int TYPE_FOOD = 1;
	public static final int TYPE_GAME = 2;
	public static final int TYPE_MOVIE = 3;
	public static final int TYPE_MAP = 4;
	public static final int TYPE_PHOTO = 5;

	private LayoutInflater mInflater = null;
	private HashMap<String, ArrayList> mData = new HashMap<String, ArrayList>();
	private Context mContext;

	public CoverFlowAdapterNew(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public void setData(HashMap<String, ArrayList> data) {
		mData = data;
	}
	
	@Override
	public int getCount() {
		return 5;
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
	public int getItemViewType(int position) {
		switch (position) {
			case 0:
				return TYPE_MAP;
			case 1:
				return TYPE_PHOTO;
			case 2:
				return TYPE_FOOD;
			case 3:
				return TYPE_GAME;
			case 4:
				return TYPE_MOVIE;
		}
		return -1;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
		int viewType = getItemViewType(position);
		ViewHolder viewHolder = null;
		if (rowView == null) {
			viewHolder = new ViewHolder();
			rowView = createView(viewHolder, viewType, position);

        }else {
			viewHolder = (ViewHolder) rowView.getTag();
		}

		if (viewType == TYPE_FOOD) {
			showFoodItem(viewHolder);
		}
		else if(viewType == TYPE_MAP){
			showMapItem(viewHolder);
		}
		else if (viewType == TYPE_GAME) {
			showGameItem(viewHolder);
		}
		else if (viewType == TYPE_MOVIE) {
			showMovieItem(viewHolder);
		}
		else if (viewType == TYPE_PHOTO) {
			showPhotoItem(viewHolder);
		}

		return rowView;
	}



	private View createView(ViewHolder viewHolder, int viewType, int position) {
		View view = null;
		if (viewType == TYPE_FOOD) {
			view = createFoodView(viewHolder);
		}else if(viewType == TYPE_MAP){
			view = createMapView(viewHolder);
		}

		else {
			view = createOtherView(viewHolder);
		}
		view.setTag(viewHolder);
		return view;
	}

	private View createFoodView(ViewHolder viewHolder) {
		View view = mInflater.inflate(R.layout.item_coverflow_food, null);
		viewHolder.menu_item_bg = (XCRoundRectImageView) view.findViewById(R.id.image_bg);
		viewHolder.menu_item_icon = (ImageView) view.findViewById(R.id.image_type);
		viewHolder.menu_image_show_more = (ImageView) view.findViewById(R.id.image_show_more);
		viewHolder.menu_text_title = (TextView) view.findViewById(R.id.text_title);
		viewHolder.menu_text_desc = (TextView) view.findViewById(R.id.text_desc);
		viewHolder.menu_text_show_more = (TextView) view.findViewById(R.id.text_show_more);

		viewHolder.menu_iv1 = (XCRoundRectImageView) view.findViewById(R.id.menu_name_iv1);
		viewHolder.menu_title1 = (TextView) view.findViewById(R.id.menu_name_title1);
		viewHolder.menu_price1 = (TextView) view.findViewById(R.id.menu_name_price1);

		viewHolder.menu_iv2 = (XCRoundRectImageView) view.findViewById(R.id.menu_name_iv2);
		viewHolder.menu_title2 = (TextView) view.findViewById(R.id.menu_name_title2);
		viewHolder.menu_price2 = (TextView) view.findViewById(R.id.menu_name_price2);

		viewHolder.menu_iv3 = (XCRoundRectImageView) view.findViewById(R.id.menu_name_iv3);
		viewHolder.menu_title3 = (TextView) view.findViewById(R.id.menu_name_title3);
		viewHolder.menu_price3 = (TextView) view.findViewById(R.id.menu_name_price3);

		viewHolder.menu_iv_Arr = new XCRoundRectImageView[]{viewHolder.menu_iv1, viewHolder.menu_iv2, viewHolder.menu_iv3};
		viewHolder.menu_title_Arr = new TextView[]{viewHolder.menu_title1, viewHolder.menu_title2, viewHolder.menu_title3};
		viewHolder.menu_price_Arr = new TextView[]{viewHolder.menu_price1, viewHolder.menu_price2, viewHolder.menu_price3};
		return view;
	}

	private View createMapView(ViewHolder viewHolder) {
		View view = mInflater.inflate(R.layout.item_coverflow_map, null);
		viewHolder.map_item_bg = (XCRoundRectImageView) view.findViewById(R.id.image_bg);
		viewHolder.map_item_icon = (ImageView) view.findViewById(R.id.image_type);
		viewHolder.map_text_title = (TextView) view.findViewById(R.id.text_title);
		viewHolder.map_text_desc = (TextView) view.findViewById(R.id.text_desc);

		viewHolder.map_iv = (XCRoundRectImageView) view.findViewById(R.id.map_iv);

		return view;
	}

	private View createOtherView(ViewHolder viewHolder) {
		View view = mInflater.inflate(R.layout.item_coverflow_other, null);
		viewHolder.other_item_bg = (XCRoundRectImageView) view.findViewById(R.id.image_bg);
		viewHolder.other_item_icon = (ImageView) view.findViewById(R.id.image_type);
		viewHolder.other_image_show_more = (ImageView) view.findViewById(R.id.image_show_more);
		viewHolder.other_text_title = (TextView) view.findViewById(R.id.text_title);
		viewHolder.other_text_desc = (TextView) view.findViewById(R.id.text_desc);
		viewHolder.other_text_show_more = (TextView) view.findViewById(R.id.text_show_more);

		viewHolder.other_iv1 = (RoundedImageView) view.findViewById(R.id.game_iv1);
		viewHolder.other_name1 = (TextView) view.findViewById(R.id.game_name_text1);

		viewHolder.other_iv2 = (RoundedImageView) view.findViewById(R.id.game_iv2);
		viewHolder.other_name2 = (TextView) view.findViewById(R.id.game_name_text2);

		viewHolder.other_iv3 = (RoundedImageView) view.findViewById(R.id.game_iv3);
		viewHolder.other_name3 = (TextView) view.findViewById(R.id.game_name_text3);

		viewHolder.other_iv_Arr = new RoundedImageView[]{viewHolder.other_iv1, viewHolder.other_iv2, viewHolder.other_iv3};
		viewHolder.other_name_Arr = new TextView[]{viewHolder.other_name1, viewHolder.other_name2, viewHolder.other_name3};

		return view;
	}


	private void showFoodItem(ViewHolder viewHolder) {
		ArrayList<MenuItemInfo> menuData = mData.get("FoodData");
		viewHolder.menu_item_bg.setImageResource(R.drawable.home_food_bg);
		viewHolder.menu_item_icon.setImageResource(R.drawable.icon_highlight);
		viewHolder.menu_image_show_more.setImageResource(R.drawable.default_button_more);
		viewHolder.menu_text_title.setText("主厨推荐");
		viewHolder.menu_text_desc.setText("最近新增6个菜式");
		viewHolder.menu_text_show_more.setText(" 查看\n 更多");

		int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
				R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
		for (int i = 0; i < 3; i++) {
			MenuItemInfo menu = menuData.get(i);
			int id = new Random().nextInt(7);
			viewHolder.menu_iv_Arr[i].setImageResource(picIds[id]);
			viewHolder.menu_title_Arr[i].setText(menu.getName());
			viewHolder.menu_title_Arr[i].setTextColor(0xffffffff);
			viewHolder.menu_title_Arr[i].setTextSize(15);
			viewHolder.menu_price_Arr[i].setText("¥" + menu.getPrice());
			viewHolder.menu_price_Arr[i].setTextColor(0xffffffff);
			viewHolder.menu_price_Arr[i].setTextSize(15);
		}

	}

	private void showMapItem(ViewHolder viewHolder) {
		viewHolder.map_item_bg.setImageResource(R.drawable.map_bg);
		viewHolder.map_item_icon.setImageResource(R.drawable.icon_map);
		viewHolder.map_text_title.setText("当前位置");
		viewHolder.map_text_desc.setText("广州市白云区云城东路505号万达广场");

		viewHolder.map_iv.setImageResource(R.drawable.wanda_loc_map);
	}

	private void showGameItem(ViewHolder viewHolder) {

		ArrayList<GameInfo> gameData = mData.get("GameData");
		viewHolder.other_item_bg.setImageResource(R.drawable.home_game_bg);
		viewHolder.other_item_icon.setImageResource(R.drawable.icon_game);
		viewHolder.other_image_show_more.setImageResource(R.drawable.default_button_more);
		viewHolder.other_text_title.setText("热门游戏");
		viewHolder.other_text_desc.setText("最近新增6个游戏");
		viewHolder.other_text_show_more.setText(" 查看\n 更多");

		int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
				R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
		for (int i = 0; i < 3; i++) {
			GameInfo gameInfo = gameData.get(i);
			int id = new Random().nextInt(7);
			viewHolder.other_iv_Arr[i].setImageResource(picIds[id]);
			viewHolder.other_name_Arr[i].setText(gameInfo.getName());
		}
	}

	private void showMovieItem(ViewHolder viewHolder) {

		ArrayList<MovieInfo> movieData = mData.get("MovieData");
//		viewHolder.other_item_bg.setImageResource(R.drawable.moive_bg);
		viewHolder.other_item_icon.setImageResource(R.drawable.icon_movie);
		viewHolder.other_image_show_more.setImageResource(R.drawable.default_button_more);
		viewHolder.other_text_title.setText("最新影片");
		viewHolder.other_text_desc.setText("新增6部新高清影片");
		viewHolder.other_text_show_more.setText(" 查看\n 更多");

		int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
				R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
		for (int i = 0; i < 3; i++) {
			MovieInfo movieInfo = movieData.get(i);
			int id = new Random().nextInt(7);
			viewHolder.other_iv_Arr[i].setImageResource(picIds[id]);
			viewHolder.other_name_Arr[i].setText(movieInfo.getName());
			viewHolder.other_name_Arr[i].setBackgroundResource(R.drawable.home_movie_name1);

		}
	}

	private void showPhotoItem(ViewHolder viewHolder) {

		ArrayList<GameInfo> gameData = mData.get("PhotoData");
		viewHolder.other_item_bg.setImageResource(R.drawable.photo_bg);
		viewHolder.other_item_icon.setImageResource(R.drawable.icon_photo);
		viewHolder.other_image_show_more.setImageResource(R.drawable.default_button_more);
		viewHolder.other_text_title.setText("记录瞬间");
		viewHolder.other_text_desc.setText("拍摄并分享最美的自己");
		viewHolder.other_text_show_more.setText(" 点击\n 开始");

		int[] picIds = new int[]{R.drawable.meishi1, R.drawable.meishi2, R.drawable.meishi3, R.drawable.meishi4,
				R.drawable.meishi5, R.drawable.meishi6, R.drawable.meishi7};
		for (int i = 0; i < 3; i++) {
			GameInfo gameInfo = gameData.get(i);
			int id = new Random().nextInt(7);
			viewHolder.other_iv_Arr[i].setImageResource(picIds[id]);
			viewHolder.other_name_Arr[i].setText(gameInfo.getName());
		}
	}


	static class ViewHolder {

		// 菜品样品展示
        public XCRoundRectImageView menu_item_bg;
        public ImageView menu_item_icon;
        public ImageView menu_image_show_more;
		public TextView menu_text_title;
		public TextView menu_text_desc;
		public TextView menu_text_show_more;

		public XCRoundRectImageView menu_iv1;
		public TextView menu_title1;
		public TextView menu_price1;

		public XCRoundRectImageView menu_iv2;
		public TextView menu_title2;
		public TextView menu_price2;

		public XCRoundRectImageView menu_iv3;
		public TextView menu_title3;
		public TextView menu_price3;

		// 游戏、电影、照片样品展示
		public XCRoundRectImageView other_item_bg;
		public ImageView other_item_icon;
		public ImageView other_image_show_more;
		public TextView other_text_title;
		public TextView other_text_desc;
		public TextView other_text_show_more;

		public RoundedImageView other_iv1;
		public TextView other_name1;

		public RoundedImageView other_iv2;
		public TextView other_name2;

		public RoundedImageView other_iv3;
		public TextView other_name3;

		// 地图
		public XCRoundRectImageView map_item_bg;
		public ImageView map_item_icon;
		public TextView map_text_title;
		public TextView map_text_desc;

		public XCRoundRectImageView map_iv;

		public XCRoundRectImageView[] menu_iv_Arr;
		public TextView[] menu_title_Arr;
		public TextView[] menu_price_Arr;

		public RoundedImageView[] other_iv_Arr;
		public TextView[] other_name_Arr;


	}
}
