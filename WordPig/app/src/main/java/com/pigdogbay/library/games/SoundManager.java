package com.pigdogbay.library.games;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mark on 10/04/2015.
 */
public class SoundManager
{
    private SoundPool _SoundPool;
    private boolean _IsMuted=false;
    private HashMap<Integer, Integer> _HashMap;
    private List<Integer> _LoadedSounds;

    public boolean isMuted() {
        return _IsMuted;
    }
    public void setIsMuted(boolean isMuted) {
        this._IsMuted = isMuted;
    }
    public SoundManager()
    {
    }

    public void initialize()
    {
        _HashMap = new HashMap<Integer, Integer>();
        _LoadedSounds = new ArrayList<Integer>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }
        _SoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status==0 && !_LoadedSounds.contains(sampleId))
                {
                    _LoadedSounds.add(sampleId);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        _SoundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        _SoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
    }

    public void loadSounds(Context context, List<Integer> resourceIds)
    {
        for (int resId : resourceIds)
        {
            int soundId = _SoundPool.load(context,resId,1);
            _HashMap.put(resId,soundId);
        }
    }

    public void play(int resourceId, float volume)
    {
        if (!_IsMuted)
        {
            int soundId = _HashMap.get(resourceId);
            if (_LoadedSounds.contains(soundId)) {
                _SoundPool.play(soundId, volume, volume, 1, 0, 1f);
            }
        }
    }

    public void release()
    {
        if (_SoundPool!=null) {
            _SoundPool.release();
            _SoundPool = null;
        }
    }

}
