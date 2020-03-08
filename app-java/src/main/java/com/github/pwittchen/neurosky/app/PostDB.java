package com.github.pwittchen.neurosky.app;



import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by god on 2017/10/15.
 */

public class PostDB extends AsyncTask {
    private List<String> list_package_finish_date;
    String line = "";
    public int flag = 0;
    public PostDB(){


    }
    public PostDB(int flag){
        this.flag = flag;
    }




    @Override
    protected Object doInBackground(Object[] params) {
        if(flag == 1){
            AddDicuss2(params);
        }
        AddDicuss(params);
        return null;
    }

    public void AddDicuss2(Object[] params){

        try{
            URL url = new URL(params[0].toString());
            String user_id = params[1].toString();
            String time = params[2].toString();
            String song = params[3].toString();
            String DELTA = params[4].toString();
            String THETA = params[5].toString();
            String LOW_ALPHA = params[6].toString();
            String HIGH_ALPHA = params[7].toString();
            String LOW_BETA = params[8].toString();
            String HIGH_BETA = params[9].toString();
            String LOW_GAMMA = params[10].toString();
            String MID_GAMMA = params[11].toString();
            String ATTENTION = params[12].toString();
            String MEDITATION = params[13].toString();
            Log.d("gagaga",MEDITATION);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String PostData = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8") + "&" +
                    URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8") + "&" +
                    URLEncoder.encode("song","UTF-8")+"="+URLEncoder.encode(song,"UTF-8") + "&" +
                    URLEncoder.encode("DELTA","UTF-8")+"="+URLEncoder.encode(DELTA,"UTF-8")+"&"+
                    URLEncoder.encode("THETA","UTF-8")+"="+URLEncoder.encode(THETA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_ALPHA","UTF-8")+"="+URLEncoder.encode(LOW_ALPHA,"UTF-8")+"&"+
                    URLEncoder.encode("HIGH_ALPHA","UTF-8")+"="+URLEncoder.encode(HIGH_ALPHA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_BETA","UTF-8")+"="+URLEncoder.encode(LOW_BETA,"UTF-8")+"&"+
                    URLEncoder.encode("HIGH_BETA","UTF-8")+"="+URLEncoder.encode(HIGH_BETA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_GAMMA","UTF-8")+"="+URLEncoder.encode(LOW_GAMMA,"UTF-8")+"&"+
                    URLEncoder.encode("MID_GAMMA","UTF-8")+"="+URLEncoder.encode(MID_GAMMA,"UTF-8")+ "&" +
                    URLEncoder.encode("ATTENTION","UTF-8")+"="+URLEncoder.encode(ATTENTION,"UTF-8") + "&" +
                    URLEncoder.encode("MEDITATION","UTF-8")+"="+URLEncoder.encode(MEDITATION,"UTF-8");
            Log.d("gagaga",PostData);
            bufferedWriter.write(PostData);
            Log.d("gagaga",PostData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));

            StringBuilder sb1 = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null) {
                sb1.append(line);
            }
            Log.d("gagaga", sb1.toString());
            bufferedReader.close();inputStream.close();httpURLConnection.disconnect();
        }catch (Exception e){
            Log.d("gagaga",e.getMessage());
        }

    }



    public void AddDicuss(Object[] params){

        try{
            URL url = new URL(params[0].toString());
            String user_id = params[1].toString();
            String time = params[2].toString();
            String song = params[3].toString();
            String DELTA = params[4].toString();
            String THETA = params[5].toString();
            String LOW_ALPHA = params[6].toString();
            String HIGH_ALPHA = params[7].toString();
            String LOW_BETA = params[8].toString();
            String HIGH_BETA = params[9].toString();
            String LOW_GAMMA = params[10].toString();
            String MID_GAMMA = params[11].toString();
            String ATTENTION = params[12].toString();
            String MEDITATION = params[13].toString();
            Log.d("gagaga",MEDITATION);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String PostData = URLEncoder.encode("user_id","UTF-8")+"="+URLEncoder.encode(user_id,"UTF-8") + "&" +
                    URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8") + "&" +
                    URLEncoder.encode("song","UTF-8")+"="+URLEncoder.encode(song,"UTF-8") + "&" +
                    URLEncoder.encode("DELTA","UTF-8")+"="+URLEncoder.encode(DELTA,"UTF-8")+"&"+
                    URLEncoder.encode("THETA","UTF-8")+"="+URLEncoder.encode(THETA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_ALPHA","UTF-8")+"="+URLEncoder.encode(LOW_ALPHA,"UTF-8")+"&"+
                    URLEncoder.encode("HIGH_ALPHA","UTF-8")+"="+URLEncoder.encode(HIGH_ALPHA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_BETA","UTF-8")+"="+URLEncoder.encode(LOW_BETA,"UTF-8")+"&"+
                    URLEncoder.encode("HIGH_BETA","UTF-8")+"="+URLEncoder.encode(HIGH_BETA,"UTF-8") + "&" +
                    URLEncoder.encode("LOW_GAMMA","UTF-8")+"="+URLEncoder.encode(LOW_GAMMA,"UTF-8")+"&"+
                    URLEncoder.encode("MID_GAMMA","UTF-8")+"="+URLEncoder.encode(MID_GAMMA,"UTF-8")+ "&" +
                    URLEncoder.encode("ATTENTION","UTF-8")+"="+URLEncoder.encode(ATTENTION,"UTF-8") + "&" +
                    URLEncoder.encode("MEDITATION","UTF-8")+"="+URLEncoder.encode(MEDITATION,"UTF-8");
            bufferedWriter.write(PostData);
            Log.d("gagaga",PostData);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "UTF-8"));

            StringBuilder sb1 = new StringBuilder("");
            while ((line = bufferedReader.readLine()) != null) {
                sb1.append(line);
            }
            Log.d("gagaga", sb1.toString());
            bufferedReader.close();inputStream.close();httpURLConnection.disconnect();
        }catch (Exception e){
            Log.d("gagaga",e.getMessage());
        }

    }
    @Override
    protected void onPostExecute(Object o) {


    }
}
