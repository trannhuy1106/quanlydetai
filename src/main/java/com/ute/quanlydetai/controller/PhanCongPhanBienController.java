package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.DeTai;
import com.ute.quanlydetai.entity.GiangVien;
import com.ute.quanlydetai.entity.PhanCongHuongDan;
import com.ute.quanlydetai.entity.PhanCongPhanBien;

import com.ute.quanlydetai.repository.DeTaiRepository;
import com.ute.quanlydetai.repository.GiangVienRepository;
import com.ute.quanlydetai.repository.PhanCongHuongDanRepository;
import com.ute.quanlydetai.repository.PhanCongPhanBienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PhanCongPhanBienController {

    private final PhanCongPhanBienRepository phanCongPhanBienRepository;
    private final PhanCongHuongDanRepository phanCongHuongDanRepository;
    private final DeTaiRepository deTaiRepository;
    private final GiangVienRepository giangVienRepository;

    public PhanCongPhanBienController(
            PhanCongPhanBienRepository phanCongPhanBienRepository,
            PhanCongHuongDanRepository phanCongHuongDanRepository,
            DeTaiRepository deTaiRepository,
            GiangVienRepository giangVienRepository) {

        this.phanCongPhanBienRepository = phanCongPhanBienRepository;
        this.phanCongHuongDanRepository = phanCongHuongDanRepository;
        this.deTaiRepository = deTaiRepository;
        this.giangVienRepository = giangVienRepository;
    }

    @GetMapping("/phanbien")
    public String danhSachPhanBien(Model model) {

        model.addAttribute(
                "dsPhanBien",
                phanCongPhanBienRepository.findAll()
        );

        return "phanbien";
    }

    @GetMapping("/phanbien/them")
    public String hienThiFormThem(Model model) {

        napDuLieuForm(model);

        return "them-phanbien";
    }

    @PostMapping("/phanbien/them")
    public String themPhanBien(
            @RequestParam Long deTaiId,
            @RequestParam Long giangVienId,
            @RequestParam(required = false) String ghiChu,
            Model model) {

        DeTai deTai = deTaiRepository.findById(deTaiId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy đề tài"));

        GiangVien giangVien = giangVienRepository.findById(giangVienId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy giảng viên"));

        boolean daPhanCong =
                phanCongPhanBienRepository
                        .existsByDeTaiIdAndGiangVienId(
                                deTaiId,
                                giangVienId
                        );

        if (daPhanCong) {

            model.addAttribute(
                    "loi",
                    "Giảng viên này đã được phân công phản biện đề tài này."
            );

            napDuLieuForm(model);

            return "them-phanbien";
        }

        List<PhanCongHuongDan> dsHuongDan =
                phanCongHuongDanRepository.findAll();

        boolean laGiangVienHuongDan = false;

        for (PhanCongHuongDan hd : dsHuongDan) {

            if (hd.getDeTai() != null
                    && hd.getGiangVien() != null
                    && hd.getDeTai().getId().equals(deTaiId)
                    && hd.getGiangVien().getId().equals(giangVienId)) {

                laGiangVienHuongDan = true;
                break;
            }
        }

        if (laGiangVienHuongDan) {

            model.addAttribute(
                    "loi",
                    "Không thể phân công! Giảng viên này đang hướng dẫn đề tài."
            );

            napDuLieuForm(model);

            return "them-phanbien";
        }

        PhanCongPhanBien phanBien =
                new PhanCongPhanBien();

        phanBien.setDeTai(deTai);
        phanBien.setGiangVien(giangVien);
        phanBien.setNgayPhanCong(LocalDateTime.now());
        phanBien.setTrangThai("DA_PHAN_CONG");
        phanBien.setGhiChu(ghiChu);

        phanCongPhanBienRepository.save(phanBien);

        return "redirect:/phanbien";
    }

    @GetMapping("/phanbien/xoa/{id}")
    public String xoaPhanBien(@PathVariable Long id) {

        phanCongPhanBienRepository.deleteById(id);

        return "redirect:/phanbien";
    }

    private void napDuLieuForm(Model model) {

        model.addAttribute(
                "dsDeTai",
                deTaiRepository.findAll()
        );

        model.addAttribute(
                "dsGiangVien",
                giangVienRepository.findAll()
        );
    }
}