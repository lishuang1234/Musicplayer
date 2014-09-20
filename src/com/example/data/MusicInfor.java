package com.example.data;

public class MusicInfor {
/**
 * 
 * @功能 歌曲信息类
 */
	private	String musicArtist;
	private	String musicTitle;
	private	long musicSize;
	private	String url;
		
		public void setmusicArtist (String musicArtist){
			this.musicArtist =musicArtist;
		}
		public void setmusicTitle (String musicTitle){
			this.musicTitle = musicTitle;
		}
		public void seturl (String url){
			this.url = url;
		}
		public void setmusicSize (long musicSize){
			this.musicSize = musicSize;
		}
	   public String getmusicArtist (){
			return musicArtist;
		}
		public String getmusicTitle (){
			return musicTitle;
		}
		public String geturl (){
			return url;
		}
		
		public long getmusicSize (){
			return musicSize; 
	        }
}
