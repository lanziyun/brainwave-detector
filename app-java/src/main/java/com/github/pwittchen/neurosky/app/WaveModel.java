package com.github.pwittchen.neurosky.app;

import android.media.MediaTimestamp;

public class WaveModel {
    String user_id;
    String date;
    String sex;
    String age;
    String song;
    String place;
    String doing;
    String DELTA;
    String THETA;
    String LOW_ALPHA;
    String HIGH_ALPHA;
    String LOW_BETA;
    String HIGH_BETA;
    String LOW_GAMMA;
    String MID_GAMMA;
    String Attention;
    String Meditation;
    String StartMusic;



    public WaveModel(String HIGH_ALPHA, String THETA, String MID_GAMMA, String HIGH_BETA, String DELTA,
                     String LOW_GAMMA, String LOW_BETA, String LOW_ALPHA, String Attention, String Meditation, String StartMusic,String song) {
        this.DELTA = DELTA;
        this.THETA = THETA;
        this.HIGH_ALPHA = HIGH_ALPHA;
        this.LOW_ALPHA = LOW_ALPHA;
        this.HIGH_BETA = HIGH_BETA;
        this.LOW_BETA = LOW_BETA;
        this.LOW_GAMMA = LOW_GAMMA;
        this.MID_GAMMA = MID_GAMMA;
        this.Attention = Attention;
        this.Meditation = Meditation;
        this.StartMusic = StartMusic;
        this.song = song;
    }
    public String getStartMusic() {
        return StartMusic;
    }

    public void setStartMusic(String startMusic) {
        StartMusic = startMusic;
    }
    public String getAttention() {
        return Attention;
    }

    public String getAll() {
        return DELTA + THETA + LOW_ALPHA + HIGH_ALPHA + LOW_BETA + HIGH_BETA + LOW_GAMMA + MID_GAMMA + Attention + Meditation;
    }

    public void setAttention(String attention) {
        Attention = attention;
    }

    public String getMeditation() {
        return Meditation;
    }

    public void setMeditation(String meditation) {
        Meditation = meditation;
    }


    public String getDELTA() {
        return DELTA;
    }

    public void setDELTA(String DELTA) {
        this.DELTA = DELTA;
    }

    public String getTHETA() {
        return THETA;
    }

    public void setTHETA(String THETA) {
        this.THETA = THETA;
    }

    public String getLOW_ALPHA() {
        return LOW_ALPHA;
    }

    public void setLOW_ALPHA(String LOW_ALPHA) {
        this.LOW_ALPHA = LOW_ALPHA;
    }

    public String getHIGH_ALPHA() {
        return HIGH_ALPHA;
    }

    public void setHIGH_ALPHA(String HIGH_ALPHA) {
        this.HIGH_ALPHA = HIGH_ALPHA;
    }

    public String getLOW_BETA() {
        return LOW_BETA;
    }

    public void setLOW_BETA(String LOW_BETA) {
        this.LOW_BETA = LOW_BETA;
    }

    public String getHIGH_BETA() {
        return HIGH_BETA;
    }

    public void setHIGH_BETA(String HIGH_BETA) {
        this.HIGH_BETA = HIGH_BETA;
    }

    public String getLOW_GAMMA() {
        return LOW_GAMMA;
    }

    public void setLOW_GAMMA(String LOW_GAMMA) {
        this.LOW_GAMMA = LOW_GAMMA;
    }

    public String getMID_GAMMA() {
        return MID_GAMMA;
    }

    public void setMID_GAMMA(String MID_GAMMA) {
        this.MID_GAMMA = MID_GAMMA;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDoing() {
        return doing;
    }

    public void setDoing(String doing) {
        this.doing = doing;
    }


    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }
}