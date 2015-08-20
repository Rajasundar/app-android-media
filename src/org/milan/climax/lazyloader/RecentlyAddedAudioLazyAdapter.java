package org.milan.climax.lazyloader;

import java.util.List;
import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Audio;
import org.milan.climax.model.Model;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecentlyAddedAudioLazyAdapter extends BaseAdapter {

	private List<Audio> data;
	private static LayoutInflater inflater = null;
	private int w;
	private Model model;
	private Audio audioValues;
	private TextView playAlbumDetail;
	private ImageView imageAlbumDetail, image1, image2, image3;
	private LinearLayout imagelinearLayout1, imagelinearLayout2,
			imagelinearLayout3, linearLayout1, linearLayout2, linearLayout3,
			main_linear_layout;
	private Cursor cursor;
	private TableLayout scrollView;
	public View rootView, popUpView;
	private RelativeLayout relLayout;
	private String profile_ip_value, category;
	private TextView text1, text2, text3;
	private android.app.Activity context;
	private MilanClimaxDataSource milanClimaxDataSource;
	private TableLayout tableLayout;

	public RecentlyAddedAudioLazyAdapter(android.app.Activity context, int w,
			String profile_ip_value, List<Audio> d, String category,
			LinearLayout main_linear_layout) {
		this.context = context;
		data = d;
		this.w = w;
		this.model = Model.getModelObj();
		this.profile_ip_value = profile_ip_value;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		popUpView = context.getLayoutInflater().inflate(
				R.layout.playsongdetail, null);
		playAlbumDetail = UIHelper.getTextView(popUpView,
				R.id.btnTitleInPlaySongDetail);

		imageAlbumDetail = UIHelper.getImageView(popUpView,
				R.id.imgAlbumDetInPlayDetail);

		tableLayout = UIHelper.getTableLayoutView(popUpView,
				R.id.tblLayoutInPlaySongDetail);
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.category = category;
		this.main_linear_layout = main_linear_layout;
	}

	public int getCount() {
		return data.size();

	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View dynamicView = convertView;
		if (convertView == null)
			dynamicView = inflater.inflate(R.layout.audio_data_view, null);
		text1 = UIHelper.getTextView(dynamicView, R.id.textView);

		image1 = UIHelper.getImageView(dynamicView, R.id.image1);

		imagelinearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout1);

		
		 linearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
		  R.id.linear_layout1);
		 

		try {
			int i = position;
			if (i < data.size()) {
				audioValues = data.get(i);
				final String firstLabel = audioValues.getLabel();
				final String firstThumbnail = audioValues.getThumbnail();
				createView(firstLabel, firstThumbnail, imagelinearLayout1,
						image1, text1);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dynamicView;
	}

	public void createView(String label, String thumbnail,
			LinearLayout imageLinearLayout, ImageView image, TextView text) {

		Log.i("tag",
				Picasso.with(context)
						.load("http://" + profile_ip_value + "/image/"
								+ thumbnail.replace("%", "%25")).toString());
		Picasso.with(context)
				.load("http://" + profile_ip_value + "/image/"
						+ thumbnail.replace("%", "%25"))
				.resize(((w - model.pxToDp(30)) / 3), model.pxToDp(120))
				/*.error(R.drawable.album)*/.centerCrop().into(image);

		LinearLayout.LayoutParams linearLayout2Params = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3), model.pxToDp(120));
		linearLayout2Params.setMargins(model.pxToDp(5), 0, 0, 0);
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3), model.pxToDp(120));
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				((w - model.pxToDp(30)) / 3),
				LinearLayout.LayoutParams.WRAP_CONTENT);

		textParams.setMargins(model.pxToDp(5), 0, 0, 0);
		imageLinearLayout.setLayoutParams(linearLayout2Params);
		image.setLayoutParams(imageParams);
		image.setPadding(model.pxToDp(10), model.pxToDp(4), model.pxToDp(10),
				model.pxToDp(4));
		text.setLayoutParams(textParams);
		text.setText(label);
		text.setSingleLine(true);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		text.setTextColor(Color.parseColor("#939393"));
		text.setBackgroundColor(Color.parseColor("#000000"));
		text.setHeight(model.pxToDp(30));
		text.setPadding(model.pxToDp(5), 0, model.pxToDp(5), 0);
		text.setGravity(Gravity.CENTER);
		//text.setTypeface(null, Typeface.BOLD);
	}

	

}