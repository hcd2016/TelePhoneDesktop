package com.desktop.telephone.telephonedesktop.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.desktop.telephone.telephonedesktop.MainActivity;
import com.desktop.telephone.telephonedesktop.base.App;
import com.desktop.telephone.telephonedesktop.bean.ContactsBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsUtil {
    static ContentResolver resolver = App.getContext().getContentResolver();

    /**
     * 获取所有联系人
     */
    public static List<ContactsBean> queryList() {
        List<ContactsBean> list = new ArrayList<>();
        //uri = content://com.android.contacts/contacts
        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
        //获得_id属性
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID}, null, null, null);
        while (cursor.moveToNext()) {
            //获得id并且在data中寻找数据
            int id = cursor.getInt(0);
            delete(id);
            uri = Uri.parse("content://com.android.contacts/contacts/" + id + "/data");
            ContactsBean contactsBean = new ContactsBean((long) id, "", "", "", "",false);
            //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
            Cursor cursor2 = resolver.query(uri, new String[]{ContactsContract.Contacts.Data.DATA1, ContactsContract.Contacts.Data.MIMETYPE}, null, null, null);
            while (cursor2.moveToNext()) {
                String data = cursor2.getString(cursor2.getColumnIndex("data1"));
                if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")) {       //如果是名字
//                    buf.append(",name="+data);
                    contactsBean.setName(data);
                } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")) {  //如果是电话
//                    buf.append(",phone="+data);
                    contactsBean.setPhone(data);
                } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")) {  //如果是email
//                    buf.append(",email="+data);
                    contactsBean.setEmail(data);
                } else if (cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")) { //如果是地址
                    contactsBean.setDesc(data);
                }
            }
//            list.add(contactsBean);
            cursor2.close();
        }
        cursor.close();
        return list;
    }

    public static void delete(long rawContactId) {
        ContentResolver cr = App.getContext().getContentResolver();

//第一步先删除Contacts表中的数据
        cr.delete(ContactsContract.Contacts.CONTENT_URI, ContactsContract.Contacts._ID + " =?", new String[]{rawContactId + ""});
//第二步再删除RawContacts表的数据
        cr.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " =?", new String[]{rawContactId + ""});
    }


