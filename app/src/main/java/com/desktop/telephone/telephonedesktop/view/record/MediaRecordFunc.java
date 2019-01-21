package com.desktop.telephone.telephonedesktop.view.record;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.os.Build;

public class MediaRecordFunc {
    private boolean isRecord = false;
    private boolean isFromCall = false;

    private MediaRecorder mMediaRecorder;

    public MediaRecordFunc(boolean isFromCall) {
        this.isFromCall = isFromCall;
    }

//    private static MediaRecordFunc mInstance;

//    public synchronized static MediaRecordFunc getInstance() {
//        if (mInstance == null)
//            mInstance = new MediaRecordFunc();
//        return mInstance;
//    }
//

    public String filePath = "";

    public int startRecordAndFile() {
        //判断是否有外部存储设备sdcard
        if (AudioFileFunc.isSdcardExit()) {
            if (isRecord) {
                return ErrorCode.E_STATE_RECODING;
            } else {
                if (mMediaRecorder == null)
                    createMediaRecord();

                try {
                    mMediaRecorder.prepare();
                    mMediaRecorder.start();
                    // 让录制状态为true
                    isRecord = true;
                    return ErrorCode.SUCCESS;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return ErrorCode.E_UNKOWN;
                }
            }

        } else {
            return ErrorCode.E_NOSDCARD;
        }
    }

    public void pause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaRecorder.pause();
        }
    }

    public void resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMediaRecorder.resume();
        }
    }

    public void stopRecordAndFile() {
        close();
    }

    public long getRecordFileSize() {
        return AudioFileFunc.getFileSize(AudioFileFunc.getAMRFilePath(isFromCall));
    }


    private void createMediaRecord() {
        /* ①Initial：实例化MediaRecorder对象 */
        mMediaRecorder = new MediaRecorder();

        /* setAudioSource/setVedioSource*/
        mMediaRecorder.setAudioSource(AudioFileFunc.AUDIO_INPUT);//设置麦克风

        /* 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default
         * THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
         */
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);

        /* 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default */
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        /* 设置输出文件的路径 */
        //创建目录
        File file = new File(AudioFileFunc.AUDIO_AMR_BASEPATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        filePath = AudioFileFunc.getAMRFilePath(isFromCall);
        File file1 = new File(filePath);
        if (file1.exists()) {
            file1.delete();
        }
        mMediaRecorder.setOutputFile(filePath);
    }


    private void close() {
        if (mMediaRecorder != null) {
            System.out.println("stopRecord");
            isRecord = false;
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    /**
     * 取消录音
     */
    public void canel() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

}