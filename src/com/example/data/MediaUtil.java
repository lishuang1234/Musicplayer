package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

/**
 * 
 * @author Administrator
 * @功能 功能类
 *
 */
public class MediaUtil {
	public MusicInfor musicInfor;
	public Cursor cursor;
	/**
	 * 
	 * @param music
	 * @return musicMap
	 * @功能 根据歌曲列表在此封装
	 */
	  public List<Map<String, Object>>  setMap( List<MusicInfor>  music){
			List<Map<String, Object>> musicMap = new ArrayList<Map<String,Object>>();
			for (Iterator iter = music.iterator(); iter.hasNext();)  {
				Map<String, Object> map = new HashMap<String, Object>();
			  MusicInfor musicinfor =(MusicInfor) iter.next();
	            map.put("musicTitle", musicinfor.getmusicTitle());// 得到该列的标题
				map.put("url", musicinfor.geturl());
				map.put("musicArtist", musicinfor.getmusicArtist());
				map.put("musicSize", musicinfor.getmusicSize());
				musicMap.add(map);
				}
			return musicMap;
				}
	  /**
	   * 
	   * @param x
	   * @return
	   * @功能 查询所有歌曲封装为歌曲信息类，以列表形式存储
	   */
	public List<MusicInfor>  musicList (Context x){
		cursor =x. getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
	//	System.out.println(cursor.getCount());
		List <MusicInfor> music = new ArrayList<MusicInfor>();
		for(int i = 0;i<cursor.getCount(); i++){
			musicInfor = new MusicInfor();
			cursor.moveToNext(); 
			String musicTitle = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE));// 得到标题
			String musicArtist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			long musicSize = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			musicInfor.setmusicArtist(musicArtist);
			musicInfor.setmusicTitle(musicTitle);
			musicInfor.setmusicSize(musicSize);
			musicInfor.seturl(url);
			music.add(musicInfor); 
      }      
		return music;
	}

}
