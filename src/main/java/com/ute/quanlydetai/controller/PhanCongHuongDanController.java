package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.PhanCongHuongDan;
import com.ute.quanlydetai.repository.PhanCongHuongDanRepository;
import com.ute.quanlydetai.repository.DeTaiRepository;
import com.ute.quanlydetai.repository.GiangVienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PhanCongHuongDanController {

    private final PhanCongHuongDanRepository phanCongHuongDanRepository;
    private final DeTaiRepository deTaiRepository;
    private final GiangVienRepository giangVienRepository;

    public PhanCongHuongDanController(
            PhanCongHuongDanRepository phanCongHuongDanRepository,
            DeTaiRepository deTaiRepository,
            GiangVienRepository giangVienRepository) {

        this.phanCongHuongDanRepository = phanCongHuongDanRepository;
        this.deTaiRepository = deTaiRepository;
        this.giangVienRepository = giangVienRepository;
    }

    @GetMapping("/phancong")
    public String danhSachPhanCong(Model model) {

        model.addAttribute(
                "dsPhanCong",
                phanCongHuongDanRepository.findAll()
        );

        return "phancong";
    }

    @GetMapping("/phancong/them")
    public String hienThiFormThem(Model model) {

        napDuLieuForm(model);

        return "them-phancong";
    }

    @PostMapping("/phancong/them")
    public String themPhanCong(
            @RequestParam Long deTaiId,
            @RequestParam Long giangVienId,
            @RequestParam String vaiTro,
            @RequestParam(required = false) String ghiChu,
            Model model) {

        if (phanCongHuongDanRepository.countByDeTaiId(deTaiId) >= 2) {

            napDuLieuForm(model);

            model.addAttribute(
                    "loi",
                    "Đề tài này đã có đủ 2 giảng viên hướng dẫn."
            );

            return "them-phancong";
        }

        if (phanCongHuongDanRepository
                .existsByDeTaiIdAndGiangVienId(deTaiId, giangVienId)) {

            napDuLieuForm(model);

            model.addAttribute(
                    "loi",
                    "Giảng viên này đã được phân công hướng dẫn đề tài."
            );

            return "them-phancong";
        }

        PhanCongHuongDan phanCong = new PhanCongHuongDan();

        phanCong.setDeTai(
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"))
        );

        phanCong.setGiangVien(
                giangVienRepository.findById(giangVienId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy giảng viên"))
        );

        phanCong.setVaiTro(vaiTro);
        phanCong.setGhiChu(ghiChu);

        phanCongHuongDanRepository.save(phanCong);

        return "redirect:/phancong";
    }

    @GetMapping("/phancong/sua/{id}")
    public String hienThiFormSua(
            @PathVariable Long id,
            Model model) {

        PhanCongHuongDan phanCong =
                phanCongHuongDanRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy phân công"));

        model.addAttribute("phanCong", phanCong);

        napDuLieuForm(model);

        return "sua-phancong";
    }

    @PostMapping("/phancong/sua/{id}")
    public String suaPhanCong(
            @PathVariable Long id,
            @RequestParam Long deTaiId,
            @RequestParam Long giangVienId,
            @RequestParam String vaiTro,
            @RequestParam(required = false) String ghiChu,
            Model model) {

        PhanCongHuongDan phanCong =
                phanCongHuongDanRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy phân công"));

        boolean doiDeTai =
                !phanCong.getDeTai().getId().equals(deTaiId);

        boolean doiGiangVien =
                !phanCong.getGiangVien().getId().equals(giangVienId);

        if (doiDeTai
                && phanCongHuongDanRepository.countByDeTaiId(deTaiId) >= 2) {

            model.addAttribute(
                    "loi",
                    "Đề tài này đã có đủ 2 giảng viên hướng dẫn."
            );

            model.addAttribute("phanCong", phanCong);
            napDuLieuForm(model);

            return "sua-phancong";
        }

        if ((doiDeTai || doiGiangVien)
                && phanCongHuongDanRepository
                .existsByDeTaiIdAndGiangVienId(deTaiId, giangVienId)) {

            model.addAttribute(
                    "loi",
                    "Giảng viên này đã được phân công hướng dẫn đề tài."
            );

            model.addAttribute("phanCong", phanCong);
            napDuLieuForm(model);

            return "sua-phancong";
        }

        phanCong.setDeTai(
                deTaiRepository.findById(deTaiId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"))
        );

        phanCong.setGiangVien(
                giangVienRepository.findById(giangVienId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy giảng viên"))
        );

        phanCong.setVaiTro(vaiTro);
        phanCong.setGhiChu(ghiChu);

        phanCongHuongDanRepository.save(phanCong);

        return "redirect:/phancong";
    }

    @GetMapping("/phancong/xoa/{id}")
    public String xoaPhanCong(@PathVariable Long id) {

        phanCongHuongDanRepository.deleteById(id);

        return "redirect:/phancong";
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