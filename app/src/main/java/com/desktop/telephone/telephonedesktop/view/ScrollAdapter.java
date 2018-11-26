package com.desktop.telephone.telephonedesktop.view;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktop.telephone.telephonedesktop.R;
import com.desktop.telephone.telephonedesktop.bean.DesktopIconBean;
import com.desktop.telephone.telephonedesktop.desktop.Activity.AllAppsActivity;

@SuppressLint("UseSparseArrays")
public class ScrollAdapter implements ScrollLayout.SAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<DesktopIconBean> mList;
    private HashMap<Integer, SoftReference<Drawable>> mCache;

    public ScrollAdapter(Context context, List<DesktopIconBean> list) {

        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

        this.mList = list;
        this.mCache = new HashMap<Integer, SoftReference<Drawable>>();
    }

    @Override
    public View getView(int position) {
        View view = null;
        if (position < mList.size()) {
            final DesktopIconBean moveItem = mList.get(position);
            view = mInflater.inflate(R.layout.item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.content_iv);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
            StateListDrawable states = new StateListDrawable();
            tv_title.setText(moveItem.getTitle());
            int imgUrl = moveItem.getImg_normal();
            int imgUrlDown = moveItem.getImg_pressed();

            Drawable pressed = null;
            Drawable normal = null;
            if(moveItem.getIconType() == 0) {
                byte[] app_icon = moveItem.getApp_icon();
                Bitmap bmp = BitmapFactory.decodeByteArray(app_icon, 0, app_icon.length);
                BitmapDrawable bd = new BitmapDrawable(bmp);
                pressed = bd;
                normal = bd;
            }else {
                SoftReference<Drawable> p = mCache.get(imgUrlDown);
                if (p != null) {
                    pressed = p.get();
                }

                SoftReference<Drawable> n = mCache.get(imgUrl);
                if (n != null) {
                    normal = n.get();
                }

                if (pressed == null) {
                    pressed = mContext.getResources().getDrawable(imgUrlDown);
                    mCache.put(imgUrlDown, new SoftReference<Drawable>(pressed));
                }

                if (normal == null) {
                    normal = mContext.getResources().getDrawable(imgUrl);
                    mCache.put(imgUrl, new SoftReference<Drawable>(normal));
                }
            }




            states.addState(new int[]{android.R.attr.state_pressed}, pressed);
            states.addState(new int[]{android.R.attr.state_focused}, pressed);
            states.addState(new int[]{}, normal);

            //点击事件
            view.findViewById(R.id.rl_item_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (moveItem.getTitle()) {
                        case "电话":
                            break;
                        case "电子相册":
                            break;
                        case "一键拨号":
                            break;
                        case "录音":
                            break;
                        case "黑白名单":
                            break;
                        case "智能通讯录":
                            break;
                        case "通话记录":
                            break;
                        case "所有应用":
                            mContext.startActivity(new Intent(mContext,AllAppsActivity.class));
                            break;
                    }
                }
            });
            iv.setImageDrawable(states);
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

    public void recycleCache() {
        if (mCache != null) {
            Set<Integer> keys = mCache.keySet();
            for (Iterator<Integer> it = keys.iterator(); it.hasNext(); ) {
                Integer key = it.next();
                SoftReference<Drawable> reference = mCache.get(key);
                if (reference != null) {
                    reference.clear();
                }
            }
            mCache.clear();
            mCache = null;
        }
    }
}

