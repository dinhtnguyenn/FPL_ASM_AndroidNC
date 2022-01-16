package com.dinhnt.asm_androidnc.models;

import java.io.Serializable;

public class KhoaHoc implements Serializable {
    private int id;
    private String tenkh;
    private String lichhoc;
    private String lichthi;
    private boolean check;

    public KhoaHoc(int id, String tenkh, String lichhoc, String lichthi) {
        this.id = id;
        this.tenkh = tenkh;
        this.lichhoc = lichhoc;
        this.lichthi = lichthi;
    }

    public KhoaHoc(int id, String tenkh, String lichhoc, String lichthi, boolean check) {
        this.id = id;
        this.tenkh = tenkh;
        this.lichhoc = lichhoc;
        this.lichthi = lichthi;
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenkh() {
        return tenkh;
    }

    public void setTenkh(String tenkh) {
        this.tenkh = tenkh;
    }

    public String getLichhoc() {
        return lichhoc;
    }

    public void setLichhoc(String lichhoc) {
        this.lichhoc = lichhoc;
    }

    public String getLichthi() {
        return lichthi;
    }

    public void setLichthi(String lichthi) {
        this.lichthi = lichthi;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
