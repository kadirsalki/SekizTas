package com.sekiztas;



import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	Button cozButon;
	Button yeniOyunButon;
	RelativeLayout layout;
	TilesPanel myPanel;
	MainActivity activity;
	int ekranGenisligi;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		AdView adView = (AdView) this.findViewById(R.id.adView2);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest); //adView i yüklüyoruz
		
	    
		GradientDrawable btnShape = new GradientDrawable(
				Orientation.BOTTOM_TOP, new int[] { Color.WHITE, Color.WHITE });
		btnShape = new GradientDrawable(Orientation.BOTTOM_TOP, new int[] {
				Color.WHITE, Color.WHITE });
		btnShape.setCornerRadius(40);
		btnShape.setStroke(5, Color.rgb(126, 00, 36));
		LayerDrawable btnLayer = new LayerDrawable(new Drawable[] { btnShape });
		

		cozButon = (Button) findViewById(R.id.cozButon);
		layout = (RelativeLayout) findViewById(R.id.anaLayout);
		yeniOyunButon = (Button) findViewById(R.id.yeniOyunButon);
		Display display = getWindowManager().getDefaultDisplay();
		ekranGenisligi = display.getWidth();
		
		myPanel = new TilesPanel(layout, this, ekranGenisligi);
		

		cozButon.setBackgroundDrawable(btnLayer);
		yeniOyunButon.setBackgroundDrawable(btnLayer);

		
		activity = this;
		cozButon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!myPanel.checkGameFinish()) {
					myPanel.solve8puzzle();
				}
			}
		});

		yeniOyunButon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				myPanel.ekraniTemizle();
				myPanel = new TilesPanel(layout, activity, ekranGenisligi);
			}
		});

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);
	}

	SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float sensitvity = 100;

			if (!myPanel.checkGameFinish()) {

				if ((e1.getX() - e2.getX()) > sensitvity) {
					myPanel.checkMove("sol");
				} else if ((e2.getX() - e1.getX()) > sensitvity) {
					myPanel.checkMove("sag");
				} else if ((e1.getY() - e2.getY()) > sensitvity) {
					myPanel.checkMove("yukari");
				} else if ((e2.getY() - e1.getY()) > sensitvity) {

					myPanel.checkMove("asagi");
				} else {

				}

			}

			return super.onFling(e1, e2, velocityX, velocityY);
		}
	};

	GestureDetector gestureDetector = new GestureDetector(
			simpleOnGestureListener);

}
