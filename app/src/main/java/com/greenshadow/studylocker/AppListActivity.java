package com.greenshadow.studylocker;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.ListView;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.widget.ProgressBar;

import java.util.List;
import java.util.ArrayList;


public class AppListActivity extends AppCompatActivity {
    private AppListAdapter adpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ListView listView = (ListView) findViewById(R.id.listview);
        adpt = new AppListAdapter(this, new ArrayList<ApplicationInfo>());
        listView.setAdapter(adpt);
        new LoadList().execute();
        if (!isAccessibilityEnabled(this, "com.greenshadow.studylocker/.AppLockService")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable the accessibility service")
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivityForResult(intent, 0);
                            Intent serviceIntent = new Intent(AppListActivity.this, AppLockService.class);
                            startService(serviceIntent);
                            Log.d("SERVICE", "Service started");
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            builder.show();
        }else{
            Intent serviceIntent = new Intent(AppListActivity.this, AppLockService.class);
            startService(serviceIntent);
            Log.d("SERVICE", "Service started");
        }
    }

    boolean b;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        b = false;
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_lock_button:
                Intent broadcast1 = new Intent("lock_changestate");
                if(!b){
                    b = true;
                    item.setTitle("Unlock");
                }
                else if(b){
                    b = false;
                    item.setTitle("Lock");
                }
                broadcast1.putExtra("state", b);
                sendBroadcast(broadcast1);
            default:
                return super.onOptionsItemSelected(item);
        }
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
            Log.d("Test",l.toString());
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

    public static boolean isAccessibilityEnabled(Context context, String id) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> runningServices = am.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : runningServices) {
            if (id.equals(service.getId())) {
                Log.d("AccService","Service enabled");
                return true;
            }
        }
        return false;
    }
}



