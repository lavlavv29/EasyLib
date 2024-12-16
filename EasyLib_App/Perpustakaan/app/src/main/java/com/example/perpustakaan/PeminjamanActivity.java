package com.example.perpustakaan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.perpustakaan.adapters.DBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.perpustakaan.adapters.CustomCursorAdapter;

public class PeminjamanActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, DialogChoice.DialogChoiceListener {
    ListView Is;
    DBHelper dbHelper;
    int listData;
    SharedPreferences viewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_peminjaman);
            // Initialize views and data here
        } catch (Exception e) {
            Log.e("PeminjamanActivity", "Error in onCreate: ", e);
        }
        setContentView(R.layout.activity_peminjaman);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playNotificationSound();

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setContentDescription("Add item");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PeminjamanActivity.this, AddActivity.class));
            }
        });

        dbHelper = new DBHelper(this);
        Is = findViewById(R.id.list_pinjam);
        Is.setOnItemClickListener(this);

        viewData = getSharedPreferences("currentListView", 0);
        listData = viewData.getInt("currentListView", 0);
        setupListView();

    }

    private void playNotificationSound() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        boolean isNotificationsOn = sharedPreferences.getBoolean("notifications", true);

        if (isNotificationsOn) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
        }
    }
    private void setupListView() {
        if (listData == 0){
            allData();
        }else if (listData == 1){
            dataDipinjam();
        }else if (listData == 2){
            dataDikembalikan();
        }
    }

    public void allData(){
        Cursor cursor = dbHelper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(customCursorAdapter);
    }

    public void dataDipinjam(){
        Cursor cursor = dbHelper.dataPinjam();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(customCursorAdapter);
    }

    public void dataDikembalikan(){
        Cursor cursor = dbHelper.dataDikembalikan();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(customCursorAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_peminjaman, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(PeminjamanActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.sort) {
            showSortDialog();
            return true;
        } else if (id == R.id.about_this_app) {
            // Navigate to AboutAppActivity
            Intent intent = new Intent(PeminjamanActivity.this, AboutAppActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String[] sortOptions = {"Name", "Dipinjam", "Dikembalikan", "Date Borrowed", "Date Returned", "Book Name"};
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sort by")
                .setItems(sortOptions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            sortByName();
                            break;
                        case 1:
                            sortByDipinjam();
                            break;
                        case 2:
                            sortByDikembalikan();
                            break;
                        case 3:
                            sortByDateBorrowed();
                            break;
                        case 4:
                            sortByDateReturned();
                            break;
                        case 5:
                            sortByBookName();
                            break;
                    }
                })
                .create()
                .show();
    }

    private void sortByName() {
        Cursor cursor = dbHelper.sortDataByName();
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(adapter);
    }

    private void sortByDipinjam() {
        Cursor cursor = dbHelper.dataPinjam();
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(adapter);
    }

    private void sortByDikembalikan() {
        Cursor cursor = dbHelper.dataDikembalikan();
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(adapter);
    }

    private void sortByDateBorrowed() {
        Cursor cursor = dbHelper.sortDataByDateBorrowed();
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(adapter);
    }

    private void sortByDateReturned() {
        Cursor cursor = dbHelper.sortDataByDateReturned();
        CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(adapter);
    }
    private void sortByBookName() {
        Cursor cursor = dbHelper.sortDataByBookName();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        Is.setAdapter(customCursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long I) {
        TextView getID = view.findViewById(R.id.listID);
        final long rowId = Long.parseLong(getID.getText().toString());
        Intent intent = new Intent(PeminjamanActivity.this, AddActivity.class);
        intent.putExtra(DBHelper.row_id, rowId);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
    }

    @Override
    public void onPositiveButtonClicked(String[] list, int position) {

    }

    @Override
    public void onNegativeButtonClicked() {

    }

}
