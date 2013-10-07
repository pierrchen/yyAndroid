package com.pierr.rockyouth;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by Pierr on 13-10-7.
 *
 * This service is both started and binded service. We want it be started, because we
 * did not want the service be associated with certain activity; we want it be binded because
 * we have more "advanced" interface such as pause/resume.
 *
 *
 * play a song list, pause/resume current song, automatically play next song.
 *
 * MusicPlayService is responsible for maintain a global play list
 */
public class MusicPlayService extends Service {

    //init in playSongList
    private PlayList mPlayList = PlayList.getInstance();
    private MediaPlayer mAudioPlayer;
    private Context mContext ;
    private LocalBinder mBinder = new LocalBinder();
    private  volatile  PlayerHandler mPlayerHandler ;
    private Looper mServiceLoop;

    private static final int  PLAY = 0 ;
    private static final int  PAUSE = 1 ;
    private static final int  RESUME = 2 ;
    private static final int  NEXT = 3 ;
    private static final int  PREVIOUS = 4 ;


    private  final class PlayerHandler extends Handler {



        private PlayerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY:
                    play(mPlayList.getCurrentSong());
                    break;
                case PAUSE:
                    pause();
                    break;
                case RESUME:
                    resume();
                    break;
                case NEXT:
                    play(mPlayList.getNextSong());
                    break;
                case PREVIOUS:
                    play(mPlayList.getPrevSong());
                    break;
                default:
                    Toast.makeText(mContext,"unknown message",Toast.LENGTH_LONG).show();
                    break;
            }
        }


        private void play(Album.Song song) {
            try {
                mAudioPlayer.setDataSource(mContext, Uri.parse(song.uri));
                mAudioPlayer.prepare();
                mAudioPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext,"Wrong song URI",Toast.LENGTH_LONG).show();
            }

        }

        private  void pause(){
            mAudioPlayer.pause();
        }

        private void resume(){

        }

    }


    public class LocalBinder extends Binder {
        MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mAudioPlayer = new MediaPlayer();
        // create a thread that has a loop which can pass to Handler
        HandlerThread handleThread = new HandlerThread("PlayerService");
        handleThread.start();

        mServiceLoop = handleThread.getLooper();
        mPlayerHandler = new PlayerHandler(mServiceLoop);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Message msg =  mPlayerHandler.obtainMessage(PLAY);
        mPlayerHandler.sendMessage(msg);
        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAudioPlayer.release();
    }


    //song player interface

    public void pause(){
        Message msg =  mPlayerHandler.obtainMessage(PAUSE);
        mPlayerHandler.sendMessage(msg);
    }


    public void resume(){
        Message msg =  mPlayerHandler.obtainMessage(RESUME);
        mPlayerHandler.sendMessage(msg);
    }

    public void playNext(){
        Message msg =  mPlayerHandler.obtainMessage(NEXT);
        mPlayerHandler.sendMessage(msg);
    }


}