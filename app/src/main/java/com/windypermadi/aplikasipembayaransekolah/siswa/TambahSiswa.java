package com.windypermadi.aplikasipembayaransekolah.siswa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.windypermadi.aplikasipembayaransekolah.R;
import com.windypermadi.aplikasipembayaransekolah.helper.Connection;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CekKoneksi;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CustomDialog;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CustomProgressbar;
import com.windypermadi.aplikasipembayaransekolah.kelas.MainKelas;
import com.windypermadi.aplikasipembayaransekolah.kelas.TambahKelas;
import com.windypermadi.aplikasipembayaransekolah.menu.MainAdmin;
import com.windypermadi.aplikasipembayaransekolah.menu.MainSiswa;

import org.json.JSONException;
import org.json.JSONObject;

public class TambahSiswa extends AppCompatActivity {
    CustomProgressbar customProgress = CustomProgressbar.getInstance();
    CekKoneksi koneksi = new CekKoneksi();
    private TextView et_cari;
    String idkelas;
    private EditText et_nis, et_nama, et_jeniskelamin, et_alamat, et_telp, et_nama_ayah, et_nama_ibu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_siswa);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        et_cari = findViewById(R.id.et_cari);
        et_cari.setText("Tambah Siswa");

        et_nis = findViewById(R.id.et_nis);
        et_nama = findViewById(R.id.et_nama);
        et_jeniskelamin = findViewById(R.id.et_jeniskelamin);
        et_alamat = findViewById(R.id.et_alamat);
        et_nama_ayah = findViewById(R.id.et_nama_ayah);
        et_nama_ibu = findViewById(R.id.et_nama_ibu);
        et_telp = findViewById(R.id.et_telp);

        Intent i = getIntent();
        idkelas = i.getStringExtra("idkelas");

        ActionButton();
    }

    private void ActionButton() {
        findViewById(R.id.text_simpan).setOnClickListener(view -> {
            if (koneksi.isConnected(TambahSiswa.this)) {
                TambahData();
            } else {
                CustomDialog.noInternet(TambahSiswa.this);
            }
        });
    }

    private void TambahData() {
        AndroidNetworking.get(Connection.CONNECT + "spp_siswa.php")
                .addQueryParameter("TAG", "tambah")
                .addQueryParameter("idkelas", idkelas)
                .addQueryParameter("nis", et_nis.getText().toString().trim())
                .addQueryParameter("nama", et_nama.getText().toString().trim())
                .addQueryParameter("jenis_kelamin", et_jeniskelamin.getText().toString().trim())
                .addQueryParameter("alamat", et_alamat.getText().toString().trim())
                .addQueryParameter("notelp", et_telp.getText().toString().trim())
                .addQueryParameter("nama_ayah", et_nama_ayah.getText().toString().trim())
                .addQueryParameter("nama_ibu", et_nama_ibu.getText().toString().trim())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        successDialog(TambahSiswa.this, response.optString("pesan"));
                    }

                    @Override
                    public void onError(ANError error) {
                        customProgress.hideProgress();
                        if (error.getErrorCode() == 400) {
                            try {
                                JSONObject body = new JSONObject(error.getErrorBody());
                                CustomDialog.errorDialog(TambahSiswa.this, body.optString("pesan"));
                            } catch (JSONException ignored) {
                            }
                        } else {
                            CustomDialog.errorDialog(TambahSiswa.this, "Sambunganmu dengan server terputus. Periksa sambungan internet, lalu coba lagi.");
                        }
                    }
                });
    }

    public void successDialog(final Context context, final String alertText) {
        final View inflater = LayoutInflater.from(context).inflate(R.layout.custom_success_dialog, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(inflater);
        builder.setCancelable(false);
        final TextView ket = inflater.findViewById(R.id.keterangan);
        ket.setText(alertText);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.transparan);
        inflater.findViewById(R.id.ok).setOnClickListener(v -> {
            Intent i = new Intent(TambahSiswa.this, ListSiswa.class);
            i.putExtra("idkelas", idkelas);
            startActivity(i);
            finish();
            alertDialog.dismiss();
        });
        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
    }
}