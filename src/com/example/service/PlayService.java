package com.example.service;

import java.io.IOException;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.data.MediaUtil;
import com.example.data.MusicInfor;
import com.example.musicplay.MainPlay;
//import android.R;



public class PlayService extends Service {
	private MusicInfor musicInfor;
	private MediaUtil mediaUtil;
   private  int   position;
   private MediaPlayer mediaPlayer;
   private  String path;
   private int flag;
   private int size;
   private String artist;
   private String song;
   private static final  String UPDATE = "com.example.action.update";
   private static final String NOW = "com.example.action.now";
   private static final String UPDATE_TIME = "com.example.action.updatetime";
   private static final String CHANGE = "com.example.action.change";
   private Intent intent2;
   private Receiver receiver;
   private int currentTime;
   private NotificationManager mn;
   private int id;
   
	//Handler发送广播更新SeekBar
  private Handler  handler = new Handler (){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what ==1 ){
				if (mediaPlayer != null){
				 currentTime = mediaPlayer.getCurrentPosition();
				 Intent sendIntent = new Intent();
				 sendIntent.setAction(UPDATE_TIME);
				 sendIntent.putExtra("currentTime", currentTime);
				 sendBroadcast(sendIntent);
		     	 handler.sendEmptyMessageDelayed(1, 1000);
				 }
			}
			super.handleMessage(msg);
		}
   };
   
   
   //初始化
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
    	mediaUtil = new MediaUtil();
		musicInfor=new MusicInfor();
	   mediaPlayer = new MediaPlayer();
	   receiver = new Receiver();
	     intent2 = new Intent();
	     mn = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		 id = 1;
		 
	   //注册广播接收器
	   IntentFilter filter = new IntentFilter();
	   filter.addAction(NOW);
	   filter.addAction(CHANGE);
	   registerReceiver(receiver, filter);
	}
	   

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if(mediaPlayer != null){
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		mn.cancel(id);
		super.onDestroy();
	}

	
	
	//接受跳转过来的Service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub   
		//接受信息
		flag = intent.getIntExtra("flag",1);
	   position = intent.getIntExtra("position", position);
		path = intent.getStringExtra("path");	
		//响应操作
		 if(flag ==0){//暂停
			pause();
		sendCast(intent2);
		}
	 else if (flag == 1){//播放
		 play(0);
	 sendCast(intent2);
		 }
		 else if(flag == 2){//上一首
				play (0);
			 sendCast(intent2);
 }
		 else if (flag == 3){//下一首
				play (0);
		 sendCast(intent2);
		 }
		 else if  (flag == 4){//随机播放
			 //设置播放器完成播放的监听器
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
					position =(int)(Math.random()* mediaUtil.musicList(PlayService.this).size() );
					   musicInfor =  mediaUtil.musicList(PlayService.this).get(position);
					    path = musicInfor.geturl();
					    play(0);  
	     sendCast(intent2) ; 
				}});
		
		}
			else if (flag == 5){//单曲循环
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
							play(0);              
		    sendCast(intent2) ;
						}});
				}
			else if (flag == 6){//顺序播放
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stud
						position ++;
						if (position> mediaUtil.musicList(PlayService.this).size()-1 ){
							position = 0;}
		      	  musicInfor =  mediaUtil.musicList(PlayService.this).get(position);
			     path = musicInfor.geturl();
			     play(0);
        sendCast(intent2) ;
		  	}});
				
			} 
		 handler.sendEmptyMessage(1);
		return super.onStartCommand(intent, flags, startId);
	}

	
	
	//发送广播给Activity更新信息
	private void sendCast(Intent sendIntent2) {
		// TODO Auto-generated method stub
		 sendIntent2.setAction(UPDATE);
		 sendIntent2.putExtra("currentPosition", position);
		sendIntent2.putExtra("size", size);
		sendIntent2.putExtra("flag", flag);
		 sendBroadcast(sendIntent2);
	}


	//播放音乐
   private void play(int current) {
		// TODO Auto-generated method stub
	   sendCast(intent2) ;
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new  MyListener(current));//缓冲监听器
			size = mediaPlayer.getDuration();
		      send();
  } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
	   }
	}
   
   
   
   //暂停音乐
private void pause() {
		// TODO Auto-generated method stub
		if (mediaPlayer.isPlaying()){
			
			mediaPlayer.pause();
			flag = 0;
			}
		else{
		mediaPlayer.start();
			flag = 1;
		}
	}
	

	//准备监听器
	public class MyListener implements OnPreparedListener {
		int current;
		public MyListener (int current){
			this.current = current;
			}

		@Override
		public void onPrepared(MediaPlayer mp) {
			// TODO Auto-generated method stub
			mediaPlayer.start();
			if (current>0){
				mediaPlayer.seekTo(current);
	       }
			
		}
		}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
//	这个是通知栏显示
	public void send (){
		musicInfor =  mediaUtil.musicList(PlayService.this).get(position);
		artist = musicInfor.getmusicArtist();
      song = musicInfor.getmusicTitle();
      System.out.println("PlayService --->   " + song);
		
		Intent intent = new Intent (PlayService.this,MainPlay.class);
		PendingIntent intent2 = PendingIntent.getActivity(PlayService.this, 0, intent, 0);//封装Intent
		Notification notifi = new Notification(R.drawable.ic_media_play,
				song+"" ,System.currentTimeMillis());//创建对象，传入图标，摘要，时间
		notifi.setLatestEventInfo(this, song+"", artist+"", intent2);//显示状态栏的标题内容，及跳转到的Activity
		notifi.number = 1;
		//	notifi.defaults |= Notification.DEFAULT_ALL;
		 mn.notify(id, notifi);
	
	}
	
	
	
	//广播接收器
public class Receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String inf = intent.getAction();
		if (inf.equals(NOW)){
		 sendCast(intent2);//更新显示MainPlay
		}
		else if(inf.equals(CHANGE)){
			flag = 1;
		currentTime  = intent.getIntExtra("changeSeek", 0);
	    play(currentTime);  
	}
	}
	
}
}