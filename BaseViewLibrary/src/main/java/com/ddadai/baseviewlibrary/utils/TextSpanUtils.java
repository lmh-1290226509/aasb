package com.ddadai.baseviewlibrary.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by shi on 2017/11/3.
 */

public class TextSpanUtils {

    public static SpanColorBuilder setTextSpanColor(TextView tv,boolean isClear){
        return new SpanColorBuilder(tv,isClear);
    }
    public static SpanColorBuilder setTextSpanColor(TextView tv){
        return new SpanColorBuilder(tv);
    }

    public static class SpanColorBuilder {
        private TextView tv;
        private int defaultColor;//设置的默认颜色
        private int defaultTvColor;//tv原本的颜色

        public SpanColorBuilder(TextView tv, boolean isClear) {
            this.tv = tv;
            if(isClear){
                tv.setText("");
            }
            defaultTvColor=tv.getTextColors().getDefaultColor();
        }




        public SpanColorBuilder(TextView tv) {
            this(tv,true);
        }

        public SpanColorBuilder defaultColor( int color){
            defaultColor=color;
            return this;
        }

        public SpanColorBuilder append(String str, int color){
            tv.append(setSpan(str,color));
            return this;
        }



        public SpanColorBuilder append(String str){
            if(defaultTvColor==defaultColor){
                tv.append(str);
            }else if(defaultColor!=0){
                tv.append(setSpan(str,defaultColor));
            }else{
                tv.append(setSpan(str,defaultTvColor));
            }
            return this;
        }



        private SpannableString setSpan(String str,int color){
            SpannableString ss=new SpannableString(str);
            ss.setSpan(new ForegroundColorSpan(color),0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return ss;
        }
    }
}
