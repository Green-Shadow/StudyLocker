package com.greenshadow.studylocker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.widget.ProgressBar;

import java.util.List;
import java.util.ArrayList;


public class AppListActivity extends AppCompatActivity {
    private AppListAdapter adpt;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        ListView listView = (ListView) findViewById(R.id.listview);
        adpt = new AppListAdapter(this,new ArrayList<ApplicationInfo>());
        listView.setAdapter(adpt);
        new LoadList().execute();

    }

    private class LoadList extends AsyncTask<Void,Void,List>{
        ProgressBar spinner = (ProgressBar)findViewById(R.id.spinner);
        @Override
        protected void onPreExecute(){
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected List doInBackground(Void... params){
           return llist();
        }

        @Override
        protected void onPostExecute(List l){
            adpt.setItemList(l);
            adpt.notifyDataSetChanged();
            spinner.setVisibility(View.GONE);
        }
    }

    private List llist(){
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<ApplicationInfo> disp_packages = new ArrayList<ApplicationInfo>();
        for(int i =0; i<packages.size(); i++){
            if(getPackageManager().getLaunchIntentForPackage(packages.get(i).packageName) != null){
                disp_packages.add(packages.get(i));
            }
        }
        return disp_packages;
    }
}



