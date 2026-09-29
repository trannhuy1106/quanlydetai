package com.ute.quanlydetai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dot_dang_ky")
public class DotDangKy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_dot", nullable = false)
    private String tenDot;

    @Column(name = "loai", nullable = false)
    private String loai;

    @Column(name = "gv_bat_dau", nullable = false)
    private LocalDateTime gvBatDau;

    @Column(name = "gv_ket_thuc", nullable = false)
    private LocalDateTime gvKetThuc;

    @Column(name = "sv_bat_dau", nullable = false)
    private LocalDateTime svBatDau;

    @Column(name = "sv_ket_thuc", nullable = false)
    private LocalDateTime svKetThuc;

    @Column(name = "han_gvpb_nop_diem")
    private LocalDateTime hanGvpbNopDiem;

    @Column(name = "ngay_bao_cao_hoi_dong")
    private LocalDateTime ngayBaoCaoHoiDong;

    @Column(name = "trang_thai")
    private String trangThai = "CHUA_MO";

    public DotDangKy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDot() {
        return tenDot;
    }

    public void setTenDot(String tenDot) {
        this.tenDot = tenDot;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public LocalDateTime getGvBatDau() {
        return gvBatDau;
    }

    public void setGvBatDau(LocalDateTime gvBatDau) {
        this.gvBatDau = gvBatDau;
    }

    public LocalDateTime getGvKetThuc() {
        return gvKetThuc;
    }

    public void setGvKetThuc(LocalDateTime gvKetThuc) {
        this.gvKetThuc = gvKetThuc;
    }

    public LocalDateTime getSvBatDau() {
        return svBatDau;
    }

    public void setSvBatDau(LocalDateTime svBatDau) {
        this.svBatDau = svBatDau;
    }

    public LocalDateTime getSvKetThuc() {
        return svKetThuc;
    }

    public void setSvKetThuc(LocalDateTime svKetThuc) {
        this.svKetThuc = svKetThuc;
    }

    public LocalDateTime getHanGvpbNopDiem() {
        return hanGvpbNopDiem;
    }

    public void setHanGvpbNopDiem(LocalDateTime hanGvpbNopDiem) {
        this.hanGvpbNopDiem = hanGvpbNopDiem;
    }

    public LocalDateTime getNgayBaoCaoHoiDong() {
        return ngayBaoCaoHoiDong;
    }

    public void setNgayBaoCaoHoiDong(LocalDateTime ngayBaoCaoHoiDong) {
        this.ngayBaoCaoHoiDong = ngayBaoCaoHoiDong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}