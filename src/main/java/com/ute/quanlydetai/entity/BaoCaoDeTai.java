package com.ute.quanlydetai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bao_cao_de_tai")
public class BaoCaoDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nhom_id", nullable = false)
    private NhomSinhVien nhom;

    @ManyToOne
    @JoinColumn(name = "de_tai_id", nullable = false)
    private DeTai deTai;

    private String tenBaoCao;

    private String fileBaoCao;

    private LocalDateTime ngayNop;

    private String trangThai;

    public BaoCaoDeTai() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NhomSinhVien getNhom() {
        return nhom;
    }

    public void setNhom(NhomSinhVien nhom) {
        this.nhom = nhom;
    }

    public DeTai getDeTai() {
        return deTai;
    }

    public void setDeTai(DeTai deTai) {
        this.deTai = deTai;
    }

    public String getTenBaoCao() {
        return tenBaoCao;
    }

    public void setTenBaoCao(String tenBaoCao) {
        this.tenBaoCao = tenBaoCao;
    }

    public String getFileBaoCao() {
        return fileBaoCao;
    }

    public void setFileBaoCao(String fileBaoCao) {
        this.fileBaoCao = fileBaoCao;
    }

    public LocalDateTime getNgayNop() {
        return ngayNop;
    }

    public void setNgayNop(LocalDateTime ngayNop) {
        this.ngayNop = ngayNop;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}