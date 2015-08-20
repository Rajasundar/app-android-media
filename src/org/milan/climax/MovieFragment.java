package org.milan.climax;

import java.util.List;

import org.milan.climax.R;
import org.milan.climax.UIhelper.UIHelper;
import org.milan.climax.asynchronous.LoadMovieView;
import org.milan.climax.db.MilanClimaxDataSource;
import org.milan.climax.model.Model;
import org.milan.climax.model.Video;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

@SuppressLint({ "NewApi", "ValidFragment" })
public class MovieFragment extends Fragment implements OnClickListener {

	private Model model;
	private android.app.Activity context;
	private String profile_ip_value;
	private MilanClimaxDataSource milanClimaxDataSource;
	public View rootView, genrePopUpView, directorPopUpView, studioPopUpView;
	private int w;
	private SharedPreferences settings;
	private int current_profile_id;
	private RelativeLayout relLayout, headerRelative;
	private TextView allMovieClick, movieGenreClick, movieDirectorClick,
			movieStudioClick;
	private ImageView closeGenreClick, closeDirectorClick, closeStudioClick,
			actionMovie, adventureMovie, comedyMovie, disasterMovie,
			dramaMovie, easternMovie, foreignMovie, mysteryMovie, romanceMovie,
			thrillerMovie;
	private TableLayout allMovieScrollView, genreMovieSrollView,
			directorMovieScrollView, studioMovieScrollView;
	private LinearLayout linearInMovieDirectors, linearInMovieStudio,
			main_linear_layout;
	private List<Video> movieArr;
	private DrawerLayout mDrawerLayout;
	private RelativeLayout relative_layout;

