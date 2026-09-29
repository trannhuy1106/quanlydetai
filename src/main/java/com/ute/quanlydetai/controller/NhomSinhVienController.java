package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.NhomSinhVien;
import com.ute.quanlydetai.repository.NhomSinhVienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class NhomSinhVienController {

    private final NhomSinhVienRepository nhomSinhVienRepository;

    public NhomSinhVienController(
            NhomSinhVienRepository nhomSinhVienRepository) {

        this.nhomSinhVienRepository = nhomSinhVienRepository;
    }

    @GetMapping("/nhom")
    public String danhSachNhom(Model model) {

        model.addAttribute(
                "dsNhom",
                nhomSinhVienRepository.findAll()
        );

        return "nhom";
    }

    @GetMapping("/nhom/them")
    public String hienThiFormThem() {
        return "them-nhom";
    }

    @PostMapping("/nhom/them")
    public String themNhom(
            @ModelAttribute NhomSinhVien nhomSinhVien) {

        nhomSinhVienRepository.save(nhomSinhVien);

        return "redirect:/nhom";
    }

    @GetMapping("/nhom/sua/{id}")
    public String hienThiFormSua(
            @PathVariable Long id,
            Model model) {

        NhomSinhVien nhom =
                nhomSinhVienRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy nhóm"));

        model.addAttribute("nhom", nhom);

        return "sua-nhom";
    }

    @PostMapping("/nhom/sua/{id}")
    public String suaNhom(
            @PathVariable Long id,
            @ModelAttribute NhomSinhVien nhomSinhVien) {

        nhomSinhVien.setId(id);

        nhomSinhVienRepository.save(nhomSinhVien);

        return "redirect:/nhom";
    }

    @GetMapping("/nhom/xoa/{id}")
    public String xoaNhom(@PathVariable Long id) {

        nhomSinhVienRepository.deleteById(id);

        return "redirect:/nhom";
    }
}