//    public void testDelete()throws Exception{
//        String name = "xzdong";
//        //根据姓名求id
//        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
//        ContentResolver resolver = this.getContext().getContentResolver();
//        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Contacts.Data._ID},"display_name=?", new String[]{name}, null);
//        if(cursor.moveToFirst()){
//            int id = cursor.getInt(0);
//            //根据id删除data中的相应数据
//            resolver.delete(uri, "display_name=?", new String[]{name});
//            uri = Uri.parse("content://com.android.contacts/data");
//            resolver.delete(uri, "raw_contact_id=?", new String[]{id+""});
//        }
//    }

    /**
     * 添加联系人
     */
    //一步一步添加数据
    public static void insertData(String name, String phone, String email, String desc) {
        //插入raw_contacts表，并获取_id属性
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        ContentResolver resolver = App.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        long contact_id = ContentUris.parseId(resolver.insert(uri, values));
        //插入data表
        uri = Uri.parse("content://com.android.contacts/data");
        //add Name
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/name");
        values.put("data1", name);
        resolver.insert(uri, values);
        values.clear();
        //add Phone
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
        values.put("data2", "2");   //手机
        values.put("data1", phone);
        resolver.insert(uri, values);
        values.clear();
        //add email
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
        values.put("data2", "2");   //单位
        values.put("data1", email);
        resolver.insert(uri, values);
        values.clear();
        //add email
        values.put("raw_contact_id", contact_id);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, "vnd.android.cursor.item/postal-address_v2");
        values.put("data2", "2");
        values.put("data1", desc);
        resolver.insert(uri, values);
    }

    public static void updateData(Long id, String phone, String name, String desc, String email) throws Exception {
        Uri uri = Uri.parse("content://com.android.contacts/data");//对data表的所有数据操作
        ContentResolver resolver = App.getContext().getContentResolver();
        ContentValues values = new ContentValues();
        values.put("data1", phone);
        resolver.update(uri, values, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/phone_v2", id + ""});
        ContentValues values1 = new ContentValues();
        values1.put("data1", name);
        resolver.update(uri, values1, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/name", id + ""});
        ContentValues values2 = new ContentValues();
        values2.put("data1", email);
        resolver.update(uri, values2, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/email_v2", id + ""});
        ContentValues values3 = new ContentValues();
        values3.put("data1", desc);
        resolver.update(uri, values3, "mimetype=? and raw_contact_id=?", new String[]{"vnd.android.cursor.item/postal-address_v2", id + ""});
    }

    public static List<ContactsBean> query() {
        List<ContactsBean> list = new ArrayList<>();
        ContentResolver resolver = App.getContext().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                int userId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                ContactsBean contactsBean = new ContactsBean((long) userId, "", "", "", "",false);

                Cursor dataCursor = resolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.RAW_CONTACT_ID + "=" + userId, null, null);
                if (dataCursor != null && dataCursor.getCount() > 0) {
                    dataCursor.moveToFirst();
                    while (dataCursor.moveToNext()) {
                        String mimeType = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                        //电话号码
                        if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) {
                            //这里DATA1和DATA4都存储的是电话号码 但是DATA1的数据里面有短横线或者空格
                            //注意 这里phone可能存在多个 后面的内容也有可能为空
                            String phone = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA4));
                            contactsBean.setPhone(phone);
                            //姓名
                        } else if ("vnd.android.cursor.item/name".equals(mimeType)) {
                            //DATA1表示姓名全称
                            String displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                            //DATA2表示名
                            String firstName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA2));
                            //DATA3表示姓
                            String lastName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA3));
                            contactsBean.setName(displayName);
                            //电子邮箱
                        } else if ("vnd.android.cursor.item/email_v2".equals(mimeType)) {
                            String email = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Data.DATA1));
                            contactsBean.setEmail(email);
                        }
                        //这里mimeType有很多种类型 这里就暂时例举了三种 如果想了解更多类型
                        // 可以进入系统/data/data/com.android.providers.contacts/databases/目录下面
                        //提取一个名为contacts2.db的数据库文件 查看mimetypes表和data表
                    }
                }
                if (dataCursor != null) {
                    dataCursor.close();
                }
                list.add(contactsBean);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }


    public static List<ContactsBean> testGetAllContact() throws Throwable {
        List<ContactsBean> list = new ArrayList<>();
        //获取联系人信息的Uri
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        //获取ContentResolver
        ContentResolver contentResolver = App.getContext().getContentResolver();
        //查询数据，返回Cursor
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        String name = "";
        long id = -1;

        while (cursor.moveToNext()) {
//            Map<String,Object> map = new HashMap<String,Object>();
//            StringBuilder sb = new StringBuilder();
            //获取联系人的ID
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人的姓名
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            //构造联系人信息
//            sb.append("contactId=").append(contactId).append(",Name=").append(name);
//            map.put("name", name);
            id = Long.parseLong(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));//联系人ID
            ContactsBean contactsBean = new ContactsBean(id, name, "", "", "",false);
            list.add(contactsBean);
        }


//            //查询电话类型的数据操作
//            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,
//                    null, null);
//            while(phones.moveToNext())
//            {
//                String phoneNumber = phones.getString(phones.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.NUMBER));
//                //添加Phone的信息
//                sb.append(",Phone=").append(phoneNumber);
//                map.put("mobile", phoneNumber);
//            }
//            phones.close();

//            //查询Email类型的数据操作
//            Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,
//                    null, null);
//            while (emails.moveToNext())
//            {
//                String emailAddress = emails.getString(emails.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Email.DATA));
//                //添加Email的信息
//                sb.append(",Email=").append(emailAddress);
//                Log.e("emailAddress", emailAddress);
//                map.put("email", emailAddress);
//
//
//            }
//            emails.close();
        //Log.i("=========ddddddddddd=====", sb.toString());

