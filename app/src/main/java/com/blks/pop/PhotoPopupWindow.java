package com.blks.pop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.blks.antrscapp.R;

public class PhotoPopupWindow extends PopupWindow {

    private ImageView img_hint;
    private PhotoListener photoListener;

    public void setPhotoListener(PhotoListener listener) {
        photoListener = listener;
    }

    public void setImageHintByPosition(int position) {
        if(position == 0){
            img_hint.setBackgroundResource(R.drawable.phototips1);
        }else if(position == 1 || position == 2){
            img_hint.setBackgroundResource(R.drawable.phototips2);
        }else if(position == 3){
            img_hint.setBackgroundResource(R.drawable.phototips3);
        }else if(position == 4){
            img_hint.setBackgroundResource(R.drawable.phototips4);
        } else {
            img_hint.setBackgroundResource(R.drawable.phototips2);
        }
    }

    public PhotoPopupWindow(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rootView = inflater.inflate(R.layout.ted_photopicdialogact_photopic, null);

        img_hint = rootView.findViewById(R.id.img_hint);
        rootView.findViewById(R.id.photo_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoListener != null) {
                    photoListener.photo();
                }
            }
        });
        rootView.findViewById(R.id.photo_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        this.setContentView(rootView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimBottom);
    }

    public interface PhotoListener {
        void photo();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        img_hint.setBackgroundResource(0);
    }
}
