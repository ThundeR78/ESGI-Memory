package fr.esgi.android.project.esgi_memory.view;

import android.content.Context;
import android.widget.ImageView;
 
public class CardView extends ImageView {
 
	private boolean isReturned = false;
	
    public CardView(Context ctx) {
    	super(ctx);
    }

	public boolean isReturned() {
		return isReturned;
	}

	public void setReturned(boolean isReturned) {
		this.isReturned = isReturned;
	}
 
	public void toggleSide() {
		isReturned = !isReturned;
	}
}