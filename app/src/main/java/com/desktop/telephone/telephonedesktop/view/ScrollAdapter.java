package com.desktop.telephone.telephonedesktop.view;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.AllAppsActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.BlacklistActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.CallActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.ContactsListActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.PhotosActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.RecordAudioActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.SosActivity;
import com.desktop.telephone.telephonedesktop.desktop.Activity.NewMainActivity;
import com.desktop.telephone.telephonedesktop.util.CallUtil;
import com.desktop.telephone.telephonedesktop.util.Utils;

@SuppressLint("UseSparseArrays")
public class ScrollAdapter implements ScrollLayout.SAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<DesktopIconBean> mList;
//    private HashMap<Integer, SoftReference<Drawable>> mCache;

    public ScrollAdapter(Context context, List<DesktopIconBean> list) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        this.mList = list;
//        this.mCache = new HashMap<Integer, SoftReference<Drawable>>();
    }

    @Override
    public View getView(int position) {
        View view = null;
        if (position < mList.size()) {
            final DesktopIconBean moveItem = mList.get(position);
            view = mInflater.inflate(R.layout.item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.content_iv);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            RelativeLayout rl_item_container = (RelativeLayout) view.findViewById(R.id.rl_item_container);
//            StateListDrawable states = new StateListDrawable();
            tv_title.setText(moveItem.getTitle());
            int imgUrl = Utils.getAppIconId(moveItem.getImg_id_name());
            rl_item_container.setBackgroundColor(moveItem.getIconBgColor());

            if (moveItem.getIconType() == 0 || moveItem.getIconType() == 2) {
                byte[] app_icon = moveItem.getApp_icon();
                Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                BitmapDrawable bd = new BitmapDrawable(bmp);
                iv.setImageDrawable(bd);
            } else if (moveItem.getIconType() == 3) {//一键拨号
                iv.setImageResource(R.drawable.one_key);
            } else {
                iv.setImageResource(imgUrl);

//                SoftReference<Drawable> p = mCache.get(imgUrlDown);
//                if (p != null) {
//                    pressed = p.get();
//                }
//
//                SoftReference<Drawable> n = mCache.get(imgUrl);
//                if (n != null) {
//                    normal = n.get();
//                }
//
//                if (pressed == null) {
//                    pressed = mContext.getResources().getDrawable(imgUrlDown);
//                    mCache.put(imgUrlDown, new SoftReference<Drawable>(pressed));
//                }
//
//                if (normal == null) {
//                    normal = mContext.getResources().getDrawable(imgUrl);
//                    mCache.put(imgUrl, new SoftReference<Drawable>(normal));
//                }
            }


//            states.addState(new int[]{android.R.attr.state_pressed}, pressed);
//            states.addState(new int[]{android.R.attr.state_focused}, pressed);
//            states.addState(new int[]{}, normal);

            //点击事件
            view.findViewById(R.id.rl_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (moveItem.getIconType() == 1) {//自定义应用指定跳转
                        switch (moveItem.getTitle()) {
                            case "电话":
//                                CallActivity.startActivity(0,mContext);
                                mContext.startActivity(new Intent(mContext,NewMainActivity.class));
                                break;
                            case "电子相册":
                                mContext.startActivity(new Intent(mContext, PhotosActivity.class));
                                break;
//                            case "一键拨号":
//                                break;
                            case "SOS":
                                mContext.startActivity(new Intent(mContext, SosActivity.class));
                                break;
                            case "录音":
                                mContext.startActivity(new Intent(mContext, RecordAudioActivity.class));
                                break;
                            case "黑白名单":
                                mContext.startActivity(new Intent(mContext, BlacklistActivity.class));
                                break;
                            case "智能通讯录":
                                mContext.startActivity(new Intent(mContext, ContactsListActivity.class));
                                break;
                            case "通话记录":
                                CallActivity.startActivity(1,mContext);
                                break;
                            case "所有应用":
                                mContext.startActivity(new Intent(mContext, AllAppsActivity.class));
                                break;
                        }
                    } else if (moveItem.getIconType() == 3) {//一键拨号
                        String phoneNum = moveItem.getPhoneNum();
//                        String name = moveItem.getTitle();
                        CallUtil.call(mContext,phoneNum);
//                        Utils.Toast("一键拨号点击");
                    } else {//系统或用户程序跳转
                        Utils.startApp(mContext, moveItem.getPackageName());
                    }
                }
            });
//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//
//                    return oni;
//                }
//            });
//            iv.setImageDrawable(states);
            view.setTag(moveItem);
        }
        return view;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public void exchange(int oldPosition, int newPositon) {
        DesktopIconBean item = this.mList.get(oldPosition);
        mList.remove(oldPosition);
        mList.add(newPositon, item);
    }

    private OnDataChangeListener dataChangeListener = null;


    public OnDataChangeListener getOnDataChangeListener() {
        return dataChangeListener;
    }

    public void setOnDataChangeListener(OnDataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public void delete(int position) {
        if (position < getCount()) {
            mList.remove(position);
        }
    }

    public void add(DesktopIconBean item) {
        mList.add(item);
    }

    public DesktopIconBean getMoveItem(int position) {
        return mList.get(position);
    }

//    public void recycleCache() {
//        if (mCache != null) {
//            Set<Integer> keys = mCache.keySet();
//            for (Iterator<Integer> it = keys.iterator(); it.hasNext(); ) {
//                Integer key = it.next();
//                SoftReference<Drawable> reference = mCache.get(key);
//                if (reference != null) {
//                    reference.clear();
//                }
//            }
//            mCache.clear();
//            mCache = null;
//        }
//    }
}

