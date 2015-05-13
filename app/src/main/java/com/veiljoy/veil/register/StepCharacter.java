package com.veiljoy.veil.register;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.veiljoy.veil.R;
import com.veiljoy.veil.activity.ActivityUserInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqihong on 15/5/12.
 */
public class StepCharacter extends RegisterStep {

    ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
    private Bitmap mAvatar=null;
    static Integer[] icons = new Integer[]{
            R.mipmap.ic_impression_huoyue,
            R.mipmap.ic_impression_chuantong,
            R.mipmap.ic_impression_jiaozhen,
            R.mipmap.ic_impression_humorous,
            R.mipmap.ic_impression_kaifang,
            R.mipmap.ic_impression_lanmang
    };
    int defaultIcon = 2; // 默认头像选择咖啡杯
    private boolean mIsChange = true;
    AvatarGridAdapter saImageItems;

    GridView mGvCharacter;

    public StepCharacter(ActivityUserInfo activity, View contentRootView) {
        super(activity, contentRootView);
        initViews();
        initEvents();
    }
    @Override
    public void initViews() {

        mGvCharacter=(GridView)this.findViewById(R.id.guide_gv_character);
        for (int i = 0; i < icons.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", icons[i]);//添加图像资源的ID
            map.put("ItemText", "NO." + String.valueOf(i));//按序号做ItemText
            lstImageItem.add(map);
        }

        saImageItems= new AvatarGridAdapter(mActivity,
                lstImageItem,
                R.layout.icon_avatar_selection,
                new String[]{"ItemImage"},
                new int[]{R.id.icon_avatar_image});

        mGvCharacter.setAdapter(saImageItems);
        mGvCharacter.setLongClickable(false);
        mGvCharacter.setBackgroundDrawable(new BitmapDrawable() );
        // 默认头像选择咖啡杯
       mGvCharacter.setSelection(defaultIcon);

    }

    @Override
    public void initEvents() {
        mGvCharacter.setOnItemClickListener(new ItemClickListener());
    }

    @Override
    public boolean validate() {

        if (mAvatar==null) {
            showCustomToast("请选择性格");

            return false;
        }
        return true;
    }

    @Override
    public boolean isChange() {
        return false;
    }
    //当AdapterView被单击(触摸屏或者键盘)，则返回的Item单击事件
    class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> arg0,//The AdapterView where the click happened
                                View view,//The view within the AdapterView that was clicked
                                int position,//The position of the view in the adapter
                                long arg3//The row id of the item that was clicked
        ) {

            saImageItems.setSelectedPosition(position);

            saImageItems.notifyDataSetInvalidated();

            //在本例中arg2 = arg3
            HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(position);

            mAvatar = BitmapFactory.decodeResource(mActivity.getResources(), icons[position]);
        }
    }

    class AvatarGridAdapter extends SimpleAdapter {


        LayoutInflater inflater;
        public AvatarGridAdapter(Context context, List<? extends Map<String, ?>> data,
                                 int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            inflater=LayoutInflater.from(context);
        }

        private int selectedPosition = 0;// 选中的位置

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView==null){
                convertView=inflater.inflate(R.layout.register_avatar_icon,null);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.gravity = Gravity.LEFT;
//                convertView.setLayoutParams(lp);
                holder=new ViewHolder();
                holder.icon=(ImageView)convertView.findViewById(R.id.register_avatar_icon);
                holder.icon.setImageResource(icons[position]);
                holder.icon.setLongClickable(false);
                convertView.setTag(holder);
            }
            else{
                holder=(ViewHolder)convertView.getTag();
            }

            if (position == selectedPosition) {
               // holder.icon.setBackgroundResource(R.drawable.bg_register_avatar_selector);
            }
            else{
                holder.icon.setBackgroundResource(0);
            }
            return convertView;
        }

        class ViewHolder{
            ImageView icon;
        }

    }
}
