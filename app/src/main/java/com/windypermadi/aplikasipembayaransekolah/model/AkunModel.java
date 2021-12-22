package com.windypermadi.aplikasipembayaransekolah.model;

public class AkunModel {
    String iduser;
    String username;
    String nama;
    String status_user;

    public AkunModel(String iduser, String username, String nama, String status_user) {
        this.iduser = iduser;
        this.username = username;
        this.nama = nama;
        this.status_user = status_user;
    }

    public String getIduser() {
        return iduser;
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getStatus_user() {
        return status_user;
    }
}
