package org.milan.climax.UIhelper;

import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UIHelper {

	public static void displayText(View view, int id, String text) {
		TextView tv = (TextView) view.findViewById(id);
		tv.setText(text);
	}
	
	public static EditText getEditText(View view, int id) {
		EditText et = (EditText) view.findViewById(id);
		return et;
	}
	
	public static TextView getTextView(View view, int id) {
		TextView textView = (TextView) view.findViewById(id);
		return textView;
	}
	
	public static RelativeLayout getrRelativeLayout(View view, int id) {
		RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(id);
		return relativeLayout;
	}
	
	public static ImageView getImageView(View view , int id){
		ImageView imageView = (ImageView) view.findViewById(id);
		return imageView;
	}
	
	public static TableLayout getTableLayoutView(View view , int id){
		TableLayout tableLayout = (TableLayout) view.findViewById(id);
		return tableLayout;
	}
	
	public static TableRow getTableRowView(View view , int id){
		TableRow tableRow = (TableRow) view.findViewById(id);
		return tableRow;
	}
	
	public static LinearLayout getLinearLayoutView(View view , int id){
		LinearLayout linearLayout = (LinearLayout) view.findViewById(id);
		return linearLayout;
	}
	
	public static RatingBar getRatingBar(View view , int id){
		RatingBar ratingBar = (RatingBar) view.findViewById(id);
		return ratingBar;
	}
	
	public static SeekBar getSeekBar(View view , int id){
		SeekBar seekBar = (SeekBar) view.findViewById(id);
		return seekBar;
	}
	
	public static Button getButton(View view, int id){
		Button button =  (Button) view.findViewById(id);
		return button;
	}
	
	public static View getView(View view, int id){
		View emptyView = view.findViewById(id);
		return emptyView;
	}
	
	public static HorizontalScrollView getHorizontalScrollView(View view, int id){
		HorizontalScrollView horizontalScrollView = (HorizontalScrollView) view.findViewById(id);
		return horizontalScrollView;
	}
}
