package com.windypermadi.aplikasipembayaransekolah.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.windypermadi.aplikasipembayaransekolah.R;
import com.windypermadi.aplikasipembayaransekolah.admintransaksi.ApprovalActivity;
import com.windypermadi.aplikasipembayaransekolah.admintransaksi.RekapitulasiPembayaranActivity;
import com.windypermadi.aplikasipembayaransekolah.akun.ListAkunActivity;
import com.windypermadi.aplikasipembayaransekolah.helper.SessionManager;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CekKoneksi;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CustomDialog;
import com.windypermadi.aplikasipembayaransekolah.helper.utils.CustomProgressbar;
import com.windypermadi.aplikasipembayaransekolah.kelas.MainKelas;
import com.windypermadi.aplikasipembayaransekolah.periode.ListPeriodeActivity;
import com.windypermadi.aplikasipembayaransekolah.siswa.ListSemuaSiswaAdmin;

import java.io.File;
import java.util.HashMap;

public class MainAdmin extends AppCompatActivity {
    CustomProgressbar customProgress = CustomProgressbar.getInstance();
    CekKoneksi koneksi = new CekKoneksi();
    public SessionManager SessionManager;
    private AppCompatTextView text_nama;
    private AppCompatImageView img_logout;
    public static String iduser, username;
    private CardView cv1, cv2, cv3, cv4, cv5;
    public static String idsekolah = "0001";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        SessionManager = new SessionManager(MainAdmin.this);
        SessionManager.checkLogin();
        HashMap<String, String> user = SessionManager.getUserDetails();
        iduser = user.get(SessionManager.KEY_ID);
        username = user.get(SessionManager.KEY_USERNAME);

        text_nama = findViewById(R.id.text_nama);
        img_logout = findViewById(R.id.img_logout);
        cv1 = findViewById(R.id.cv1);
        cv2 = findViewById(R.id.cv2);
        cv3 = findViewById(R.id.cv3);
        cv4 = findViewById(R.id.cv4);
        cv5 = findViewById(R.id.cv5);

        ActionButton();
        text_nama.setText("Halo " + username);
    }

    private void ActionButton() {
        img_logout.setOnClickListener(v -> logoutUser());
        cv1.setOnClickListener(v -> startActivity(new Intent(MainAdmin.this, MainKelas.class)));
        cv2.setOnClickListener(v -> startActivity(new Intent(MainAdmin.this, ListSemuaSiswaAdmin.class)));
        cv3.setOnClickListener(v -> startActivity(new Intent(MainAdmin.this, ApprovalActivity.class)));
        cv4.setOnClickListener(v -> startActivity(new Intent(MainAdmin.this, ListAkunActivity.class)));
        cv5.setOnClickListener(v -> startActivity(new Intent(MainAdmin.this, ListPeriodeActivity.class)));
    }

    private void logoutUser() {
        clearApplicationData();
        SessionManager.logoutUser();
        finishAffinity();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            assert children != null;
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            assert children != null;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }
}