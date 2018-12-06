package com.desktop.telephone.telephonedesktop.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.desktop.telephone.telephonedesktop.bean.AppInfoBean;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.db.DBManager;
import com.desktop.telephone.telephonedesktop.gen.AppInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.BlackListInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.CallRecordBeanDao;
import com.desktop.telephone.telephonedesktop.gen.ContactsBeanDao;
import com.desktop.telephone.telephonedesktop.gen.DesktopIconBeanDao;
import com.desktop.telephone.telephonedesktop.gen.PhotoInfoBeanDao;
import com.desktop.telephone.telephonedesktop.gen.SystemStatusBeanDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DaoUtil {
    public static DesktopIconBeanDao getDesktopIconBeanDao() {
        return DBManager.getInstance().getNewSession().getDesktopIconBeanDao();
    }

    public static AppInfoBeanDao getAppInfoBeanDao() {
        return DBManager.getInstance().getNewSession().getAppInfoBeanDao();
    }

    public static PhotoInfoBeanDao getPhotoInfoBeanDao() {
        return DBManager.getInstance().getNewSession().getPhotoInfoBeanDao();
    }
    public static BlackListInfoBeanDao getBlackListInfoBeanDao() {
        return DBManager.getInstance().getNewSession().getBlackListInfoBeanDao();
    }
    public static SystemStatusBeanDao getSystemStatusBeanDao() {
        return DBManager.getInstance().getNewSession().getSystemStatusBeanDao();
    }
    public static ContactsBeanDao getContactsBeanDao() {
        return DBManager.getInstance().getNewSession().getContactsBeanDao();
    }
    public static CallRecordBeanDao getCallRecordBeanDao() {
        return DBManager.getInstance().getNewSession().getCallRecordBeanDao();
    }

    public static void closeDb() {
        DBManager.getInstance().closeConnection();
    }

    //根据mid排序查询
    public static List<DesktopIconBean> querydata() {
        Query<DesktopIconBean> nQuery = getDesktopIconBeanDao().queryBuilder()
//                .where(UserDao.Properties.Name.eq("user1"))//.where(UserDao.Properties.Id.notEq(999))
                .orderAsc(DesktopIconBeanDao.Properties.Mid)//.limit(5)//orderDesc
                .build();
        return nQuery.list();
    }

    /**
     * 根据查询条件,返回数据列表
     *
     * @param where  条件
     * @param params 参数
     * @return 数据列表
     */
    public List<DesktopIconBean> queryN(String where, String... params) {
        return getDesktopIconBeanDao().queryRaw(where, params);
    }

    /**
     * 批量插入或修改用户信息
     *
     * @param list 用户信息列表
     */
    public static void saveNLists(final List<DesktopIconBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getDesktopIconBeanDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    DesktopIconBean desktopIconBean = list.get(i);
                    desktopIconBean.setMid(i);
                    getDesktopIconBeanDao().insertOrReplace(desktopIconBean);
                }
            }
        });
    }

    /**
     * 批量插入或修改用户信息
     *
     * @param list 用户信息列表
     */
    public static void saveAppInfoBeanNLists(final List<AppInfoBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getDesktopIconBeanDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    AppInfoBean appInfoBean = list.get(i);
                    appInfoBean.setId(i);
                    appInfoBean.setSortId(i);
                    getAppInfoBeanDao().insertOrReplace(appInfoBean);
                }
            }
        });
    }

    //插入一条数据
    public static void insert(DesktopIconBean desktopIconBean) {
        getDesktopIconBeanDao().insert(desktopIconBean);
    }

    /**
     * 根据条件查询
     *
     * @param whereCondition
     * @return
     */
    private List<DesktopIconBean> querydataBy(WhereCondition whereCondition) {////查询条件
        Query<DesktopIconBean> nQuery = getDesktopIconBeanDao().queryBuilder()
                .where(whereCondition)//.where(UserDao.Properties.Id.notEq(999))
                .build();
        return nQuery.list();
    }
    /**
     * 根据条件查询
     *
     * @param whereCondition
     * @return
     */
    public static AppInfoBean querydataByWhere(WhereCondition whereCondition) {////查询条件
        Query<AppInfoBean> nQuery = getAppInfoBeanDao().queryBuilder()
                .where(whereCondition)//.where(UserDao.Properties.Id.notEq(999))
                .build();
        return nQuery.unique();
    }

    /**
     * 删除所有数据
     */
    public void deleteAllNote() {
        getDesktopIconBeanDao().deleteAll();
    }

    /**
     * 删除一条数据
     */
    public void deleteNote(DesktopIconBean desktopIconBean) {
        getDesktopIconBeanDao().delete(desktopIconBean);
    }

    /**
     * 根据用户信息,插件或修改信息
     *
     * @return 插件或修改的用户id
     */
    public long saveN(DesktopIconBean desktopIconBean) {
        return getDesktopIconBeanDao().insertOrReplace(desktopIconBean);
    }

    /**
     * byte[] 转 drawable 用于数据库存储
     *
     * @param bytes
     * @return
     */
    public static BitmapDrawable byteToDrawable(byte[] bytes) {
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        BitmapDrawable bd = new BitmapDrawable(bmp);
        return bd;
    }

    /**
     * drawable 转 byte[] 用于数据库存储
     *
     * @return
     */
    public static byte[] drawableToByte(Drawable drawable) {
        Bitmap bmp = (((BitmapDrawable) drawable).getBitmap());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        return os.toByteArray();
    }

