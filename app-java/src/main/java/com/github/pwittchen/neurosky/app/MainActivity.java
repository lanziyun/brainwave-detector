package com.github.pwittchen.neurosky.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.shidian.excel.ExcelUtils;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";
  private final static String Record = "Record";
  private NeuroSky neuroSky;

  private static String[] title = {"High_alpga","Theta","Mid_Gamma","High_beta","Delta","Low_gamma","Low_beta","Low_alpha"};
  @BindView(R.id.tv_state) TextView tvState;
  @BindView(R.id.tv_attention) TextView tvAttention;
  @BindView(R.id.tv_meditation) TextView tvMeditation;
  @BindView(R.id.tv_blink) TextView tvBlink;
  Integer attention = -1;
  Integer mediatation = -1;
  Integer poorSignal = -1;
  private Map<String, Object> brainWaveItem;
  private Map<String, Integer> bridge;//從handleBrainWavesChange傳到外面
  private static final String TAG = "YoutubePlayerActivity";
  private ArrayList<ArrayList<String>> recordList;
  private Map<String, Integer> brainWaveRow;
  public ArrayList<WaveModel> modelWaveModelList = new ArrayList<WaveModel>();
  public ArrayList<String> attetionList = new ArrayList<String>();
  public ArrayList<String> meditationList = new ArrayList<String>();

  Button button;
  Button getAll_btn;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    button = findViewById(R.id.test);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Log.d("gagaga","gagag");
        for(int i = 0 ; i < modelWaveModelList.size() ; i++){
          PostDB postDB = new PostDB();
          postDB.execute("http://172.20.10.6/register_finish.php","1","2019/10/02",modelWaveModelList.get(i).getDELTA(),
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


//    brainWaveItem = new HashMap<>();
//    brainWaveData = new HashMap<>();

//    brainWaveItem.put("ATTENTION", attention);
//    brainWaveItem.put("MEDITATION", mediatation);
//    brainWaveItem.put("ROW",bridge);
//    Log.d(LOG_TAG, "中"+String.valueOf(brainWaveItem));

//    bridge.get("DELTA");
//    bridge.get("THETA");
//    bridge.get("LOW_ALPHA");
//    bridge.get("HIGH_ALPHA");
//    bridge.get("LOW_BETA");
//    bridge.get("HIGH_BETA");
//    bridge.get("LOW_GAMMA");
//    bridge.get("MID_GAMMA");

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

  //export excel
  private File file;
  private String fileName;
//  public void exportExcel(View view) {
//    file = new File(getSDPath() + "/Record");
//    makeDir(file);
//    ExcelUtils.initExcel(file.toString() + "/腦波.xls", title);
//    fileName = getSDPath() + "/Record/腦波.xls";
//    ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
//  }
  private  String getSDPath() {
    File sdDir = null;
    boolean sdCardExist = Environment.getExternalStorageState().equals(
            android.os.Environment.MEDIA_MOUNTED);
    if (sdCardExist) {
      sdDir = Environment.getExternalStorageDirectory();
    }
    String dir = sdDir.toString();
    return dir;
  }

  public  void makeDir(File dir) {
    if (!dir.getParentFile().exists()) {
      makeDir(dir.getParentFile());
    }
    dir.mkdir();
  }
  @OnClick(R.id.btn_disconnect) void disconnect() {
    neuroSky.disconnect();
  }

  @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
    neuroSky.start();
  }

  @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
    neuroSky.stop();
  }
}