//            //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
//            Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
//                    null,
//                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
//                    null, null);
//            while (address.moveToNext())
//            {
//                String workAddress = address.getString(address.getColumnIndex(
//                        ContactsContract.CommonDataKinds.StructuredPostal.DATA));
//
//
//                //添加Email的信息
//                sb.append(",address").append(workAddress);
//                map.put("address", workAddress);
//            }
//            address.close();
        //Log.i("=========ddddddddddd=====", sb.toString());

//            //查询==公司名字==类型的数据操作.Organization.COMPANY  ContactsContract.Data.CONTENT_URI
//            String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
//            String[] orgWhereParams = new String[]{id,
//                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
//            Cursor orgCur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
//                    null, orgWhere, orgWhereParams, null);
//            if (orgCur.moveToFirst()) {
//                //组织名 (公司名字)
//                String company = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
//                //职位
//                String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
//                sb.append(",company").append(company);
//                sb.append(",title").append(title);
//                map.put("company", company);
//                map.put("title", title);
//            }
//            orgCur.close();
//            list.add(map);
//            Log.i("=========orgName=====", sb.toString());//查看所有的数据
//            Log.e("=========map=====", map.toString());//有很多数据的时候，只会添加一条  例如邮箱，
//        }

//        Log.i("=========list=====", list.toString());//
        cursor.close();
        return list;
    }


    public static List<ContactsBean> getNameAllPhoneContacts() {
        List<ContactsBean> listContacts = new ArrayList<>();
        ContentResolver cr = App.getContext().getContentResolver();
        String[] mContactsProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String name;
        String contactsId;
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contactsId = cursor.getColumnName(0);
                name = cursor.getString(1);
                ContactsBean contactsBean = new ContactsBean(Long.parseLong(contactsId), name, "", "", "",false);
                listContacts.add(contactsBean);
            }
        }
        return listContacts;
    }

    /**
     * 获取所有拥有手机号的联系人
     *
     * @return
     */
    public static List<ContactsBean> getAllPhoneContacts() {
        List<ContactsBean> listContacts = new ArrayList<>();
        int id = -1;
        ContentResolver cr = App.getContext().getContentResolver();
        long timeStart = new Date().getTime();
        String[] mContactsProjection = new String[]{
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
        };

        String contactsId;
        String phoneNum;
        String name;
        long photoId;
        String email = "";
        String desc = "";
        byte[] photoBytes = null;

        //查询contacts表中的所有数据
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                contactsId = cursor.getString(0);
                phoneNum = cursor.getString(1);
                name = cursor.getString(2);
                photoId = cursor.getLong(3);

                if (photoId > 0) {//有头像
                    Cursor cursorPhoto = cr.query(ContactsContract.RawContactsEntity.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                            ContactsContract.RawContactsEntity.CONTACT_ID + " = ? and " + ContactsContract.RawContactsEntity.MIMETYPE + " = ? and " + ContactsContract.RawContactsEntity.DELETED + " = ?",
                            new String[]{contactsId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, "0"},
                            null);
                    if (cursorPhoto.moveToNext()) {
                        photoBytes = cursorPhoto.getBlob(0);
                    }
                    cursorPhoto.close();
                } else {
                    photoBytes = null;
                }

                //查询Email类型的数据操作
                Cursor emails = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactsId,
                        null, null);
                while (emails.moveToNext()) {
                    String emailAddress = emails.getString(emails.getColumnIndex(
                            ContactsContract.CommonDataKinds.Email.DATA));
                    //添加Email的信息
                    email = emailAddress;
                }
                emails.close();

                //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
                Cursor address = cr.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactsId,
                        null, null);
                while (address.moveToNext()) {
                    String workAddress = address.getString(address.getColumnIndex(
                            ContactsContract.CommonDataKinds.StructuredPostal.DATA));
                    desc = workAddress;
                }
                address.close();

                // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
                phoneNum = phoneNum.replaceAll("^(\\+86)", "");
                phoneNum = phoneNum.replaceAll("^(86)", "");
                phoneNum = phoneNum.replaceAll("-", "");
                phoneNum = phoneNum.replaceAll(" ", "");
                phoneNum = phoneNum.trim();
                ContactsBean contactsBean = new ContactsBean(Long.parseLong(contactsId), name, email, desc, phoneNum,false);
                listContacts.add(contactsBean);
