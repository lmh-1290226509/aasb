package com.ddadai.baseviewlibrary.view.ImageLayout;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ddadai.baseviewlibrary.R;
import com.ddadai.baseviewlibrary.adapter.BaseRecyclerAdapter;
import com.ddadai.baseviewlibrary.recycler.BordItemDecoration;
import com.ddadai.baseviewlibrary.utils.RecyclerViewUitl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shi on 2017/3/29.
 */

public class AddImageLayout extends FrameLayout {


    public enum ImageShowType {
        JUST_SHOW,//只显示图片
        NO_DELETE,//不支持删除，
        NO_ADD,//不支持添加
        ALL,//支持添加，显示，删除
        SINGLE,//只能添加单张图片
    }


    public ImageShowType lastImageShowType = ImageShowType.ALL;
//    public ImageShowType imageShowType = ImageShowType.ALL;


    private RecyclerView imgRv;
    private ImageAddAdapter mAdapter;
    private List<ImageAddModel> models;

    private int maxSize = 5;

    private OnImageAddLayoutListener onImageAddLayoutListener;

    public AddImageLayout(@NonNull Context context) {
        super(context);
        initView();
    }

    public AddImageLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AddImageLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }


    private void initView() {
        View.inflate(getContext(), R.layout.layout_add_image, this);
        models = new ArrayList<>();
        imgRv = findViewById(R.id.imgRv);
        RecyclerViewUitl.setHorizontal(imgRv);
        mAdapter = new ImageAddAdapter(getContext(), this);
        imgRv.setAdapter(mAdapter);
        mAdapter.setModels(models);
        imgRv.addItemDecoration(new BordItemDecoration(8,8,8,8,true,BordItemDecoration.HORIZONTAL));
        mAdapter.setOnItemClickRecycleListener(new BaseRecyclerAdapter.OnItemClickRecycleListener() {
            @Override
            public void itemClick(int position, Object model) {
                if (onImageAddLayoutListener == null) {
                    return;
                }
                ImageShowType imageShowType = mAdapter.getImageShowType();

                switch (imageShowType) {
                    case ALL:
                    case SINGLE:
                    case NO_DELETE:
                        if (position == mAdapter.getItemCount() - 1) {
                            onImageAddLayoutListener.addImg(maxSize - getImgList().size());
                        } else {
                            boolean b = onImageAddLayoutListener.clickImage(position, (ImageAddModel) model);
                            if (!b) {
//                                IntentUtils.imageShow(getContext())
//                                        .img(((ImageAddModel) model).imageUrl)
//                                        .build();
                            }
                        }
                        break;
                    case NO_ADD:
                    case JUST_SHOW:
                        boolean b = onImageAddLayoutListener.clickImage(position, (ImageAddModel) model);
                        if (!b) {
//                            IntentUtils.imageShow(getContext())
//                                    .img(((ImageAddModel) model).imageUrl)
//                                    .build();
                        }
                    break;
                }


            }
        });
    }

    public void setMaxImageSize(int maxSize) {
        this.maxSize = maxSize;
    }


    public List<ImageAddModel> getImages() {
        return models;
    }

    public void setImageShowTye(ImageShowType imageShowType) {
//        this.imageShowType = imageShowType;
        if (imageShowType == ImageShowType.JUST_SHOW)
            maxSize = 100;

        lastImageShowType = imageShowType;
        mAdapter.setImageShowType(imageShowType);
    }


    public void addImageString(String image) {
        ImageAddModel model = new ImageAddModel();
        model.imageUrl = image;
        addImg(model);
    }

    public void addImageStrings(List<String> images) {
        if (images != null && !images.isEmpty()) {
            for (String img :images) {
                addImageString(img);
            }
        }
    }

    public void addImg(ImageAddModel imgModel) {
//        if (lastImageShowType == ImageShowType.JUST_SHOW) {
//            maxSize = 100;
//        } else {
//            maxSize = 5;
//        }
        if (models.size() >= maxSize) {
            mAdapter.setImageShowType(ImageShowType.NO_ADD);
            return;
        }
        models.add(imgModel);
        if (models.size() >= maxSize) {
            mAdapter.setImageShowType(ImageShowType.NO_ADD);
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    public void addImgs(List<ImageAddModel> imgModels) {
//        if (lastImageShowType == ImageShowType.JUST_SHOW) {
//            maxSize = 100;
//        } else {
//            maxSize = 5;
//        }
        if (imgModels == null || imgModels.isEmpty()) {
            return;
        }
        if (imgModels.size() + models.size() < maxSize) {
            models.addAll(imgModels);
            mAdapter.notifyDataSetChanged();
        } else if (imgModels.size() + models.size() == maxSize) {
            models.addAll(imgModels);
            mAdapter.setImageShowType(ImageShowType.NO_ADD);
            mAdapter.notifyDataSetChanged();
        } else if (models.size() >= maxSize) {
            return;
        } else {
            for (int i = 0; i < imgModels.size(); i++) {
                models.add(imgModels.get(i));
                if (models.size() >= maxSize) {
                    mAdapter.setImageShowType(ImageShowType.NO_ADD);
                    break;
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<String> getImgList() {
        List<String> imgs = new ArrayList<>();
        for (ImageAddModel m :
                models) {
            imgs.add(m.imageUrl);
        }
        return imgs;
    }

    public void clearImgs() {
        models.clear();
        mAdapter.setImageShowType(lastImageShowType);
//        mAdapter.notifyDataSetChanged();
    }

    public void setOnImageAddLayoutListener(OnImageAddLayoutListener onImageAddLayoutListener) {
        this.onImageAddLayoutListener = onImageAddLayoutListener;
//        mAdapter.setOnImageAddLayoutListener(onImageAddLayoutListener);
    }

    public OnImageAddLayoutListener getOnImageAddLayoutListener() {
        return onImageAddLayoutListener;
    }

    public interface OnImageAddLayoutListener {
        /**
         * 点击了添加图片的按钮，执行添加图片的操作
         * <p>
         * maxsize:当前最多添加几张图片
         **/
        void addImg(int maxSize);

        /***点击了position位置的图片,返回true:则不处理，false，则这个控件处理***/
        boolean clickImage(int position, ImageAddModel model);

        /***删除position位置的图片,返回true:则不处理，false，则这个控件处理***/
        boolean deleteImage(int positon, ImageAddModel model);


        void showImage(int position, ImageAddModel model, ImageView imageView,ImageShowType imageShowType);
    }
}
