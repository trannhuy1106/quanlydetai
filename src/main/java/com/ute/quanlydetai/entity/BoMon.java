package com.ute.quanlydetai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "bo_mon")
public class BoMon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_bo_mon", nullable = false, unique = true)
    private String maBoMon;

    @Column(name = "ten_bo_mon", nullable = false)
    private String tenBoMon;

    @Column(name = "trang_thai")
    private String trangThai = "HOAT_DONG";

    public BoMon() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaBoMon() {
        return maBoMon;
    }

    public void setMaBoMon(String maBoMon) {
        this.maBoMon = maBoMon;
    }

    public String getTenBoMon() {
        return tenBoMon;
    }

    public void setTenBoMon(String tenBoMon) {
        this.tenBoMon = tenBoMon;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}