//                // 如果当前号码是手机号码
//                if (CheckUtil.PhoneNumberCheck(phoneNum)) {
//                    UserExaminee user = new UserExaminee();
//                    user.setId(String.valueOf(id));
//                    user.setUsername(name);
//                    user.setPhonenumber(phoneNum);
//                    user.setContactPhoto(photoBytes);
//                    user.setModelType(2);
//                    listContacts.add(user);
//                    id -= 1;
//                }
            }
        }
        cursor.close();
        return listContacts;
    }

    public static List<ContactsBean> getContactsName(Context context) {
        //定义常量，节省重复引用的时间
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        String ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        //临时变量
        String contactId;
        String displayName;
        //生成ContentResolver对象
        ContentResolver contentResolver = context.getContentResolver();
        // 获取手机联系人
        Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/contacts"), null, null, null, null);
        List<ContactsBean> list = new ArrayList<>();
        // 无联系人直接返回
        if (!cursor.moveToFirst()) {//moveToFirst定位到第一行
            return list;
        }
        do {
            // 获得联系人的ID：String类型  列名--》列数--》列内容
            contactId = cursor.getString(cursor.getColumnIndex(ID));
            // 获得联系人姓名：String类型
            displayName = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
//            // 查看联系人有多少个号码，如果没有号码，返回0
//            int phoneCount = cursor.getInt(cursor.getColumnIndex(HAS_PHONE_NUMBER));
            ContactsBean contactsBean = new ContactsBean(Long.parseLong(contactId), displayName, "", "", "",false);
            list.add(contactsBean);
        } while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    //根据cotact_id来获取该联系人的手机号
    public static ContactsBean getDetailFromContactID(Context context, ContactsBean item) {
        Uri CONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Cursor phoneCursor;
        ContentResolver contentResolver = context.getContentResolver();
        if (item.getId() == null) {
            return item;
        }
        phoneCursor = contentResolver.query(CONTENT_URI, null, CONTACT_ID + "=" + item.getId(), null, null);
        if (!phoneCursor.moveToFirst()) {
            return item;
        }
        do {
            String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
            // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
            phoneNum = phoneNum.replaceAll("^(\\+86)", "");
            phoneNum = phoneNum.replaceAll("^(86)", "");
            phoneNum = phoneNum.replaceAll("-", "");
            phoneNum = phoneNum.replaceAll(" ", "");
            phoneNum = phoneNum.trim();
            item.setPhone(phoneNum);
        } while (phoneCursor.moveToNext());
        phoneCursor.close();

        //查询Email类型的数据操作
        Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + item.getId(),
                null, null);
        while (emails.moveToNext()) {
            String emailAddress = emails.getString(emails.getColumnIndex(
                    ContactsContract.CommonDataKinds.Email.DATA));
            item.setEmail(emailAddress);
        }
        emails.close();

        //查询==地址==类型的数据操作.StructuredPostal.TYPE_WORK
        Cursor address = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + item.getId(),
                null, null);
        while (address.moveToNext()) {
            String workAddress = address.getString(address.getColumnIndex(
                    ContactsContract.CommonDataKinds.StructuredPostal.DATA));
            item.setDesc(workAddress);
        }
        address.close();
        return item;
    }


    public static String getDisplayNameByNumber(Context context, String number) {
        String displayName = null;
        Cursor cursor = null;
        String CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;

        try {
            ContentResolver resolver = context.getContentResolver();
            Uri uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon().appendPath(number).build();
            String[] projection = new String[] { CONTACT_ID, DISPLAY_NAME };
            cursor = resolver.query(uri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexName = cursor.getColumnIndex(DISPLAY_NAME);
                displayName = cursor.getString(columnIndexName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return displayName;
    }

    /*
     * 根据电话号码取得联系人姓名
     */
    public static String getContactNameByPhoneNumber(Context context, String phoneNum) {
        String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + phoneNum + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor == null) {
            return null;
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            return name;
        }
        return null;
    }
}
