package com.github.pwittchen.neurosky.app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable{

    private final static String LOG_TAG = "NeuroSky";
    private final static String Record = "Record";
    private NeuroSky neuroSky;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Timer timer;
    private boolean isSeekBarChanging;
    Button pauseButton;
    private boolean isPause = false;
    private static String StartMusic = "0";//紀錄音樂播放沒
    public static int song2 = 0;//紀錄音樂在第幾首
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_meditation)
    TextView tvMeditation;
    @BindView(R.id.tv_blink)
    TextView tvBlink;
    Integer attention = -1;
    Integer mediatation = -1;
    private ArrayList<ArrayList<String>> recordList;
    public ArrayList<WaveModel> modelWaveModelList = new ArrayList<WaveModel>();
    public ArrayList<String> attetionList = new ArrayList<String>();
    public ArrayList<String> meditationList = new ArrayList<String>();
    Date date = new Date();
    Button getAll_btn;
    Button postdb;
    Button clear;
    Button userplus;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.mix_4m55s_new);//295秒=10首25秒+9首隔5秒間隔
        seekBar = (SeekBar) findViewById(R.id.playSeekBar);
        seekBar.setOnSeekBarChangeListener(new MySeekBar());
        ImageButton playButton = (ImageButton) findViewById(R.id.start);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                playButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("song22222","開始音樂");
                        mediaPlayer.start();
                        seekBar.setMax(mediaPlayer.getDuration());
                        song2=1;

                        StartMusic = "1";
                        Thread t = new Thread(MainActivity.this);
                        t.start();

                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                Toast.makeText(MainActivity.this, "The song is Over", Toast.LENGTH_SHORT).show();
                                t.interrupt();
                                disconnect();//音樂播完，關腦波偵測
                            }
                        });

                        timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (!isSeekBarChanging) {
                                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                }
                            }
                        }, 0, 50);
                    }
                });
            }
        });

