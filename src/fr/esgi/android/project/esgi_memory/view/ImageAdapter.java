package fr.esgi.android.project.esgi_memory.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
 
    private Context mContext;
    private Integer[] images;
 
    public ImageAdapter(Context mContext, Integer[] images) {
        this.mContext = mContext;
        this.images = images;
    }
 
    public int getCount() {
        return images.length;
    }
 
    public Object getItem(int position) {
        return images[position];
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }else{
            imageView = (ImageView) convertView;
        }
//        imageView.setEnabled(false);
//        imageView.setClickable(false);
//        imageView.setFocusable(false);
        imageView.setImageResource(images[position]);
        imageView.setTag(images[position]);	//ImageId is the tag of the view
        
        return imageView;
    }

    //http://stackoverflow.com/questions/13697957/how-to-disable-item-in-gridview-in-android
    @Override
    public boolean isEnabled(int position) {
    	return super.isEnabled(position);
    }
    
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
    }
}