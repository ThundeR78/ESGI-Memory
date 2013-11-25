package fr.esgi.android.project.esgi_memory.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
 
    private Context mContext;
    private Integer[] images;
    private Integer defaultCard;
    private boolean[] cardsReturned;
 
    public ImageAdapter(Context mContext, Integer[] images, Integer cardBackId) {
    	super();
        this.mContext = mContext;
        this.images = images;
        this.defaultCard = cardBackId;
        
        cardsReturned = new boolean[images.length];
        for (int i=0; i < cardsReturned.length ;i++)
            cardsReturned[i] = false;
    }
 
    public int getCount() {
        return images.length;
    }
 
    public Object getItem(int position) {
        return images[position];
    }
 
    public void toggleItem(int position) {
    	cardsReturned[position] = !cardsReturned[position];
    	Log.d("BOOL", "Toggle:"+cardsReturned[position]);
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
        if (cardsReturned[position])
        	imageView.setImageResource(images[position]);
        else
        	imageView.setImageResource(defaultCard);
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