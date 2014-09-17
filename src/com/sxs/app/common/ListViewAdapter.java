package com.sxs.app.common;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sxs.app.braintwisters.R;
import com.sxs.app.data.MenuItemVO;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-21
 * Time: 下午11:19
 * To change this template use File | Settings | File Templates.
 */
public class ListViewAdapter extends BaseAdapter {

    private List<MenuItemVO> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    
    private int[] icons = {R.drawable.menu_01,R.drawable.menu_02,R.drawable.menu_03,R.drawable.menu_04,R.drawable.menu_05,R.drawable.menu_06,R.drawable.menu_07,R.drawable.menu_08,R.drawable.menu_09};

    private static final String TAG = "ListViewAdapter" ;

    public  ListViewAdapter(Context context,List<MenuItemVO> list){
        mInflater = LayoutInflater.from(context);
        this.mDataList = list;
        this.mContext = context;

    }


    @Override
    public int getCount() {
        return  mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MenuItemVO entity = mDataList.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.picIcon = (ImageView)convertView.findViewById(R.id.iconImage) ;
            viewHolder.title = (TextView) convertView
                    .findViewById(R.id.itemText);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(entity.type);
        viewHolder.picIcon.setImageResource(icons[position]);
        return convertView;
    }

    class ViewHolder {

        public ImageView picIcon;
        public TextView title;
    }
}
