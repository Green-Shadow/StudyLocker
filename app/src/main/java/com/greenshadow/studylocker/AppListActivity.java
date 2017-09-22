package com.greenshadow.studylocker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;

import java.util.List;
import java.util.ArrayList;

public class AppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> disp_packages = new ArrayList<ApplicationInfo>();
        for(int i =0; i<packages.size(); i++){
            if(this.getPackageManager().getLaunchIntentForPackage(packages.get(i).packageName) != null){
                disp_packages.add(packages.get(i));
            }
        }
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new AppListAdapter(this,disp_packages));
    }
    }

