package com.greenshadow.studylocker;

import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.content.Context;
import android.view.*;
import android.content.pm.PackageManager;
import android.widget.Switch;
import android.widget.CompoundButton;

import java.util.List;

public class AppListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ApplicationInfo> packages;
    private PackageManager packageManager;

    public AppListAdapter(Context context,List<ApplicationInfo> items) {
        mContext = context;
        packages = items;
        packageManager = mContext.getPackageManager();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return packages.size();
    }

    @Override
    public Object getItem(int position) {
        return packages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.list_item_layout, parent, false);
        ImageView appIcon = (ImageView)rowView.findViewById(R.id.appIcon);
        TextView appName = (TextView)rowView.findViewById(R.id.appName);
        final ApplicationInfo info = (ApplicationInfo) getItem(position);
        final String pName = info.packageName;
        final DBHandler db = new DBHandler(mContext);
        Switch swi = rowView.findViewById(R.id.lockOnOff);
        swi.setChecked(db.isAppPresent(pName));
        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){db.addApp(pName);}
                else{
                    Log.d("DBHelper" ,String.valueOf(db.getAppsCount()) + " before delete");
                    db.deleteApp(pName);
                    Log.d("DBHelper" ,String.valueOf(db.getAppsCount()) + " after delete");
                }
            }
        });
        appName.setText(packageManager.getApplicationLabel(info).toString());
        appIcon.setImageDrawable(packageManager.getApplicationIcon(info));
        return rowView;
    }

    public void setItemList(List<ApplicationInfo> items) {
        this.packages = items;
    }

}
