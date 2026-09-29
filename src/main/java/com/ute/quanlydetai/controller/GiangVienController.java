package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.GiangVien;
import com.ute.quanlydetai.repository.GiangVienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GiangVienController {

    private final GiangVienRepository giangVienRepository;

    public GiangVienController(GiangVienRepository giangVienRepository) {
        this.giangVienRepository = giangVienRepository;
    }

    @GetMapping("/giangvien")
    public String danhSachGiangVien(Model model) {
        model.addAttribute("dsGiangVien", giangVienRepository.findAll());
        return "giangvien";
    }

    @GetMapping("/giangvien/them")
    public String hienThiFormThemGiangVien() {
        return "them-giangvien";
    }

    @PostMapping("/giangvien/them")
    public String themGiangVien(@ModelAttribute GiangVien giangVien) {
        giangVienRepository.save(giangVien);
        return "redirect:/giangvien";
    }

    @GetMapping("/giangvien/sua/{id}")
    public String hienThiFormSuaGiangVien(@PathVariable Long id, Model model) {

        GiangVien giangVien = giangVienRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy giảng viên"));

        model.addAttribute("giangVien", giangVien);

        return "sua-giangvien";
    }

    @PostMapping("/giangvien/sua/{id}")
    public String suaGiangVien(@PathVariable Long id,
                               @ModelAttribute GiangVien giangVien) {

        giangVien.setId(id);
        giangVienRepository.save(giangVien);

        return "redirect:/giangvien";
    }

    @GetMapping("/giangvien/xoa/{id}")
    public String xoaGiangVien(@PathVariable Long id) {

        giangVienRepository.deleteById(id);

        return "redirect:/giangvien";
    }
}