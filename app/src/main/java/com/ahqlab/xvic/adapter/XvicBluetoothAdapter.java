package com.ahqlab.xvic.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahqlab.xvic.R;
import com.ahqlab.xvic.domain.BluetoothItem;

import java.util.List;

public class XvicBluetoothAdapter extends BaseExpandableListAdapter {
    private final String TAG = XvicBluetoothAdapter.class.getSimpleName();
    private List<String> mGroupItems;
    private List<List<BluetoothItem>> mItems;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mLayout;

    public XvicBluetoothAdapter(Context context, int layout, List<String> groupItems, List<List<BluetoothItem>> items) {
        mContext = context;
        mLayout = layout;
        mGroupItems = groupItems;
        mItems = items;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mGroupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mItems.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mItems.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
            convertView = mInflater.inflate(R.layout.bluetooth_parent_item, parent, false);
            holder = new ViewHolder();
            holder.groupTitle = convertView.findViewById(R.id.group_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ( holder.groupTitle != null )
            holder.groupTitle.setText(mGroupItems.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if ( convertView == null ) {
            convertView = mInflater.inflate(mLayout, parent, false);
            holder = new ViewHolder();
            holder.pairingImg = convertView.findViewById(R.id.pairing_img);
            holder.btName = convertView.findViewById(R.id.bluetooth_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.btName.setText(mItems.get(groupPosition).get(childPosition).getName());
        if ( mItems.get(groupPosition).get(childPosition).isState() ) {
            holder.pairingImg.setVisibility(View.VISIBLE);
            holder.btName.setTypeface(null, Typeface.BOLD);
        } else {
            holder.pairingImg.setVisibility(View.INVISIBLE);
            holder.btName.setTypeface(null, Typeface.NORMAL);
        }
        Log.e(TAG, String.format("height : %d", convertView.getHeight()));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public class ViewHolder {
        TextView groupTitle;
        ImageView pairingImg;
        TextView btName;
    }
}
