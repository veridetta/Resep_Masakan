package com.vrcorp.resepmasakan.db;

public class DBModel {
    private String id;
    private String judul, gambar, url, des, kategori, favorit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }
    public void setJudul(String nama) {
        this.judul = judul;
    }

    public String getGambar() {
        return gambar;
    }
    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }

    public String getKategori() {
        return kategori;
    }
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getFavorit() {
        return favorit;
    }
    public void setFavorit(String favorit) {
        this.favorit = favorit;
    }

}