package fr.esgi.android.project.esgi_memory.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import fr.esgi.android.project.esgi_memory.R;
import fr.esgi.android.project.esgi_memory.business.Card;
 
public class CardAdapter extends BaseAdapter {
 
    private Context context;
    private Card[] cards;
    private Integer defaultCard;
 
    public CardAdapter(Context mContext, Card[] cards, Integer cardBackId) {
    	super();
        this.context = mContext;
        this.cards = cards;
        this.defaultCard = cardBackId;
    }
 
    @Override
	public int getCount() {
        return cards.length;
    }
 
    @Override
	public Object getItem(int position) {
        return cards[position];
    }
    
    @Override
	public long getItemId(int position) {
        return position;
    }
 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
    	CardView cardView;
        Card card = cards[position];
        
        if (view == null){
        	//Create a new card view with the layout
        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	view = inflater.inflate(R.layout.view_card, parent, false);
            
        	//Create new holder view
        	cardView = new CardView(context);
        	cardView.imageview = (ImageView) view.findViewById(R.id.imageView);
        	
        	view.setTag(cardView);
        } else {
        	cardView = (CardView) view.getTag();
        }

        //Change image 
        cardView.imageview.setImageResource((card.isReturned) ? card.imageId : defaultCard);
        
        cardView.setTag(card);	//Card is the tag of the view
        
        return view;
    }

    //http://stackoverflow.com/questions/13697957/how-to-disable-item-in-gridview-in-android
//    @Override
//    public boolean isEnabled(int position) {
//    	return super.isEnabled(position);
//    }
//    
//    @Override
//    public boolean areAllItemsEnabled() {
//    	return false;
//    }
}