	public MovieFragment(DrawerLayout mDrawerLayout) {
		model = Model.getModelObj();
		this.context = model.getContext();
		this.milanClimaxDataSource = model.getMilanClimaxDataSource();
		settings = model.getContext().getSharedPreferences("PREF_NAME",
				Context.MODE_PRIVATE);
		this.mDrawerLayout = mDrawerLayout;
		current_profile_id = settings.getInt("Profile_id", 0);
		this.profile_ip_value = milanClimaxDataSource
				.CurrentProfileIp((long) current_profile_id);
		Log.i("LOGCAT", Integer.toString(current_profile_id));
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// Getting Data
		w = context.getWindowManager().getDefaultDisplay().getWidth();
		movieArr = milanClimaxDataSource
				.selectVideoCursorDataWithId((long) current_profile_id);
		// Getting views
		rootView = context.getLayoutInflater().inflate(R.layout.movie_fragment,
				container, false);
		headerRelative = UIHelper.getrRelativeLayout(rootView, R.id.relForHead);
		headerRelative.setOnTouchListener(new RelativeLayoutTouchListener((MainActivity) model.getContext() , model));
		model.openMenusAndRemote(rootView, mDrawerLayout, context);
		main_linear_layout = UIHelper.getLinearLayoutView(rootView,
				R.id.linearForAlbums);
		relative_layout = UIHelper.getrRelativeLayout(rootView,
				R.id.transparent_background);

		// Finding Event Handlers
		// Genres
		movieGenreClick = UIHelper
				.getTextView(rootView, R.id.txtGenresInMovies);
		allMovieClick = UIHelper.getTextView(rootView, R.id.txtAllInMovies);
		movieDirectorClick = UIHelper.getTextView(rootView,
				R.id.txtDirectorsInMovies);
		movieStudioClick = UIHelper.getTextView(rootView,
				R.id.txtStudiosInMovies);

		// Event Handling
		movieGenreClick.setOnClickListener(this);
		allMovieClick.setOnClickListener(this);
		movieDirectorClick.setOnClickListener(this);
		movieStudioClick.setOnClickListener(this);
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

		// Loading Dynamic views
		// new LoadMovieView(context, movieCursor, rootView, profile_ip_value,
		// w).execute();
		new LoadMovieView(context, movieArr, rootView, profile_ip_value, w,
				"movie", main_linear_layout, relative_layout).execute();
		return rootView;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Log.i("Movie Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.i("Movie Fragment", "Asynchronous Task stopped");
		super.onStop();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		((MainActivity) model.getContext()).setAlpha(v);
		if (v.getId() == R.id.txtGenresInMovies) {
			relative_layout.setVisibility(View.VISIBLE);
			genrePopUpView = context.getLayoutInflater().inflate(
					R.layout.moviegenres, null);
			movieGenreClick = UIHelper.getTextView(rootView,
					R.id.txtGenresInMovies);
			closeGenreClick = UIHelper.getImageView(genrePopUpView,
					R.id.imgCloseInMovieGenres);
			this.setPopupWindow();
			closeGenreClick.setOnClickListener(this);
			actionMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgActionInMovieGenres);
			adventureMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgAdventureInMovieGenres);
			comedyMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgComedyInMovieGenres);
			disasterMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgDisasterInMovieGenres);
			dramaMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgDramaInMovieGenres);
			easternMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgEasternInMovieGenres);
			foreignMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgForeignInMovieGenres);
			mysteryMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgMysteryInMovieGenres);
			romanceMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgRomanceInMovieGenres);
			thrillerMovie = UIHelper.getImageView(genrePopUpView,
					R.id.imgthrillerInMovieGenres);

			closeGenreClick.setOnClickListener(this);
			actionMovie.setOnClickListener(this);
			adventureMovie.setOnClickListener(this);
			comedyMovie.setOnClickListener(this);
			disasterMovie.setOnClickListener(this);
			dramaMovie.setOnClickListener(this);
			easternMovie.setOnClickListener(this);
			foreignMovie.setOnClickListener(this);
			mysteryMovie.setOnClickListener(this);
			romanceMovie.setOnClickListener(this);
			thrillerMovie.setOnClickListener(this);
			model.getPopUpMessageProfileList().setContentView(genrePopUpView);
			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			model.getPopUpMessageProfileList().showAtLocation(rootView,
					Gravity.CENTER, 0, model.pxToDp(20));
		} else if (v.getId() == R.id.imgCloseInMovieGenres) {
			relative_layout.setVisibility(View.INVISIBLE);
			model.closePopup(model.getPopUpMessageProfileList());
		} else if (v.getId() == R.id.txtAllInMovies) {

			relative_layout.setVisibility(View.VISIBLE);
			movieArr = milanClimaxDataSource
					.selectVideoCursorDataWithId((long) current_profile_id);
			movieGenreClick.setTextColor(Color.parseColor("#ffffff"));
			allMovieClick.setTextColor(Color.parseColor("#000000"));
			movieDirectorClick.setTextColor(Color.parseColor("#ffffff"));
			movieStudioClick.setTextColor(Color.parseColor("#ffffff"));
			new LoadMovieView(context, movieArr, rootView, profile_ip_value, w,
					"movie", main_linear_layout, relative_layout).execute();
			relative_layout.setVisibility(View.INVISIBLE);
		} else if (v.getId() == R.id.txtDirectorsInMovies) {
			relative_layout.setVisibility(View.VISIBLE);
			final Cursor cursor = milanClimaxDataSource
					.selectDirectorCursorDataWithId((long) current_profile_id);

			directorPopUpView = context.getLayoutInflater().inflate(
					R.layout.moviedirectors, null);
			linearInMovieDirectors = UIHelper.getLinearLayoutView(
					directorPopUpView, R.id.linearInMovieDirectors);
			linearInMovieDirectors.removeAllViews();
			movieDirectorClick = UIHelper.getTextView(rootView,
					R.id.txtDirectorsInMovies);
			closeDirectorClick = UIHelper.getImageView(directorPopUpView,
					R.id.imgCloseInMovieDirector);
			model.setPopUpMessageProfileList(new PopupWindow(relLayout,
					LayoutParams.WRAP_CONTENT, model.getHeight(context)
							- model.pxToDp(75)));
			closeDirectorClick.setOnClickListener(this);
			this.setPopupWindow();
			while (cursor.moveToNext()) {
				if (cursor.getPosition() <= cursor.getCount()) {
					LinearLayout linearDirectorImageContent = new LinearLayout(
							context);

					LinearLayout linearDirectorTextContent = new LinearLayout(
							context);
					LinearLayout.LayoutParams textLinearLayoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					textLinearLayoutParams.setMargins(0, model.pxToDp(2), 0, 0);
					LinearLayout.LayoutParams imageLinearLayoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					imageLinearLayoutParams.setMargins(0, model.pxToDp(25), 0,
							0);
					for (int i = 0; i < 3; i++) {
						if (i != 0) {
							int totalCount = cursor.getCount();
							cursor.moveToNext();
							int currentPosition = cursor.getPosition();
							if (totalCount == currentPosition) {
								int produceCountImageView = totalCount % 3;
								int countViews = 0;
								if (produceCountImageView == 1)
									countViews = 2;
								else if (produceCountImageView == 2)
									countViews = 1;
								for (int k = 0; k < countViews; k++) {
									ImageView imageView1 = new ImageView(
											context);
									LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
											model.pxToDp(65), model.pxToDp(65));
									imageParams1.setMargins(model.pxToDp(20),
											0, model.pxToDp(20), 0);
									TextView textView1 = new TextView(context);
									textView1.setSingleLine(true);
									textView1.setTextColor(Color
											.parseColor("#ffffff"));
									textView1.setTextSize(
											TypedValue.COMPLEX_UNIT_SP, 12);
									textView1.setGravity(Gravity.CENTER);
									LinearLayout.LayoutParams textParams1 = new LinearLayout.LayoutParams(
											model.pxToDp(65),
											LinearLayout.LayoutParams.WRAP_CONTENT);
									textParams1.setMargins(model.pxToDp(20), 0,
											model.pxToDp(20), 0);
									linearDirectorImageContent.addView(
											imageView1, imageParams1);
									linearDirectorTextContent.addView(
											textView1, textParams1);
								}
								break;
							}
						}
						linearDirectorTextContent
								.setGravity(Gravity.CENTER_HORIZONTAL);
						linearDirectorImageContent
								.setGravity(Gravity.CENTER_HORIZONTAL);

						ImageView imageView = new ImageView(context);
						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								model.pxToDp(65), model.pxToDp(65));
						imageParams.setMargins(model.pxToDp(20), 0,
								model.pxToDp(20), 0);
						imageView
								.setBackgroundResource(R.drawable.moviedirector);
						final String directorName = cursor.getString(0);
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								movieArr = milanClimaxDataSource
										.selectDirectorMovieDataWithId(
												(long) current_profile_id,
												directorName);
								movieGenreClick.setTextColor(Color
										.parseColor("#ffffff"));
								allMovieClick.setTextColor(Color
										.parseColor("#ffffff"));
								movieDirectorClick.setTextColor(Color
										.parseColor("#000000"));
								movieStudioClick.setTextColor(Color
										.parseColor("#ffffff"));
								new LoadMovieView(context, movieArr, rootView,
										profile_ip_value, w, "movie",
										main_linear_layout, relative_layout)
										.execute();
								model.closePopup(model
										.getPopUpMessageProfileList());
								relative_layout.setVisibility(View.INVISIBLE);
							}
						});
						TextView textView = new TextView(context);
						textView.setSingleLine(true);
						textView.setText(directorName);
						textView.setTextColor(Color.parseColor("#ffffff"));
						textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						textView.setGravity(Gravity.CENTER);
						LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
								model.pxToDp(70),
								LinearLayout.LayoutParams.WRAP_CONTENT);
						textParams.setMargins(model.pxToDp(15), 0,
								model.pxToDp(15), 0);
						linearDirectorImageContent.addView(imageView,
								imageParams);
						linearDirectorTextContent.addView(textView, textParams);

					}

					linearInMovieDirectors.addView(linearDirectorImageContent,
							imageLinearLayoutParams);
					linearInMovieDirectors.addView(linearDirectorTextContent,
							textLinearLayoutParams);
				}
			}
			model.getPopUpMessageProfileList()
					.setContentView(directorPopUpView);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.setPopUpOPen(true);
			model.getPopUpMessageProfileList().showAtLocation(rootView,
					Gravity.CENTER, 0, model.pxToDp(20));

		} else if (v.getId() == R.id.txtStudiosInMovies) {
			relative_layout.setVisibility(View.VISIBLE);
			final Cursor cursor1 = milanClimaxDataSource
					.selectStudioCursorDataWithId((long) current_profile_id);
			studioPopUpView = context.getLayoutInflater().inflate(
					R.layout.moviestudio, null);
			linearInMovieStudio = UIHelper.getLinearLayoutView(studioPopUpView,
					R.id.linearInMovieStudio);
			linearInMovieStudio.removeAllViews();
			movieStudioClick = UIHelper.getTextView(rootView,
					R.id.txtStudiosInMovies);
			closeStudioClick = UIHelper.getImageView(studioPopUpView,
					R.id.imgCloseInMovieStudio);
			this.setPopupWindow();
			closeStudioClick.setOnClickListener(this);
			while (cursor1.moveToNext()) {
				if (cursor1.getPosition() <= cursor1.getCount()) {
					LinearLayout linearStudioImageContent = new LinearLayout(
							context);
					// linearStudioImageContent
					// .setGravity(Gravity.CENTER_HORIZONTAL);
					LinearLayout linearStudioTextContent = new LinearLayout(
							context);
					linearStudioTextContent
							.setGravity(Gravity.CENTER_HORIZONTAL);
					LinearLayout.LayoutParams textLinearLayoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					textLinearLayoutParams.setMargins(0, model.pxToDp(2), 0, 0);
					LinearLayout.LayoutParams imageLinearLayoutParams = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);
					imageLinearLayoutParams.setMargins(0, model.pxToDp(25), 0,
							0);

					for (int j = 0; j < 3; j++) {
						if (j != 0) {
							int totalCount = cursor1.getCount();
							cursor1.moveToNext();
							int currentPosition = cursor1.getPosition();
							if (totalCount == currentPosition) {
								int produceCountImageView = totalCount % 3;
								int countViews = 0;
								if (produceCountImageView == 1)
									countViews = 2;
								else if (produceCountImageView == 2)
									countViews = 1;
								for (int k = 0; k < countViews; k++) {
									ImageView imageView1 = new ImageView(
											context);
									LinearLayout.LayoutParams imageParams1 = new LinearLayout.LayoutParams(
											model.pxToDp(65), model.pxToDp(65));
									imageParams1.setMargins(model.pxToDp(20),
											0, model.pxToDp(20), 0);
									TextView textView1 = new TextView(context);
									textView1.setSingleLine(true);
									textView1.setTextColor(Color
											.parseColor("#ffffff"));
									textView1.setTextSize(
											TypedValue.COMPLEX_UNIT_SP, 12);
									textView1.setGravity(Gravity.CENTER);
									LinearLayout.LayoutParams textParams1 = new LinearLayout.LayoutParams(
											model.pxToDp(70),
											LinearLayout.LayoutParams.WRAP_CONTENT);
									textParams1.setMargins(model.pxToDp(15), 0,
											model.pxToDp(15), 0);
									linearStudioImageContent.addView(
											imageView1, imageParams1);
									linearStudioTextContent.addView(textView1,
											textParams1);
								}
								break;
							}

						}
						linearStudioImageContent
								.setGravity(Gravity.CENTER_HORIZONTAL);
						ImageView imageView = new ImageView(context);
						LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
								model.pxToDp(65), model.pxToDp(65));
						imageParams.setMargins(model.pxToDp(20), 0,
								model.pxToDp(20), 0);
						imageView.setBackgroundResource(R.drawable.moviestudio);
						final String studioName = cursor1.getString(0);
						imageView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub

								movieArr = milanClimaxDataSource
										.selectStudioMovieDataWithId(
												(long) current_profile_id,
												studioName);
								movieGenreClick.setTextColor(Color
										.parseColor("#ffffff"));
								allMovieClick.setTextColor(Color
										.parseColor("#ffffff"));
								movieStudioClick.setTextColor(Color
										.parseColor("#000000"));
								movieDirectorClick.setTextColor(Color
										.parseColor("#ffffff"));
								new LoadMovieView(context, movieArr, rootView,
										profile_ip_value, w, "movie",
										main_linear_layout, relative_layout)
										.execute();
								model.closePopup(model
										.getPopUpMessageProfileList());
								relative_layout.setVisibility(View.INVISIBLE);
							}
						});
						TextView textView = new TextView(context);
						textView.setSingleLine(true);
						if (studioName.length() < 1)
							textView.setText("Others");
						else
							textView.setText(studioName);
						textView.setTextColor(Color.parseColor("#ffffff"));
						textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
						textView.setGravity(Gravity.CENTER);
						LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
								model.pxToDp(70),
								LinearLayout.LayoutParams.WRAP_CONTENT);
						textParams.setMargins(model.pxToDp(20), 0,
								model.pxToDp(20), 0);
						linearStudioImageContent
								.addView(imageView, imageParams);
						linearStudioTextContent.addView(textView, textParams);

					}

					linearInMovieStudio.addView(linearStudioImageContent,
							imageLinearLayoutParams);
					linearInMovieStudio.addView(linearStudioTextContent,
							textLinearLayoutParams);
				}
			}
			model.getPopUpMessageProfileList().setContentView(studioPopUpView);
			model.getPopUpMessageProfileList().setAnimationStyle(
					R.style.PopupWindow);
			model.getPopUpMessageProfileList().setWidth(w - model.pxToDp(20));
			model.getPopUpMessageProfileList().showAtLocation(rootView,
					Gravity.CENTER, 0, model.pxToDp(20));

		} else if (v.getId() == R.id.imgCloseInMovieDirector) {
			relative_layout.setVisibility(View.INVISIBLE);
			model.closePopup(model.getPopUpMessageProfileList());
		} else if (v.getId() == R.id.imgCloseInMovieStudio) {
			relative_layout.setVisibility(View.INVISIBLE);
			model.closePopup(model.getPopUpMessageProfileList());
		} else if (v.getId() == R.id.imgActionInMovieGenres) {
			loadGenreMovies("action");
		} else if (v.getId() == R.id.imgAdventureInMovieGenres) {
			loadGenreMovies("adventure");
		} else if (v.getId() == R.id.imgComedyInMovieGenres) {
			loadGenreMovies("comedy");
		} else if (v.getId() == R.id.imgDisasterInMovieGenres) {
			loadGenreMovies("disaster");
		} else if (v.getId() == R.id.imgDramaInMovieGenres) {
			loadGenreMovies("drama");
		} else if (v.getId() == R.id.imgEasternInMovieGenres) {
			loadGenreMovies("eastern");
		} else if (v.getId() == R.id.imgForeignInMovieGenres) {
			loadGenreMovies("foreign");
		} else if (v.getId() == R.id.imgMysteryInMovieGenres) {
			loadGenreMovies("mystery");
		} else if (v.getId() == R.id.imgRomanceInMovieGenres) {
			loadGenreMovies("romance");
		} else if (v.getId() == R.id.imgthrillerInMovieGenres) {
			loadGenreMovies("thriller");
		}
	}

	public void loadGenreMovies(String genreCategory) {
		movieArr = milanClimaxDataSource.selectGenreMovieDataWithId(
				(long) current_profile_id, genreCategory);
		movieGenreClick.setTextColor(Color.parseColor("#000000"));
		allMovieClick.setTextColor(Color.parseColor("#ffffff"));
		movieDirectorClick.setTextColor(Color.parseColor("#ffffff"));
		movieStudioClick.setTextColor(Color.parseColor("#ffffff"));
		new LoadMovieView(context, movieArr, rootView, profile_ip_value, w,
				"movie", main_linear_layout, relative_layout).execute();
		model.closePopup(model.getPopUpMessageProfileList());
		relative_layout.setVisibility(View.INVISIBLE);
	}

	public void setPopupWindow() {
		model.setPopUpMessageProfileList(new PopupWindow(relLayout,
				LayoutParams.WRAP_CONTENT, model.getHeight(context)
						- model.pxToDp(75)));
	}

}
