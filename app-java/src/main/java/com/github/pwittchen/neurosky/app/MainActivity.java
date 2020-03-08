package com.github.pwittchen.neurosky.app;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";
  private final static String Record = "Record";
  private NeuroSky neuroSky;

  private MediaPlayer mediaPlayer;
  private SeekBar seekBar;
  private Timer timer;
  private boolean isSeekBarChanging;
  private int currentPosition;
  Button pauseButton;
  private boolean isPause = false;

  private static String[] title = {"High_alpga","Theta","Mid_Gamma","High_beta","Delta","Low_gamma","Low_beta","Low_alpha"};
  @BindView(R.id.tv_state) TextView tvState;
  @BindView(R.id.tv_attention) TextView tvAttention;
  @BindView(R.id.tv_meditation) TextView tvMeditation;
  @BindView(R.id.tv_blink) TextView tvBlink;
  Integer attention = -1;
  Integer mediatation = -1;

  private ArrayList<ArrayList<String>> recordList;
  public ArrayList<WaveModel> modelWaveModelList = new ArrayList<WaveModel>();
  public ArrayList<String> attetionList = new ArrayList<String>();
  public ArrayList<String> meditationList = new ArrayList<String>();

  Date date = new Date();

  Button button;
  Button getAll_btn;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);


    mediaPlayer = MediaPlayer.create(this, R.raw.mix_4m55s);
    seekBar = (SeekBar) findViewById(R.id.playSeekBar);
    seekBar.setOnSeekBarChangeListener(new MySeekBar());
    Button playButton = (Button) findViewById(R.id.start);
    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mediaPlayer) {
      playButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
          mediaPlayer.start();
          seekBar.setMax(mediaPlayer.getDuration());
          if(isPause){
            pauseButton.setText("藍寶中場休息!");
            isPause = false;//设置暂停标记变量的值为false
          }
          mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
              Toast.makeText(MainActivity.this, "The song is Over", Toast.LENGTH_SHORT).show();
            }
          });
          timer = new Timer();
          timer.schedule(new TimerTask() {
            @Override
            public void run() {
              if(!isSeekBarChanging){
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
              }
            }
          },0,50);
        }
    });
    }
    });

    Button stopButton = (Button) findViewById(R.id.stop);
    stopButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.pause();
          mediaPlayer.seekTo(0);
        }
      }
    });
    pauseButton = (Button) findViewById(R.id.pause);
    pauseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(mediaPlayer.isPlaying()&&!isPause){
          mediaPlayer.pause();//暂停播放
          isPause = true;
          ((Button)view).setText("藍寶繼續放肆起來~");
          playButton.setEnabled(true);//“播放”按钮可用
        }else{
          mediaPlayer.start();//继续播放
          ((Button)view).setText("藍寶停停停....");
          isPause = false;
          playButton.setEnabled(false);//“播放”按钮不可用
        }
      }
    });


    button = findViewById(R.id.test);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d("gagaga","gagag");
        SimpleDateFormat format0 = new SimpleDateFormat("HH:mm:ss");
        String time = format0.format(date.getTime());
        String[] time2 = time.split(":");

        for(int i = 0 ; i < modelWaveModelList.size() ; i++){
          time2[2] = (Integer.parseInt(time2[2])+1)+"";
          if(time2[2].equals("60")){
            time2[2] = "0";
            time2[1] = (Integer.parseInt(time2[1])+1)+"";
            if(time2[1].equals("60")){
              time2[1] = "0";
              time2[0] = (Integer.parseInt(time2[0])+1)+"";
            }
          }
          time = time2[0] + ":" + time2[1] + ":" + time2[2];
          PostDB postDB = new PostDB();
          postDB.execute("http://192.168.43.149/register_finish.php","1",time,"1",modelWaveModelList.get(i).getDELTA(),
                  modelWaveModelList.get(i).getTHETA(),modelWaveModelList.get(i).getLOW_ALPHA(),modelWaveModelList.get(i).getHIGH_ALPHA(),
                  modelWaveModelList.get(i).getLOW_BETA(),modelWaveModelList.get(i).getHIGH_BETA(),modelWaveModelList.get(i).getLOW_GAMMA(),
                  modelWaveModelList.get(i).getMID_GAMMA(),modelWaveModelList.get(i).getAttention(),modelWaveModelList.get(i).getMeditation());

        }
      }
    });
    getAll_btn = findViewById(R.id.getAll);
    getAll_btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        for(int i = 0 ; i < modelWaveModelList.size() ; i++){
          modelWaveModelList.get(i).setAttention(attetionList.get(i));
          modelWaveModelList.get(i).setMeditation(meditationList.get(i));
        }
        for(int i = 0 ; i < modelWaveModelList.size() ; i++){
          Log.d("Record",modelWaveModelList.get(i).getAll());
        }

      }
    });
    ButterKnife.bind(this);
    neuroSky = createNeuroSky();
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

  @Override protected void onResume() {
    super.onResume();
    if (neuroSky != null && neuroSky.isConnected()) {
      neuroSky.start();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (neuroSky != null && neuroSky.isConnected()) {
      neuroSky.stop();
    }
  }

  @NonNull private NeuroSky createNeuroSky() {
    return new NeuroSky(new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
        handleStateChange(state);
      }

      @Override public void onSignalChange(Signal signal) {
        handleSignalChange(signal);
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        handleBrainWavesChange(brainWaves);
      }
    });
  }

  private void handleStateChange(final State state) {
    if (neuroSky != null && state.equals(State.CONNECTED)) {
      neuroSky.start();
    }

    tvState.setText(state.toString());
    Log.d(LOG_TAG, state.toString());
  }
  private void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
//    brainWaveRow = new HashMap<>();
      recordList = new ArrayList<>();
      for (BrainWave brainWave : brainWaves) {
        ArrayList<String> beanList = new ArrayList<String>();
        beanList.add(String.valueOf(brainWave.getValue()));
        recordList.add(beanList);
//      brainWaveRow.put(brainWave.toString(),  brainWave.getValue());
        Log.d(LOG_TAG, String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
      }
      WaveModel waveModel = new WaveModel(recordList.get(0).toString().replace("[","").replace("]",""),recordList.get(1).toString().replace("[","").replace("]",""),recordList.get(2).toString().replace("[","").replace("]",""),
              recordList.get(3).toString().replace("[","").replace("]",""),recordList.get(4).toString().replace("[","").replace("]",""),recordList.get(5).toString().replace("[","").replace("]",""),recordList.get(6).toString().replace("[","").replace("]",""),
              recordList.get(7).toString().replace("[","").replace("]",""),"0","0");
      modelWaveModelList.add(waveModel);
      Log.d(Record, String.valueOf(recordList));
//    bridge = brainWaveRow;
  }



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

  @OnClick(R.id.btn_connect) void connect() {
    try {
      neuroSky.connect();
    } catch (BluetoothNotEnabledException e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
      Log.d(LOG_TAG, e.getMessage());
    }
  }

  @OnClick(R.id.btn_disconnect) void disconnect() {
    neuroSky.disconnect();
  }

//  @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
//    neuroSky.start();
//  }
//
//  @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
//    neuroSky.stop();
//  }
}
