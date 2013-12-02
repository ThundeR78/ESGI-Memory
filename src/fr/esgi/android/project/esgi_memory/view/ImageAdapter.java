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
    private Integer defaultCard;
 
    public ImageAdapter(Context mContext, Integer[] images, Integer cardBackId) {
    	super();
        this.mContext = mContext;
        this.images = images;
        this.defaultCard = cardBackId;
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
        CardView cardView;
        if(convertView == null){
            cardView = new CardView(mContext);        
            cardView.setLayoutParams(new GridView.LayoutParams(100, 100));
            cardView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardView.setPadding(8, 8, 8, 8);
        }else{
            cardView = (CardView) convertView;
        }
//        cardView.setEnabled(false);
//        cardView.setClickable(false);
//        cardView.setFocusable(false);
        if (cardView.isReturned())
        	cardView.setImageResource(images[position]);
        else
        	cardView.setImageResource(defaultCard);
        cardView.setTag(images[position]);	//ImageId is the tag of the view
        
        return cardView;
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