package com.ute.quanlydetai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "de_tai")
public class DeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_de_tai", nullable = false, unique = true)
    private String maDeTai;

    @Column(name = "ten_de_tai", nullable = false)
    private String tenDeTai;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "giang_vien")
    private String giangVien;

    @ManyToOne
    @JoinColumn(name = "bo_mon_id")
    private BoMon boMon;

    @ManyToOne
    @JoinColumn(name = "dot_dang_ky_id")
    private DotDangKy dotDangKy;

    @Column(name = "trang_thai")
    private String trangThai = "CON_TRONG";

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    public DeTai() {
    }

    @PrePersist
    public void prePersist() {
        if (ngayTao == null) {
            ngayTao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaDeTai() {
        return maDeTai;
    }

    public void setMaDeTai(String maDeTai) {
        this.maDeTai = maDeTai;
    }

    public String getTenDeTai() {
        return tenDeTai;
    }

    public void setTenDeTai(String tenDeTai) {
        this.tenDeTai = tenDeTai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(String giangVien) {
        this.giangVien = giangVien;
    }

    public BoMon getBoMon() {
        return boMon;
    }

    public void setBoMon(BoMon boMon) {
        this.boMon = boMon;
    }

    public DotDangKy getDotDangKy() {
        return dotDangKy;
    }

    public void setDotDangKy(DotDangKy dotDangKy) {
        this.dotDangKy = dotDangKy;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}