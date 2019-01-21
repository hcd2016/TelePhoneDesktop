package com.desktop.telephone.telephonedesktop.view.record;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.media.MediaRecorder;
import android.os.Environment;

public class AudioFileFunc {
    //音频输入-麦克风
    public final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 44100;  //44.1KHz,普遍使用的频率
    //录音输出文件
    private final static String AUDIO_RAW_FILENAME = "RawAudio.raw";
    private final static String AUDIO_WAV_FILENAME = "FinalAudio.wav";
    public final static String AUDIO_AMR_FILENAME = ".amr";
    public final static String AUDIO_AMR_BASEPATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/audiorecord/amr/";


    //获取当前时间，以其为名来保存录音
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    /**
     * 判断是否有外部存储设备sdcard
     * @return true | false
     */
    public static boolean isSdcardExit(){
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 获取麦克风输入的原始音频流文件路径
     * @return
     */
    public static String getRawFilePath(){
//        String mAudioRawPath = "";
//        if(isSdcardExit()){
//            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//            mAudioRawPath = fileBasePath+"/"+AUDIO_RAW_FILENAME;
//        }
        return AUDIO_AMR_BASEPATH+AUDIO_AMR_FILENAME;
    }

    /**
     * 获取编码后的WAV格式音频文件路径
     * @return
     */
    public static String getWavFilePath(){
        String mAudioWavPath = "";
        if(isSdcardExit()){
            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mAudioWavPath = fileBasePath+"/"+AUDIO_WAV_FILENAME;
        }
        return mAudioWavPath;
    }


    /**
     * 获取编码后的AMR格式音频文件路径
     * @return
     */
    public static String getAMRFilePath(boolean isFromCall){
//        String mAudioAMRPath = "";
//        if(isSdcardExit()){
//            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath();
//            mAudioAMRPath = fileBasePath+"/"+AUDIO_AMR_FILENAME;
//        }
        if(isFromCall) {
            return AUDIO_AMR_BASEPATH+"call"+getCurrentTime()+AUDIO_AMR_FILENAME;
        }else {
            return AUDIO_AMR_BASEPATH+getCurrentTime()+AUDIO_AMR_FILENAME;
        }
    }


    /**
     * 获取文件大小
     * @param path,文件的绝对路径
     * @return
     */
    public static long getFileSize(String path){
        File mFile = new File(path);
        if(!mFile.exists())
            return -1;
        return mFile.length();
    }

    /**
     * 获取全部amr文件列表
     * @return
     */
    public static List<File> getAmrFiles() {
        List<File> list = new ArrayList<>();
        String fileBasePath = AUDIO_AMR_BASEPATH;

        File rootFile = new File(fileBasePath);
        if (!rootFile.exists()) {
        } else {
            File[] files = rootFile.listFiles();
            for (File file : files) {
                list.add(file);
            }
        }
        return list;
    }
}