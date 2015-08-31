package com.example.geek.porter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.LastOwnerException;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.text.Html;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
	HashMap<String, String> hashmap;
	private GoogleMap googleMap;
	static LatLng iiitm = new LatLng(26.250438,78.173469);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		TextView t1,t2,t3,t4,t5,t6,t7,t8,t9;
		RatingBar rb;
		Intent intent = getIntent();
		hashmap = (HashMap<String, String>) intent.getSerializableExtra("hashMap");
		t1=(TextView)findViewById(R.id.name);
		t1.setText(hashmap.get("name"));
		t2=(TextView)findViewById(R.id.type);
		t2.setText(hashmap.get("type"));
		t6=(TextView)findViewById(R.id.price);
		t6.setText("Rs. "+hashmap.get("price"));
		t3=(TextView)findViewById(R.id.date);
		t3.setText(hashmap.get("date"));
		ImageView color=(ImageView)findViewById(R.id.color);
		color.setBackgroundColor(Color.parseColor(hashmap.get("color")));
		//  t4=(TextView)findViewById(R.id.color);
//        t4.setText(hashmap.get("color"));
		  t5=(TextView)findViewById(R.id.weight);
		   t5.setText(hashmap.get("weight"));
		t7=(TextView)findViewById(R.id.phone);
		t7.setText(hashmap.get("phone"));
		t8=(TextView)findViewById(R.id.quantity);
		t8.setText(hashmap.get("quantity"));


     try {

			initilizeMap();

		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageView image = (ImageView) findViewById(R.id.imagee);
		image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		new DownloadImageTask(image).execute(hashmap.get("imagee"));
		image.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
			public void onSwipeTop() {
				Toast.makeText(MyActivity.this, "top", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeRight() {
				Toast.makeText(MyActivity.this, "right", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeLeft() {
				Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
			}
			public void onSwipeBottom() {
				Toast.makeText(MyActivity.this, "bottom", Toast.LENGTH_SHORT).show();
			}

			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);

		return super.onCreateOptionsMenu(menu);
	}


	@SuppressLint("NewApi")
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			Marker TP = googleMap.addMarker(new MarkerOptions().
					position(iiitm).title("position"));
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			//googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			googleMap.getUiSettings().setZoomControlsEnabled(true);
			CameraPosition cameraPosition = new CameraPosition.Builder().target(iiitm).zoom(15).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void share(View v)
	{
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey,Download the Porter App @ "+hashmap.get("link")+"  and Enjoy!");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);
	}
	public void store(View v)
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hashmap.get("link")));
		startActivity(browserIntent);
	}
	public void sms(View v)
	{
		Intent i = new Intent(this,SMSActivity.class);
		startActivity(i);
	}



	public static Bitmap StringToBitmap(String encodedString) {
		try {
			byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
			return bitmap;
		} catch (NullPointerException e) {
			e.getMessage();
			return null;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	@Override
	public void onPause(){
		super.onPause();
		if(googleMap != null) {
			googleMap = null;
			{
			}
		}

	}
	class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView bmImage;

		public DownloadImageTask(ImageView bmImage) {
			this.bmImage = bmImage;
		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			bmImage.setImageBitmap(result);
		}

	}

	class OnSwipeTouchListener implements OnTouchListener {

		protected final GestureDetector gestureDetector;

		public OnSwipeTouchListener (Context ctx){
			gestureDetector = new GestureDetector(ctx, new GestureListener());
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								onSwipeRight();
							} else {
								onSwipeLeft();
							}
						}
						result = true;
					}
					else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
					result = true;

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() {
		}

		public void onSwipeLeft() {
		}

		public void onSwipeTop() {
		}

		public void onSwipeBottom() {
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}