package com.ute.quanlydetai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ket_qua_de_tai")
public class KetQuaDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "de_tai_id", nullable = false, unique = true)
    private DeTai deTai;

    @ManyToOne
    @JoinColumn(name = "chu_tich_id", nullable = false)
    private GiangVien chuTich;

    private Double diemCuoi;

    private LocalDateTime ngayTongHop;

    private String trangThai;

    public KetQuaDeTai() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeTai getDeTai() {
        return deTai;
    }

    public void setDeTai(DeTai deTai) {
        this.deTai = deTai;
    }

    public GiangVien getChuTich() {
        return chuTich;
    }

    public void setChuTich(GiangVien chuTich) {
        this.chuTich = chuTich;
    }

    public Double getDiemCuoi() {
        return diemCuoi;
    }

    public void setDiemCuoi(Double diemCuoi) {
        this.diemCuoi = diemCuoi;
    }

    public LocalDateTime getNgayTongHop() {
        return ngayTongHop;
    }

    public void setNgayTongHop(LocalDateTime ngayTongHop) {
        this.ngayTongHop = ngayTongHop;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}