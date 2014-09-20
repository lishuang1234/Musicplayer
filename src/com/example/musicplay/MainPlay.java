package com.example.musicplay;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.data.MediaUtil;
import com.example.data.MusicInfor;

/**
 * 
 * @author Administrator
 *@���� ʵ�ֲ��Ž��漰���ֲ���ģʽ
 */
public class MainPlay extends Activity {
	private TextView musicTitle2;
	private TextView musicArtist2;
	private SeekBar progress;
	private Button start;
	private Button before;
	private Button last;
	private Button circle;
	private Button repeat;
	private TextView current;
	private TextView allSize;
	private boolean state;
	private boolean circleOne;
	private int repeatStatus;
	private int position;
	private int currentPosition;
	private int  currentTime;
	private int changeTime;
	private int flag ;
	private int flag2;
	private Receiver receiver;
	private String path;
	private MediaUtil mediaUtil ;
	private MusicInfor musicInfor;
	private int size;
	private List <MusicInfor> music;
	private static final  String UPDATE = "com.example.action.update";
	private static final String NOW = "com.example.action.now";
	private static final String UPDATE_TIME = "com.example.action.updatetime";
	private static final  String CHANGE = "com.example.action.change";
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   setContentView(R.layout.play);

	   ActionBar actionBar = getActionBar();//�õ�ActionBar
	    actionBar.setDisplayHomeAsUpEnabled(true);//����ͼ��
	    
	   mediaUtil =new MediaUtil();
	   musicInfor = new MusicInfor();
	   state = true;//��ʾ��ͣ����
	   circleOne = false;
	   repeatStatus = 0;
	  
	   //�õ���ת�����ı������position
	   Intent intent = getIntent();
	   position = intent.getIntExtra("position", position);
	   flag = intent.getIntExtra("flag", 0);   
	   