/*
        ImageButton stopButton = (ImageButton) findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                }
            }
        });
        ImageButton pauseButton = (ImageButton) findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying() && !isPause) {
                    mediaPlayer.pause();//暂停播放
                    isPause = true;
                    ((Button) view).setText("播放");
                    playButton.setEnabled(true);//“播放”按钮可用
                } else {
                    mediaPlayer.start();//继续播放
                    ((Button) view).setText("暫停");
                    isPause = false;
                    playButton.setEnabled(false);//“播放”按钮不可用
                }
            }
        });
*/
        postdb = findViewById(R.id.post);
        postdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("gagaga", "gagag");
                SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
                String time = format0.format(date.getTime());
                String[] time2 = time.split(":");
                user_id = 37;

                for (int i = 0; i < modelWaveModelList.size(); i++) {
                    time2[2] = (Integer.parseInt(time2[2]) + 1) + "";
                    if (time2[2].equals("60")) {
                        time2[2] = "0";
                        time2[1] = (Integer.parseInt(time2[1]) + 1) + "";
                        if (time2[1].equals("60")) {
                            time2[1] = "0";
                            time2[0] = (Integer.parseInt(time2[0]) + 1) + "";
                        }
                    }
//172.20.10.6我的 192.168.43.161
                    time = time2[0] + ":" + time2[1] + ":" + time2[2];
                    PostDB postDB = new PostDB();
                    postDB.execute("http://172.20.10.6:80/register_finish.php", "41", time, modelWaveModelList.get(i).getSong(), modelWaveModelList.get(i).getDELTA(),
                            modelWaveModelList.get(i).getTHETA(), modelWaveModelList.get(i).getLOW_ALPHA(), modelWaveModelList.get(i).getHIGH_ALPHA(),
                            modelWaveModelList.get(i).getLOW_BETA(), modelWaveModelList.get(i).getHIGH_BETA(), modelWaveModelList.get(i).getLOW_GAMMA(),
                            modelWaveModelList.get(i).getMID_GAMMA(), modelWaveModelList.get(i).getAttention(), modelWaveModelList.get(i).getMeditation(), modelWaveModelList.get(i).getStartMusic());

                }
            }
        });

        getAll_btn = findViewById(R.id.getAll);
        getAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    for (int i = 0; i < modelWaveModelList.size(); i++) {
                        modelWaveModelList.get(i).setAttention(attetionList.get(i));
                        modelWaveModelList.get(i).setMeditation(meditationList.get(i));
                    }
                    for (int i = 0; i < modelWaveModelList.size(); i++) {
                        Log.d("Record", modelWaveModelList.get(i).getAll());

                    }
            }
        });

        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modelWaveModelList.clear();
                Log.d("clear","已清空！！！");
            }
        });

        userplus = findViewById(R.id.userplus);
        userplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_id++;
            }
        });

        ButterKnife.bind(this);
        neuroSky = createNeuroSky();
    }
    //每30秒換一首歌曲
    @Override
    public void run() {
        try {
            while(true){
                Log.d("song22222","++前"+song2);
                Thread.sleep(30000);
                Log.d("song22222","++中"+song2);
                song2++;
                Log.d("song22222","++後"+song2);
            }
        } catch (InterruptedException e) {
            Log.d("song22222",e.toString());
            System.out.println("I am interrupted....");
        }
    }
    @Override
    protected void onDestroy() {
        mediaPlayer.release();
        timer.cancel();
        timer = null;
        mediaPlayer = null;
        super.onDestroy();
    }



    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滾動時,應當暫停後臺定時器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑動結束後，重新設定值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (neuroSky != null && neuroSky.isConnected()) {
            neuroSky.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (neuroSky != null && neuroSky.isConnected()) {
            neuroSky.stop();
        }
    }

    @NonNull
    private NeuroSky createNeuroSky() {
        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override
            public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override
            public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override
            public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainWavesChange(brainWaves);
            }
        });
    }

    private void handleStateChange(final State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            neuroSky.start();
        }
        tvState.setText(state.toString());
        //Log.d(LOG_TAG, state.toString());
    }

    //取8種原始腦波
    private void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
        recordList = new ArrayList<>();
        for (BrainWave brainWave : brainWaves) {
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(String.valueOf(brainWave.getValue()));
            recordList.add(beanList);
        }
        WaveModel waveModel = new WaveModel(recordList.get(0).toString().replace("[", "").replace("]", ""), recordList.get(1).toString().replace("[", "").replace("]", ""), recordList.get(2).toString().replace("[", "").replace("]", ""),
                recordList.get(3).toString().replace("[", "").replace("]", ""), recordList.get(4).toString().replace("[", "").replace("]", ""), recordList.get(5).toString().replace("[", "").replace("]", ""), recordList.get(6).toString().replace("[", "").replace("]", ""),
                recordList.get(7).toString().replace("[", "").replace("]", ""), "0", "0", StartMusic,song2+"");
        modelWaveModelList.add(waveModel);

        Log.d("song22222","接收腦波");
        Log.d(Record, String.valueOf(recordList));
    }
//取A、M腦波
    private void handleSignalChange(final Signal signal) {
        switch (signal) {
            case ATTENTION:
                tvAttention.setText(getFormattedMessage("attention: %d", signal));
                Log.d(LOG_TAG, getFormattedMessage("attention: %d", signal));
                attention = signal.getValue();
                attetionList.add(attention.toString());
                break;
            case MEDITATION:
                tvMeditation.setText(getFormattedMessage("meditation: %d", signal));
                Log.d(LOG_TAG, getFormattedMessage("meditation: %d", signal));
                mediatation = signal.getValue();
                meditationList.add(mediatation.toString());
                break;
            case BLINK:
                tvBlink.setText(getFormattedMessage("blink: %d", signal));
                break;
        }

    }

    private String getFormattedMessage(String messageFormat, Signal signal) {
        return String.format(Locale.getDefault(), messageFormat, signal.getValue());
    }

    @OnClick(R.id.btn_connect)
    void connect() {
        try {
            neuroSky.connect();
        } catch (BluetoothNotEnabledException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    @OnClick(R.id.btn_disconnect)
    void disconnect() {
        neuroSky.disconnect();
    }
}
