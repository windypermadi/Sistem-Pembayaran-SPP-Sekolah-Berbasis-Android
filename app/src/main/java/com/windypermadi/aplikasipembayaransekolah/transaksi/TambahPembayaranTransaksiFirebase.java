package com.windypermadi.aplikasipembayaransekolah.transaksi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.windypermadi.aplikasipembayaransekolah.R;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class TambahPembayaranTransaksiFirebase extends AppCompatActivity implements View.OnClickListener {
    //Deklarasi Variabel untuk Widget
    private Button Upload, UnggahGambar;
    private ImageView ImageContainer;
    private ProgressBar progressBar;

    //Deklarasi Variable StorageReference
    private StorageReference reference;

    //Kode permintaan untuk memilih metode pengambilan gamabr
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_pembayaran_transaksi_firebase);
        Upload = findViewById(R.id.upload);
        Upload.setOnClickListener(this);
        UnggahGambar = findViewById(R.id.select_Image);
        UnggahGambar.setOnClickListener(this);
        ImageContainer = findViewById(R.id.imageContainer);
        progressBar = findViewById(R.id.progressBar);

        //Mendapatkan Referensi dari Firebase Storage
        reference = FirebaseStorage.getInstance().getReference();
    }

    private void uploadImage() {
        ImageContainer.setDrawingCacheEnabled(true);
        ImageContainer.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) ImageContainer.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        //Mengkompress bitmap menjadi JPG dengan kualitas gambar 100%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();

        //Lokasi lengkap dimana gambar akan disimpan
        String namaFile = UUID.randomUUID() + ".jpg";
        String pathImage = "gambar/" + namaFile;
        UploadTask uploadTask = reference.child(pathImage).putBytes(bytes);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TambahPembayaranTransaksiFirebase.this, "Uploading Berhasil", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(TambahPembayaranTransaksiFirebase.this, "Uploading Gagal", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressBar.setProgress((int) progress);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Menghandle hasil data yang diambil dari kamera atau galeri untuk ditampilkan pada ImageView

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility(View.VISIBLE);
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ImageContainer.setImageBitmap(bitmap);
                }
                break;

            case REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    ImageContainer.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    ImageContainer.setImageURI(uri);
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                //Menerapkan kejadian saat tombol upload di klik
                uploadImage();
                break;
            case R.id.select_Image:
                //Menerapkan kejadian saat tombol pilih gambar di klik
                getImage();
                break;
        }
    }

    //Method ini digunakan untuk mengambil gambar dari Kamera
    private void getImage() {
        CharSequence[] menu = {"Kamera", "Galeri"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this)
                .setTitle("Upload Image")
                .setItems(menu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //Mengambil gambar dari Kemara ponsel
                                Intent imageIntentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(imageIntentCamera, REQUEST_CODE_CAMERA);
                                break;

                            case 1:
                                //Mengambil gambar dari galeri
                                Intent imageIntentGallery = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(imageIntentGallery, REQUEST_CODE_GALLERY);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + which);
                        }
                    }
                });
        dialog.create();
        dialog.show();
    }
}