//    private static void getuserById() {
//        User user =getUserDao().load(1l);
//        Log.i("tag", "结果：" + user.getId() + "," + user.getName() + "," + user.getAge() + "," + user.getIsBoy() + ";");
//
//
//    }
//

//
//    private void updatadata() {
//        //更改数据
//        List<User> userss = getUserDao().loadAll();
//        User user = new User(userss.get(0).getId(), "更改后的数据用户", 22, true);
//        getUserDao().update(user);
//
//    }
//
//    private void querydata() {
//        //查询数据详细
//        List<User> users = getUserDao().loadAll();
//        Log.i("tag", "当前数量：" + users.size());
//        for (int i = 0; i < users.size(); i++) {
//            Log.i("tag", "结果：" + users.get(i).getId() + "," + users.get(i).getName() + "," + users.get(i).getAge() + "," + users.get(i).getIsBoy() + ";");
//        }
//    }
//


    //
////        QueryBuilder qb = userDao.queryBuilder();
////        qb.where(Properties.FirstName.eq("Joe"),
////                qb.or(Properties.YearOfBirth.gt(1970),
////                        qb.and(Properties.YearOfBirth.eq(1970), Properties.MonthOfBirth.ge(10))));
////        List youngJoes = qb.list();
//    }
//
//
//    /**
//     * 根据查询条件,返回数据列表
//     * @param where        条件
//     * @param params       参数
//     * @return             数据列表
//     */
//    public List<User> queryN(String where, String... params){
//        return getUserDao().queryRaw(where, params);
//    }
//
//    /**
//     * 根据用户信息,插件或修改信息
//     * @param user              用户信息
//     * @return 插件或修改的用户id
//     */
//    public long saveN(User user){
//        return getUserDao().insertOrReplace(user);
//    }
//
//    /**
//     * 批量插入或修改用户信息
//     * @param list      用户信息列表
//     */
//    public void saveNLists(final List<User> list){
//        if(list == null || list.isEmpty()){
//            return;
//        }
//        getUserDao().getSession().runInTx(new Runnable() {
//            @Override
//            public void run() {
//                for(int i=0; i<list.size(); i++){
//                    User user = list.get(i);
//                    getUserDao().insertOrReplace(user);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 删除所有数据
//     */
//    public void deleteAllNote(){
//        getUserDao().deleteAll();
//    }
//
//    /**
//     * 根据用户类,删除信息
//     * @param user    用户信息类
//     */
//    public void deleteNote(User user){
//        getUserDao().delete(user);
//    }

}
