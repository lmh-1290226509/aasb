package com.blks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.blks.antrscapp.R;
import com.blks.app.BtnOnClickListenter;
import com.blks.app.ImageInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GridPicAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater; // 视图容器
	private int selectedPosition = -1;// 选中的位置
	private boolean shape;
	private List<ImageInfo> list;
	private BtnOnClickListenter btnOnClickListenter;

	public GridPicAdapter(Context context) {
		this.context = context;
	}

	public GridPicAdapter(Context context, List<ImageInfo> list,
			BtnOnClickListenter btnOnClickListenter) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.list =new ArrayList<ImageInfo>();
		this.list.clear();
		this.list.addAll(list);
		this.btnOnClickListenter = btnOnClickListenter;
	}

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		this.shape = shape;
	}

	public void setList(List<ImageInfo> list) {
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void onDestroy(){
		this.list.clear();
		notifyDataSetChanged();
	}

	public int getCount() {
		return list.size() + 1;
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public void setSelectedPosition(int position) {
		selectedPosition = position;
	}

	public int getSelectedPosition() {
		return selectedPosition;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_gv_pic, parent, false);
			AbsListView.LayoutParams param = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(param);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.item_grida_image);
			holder.btn_grida = (Button) convertView
					.findViewById(R.id.btn_grida);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == list.size()) {

			Glide.with(context)
					.load(R.drawable.tianjia_shenqing)
					.into(holder.image);
//			holder.image.setImageBitmap(BitmapFactory.decodeResource(
//					context.getResources(), R.drawable.tianjia_shenqing));
			if (position == 0) {
				holder.image.setVisibility(View.VISIBLE);
				holder.btn_grida.setVisibility(View.GONE);
			} else {
				holder.image.setVisibility(View.VISIBLE);
				holder.btn_grida.setVisibility(View.GONE);
			}

		} else {
//			Bitmap bitmap = ImageUtil.convertStringToIcon(list.get(position)
//					.getBitmapStr());
			// ImageLoader.getInstance().displayImage(
			// list.get(position).getBitmapStr(), holder.image);

			// holder.image.setBackgroundDrawable(new BitmapDrawable(bitmap));
//			holder.image.setImageBitmap(bitmap);
			ImageInfo model= (ImageInfo) getItem(position);
			Glide.with(context)
					.load(model.getLocalImage())
					.into(holder.image);
		}
		holder.btn_grida.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				btnOnClickListenter.onClickListener(position, 0, "", "");
			}
		});
		return convertView;
	}

	public class ViewHolder {
		public ImageView image;
		public Button btn_grida;
	}

}