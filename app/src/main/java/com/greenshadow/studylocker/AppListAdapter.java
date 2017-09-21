package com.greenshadow.studylocker;


import android.content.pm.ApplicationInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.*;
import android.content.pm.PackageManager;


import java.util.List;

public class AppListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ApplicationInfo> packages;
    private PackageManager packageManager;

    public AppListAdapter(Context context, List<ApplicationInfo> items) {
        mContext = context;
        packageManager = mContext.getPackageManager();
        packages = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return packages.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_layout, parent, false);
        ImageView appIcon = (ImageView)rowView.findViewById(R.id.appIcon);
        TextView appName = (TextView)rowView.findViewById(R.id.appName);
        ApplicationInfo info = (ApplicationInfo) getItem(position);
        appName.setText(packageManager.getApplicationLabel(info).toString());
        appIcon.setImageDrawable(packageManager.getApplicationIcon(info));
        return rowView;
    }

}
