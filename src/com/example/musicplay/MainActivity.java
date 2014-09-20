 


package com.example.musicplay;

import java.util.ArrayList; 
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.example.data.MediaUtil;
import com.example.data.MusicInfor;

/**
 * 
 * @author Administrator
 *@功能 实现歌曲列表
 */
public class MainActivity extends Activity {
	private ListView listView;
	private int position  ;
	private MusicInfor musicInfor;
	private MediaUtil mediaList;
	private TextView musicName;
	private TextView musicLong;
	private Button musicButton;
	private String path;
	private int currentPosition;
	private List <MusicInfor> music;
	private Receiver receiver;
	private static  final String UPDATE = "com.example.action.update";
	private static final  String NOW = "com.example.action.now";
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//注册广播接收器
		receiver = new Receiver();
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(UPDATE);
	    registerReceiver(receiver, filter);

	   mediaList =new MediaUtil();
		musicInfor = new MusicInfor();
		listView = (ListView) findViewById(R.id.listView);
		musicName = (TextView)findViewById (R.id.musicName);
		musicLong=(TextView)findViewById(R.id.musicLong);
		musicButton = (Button)findViewById(R.id.musicButton);
	    music = new ArrayList<MusicInfor>();
	    music =mediaList.musicList(MainActivity.this);//得到歌曲列表
	    SimpleAdapter list = new SimpleAdapter(this, mediaList.setMap(music),
				R.layout.list_item,
				new String[] { "musicTitle", "musicArtist" }, new int[] {
			R.id.musicTitle, R.id.musicArtist });//数据适配器
          listView.setAdapter(list);
          

	//绑定ListView的监听器
	listView.setOnItemClickListener(new OnItemClickListener() {
		@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent= new Intent();
				intent.putExtra("position", arg2);
				intent.setClass(MainActivity.this, MainPlay.class);
				startActivity(intent);//启动下一个Activity
				playMusic (arg2);	//播放该条目
				}
		} );
	   Intent intent2 = new Intent ();
	   intent2.setAction(NOW);
	   sendBroadcast(intent2);//发送广播消息更新控件信息
	}
	
		   //启动Service播放该条目
			public  void playMusic(int arg2) {
				// TODO Auto-generated method stub
				   musicInfor = music.get(arg2);//得到对象
				    path = musicInfor.geturl();
					Intent intent = new Intent();
					intent.putExtra("path", path);
					intent.putExtra("position", arg2);
					intent.putExtra("flag", 1);
					intent.setAction("com.example.service.Play");
					startService(intent);
					}
			
			@Override
			protected void onResume() {
				// TODO Auto-generated method stub
				super.onResume();
			    //绑定按钮监听器
			    musicButton.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
				     	intent.putExtra("flag", 6);
				        intent.putExtra("position", position);
						intent.setClass(MainActivity.this , MainPlay.class);
						startActivity(intent);//Activity跳转
					}

				});
			}
			
			
       //返回键消息提示
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getAction() == KeyEvent.ACTION_DOWN) {
					new AlertDialog.Builder(this)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("退出")
							.setMessage("您确定要退出？")
							.setNegativeButton("取消", null)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											finish();
											Intent intent = new Intent();
											intent.setAction("com.example.service.Play");
											unregisterReceiver(receiver);
											stopService(intent); // 停止后台服务
										}
									}).show();
				}
				return super.onKeyDown(keyCode, event);
			}
			
			
	//广播接收器接受Service的广播
	public  class Receiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if( intent.getAction().equals(UPDATE)){
			position =currentPosition = intent.getIntExtra("currentPosition", 0);
			musicName.setText(music.get(currentPosition).getmusicTitle());
			musicLong.setText(music.get(currentPosition).getmusicArtist());
			}
			
			}
	}
	
}

	
	