	   findviewbyid();//�ҵ������ؼ�
	   setOnClik(); //�󶨿ؼ�������	
	  
	   
	   //ע��㲥������
	   receiver = new Receiver();
	   IntentFilter filter = new IntentFilter();
	   filter.addAction(UPDATE);
	   filter.addAction(UPDATE_TIME);
	   registerReceiver(receiver, filter);
	   
	   
	   Intent intent2 = new Intent ();
	   intent2.setAction(NOW);
	   sendBroadcast(intent2);//���͹㲥��Ϣ���¿ؼ���Ϣ
	   
	   }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		   //seekBar������
		   progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			  changeTime =	seekBar.getProgress();
			  Intent intent3 = new Intent ();
			  intent3.setAction(CHANGE);
			  intent3.putExtra("changeSeek", changeTime);
			  sendBroadcast(intent3);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
			
			}
		});
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	
//�ҵ������ؼ�
	private void findviewbyid() {
		// TODO Auto-generated method stub
		musicTitle2 = (TextView)findViewById (R.id.musicTitle2);
		musicArtist2 = (TextView)findViewById (R.id.musicArtist2);
		current = (TextView)findViewById (R.id.current);
		allSize = (TextView)findViewById (R.id.allsize);
		start = (Button)findViewById (R.id.start);
		repeat = (Button)findViewById (R.id.repeat);
		circle = (Button)findViewById (R.id.circle);
		last = (Button)findViewById (R.id.last);
		before = (Button)findViewById (R.id.before);
		progress = (SeekBar)findViewById (R.id.progress);
		}
	
	
     //�󶨿ؼ�������
	private void setOnClik() {
		// TODO Auto-generated method stub
		MyClickListener listener = new MyClickListener();
		start.setOnClickListener(listener);
		last.setOnClickListener(listener);
		before.setOnClickListener(listener);
		circle.setOnClickListener(listener);
		repeat.setOnClickListener(listener);

	}

	
	
	//��ť�ļ�����
	class MyClickListener implements OnClickListener
	{	
   
         Intent intent = new Intent ();
			@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()){
			case R.id.start:
				if(state){//��ͣ
		        intent.setAction("com.example.service.Play");
		        flag =0;
				start.setBackgroundResource(R.drawable.start);
				state = false;
				break;
				}
				else
				{//��ʼ
					intent.setAction("com.example.service.Play");
					 flag =0;
					start.setBackgroundResource(R.drawable.pause);
					state = true;
					break;
				}
		
			case R.id.before://��һ��
				intent.setAction("com.example.service.Play");
				flag = 2;
				before.setBackgroundResource(R.drawable.beforpush);
				before(position);
				before.setBackgroundResource(R.drawable.before);
				break;
			case R.id.last://��һ��
				intent.setAction("com.example.service.Play");
				flag = 3;
				last.setBackgroundResource(R.drawable.lastpush);
				last(position);
				last.setBackgroundResource(R.drawable.last);	
				break; 
			case R.id.circle://�������
				if (!circleOne){
				Toast.makeText(MainPlay.this, "������ſ���",Toast.LENGTH_SHORT  ).show();
				intent.setAction("com.example.service.Play");
				flag = 4;
		       circle.setBackgroundResource(R.drawable.circlepush);
		       circleOne = true;
		       repeat.setClickable(false);
		       }
		       else
		       {
		    	   Toast.makeText(MainPlay.this, "������Źر�",Toast.LENGTH_SHORT  ).show();
		    	   circle.setBackgroundResource(R.drawable.circle);
		    	   circleOne = false;
		    	    repeat.setClickable(true);
		    	    flag = 6;//˳�򲥷�
		       }
				break;
			case R.id.repeat:
				if (repeatStatus == 0){//����ѭ��
					Toast.makeText(MainPlay.this, "����ѭ������",Toast.LENGTH_SHORT  ).show();
				intent.setAction("com.example.service.Play");
				flag = 5;
				repeat.setBackgroundResource(R.drawable.repeatother);
				repeatStatus = 1;
				circle.setClickable(false);
			}
				else if (repeatStatus == 1){//˳�򲥷�
					Toast.makeText(MainPlay.this, "ȫ��ѭ������",Toast.LENGTH_SHORT  ).show();
					intent.setAction("com.example.service.Play");
					flag = 6;
					repeat.setBackgroundResource(R.drawable.repeatpush);
					repeatStatus = 2;
					}
				else if (repeatStatus == 2){
					Toast.makeText(MainPlay.this, "ѭ���ر�",Toast.LENGTH_SHORT  ).show();
					repeat.setBackgroundResource(R.drawable.repeat);
					repeatStatus =0;
					circle.setClickable(true);
					}
			}			
	    musicInfor =  mediaUtil.musicList(MainPlay.this).get(position);
	    path = musicInfor.geturl();
	    intent.putExtra("position", position);
	    intent.putExtra("flag",flag);
		intent.putExtra("path", path);
      	startService(intent);
         }
	
			
	//�õ���һ�׸��λ��
			private void before (int end) {
				// TODO Auto-generated method stub
				position =end-1;
				if (position < 0){
					position =0;
					}
			}
			
			
		//�õ���һ�׸��λ��
		   private void last (int end) {
				// TODO Auto-generated method stub
				position = end+1;
				if (position > mediaUtil.musicList(MainPlay.this).size()-1){
					position = mediaUtil.musicList(MainPlay.this).size()-1;
					}
			}
	}
	
	
	

	 @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        // Inflate the menu; this adds items to the action bar if it is present.  
	        super.onCreateOptionsMenu(menu);  
	        //��Ӳ˵���  
	        MenuItem add=menu.add(0,0,0,"�����б�");  
	        //�󶨵�ActionBar    
	        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  
	        return true;  
	    }  
	 
	 
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(this, MainActivity.class); 
			startActivity(intent);
			return super.onOptionsItemSelected(item);
		}

		

	
	//�㲥���������ܹ㲥
	public class Receiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String inf = intent.getAction();
		if (inf.equals(UPDATE_TIME)){//������ʾʱ�������
		 currentTime = intent.getIntExtra("currentTime", 0);
		progress.setProgress(currentTime);
		 current.setText(getTime(currentTime));
		}
		else if (inf.equals(UPDATE)){//������ʾ���ָ���
      position = currentPosition = intent.getIntExtra("currentPosition", 0);
		size = intent.getIntExtra("size",  0);
		flag2 = intent.getIntExtra("flag", 1);
		progress.setMax(size);
		allSize.setText(getTime(size));
		musicTitle2.setText(mediaUtil.musicList(MainPlay.this).get(currentPosition).getmusicTitle());
		musicArtist2.setText(mediaUtil.musicList(MainPlay.this).get(currentPosition).getmusicArtist());
		if (flag2 == 4){
			  circle.setBackgroundResource(R.drawable.circlepush);
		
		}
		else if (flag2 ==5){
			repeat.setBackgroundResource(R.drawable.repeatother);
	
		}
		else if (flag2 == 6){
			repeat.setBackgroundResource(R.drawable.repeatpush);
		}
		else if (flag2 == 1){
			start.setBackgroundResource(R.drawable.pause);
		}
		else if (flag2 ==0){
			start.setBackgroundResource(R.drawable.start);
		}
		}
		}
	
  //�õ���ʾʱ��
	private String  getTime(int size) {
		int min;
		int sec;
		int hour;
		hour = (int) ( size /1000.0 /3600);
		min = (int)(size /1000.0/60);
		sec =(int)((size/1000.0/60 - size/1000/60)*60);
		return  ""+hour+":"+min + ":"+ sec;
		// TODO Auto-generated method stub
	}
}


}
