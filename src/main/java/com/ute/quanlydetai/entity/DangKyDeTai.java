package com.ute.quanlydetai.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dang_ky_de_tai")
public class DangKyDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @ManyToOne
@JoinColumn(name = "sinh_vien_id")
private SinhVien sinhVien;

@ManyToOne
@JoinColumn(name = "nhom_id")
private NhomSinhVien nhom;

    @ManyToOne
    @JoinColumn(name = "de_tai_id", nullable = false)
    private DeTai deTai;

    @ManyToOne
    @JoinColumn(name = "dot_dang_ky_id", nullable = false)
    private DotDangKy dotDangKy;

    @Column(name = "trang_thai")
    private String trangThai = "CHO_DUYET";

    @Column(name = "ghi_chu")
    private String ghiChu;

    public DangKyDeTai() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public DeTai getDeTai() {
        return deTai;
    }

    public void setDeTai(DeTai deTai) {
        this.deTai = deTai;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    public NhomSinhVien getNhom() {
    return nhom;
}

public void setNhom(NhomSinhVien nhom) {
    this.nhom = nhom;
}
}