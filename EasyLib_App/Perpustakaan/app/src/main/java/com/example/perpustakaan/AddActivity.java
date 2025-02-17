package com.example.perpustakaan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perpustakaan.adapters.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    DBHelper dbHelper;
    TextView TvStatus;
    Button BtnProses;
    EditText TxID, TxNama, TxJudul, TxtglPinjam, TxtglKembali, TxStatus;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        TxID = findViewById(R.id.txID);
        TxNama = findViewById(R.id.txNamaAnggota);
        TxJudul = findViewById(R.id.txJudul);
        TxtglPinjam = findViewById(R.id.txPinjam);
        TxtglKembali = findViewById(R.id.txKembali);
        TxStatus = findViewById(R.id.txStatus);

        TvStatus = findViewById(R.id.txStatus);
        BtnProses = findViewById(R.id.btnProses);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        getData();

        TxtglKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        BtnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesKembali();
            }
        });

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);
    }

    private void prosesKembali() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
        builder.setMessage("Proses ke pengembalian buku?");
        builder.setCancelable(true);
        builder.setPositiveButton("Proses", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String kembali = "Dikembalikan";

                ContentValues values = new ContentValues();

                values.put(DBHelper.row_status, kembali);
                dbHelper.updateData(values, id);
                Toast.makeText(AddActivity.this, "Proses Pengembalian Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TxtglKembali.setText(dateFormatter.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData() {
        Calendar c1 = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        String tglPinjam = sdf1.format(c1.getTime());
        TxtglPinjam.setText(tglPinjam);

        Cursor cur = dbHelper.oneData(id);
        if(cur.moveToFirst()){
            @SuppressLint("Range") String idpinjam = cur.getString(cur.getColumnIndex(DBHelper.row_id));
            @SuppressLint("Range") String nama = cur.getString(cur.getColumnIndex(DBHelper.row_nama));
            @SuppressLint("Range") String judul = cur.getString(cur.getColumnIndex(DBHelper.row_judul));
            @SuppressLint("Range") String pinjam = cur.getString(cur.getColumnIndex(DBHelper.row_pinjam));
            @SuppressLint("Range") String kembali = cur.getString(cur.getColumnIndex(DBHelper.row_kembali));
            @SuppressLint("Range") String status = cur.getString(cur.getColumnIndex(DBHelper.row_status));

            TxID.setText(idpinjam);
            TxNama.setText(nama);
            TxJudul.setText(judul);
            TxtglPinjam.setText(pinjam);
            TxtglKembali.setText(kembali);
            TxStatus.setText(status);

            if (TxID.equals("")){
                TvStatus.setVisibility(View.GONE);
                TxStatus.setVisibility(View.GONE);
                BtnProses.setVisibility(View.GONE);
            }else{
                TvStatus.setVisibility(View.VISIBLE);
                TxStatus.setVisibility(View.VISIBLE);
                BtnProses.setVisibility(View.VISIBLE);
            }

            if(status.equals("Dipinjam")){
                BtnProses.setVisibility(View.VISIBLE);
            }else {
                BtnProses.setVisibility(View.GONE);
                TxNama.setEnabled(false);
                TxJudul.setEnabled(false);
                TxtglKembali.setEnabled(false);
                TxStatus.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        String idpinjam = TxID.getText().toString().trim();
        String status = TxStatus.getText().toString().trim();

        MenuItem itemClear = menu.findItem(R.id.action_clear);
        MenuItem itemSave = menu.findItem(R.id.action_save);

        if (idpinjam.equals("")){
            itemClear.setVisible(true);
        }else {
            itemClear.setVisible(false);
        }

        if(status.equals("Dikembalikan")){
            itemSave.setVisible(false);
            itemClear.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            insertAndUpdate();
            return true;
        } else if (item.getItemId() == R.id.action_clear) {
            TxNama.setText("");
            TxJudul.setText("");
            TxtglKembali.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void insertAndUpdate(){
        String idpinjam = TxID.getText().toString().trim();
        String nama = TxNama.getText().toString().trim();
        String judul = TxJudul.getText().toString().trim();
        String tglPinjam = TxtglPinjam.getText().toString().trim();
        String tglKembali = TxtglKembali.getText().toString().trim();
        String status = "Dipinjam";

        ContentValues values = new ContentValues();

        values.put(DBHelper.row_nama, nama);
        values.put(DBHelper.row_judul, judul);
        values.put(DBHelper.row_kembali, tglKembali);
        values.put(DBHelper.row_status, status);

        if (nama.equals("") || judul.equals("") || tglKembali.equals("")){
            Toast.makeText(AddActivity.this, "Isi Data Dengan Lengkap", Toast.LENGTH_SHORT).show();
        }else {
            if(idpinjam.equals("")){
                values.put(DBHelper.row_pinjam, tglPinjam);
                dbHelper.insertData(values);
            }else {
                dbHelper.updateData(values, id);
            }

            Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

