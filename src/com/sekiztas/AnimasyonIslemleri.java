package com.sekiztas;

import java.util.Timer;
import java.util.TimerTask;

import android.R.layout;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AnimasyonIslemleri {
	Handler myHandler = new Handler();
	private Timer timer;
	TextView bosDugme,doluDugme;
	float nereye;
	String durum;
	TextView dolu;
	TextView bos;
	RelativeLayout rl;
	
	public  void animasyonUygula(RelativeLayout rl, Context con, TextView doluDugme, TextView bosDugme, String durum) {
		this.rl = rl;
		
		this.dolu = new TextView(con);
		this.bos = new TextView(con);
		
		this.bosDugme = bosDugme;
		this.doluDugme = doluDugme;
		
		doluDugme.setVisibility(View.INVISIBLE);
		//bosDugme.setVisibility(View.INVISIBLE);
		
		dolu.setBackground(doluDugme.getBackground());
		dolu.setHeight(doluDugme.getHeight());
		dolu.setWidth(doluDugme.getWidth());
		dolu.setX(doluDugme.getX());
		dolu.setY(doluDugme.getY()+doluDugme.getWidth());
		
		bos.setBackground(bosDugme.getBackground());
		bos.setHeight(bosDugme.getHeight());
		bos.setWidth(bosDugme.getWidth());
		bos.setX(bosDugme.getX());
		bos.setY(bosDugme.getY()+doluDugme.getWidth());
		
		rl.addView(dolu);
		//rl.addView(bos);
		
		this.nereye = nereye;
		this.durum = durum;
		timer = new Timer();
		timerStart();
	}

	public void updateGUI() {
		myHandler.post(myRunnable);
	}

	public void durdur() {
		timer.cancel();
		myHandler.removeCallbacks(myRunnable);
		
		doluDugme.setVisibility(View.VISIBLE);
		bosDugme.setVisibility(View.VISIBLE);
		
		Drawable tempIcon = bosDugme.getBackground();

		bosDugme.setBackground(doluDugme
				.getBackground());

		doluDugme.setBackground(tempIcon);
		rl.removeView(bos);
		rl.removeView(dolu);
	}

	final Runnable myRunnable = new Runnable() {
		public void run() {
			
			if(durum.equals("yukari")){
				dolu.setY(dolu.getY()-10);
				if(dolu.getY() <= bos.getY()){
					durdur();
				}
			}
			else if(durum.equals("asagi")){
				dolu.setY(dolu.getY()+10);
				if(dolu.getY() >= bos.getY()){
					durdur();
				}
			}
			else if(durum.equals("saga")){
				dolu.setX(dolu.getX()+10);
				if(dolu.getX() >= bos.getX()){
					durdur();
				}
			}
			else if(durum.equals("sola")){
				dolu.setX(dolu.getX()-10);
				if(dolu.getX() <= bos.getX()){
					durdur();
				}
			}
			
			
		}

	};

	public void timerStart() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateGUI();
			}
		}, 0, 5);
	}

}
