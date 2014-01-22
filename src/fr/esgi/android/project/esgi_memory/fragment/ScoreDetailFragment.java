package fr.esgi.android.project.esgi_memory.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.adenclassifieds.android.cadremploi.DetailFragmentsSlider;
import com.adenclassifieds.android.cadremploi.R;
import com.adenclassifieds.android.cadremploi.business.Edito;
import com.adenclassifieds.android.cadremploi.server.EditoDetailRequest;
import com.adenclassifieds.android.cadremploi.server.RSSRequest;
import com.adenclassifieds.android.common.fragment.RequestDetailFragment;
import com.adenclassifieds.android.common.xiti.XitiUtil;

public class ScoreDetailFragment extends RequestDetailFragment<Edito, RSSRequest<Edito>> {
	private static final String TAG = "DetailEditoFragment";
	
	TextView detail_title;
	WebView webview;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		Log.v(TAG, "onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		Log.v(TAG, "onCreateView");

		View contentView = inflater.inflate(R.layout.detail_edito_fragment, container, false);
		
		detail_title = (TextView) contentView.findViewById(R.id.detail_title);
		webview = (WebView) contentView.findViewById(R.id.webview);
		
		return contentView;
	}

	@Override
	protected RSSRequest<Edito> createNewRequest(Edito inItem) {
		return new EditoDetailRequest(inItem);
	}

	@Override
	protected void displayItem(Edito inItem) {
		if (inItem != null && this.isAdded()) {
			if(this.getUserVisibleHint() && !inItem.isComplete()) {
				// For Xiti
				XitiUtil.sendTag("actualites::article");
			}
			
			detail_title.setText(inItem.title);
	
			if (inItem.content != null) {
				StringBuilder lc_builder = new StringBuilder(
						"<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><style type=\"text/css\">body {font-family: \"helvetica\"; font-size: 13; text-align:justify;}</style></head><body>");
				lc_builder.append(inItem.content);
				lc_builder.append("</body></html>");
				webview.loadDataWithBaseURL("http://localhost", lc_builder.toString(), "text/html", "UTF-8", null);
				webview.setVisibility(View.VISIBLE);
			}
			else {
				webview.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected String getParcelableItemKey() {
		return DetailFragmentsSlider.ITEM_KEY;
	}

}
