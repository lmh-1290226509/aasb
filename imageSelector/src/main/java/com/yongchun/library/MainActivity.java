package com.yongchun.library;

import java.util.ArrayList;

//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yongchun.library.view.ImageSelectorActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				this).threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.tasksProcessingOrder(QueueProcessingType.LIFO) // Remove
//				.build();
//		ImageLoader.getInstance().init(config);
	}
	
	public void test(View v){
		ImageSelectorActivity.start(MainActivity.this, 3, ImageSelectorActivity.MODE_MULTIPLE, true,true,true);
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
//            startActivity(new Intent(this,SelectResultActivity.class).putExtra(SelectResultActivity.EXTRA_IMAGES,images));
    
	        StringBuffer sb=new StringBuffer();
	        if(images==null){
	        	return;
	        }
	        for (String string : images) {
				sb.append(string);
				sb.append("|");
			}
	        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
