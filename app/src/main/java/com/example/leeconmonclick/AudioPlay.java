package com.example.leeconmonclick;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.content.Context;

public class AudioPlay {

    public static MediaPlayer mediaPlayer;
    private static SoundPool soundPool;
    public static boolean isplayingAudio=false;
    public static void playAudio(Context c,int id){
        mediaPlayer = MediaPlayer.create(c,id);
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        if(!mediaPlayer.isPlaying())
        {
            isplayingAudio=true;
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
        }
    }
    public static void stopAudio(){
        isplayingAudio=false;
        mediaPlayer.pause();
    }

    public static void restart(){
        isplayingAudio=true;
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static boolean isIsplayingAudio() {
        return isplayingAudio;
    }

    public static void setIsplayingAudio(boolean isplayingAudio) {
        AudioPlay.isplayingAudio = isplayingAudio;
    }
}