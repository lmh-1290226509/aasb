package com.ddadai.baseviewlibrary.view.ImageLayout;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;


/**
 * 标准的适配器模版
 * Created by shi on 2017/3/20.
 */

public class ImageAddAdapter extends BaseRecyclerAdapter {



    private AddImageLayout.ImageShowType imageShowType;
//    private AddImageLayout.OnImageAddLayoutListener onImageAddLayoutListener;
    private AddImageLayout addImageLayout;

    public ImageAddAdapter(Context c, AddImageLayout addImageLayout) {
        super(c);
        imageShowType = AddImageLayout.ImageShowType.ALL;
        this.addImageLayout=addImageLayout;
    }


//    public void setOnImageAddLayoutListener(AddImageLayout.OnImageAddLayoutListener onImageAddLayoutListener) {
//        this.onImageAddLayoutListener = onImageAddLayoutListener;
//    }


    public AddImageLayout.ImageShowType getImageShowType() {
        return imageShowType;
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        switch (imageShowType){
            case ALL:
            case NO_DELETE:
                itemCount+=1;
                break;
            case JUST_SHOW:
            case NO_ADD:
                break;
            case SINGLE:
                if(itemCount==0){
                    itemCount=1;
                }
                break;
        }
        return itemCount ;
    }



    public void setImageShowType(AddImageLayout.ImageShowType imageShowType) {
        this.imageShowType = imageShowType;
        notifyDataSetChanged();
    }



    @Override
    public Object getItem(int positon) {
        switch (imageShowType){
            case ALL:
            case NO_DELETE:
                if (positon == getItemCount() - 1) {
                    return null;
                }
                break;
            case NO_ADD:
            case JUST_SHOW:
                return super.getItem(positon);
            case SINGLE:
                return super.getItem(positon);
        }

        return super.getItem(positon);
    }

    @Override
    protected int getRootViewLayoutId(int itemType) {
        return R.layout.item_image_add;
    }

    @Override
    protected MyViewHolder newViewHolder(View rootView, int itemType) {
        return new Holder(rootView);
    }

    @Override
    protected void initViewHolder(MyViewHolder holder, final int position, Object item) {
        Holder h = (Holder) holder;

        switch (imageShowType){
            case SINGLE:
                if(item==null){
                    h.deleteImg.setVisibility(View.GONE);
                    h.img.setImageResource(R.drawable.photograph);
                }else{
                    h.deleteImg.setVisibility(View.VISIBLE);
                    final ImageAddModel imgModel = (ImageAddModel) item;
                    if( addImageLayout.getOnImageAddLayoutListener()!=null){
                        addImageLayout.getOnImageAddLayoutListener()
                                .showImage(position,imgModel,h.img,imageShowType);
                    }
//                    Glide.with(mContext)
//                            .load(imgModel.imageUrl)
//                            .centerCrop()
//                            .into(h.img);
                    h.deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddImageLayout.OnImageAddLayoutListener onImageAddLayoutListener = addImageLayout.getOnImageAddLayoutListener();
                            if (onImageAddLayoutListener != null) {
                                boolean b = onImageAddLayoutListener.deleteImage(position, imgModel);
                                if (!b) {
                                    models.remove(position);
                                    notifyDataSetChanged();
                                }
                                if(addImageLayout.lastImageShowType!=imageShowType&&addImageLayout.lastImageShowType!=null){
                                    setImageShowType(addImageLayout.lastImageShowType);
                                }
                            }
                        }
                    });
                }
                break;
            case NO_DELETE:
                h.deleteImg.setVisibility(View.GONE);
                if (position == getItemCount() - 1) {
                    h.img.setImageResource(R.drawable.photograph);
                    h.deleteImg.setVisibility(View.GONE);
                }else{
                    final ImageAddModel imgModel = (ImageAddModel) item;
//                    Glide.with(mContext)
//                            .load(imgModel.imageUrl)
//                            .centerCrop()
//                            .into(h.img);
                    if( addImageLayout.getOnImageAddLayoutListener()!=null){
                        addImageLayout.getOnImageAddLayoutListener()
                                .showImage(position,imgModel,h.img,imageShowType);
                    }
                }
                break;
            case ALL:
                if (position == getItemCount() - 1) {
                    h.img.setImageResource(R.drawable.photograph);
                    h.deleteImg.setVisibility(View.GONE);
                }else{
                    h.deleteImg.setVisibility(View.VISIBLE);
                    final ImageAddModel imgModel = (ImageAddModel) item;
//                    Glide.with(mContext)
//                            .load(imgModel.imageUrl)
//                            .centerCrop()
//                            .into(h.img);
                    if( addImageLayout.getOnImageAddLayoutListener()!=null){
                        addImageLayout.getOnImageAddLayoutListener()
                                .showImage(position,imgModel,h.img,imageShowType);
                    }
                    h.deleteImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AddImageLayout.OnImageAddLayoutListener onImageAddLayoutListener = addImageLayout.getOnImageAddLayoutListener();
                            if (onImageAddLayoutListener != null) {
                                boolean b = onImageAddLayoutListener.deleteImage(position, imgModel);
                                if (!b) {
                                    models.remove(position);
                                    notifyDataSetChanged();
                                }
                                if(addImageLayout.lastImageShowType!=imageShowType&&addImageLayout.lastImageShowType!=null){
                                    setImageShowType(addImageLayout.lastImageShowType);
                                }
                            }
                        }
                    });
                }
                break;
            case JUST_SHOW:
                h.deleteImg.setVisibility(View.GONE);
                final ImageAddModel imgModel = (ImageAddModel) item;
                if( addImageLayout.getOnImageAddLayoutListener()!=null){
                    addImageLayout.getOnImageAddLayoutListener()
                            .showImage(position,imgModel,h.img,imageShowType);
                }
//                Glide.with(mContext)
//                        .load(imgModel.imageUrl)
//                        .placeholder(R.drawable.default_picture)
//                        .error(R.drawable.error_picture)
//                        .centerCrop()
//                        .into(h.img);
                break;
            case NO_ADD:
                final ImageAddModel model = (ImageAddModel) item;
//                Glide.with(mContext)
//                        .load(model.imageUrl)
//                        .centerCrop()
//                        .into(h.img);
                if( addImageLayout.getOnImageAddLayoutListener()!=null){
                    addImageLayout.getOnImageAddLayoutListener()
                            .showImage(position,model,h.img,imageShowType);
                }
                h.deleteImg.setVisibility(View.VISIBLE);
                h.deleteImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddImageLayout.OnImageAddLayoutListener onImageAddLayoutListener = addImageLayout.getOnImageAddLayoutListener();
                        if (onImageAddLayoutListener != null) {
                            boolean b = onImageAddLayoutListener.deleteImage(position, model);
                            if (!b) {
                                models.remove(position);
                                notifyDataSetChanged();
                            }
                            if(addImageLayout.lastImageShowType!=imageShowType&&addImageLayout.lastImageShowType!=null){
                                setImageShowType(addImageLayout.lastImageShowType);
                            }
                        }
                    }
                });
                break;
        }
    }

    private class Holder extends MyViewHolder {
        ImageView img, deleteImg;

        public Holder(View arg0) {
            super(arg0);
        }

        @Override
        protected void initView() {
            img = (ImageView) findViewById(R.id.img);
            deleteImg = (ImageView) findViewById(R.id.deleteImg);
        }
    }
}
