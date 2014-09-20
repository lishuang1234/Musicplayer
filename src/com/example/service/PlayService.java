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
   
	//Handler���͹㲥����SeekBar
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
   
   
   //��ʼ��
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
		 
	   //ע��㲥������
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

	
	
	//������ת������Service
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub   
		//������Ϣ
		flag = intent.getIntExtra("flag",1);
	   position = intent.getIntExtra("position", position);
		path = intent.getStringExtra("path");	
		//��Ӧ����
		 if(flag ==0){//��ͣ
			pause();
		sendCast(intent2);
		}
	 else if (flag == 1){//����
		 play(0);
	 sendCast(intent2);
		 }
		 else if(flag == 2){//��һ��
				play (0);
			 sendCast(intent2);
 }
		 else if (flag == 3){//��һ��
				play (0);
		 sendCast(intent2);
		 }
		 else if  (flag == 4){//�������
			 //���ò�������ɲ��ŵļ�����
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
			else if (flag == 5){//����ѭ��
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
							play(0);              
		    sendCast(intent2) ;
						}});
				}
			else if (flag == 6){//˳�򲥷�
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

	
	
	//���͹㲥��Activity������Ϣ
	private void sendCast(Intent sendIntent2) {
		// TODO Auto-generated method stub
		 sendIntent2.setAction(UPDATE);
		 sendIntent2.putExtra("currentPosition", position);
		sendIntent2.putExtra("size", size);
		sendIntent2.putExtra("flag", flag);
		 sendBroadcast(sendIntent2);
	}


	//��������
   private void play(int current) {
		// TODO Auto-generated method stub
	   sendCast(intent2) ;
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new  MyListener(current));//���������
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
   
   
   
   //��ͣ����
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
	

	//׼��������
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
	
	
//	�����֪ͨ����ʾ
	public void send (){
		musicInfor =  mediaUtil.musicList(PlayService.this).get(position);
		artist = musicInfor.getmusicArtist();
      song = musicInfor.getmusicTitle();
      System.out.println("PlayService --->   " + song);
		
		Intent intent = new Intent (PlayService.this,MainPlay.class);
		PendingIntent intent2 = PendingIntent.getActivity(PlayService.this, 0, intent, 0);//��װIntent
		Notification notifi = new Notification(R.drawable.ic_media_play,
				song+"" ,System.currentTimeMillis());//�������󣬴���ͼ�꣬ժҪ��ʱ��
		notifi.setLatestEventInfo(this, song+"", artist+"", intent2);//��ʾ״̬���ı������ݣ�����ת����Activity
		notifi.number = 1;
		//	notifi.defaults |= Notification.DEFAULT_ALL;
		 mn.notify(id, notifi);
	
	}
	
	
	
	//�㲥������
public class Receiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String inf = intent.getAction();
		if (inf.equals(NOW)){
		 sendCast(intent2);//������ʾMainPlay
		}
		else if(inf.equals(CHANGE)){
			flag = 1;
		currentTime  = intent.getIntExtra("changeSeek", 0);
	    play(currentTime);  
	}
	}
	
}
}