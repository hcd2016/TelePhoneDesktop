package com.desktop.telephone.telephonedesktop.util;

import android.os.Environment;

import com.desktop.telephone.telephonedesktop.bean.BlackListInfoBean;
import com.desktop.telephone.telephonedesktop.bean.SystemStatusBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * 黑白名单文件
 */
public class BlackListFileUtil {
    public static String blackFileName = "blacklist_info";
    public static String whiteFileName = "white_info";


    public static String path = Environment.getExternalStorageDirectory().getPath() + "/Wangcaibao/black_phone_list";

    /**
     * 判断文件夹是否存在并创建
     */
    public static void createFlies() {
        File dirFirstFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Wangcaibao/black_phone_list");
        if (!dirFirstFile.exists()) {
            dirFirstFile.mkdirs();
        }
    }

    /**
     * 更新文件内容
     *
     */
    public static void updateFile() {
        List<BlackListInfoBean> list = DaoUtil.getBlackListInfoBeanDao().loadAll();
        if(list == null || list.size() == 0) {
            return;
        }
        createFlies();
        File blackDefaultFile = new File(path, "blacklist_info");
        File blackDefaultFile1 = new File(path, "blacklist_info1");
        File whiteDefaultFile = new File(path, "whitelist_info");
        File whiteDefaultFile1 = new File(path, "whitelist_info1");
        cleanFileContent();//先清空文件内容
        for (BlackListInfoBean blackListInfoBean : list) {
            String phone = blackListInfoBean.getPhone();

            int blackListModeType = DaoUtil.getSystemStatusBeanDao().loadAll().get(0).getBlackListModeType();
            if (blackListModeType == 0) {//当前是普通模式
                if (blackListInfoBean.getType() == 1) {//黑名单号码
                    writeToFile(blackDefaultFile1, phone);
                } else {//白名单号码
                    writeToFile(whiteDefaultFile1, phone);
                }
            } else if (blackListModeType == 1) {//当前是黑名单模式
                if (blackListInfoBean.getType() == 1) {//黑名单号码
                    writeToFile(blackDefaultFile, phone);
                } else {//白名单号码
                    writeToFile(whiteDefaultFile1, phone);
                }
            } else {//当前是白名单模式
                if (blackListInfoBean.getType() == 1) {//黑名单号码
                    writeToFile(blackDefaultFile1, phone);
                } else {//白名单号码
                    writeToFile(whiteDefaultFile, phone);
                }
            }
        }
        DaoUtil.closeDb();
    }


    public static void writeToFile(File file, String content) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write((content+"\r\n").getBytes());
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空文件内容
     */
    public static void cleanFileContent(){
        createFlies();
        File blackDefaultFile = new File(path, "blacklist_info");
        File blackDefaultFile1 = new File(path, "blacklist_info1");
        File whiteDefaultFile = new File(path, "whitelist_info");
        File whiteDefaultFile1 = new File(path, "whitelist_info1");
        if(blackDefaultFile.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(blackDefaultFile, "rwd");
                raf.setLength(0);
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(blackDefaultFile1.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(blackDefaultFile1, "rwd");
                raf.setLength(0);
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(whiteDefaultFile.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(whiteDefaultFile, "rwd");
                raf.setLength(0);
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(whiteDefaultFile1.exists()) {
            try {
                RandomAccessFile raf = new RandomAccessFile(whiteDefaultFile1, "rwd");
                raf.setLength(0);
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 模式设置改变文件名
     */
    public static void setModeChangeFile(int mode) {
        int blackListModeType = DaoUtil.getSystemStatusBeanDao().loadAll().get(0).getBlackListModeType();
        File blackDefaultFile = new File(path, "blacklist_info");
        File blackDefaultFile1 = new File(path, "blacklist_info1");
        File whiteDefaultFile = new File(path, "whitelist_info");
        File whiteDefaultFile1 = new File(path, "whitelist_info1");
        if (mode == 0) {//设置为普通模式
            if (blackListModeType == 0) {//之前为普通模式
                return;
            } else if (blackListModeType == 1) {//之前为黑名单
                blackDefaultFile.renameTo(blackDefaultFile1);
            } else {//之前为白名单
                whiteDefaultFile.renameTo(whiteDefaultFile1);
            }

        } else if (mode == 1) {//设置为黑名单模式
            if (blackListModeType == 0) {//之前为普通模式
                if (blackDefaultFile1.exists()) {
                    blackDefaultFile1.renameTo(blackDefaultFile);
                } else {//之前黑名单文件不存在,创建黑名单
                    try {
                        blackDefaultFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return;
            } else if (blackListModeType == 1) {//之前为黑名单
                return;
            } else {//之前为白名单
                whiteDefaultFile.renameTo(whiteDefaultFile1);
                if (blackDefaultFile1.exists()) {
                    blackDefaultFile1.renameTo(blackDefaultFile);
                } else {
                    try {
                        blackDefaultFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {//设置为白名单模式
            if (blackListModeType == 0) {//之前为普通模式
                if (whiteDefaultFile1.exists()) {
                    whiteDefaultFile1.renameTo(whiteDefaultFile);
                } else {
                    try {
                        whiteDefaultFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (blackListModeType == 1) {//之前为黑名单
                blackDefaultFile.renameTo(blackDefaultFile1);

                if (whiteDefaultFile1.exists()) {
                    whiteDefaultFile1.renameTo(whiteDefaultFile);
                } else {
                    try {
                        whiteDefaultFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {//之前为白名单
                return;
            }
        }
        DaoUtil.closeDb();
    }

}
