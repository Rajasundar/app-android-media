package org.milan.climax.lazyloader;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.milan.climax.R;
import org.milan.climax.MainActivity;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadBitmapImage;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;
import org.milan.climax.model.Video;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class VideoLazyAdapter extends BaseAdapter {

	private List<Video> data;
	private static LayoutInflater inflater = null;
	private int w;
	private Model model;
	private Video videoValues;
	private TextView playMovieDetail;
	private ImageView imageAlbumDetail, image1, image2, image3;
	private LinearLayout imagelinearLayout1, imagelinearLayout2,
			imagelinearLayout3, linearLayout1, linearLayout2, linearLayout3,
			main_linear_layout;
	private Cursor cursor;
	private TableLayout scrollView;
	public View rootView, popUpView;
	private RelativeLayout relLayout, relative_layout;
	private String profile_ip_value, category;
	private TextView text1, text2, text3;
	private android.app.Activity context;
	private MilanClimaxDataSource milanClimaxDataSource;
	private TableLayout tableLayout;

	public VideoLazyAdapter(final android.app.Activity context, int w,
			String profile_ip_value, List<Video> d, String category,
			final LinearLayout main_linear_layout, RelativeLayout relativeLayout) {
		this.context = context;
		data = d;
		this.w = w;
		this.model = Model.getModelObj();
		this.profile_ip_value = profile_ip_value;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		popUpView = context.getLayoutInflater().inflate(
				R.layout.playmoviedetail, null);
		// popUpView.setPadding(model.pxToDp(10), model.pxToDp(10),
		// model.pxToDp(10), model.pxToDp(10));
		playMovieDetail = UIHelper.getTextView(popUpView,
				R.id.btnTitleInPlayMovieDetail);
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		this.category = category;
		this.main_linear_layout = main_linear_layout;
		this.relative_layout = relativeLayout;
		model.setPopUpMessageProfileList(new PopupWindow(main_linear_layout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)
						- model.pxToDp(75)));
		relative_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.INVISIBLE);
				try {
					model.getPopUpMessageProfileList().dismiss();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					model.setPopUpMessageProfileList(new PopupWindow(
							main_linear_layout, LayoutParams.WRAP_CONTENT,
							model.getHeight(context) - model.pxToDp(75)));
					e.printStackTrace();
				}
			}
		});
	}

	public int getCount() {
		if ((data.size() % 3) == 0)
			return data.size() / 3;
		else
			return (data.size() / 3) + 1;

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
			dynamicView = inflater.inflate(R.layout.video_item, null);

		text1 = UIHelper.getTextView(dynamicView, R.id.text1);
		text2 = UIHelper.getTextView(dynamicView, R.id.text2);
		text3 = UIHelper.getTextView(dynamicView, R.id.text3);

		image1 = UIHelper.getImageView(dynamicView, R.id.image1);
		image2 = UIHelper.getImageView(dynamicView, R.id.image2);
		image3 = UIHelper.getImageView(dynamicView, R.id.image3);

		imagelinearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout1);
		imagelinearLayout2 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout2);
		imagelinearLayout3 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.image_linear_layout3);

		linearLayout1 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout1);
		linearLayout2 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout2);
		linearLayout3 = UIHelper.getLinearLayoutView(dynamicView,
				R.id.linear_layout3);

		try {
			int i = (3 * position);
			if (i < data.size()) {
				imagelinearLayout1.setVisibility(View.VISIBLE);
				text1.setVisibility(View.VISIBLE);
				videoValues = data.get(i);
				final String firstLabel = videoValues.getLabel();
				final String firstThumbnail = videoValues.getThumbnail();
				final String firstDirectorName = videoValues.getDirector_name();
				final String firstDescription = videoValues.getDescription();
				final String firstFanArt = videoValues.getFanArt();
				final String firstGenre = videoValues.getGenreName();
				final String firstRating = videoValues.getRating();
				final String firstTagline = videoValues.getTagline();
				final String firstyear = videoValues.getYear();
				final int firstDuration = videoValues.getDuration();
				final int movieID1 = videoValues.getMovie_id();
				Log.i("Tag--->", "--------->" + i);
				createView(firstLabel, firstThumbnail, imagelinearLayout1,
						image1, text1);
				linearLayout1.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// openPopUp(firstLabel, firstThumbnail, linearLayout1);
						relative_layout.setVisibility(View.VISIBLE);
						openMoviePopUp(firstLabel, firstThumbnail,
								firstDirectorName, firstDescription,
								firstFanArt, firstGenre, firstRating,
								firstTagline, firstyear, firstDuration,
								movieID1);
					}
				});
			} else {
				imagelinearLayout1.setVisibility(View.GONE);
				text1.setVisibility(View.GONE);
			}
			int j = (3 * position) + 1;
			if (j < data.size()) {
				imagelinearLayout2.setVisibility(View.VISIBLE);
				text2.setVisibility(View.VISIBLE);
				videoValues = data.get(j);
				Log.i("Movie-id",
						"--------------->" + videoValues.getMovie_id());

				Log.i("Tag--->", "--------->" + j);
				final String secondLabel = videoValues.getLabel();
				final String secondThumbnail = videoValues.getThumbnail();
				final String secondDirectorName = videoValues
						.getDirector_name();
				final String secondDescription = videoValues.getDescription();
				final String secondFanArt = videoValues.getFanArt();
				final String secondGenre = videoValues.getGenreName();
				final String secondRating = videoValues.getRating();
				final String secondTagline = videoValues.getTagline();
				final String secondyear = videoValues.getYear();
				final int secondDuration = videoValues.getDuration();
				final int movieID2 = videoValues.getMovie_id();
				createView(secondLabel, secondThumbnail, imagelinearLayout2,
						image2, text2);
				linearLayout2.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						try {
							relative_layout.setVisibility(View.VISIBLE);
							openMoviePopUp(secondLabel, secondThumbnail,
									secondDirectorName, secondDescription,
									secondFanArt, secondGenre, secondRating,
									secondTagline, secondyear, secondDuration,
									movieID2);
						} catch (OutOfMemoryError e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			} else {
				imagelinearLayout2.setVisibility(View.GONE);
				text2.setVisibility(View.GONE);
			}

			int k = (3 * position) + 2;
			if (k < data.size()) {
				imagelinearLayout3.setVisibility(View.VISIBLE);
				text3.setVisibility(View.VISIBLE);
				videoValues = data.get(k);
				Log.i("Tag--->", "--------->" + k);
				final String thirdLabel = videoValues.getLabel();
				final String thirdThumbnail = videoValues.getThumbnail();
				final String thirdDirectorName = videoValues.getDirector_name();
				final String thirdDescription = videoValues.getDescription();
				final String thirdFanArt = videoValues.getFanArt();
				final String thirdGenre = videoValues.getGenreName();
				final String thirdRating = videoValues.getRating();
				final String thirdTagline = videoValues.getTagline();
				final String thirdyear = videoValues.getYear();
				final int thirdDuration = videoValues.getDuration();
				final int movieID3 = videoValues.getMovie_id();
				Log.i("Main", "----------------------->" + movieID3);
				createView(thirdLabel, thirdThumbnail, imagelinearLayout3,
						image3, text3);
				linearLayout3.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						relative_layout.setVisibility(View.VISIBLE);
						openMoviePopUp(thirdLabel, thirdThumbnail,
								thirdDirectorName, thirdDescription,
								thirdFanArt, thirdGenre, thirdRating,
								thirdTagline, thirdyear, thirdDuration,
								movieID3);
					}
				});
			} else {
				imagelinearLayout3.setVisibility(View.GONE);
				text3.setVisibility(View.GONE);
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
				/*.error(R.drawable.movieart)*/.centerCrop().into(image);

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

	public void openMoviePopUp(final String label, final String thumbnail,
			final String director, final String description,
			final String fanArt, final String genre, final String rating,
			final String tagline, final String year, int duration,
			final int movieID) {
		String hours = String.valueOf(duration / 3600);
		if (hours.length() < 2) {
			hours = "0" + hours;
		}
		final String finlHours = hours;
		String minutes = String.valueOf((duration % 3600) / 60);
		if (minutes.length() < 2) {
			minutes = "0" + minutes;
		}
		final String finlMinutes = minutes;
		model.setPopUpMessageProfileList(new PopupWindow(relLayout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)
						- model.pxToDp(75)));
		UIHelper.getTextView(popUpView, R.id.txtValDirectorInMovieDet).setText(
				director.replace(",", "/"));
		UIHelper.getTextView(popUpView, R.id.txtValGenreInMovieDet).setText(
				genre.replace(",", "/"));
		UIHelper.getTextView(popUpView, R.id.txtValYearInMovieDet)
				.setText(year);
		UIHelper.getTextView(popUpView, R.id.txtValDurationInMovieDet).setText(
				finlHours + ":" + finlMinutes);
		UIHelper.getTextView(popUpView, R.id.txtValTagLineInMovieDet).setText(
				tagline);
		UIHelper.getTextView(popUpView, R.id.txtValDescriptionInMovieDet)
				.setText(description);
		UIHelper.getRatingBar(popUpView, R.id.ratingBar).setRating(
				Float.parseFloat(rating) / 2);
		ImageView playMovie = UIHelper.getImageView(popUpView,
				R.id.play_movie_button);
		playMovie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) model.getContext()).setAlpha(v);
				playMovie(label, thumbnail, movieID, linearLayout1);
			}
		});

		playMovieDetail.setText(label);
		// Bitmap bitmap = transform(image);

		Bitmap bmp = null;
		RelativeLayout relativeLayout = (RelativeLayout) popUpView
				.findViewById(R.id.relForMovieDetail);
		try {

			new LoadBitmapImage("http://" + profile_ip_value + "/image/"
					+ fanArt.replace("%", "%25"), model, relativeLayout)
					.execute();
			/*
			 * bmp = getBitmapFromURL("http://" + profile_ip_value + "/image/" +
			 * fanArt.replace("%", "%25")); Bitmap bmp2 = transform(bmp);
			 * BitmapDrawable bmp3 = new BitmapDrawable(bmp2);
			 * 
			 * relativeLayout.setBackgroundDrawable(bmp3);
			 */
		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			relativeLayout.setBackgroundResource(R.drawable.moviebackbg);
		}
		Log.i("Image---->",
				"http://" + profile_ip_value + "/image/"
						+ fanArt.replace("%", "%25"));
		if (bmp == null) {
			relativeLayout.setBackgroundResource(R.drawable.moviebackbg);
		}

		try {

			model.getPopUpMessageProfileList().setContentView(popUpView);

			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			if (model.getPopUpMessageProfileList().isShowing()) {
				model.getPopUpMessageProfileList().dismiss();
			}
			model.getPopUpMessageProfileList().showAtLocation(
					main_linear_layout, Gravity.CENTER, 0, model.pxToDp(15));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		popUpView.findViewById(R.id.closeInMovieDetail).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						((MainActivity) model.getContext()).setAlpha(v);
						relative_layout.setVisibility(View.INVISIBLE);
						try {
							model.getPopUpMessageProfileList().dismiss();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							model.setPopUpMessageProfileList(new PopupWindow(
									main_linear_layout,
									LayoutParams.WRAP_CONTENT, model
											.getHeight(context)
											- model.pxToDp(75)));
							e.printStackTrace();
						}
					}
				});
	}

	@SuppressWarnings("deprecation")
	public void playMovie(String label, String thumbnail, int movie_id,
			LinearLayout linear_layout) {
		HttpResponse httpResponse = model
				.postJsonValuesToServer(
						"http://" + profile_ip_value + "/jsonrpc",
						"{\"jsonrpc\" : \"2.0\", \"method\" : \"Player.Open\",\"params\" : {\"item\":{\"movieid\":"
								+ movie_id + "}}, \"id\" : \"2\"}");
		Log.i("tag", "-------------->" + httpResponse);
		if (httpResponse != null) {
			if (model.getCurrentActivationStatus() == 1)
				model.getXMLFromService(
						"http://" + model.getCurrentGatewayIP()
								+ "/Milan/Drivers/Play_Pause/Play.py");
			((MainActivity) context).openRemoteAnimatedLoadImage();
		} else {
			model.setErrorMsgForToast(context);
		}
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}