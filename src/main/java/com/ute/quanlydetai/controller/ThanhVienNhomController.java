package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.ThanhVienNhom;
import com.ute.quanlydetai.repository.ThanhVienNhomRepository;
import com.ute.quanlydetai.repository.NhomSinhVienRepository;
import com.ute.quanlydetai.repository.SinhVienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ThanhVienNhomController {

    private final ThanhVienNhomRepository thanhVienNhomRepository;
    private final NhomSinhVienRepository nhomSinhVienRepository;
    private final SinhVienRepository sinhVienRepository;

    public ThanhVienNhomController(
            ThanhVienNhomRepository thanhVienNhomRepository,
            NhomSinhVienRepository nhomSinhVienRepository,
            SinhVienRepository sinhVienRepository) {

        this.thanhVienNhomRepository = thanhVienNhomRepository;
        this.nhomSinhVienRepository = nhomSinhVienRepository;
        this.sinhVienRepository = sinhVienRepository;
    }

    @GetMapping("/thanhviennhom")
    public String danhSachThanhVien(Model model) {

        model.addAttribute(
                "dsThanhVien",
                thanhVienNhomRepository.findAll()
        );

        return "thanhviennhom";
    }

    @GetMapping("/thanhviennhom/them")
    public String hienThiFormThem(Model model) {

        model.addAttribute(
                "dsNhom",
                nhomSinhVienRepository.findAll()
        );

        model.addAttribute(
                "dsSinhVien",
                sinhVienRepository.findAll()
        );

        return "them-thanhviennhom";
    }

    @PostMapping("/thanhviennhom/them")
    public String themThanhVien(
            @RequestParam Long nhomId,
            @RequestParam Long sinhVienId,
            @RequestParam String vaiTro,
            Model model) {

        if (thanhVienNhomRepository.countByNhomId(nhomId) >= 3) {

            napDuLieuForm(model);

            model.addAttribute(
                    "loi",
                    "Nhóm đã đủ 3 thành viên."
            );

            return "them-thanhviennhom";
        }

        if (thanhVienNhomRepository.existsBySinhVienId(sinhVienId)) {

            napDuLieuForm(model);

            model.addAttribute(
                    "loi",
                    "Sinh viên này đã thuộc một nhóm."
            );

            return "them-thanhviennhom";
        }

        if ("NHOM_TRUONG".equals(vaiTro)
                && thanhVienNhomRepository
                .existsByNhomIdAndVaiTro(nhomId, "NHOM_TRUONG")) {

            napDuLieuForm(model);

            model.addAttribute(
                    "loi",
                    "Nhóm này đã có nhóm trưởng."
            );

            return "them-thanhviennhom";
        }

        ThanhVienNhom thanhVien = new ThanhVienNhom();

        thanhVien.setNhom(
                nhomSinhVienRepository.findById(nhomId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy nhóm"))
        );

        thanhVien.setSinhVien(
                sinhVienRepository.findById(sinhVienId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy sinh viên"))
        );

        thanhVien.setVaiTro(vaiTro);

        thanhVienNhomRepository.save(thanhVien);

        return "redirect:/thanhviennhom";
    }

    @GetMapping("/thanhviennhom/xoa/{id}")
    public String xoaThanhVien(@PathVariable Long id) {

        thanhVienNhomRepository.deleteById(id);

        return "redirect:/thanhviennhom";
    }

    private void napDuLieuForm(Model model) {

        model.addAttribute(
                "dsNhom",
                nhomSinhVienRepository.findAll()
        );

        model.addAttribute(
                "dsSinhVien",
                sinhVienRepository.findAll()
        );
    }
}