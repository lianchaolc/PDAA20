package afu.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import com.example.pda.R;

/**
 * Created with IntelliJ IDEA. User: liu_rui Date: 2020/4/15 Time: 20:27 To
 * change this template use File | Settings | File Templates. Description:
 */
public class SoundUtil {

	private static SoundUtil soundUtil;
	public static Context mContext;
	private MediaPlayer player;

	private SoundUtil() {
	}

	public static SoundUtil initSoundPool() {
		if (soundUtil == null) {
			soundUtil = new SoundUtil();
		}
		return soundUtil;
	}

	private void initSound() {
		if (player != null)
			return;
		// 音频资源res\raw\d.wav
		player = MediaPlayer.create(mContext, R.raw.beep);
	}

	public void play() {
		initSound();
		if (mContext != null)
			if (player != null)
				// 音频资源res\raw\d.wav
				player.start();
		Log.e("TAG", "play: " + player + "\tmContext: " + mContext);
	}
}
