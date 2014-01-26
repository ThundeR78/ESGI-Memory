package fr.esgi.android.project.esgi_memory.view;

import fr.esgi.android.project.esgi_memory.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
 
public class ImageAdapter extends BaseAdapter {
 
    private Context context;
    private Integer[] images;
    private Integer defaultCard;
 
    public ImageAdapter(Context mContext, Integer[] images, Integer cardBackId) {
    	super();
        this.context = mContext;
        this.images = images;
        this.defaultCard = cardBackId;
    }
 
    @Override
	public int getCount() {
        return images.length;
    }
 
    @Override
	public Object getItem(int position) {
        return images[position];
    }
    
    @Override
	public long getItemId(int position) {
        return position;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
    	CardView cardView;
        
        if (view == null){
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	view = inflater.inflate(R.layout.view_card, parent, false);
            
        	cardView = new CardView(context);
        	cardView.imageview = (ImageView) view.findViewById(R.id.imageView);
//            cardView.setLayoutParams(new GridView.LayoutParams(R.dimen.card_size, R.dimen.card_size));
//            cardView.setScaleType(ImageView.ScaleType.FIT_XY);
//            cardView.setBackgroundResource(R.color.white);
//            cardView.setPadding(8, 8, 8, 8);
        	view.setTag(cardView);
        } else {
//            cardView = (CardView) convertView;
        	cardView = (CardView) view.getTag();
        }

        if (cardView.isReturned())
        	cardView.imageview.setImageResource(images[position]);
        else
        	cardView.imageview.setImageResource(defaultCard);
        cardView.setTag(images[position]);	//ImageId is the tag of the view
        
        return view;
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