package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.repository.SinhVienRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.ute.quanlydetai.entity.SinhVien;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class SinhVienController {

    private final SinhVienRepository sinhVienRepository;

    public SinhVienController(SinhVienRepository sinhVienRepository) {
        this.sinhVienRepository = sinhVienRepository;
    }

    @GetMapping("/sinhvien")
    public String danhSachSinhVien(Model model) {
        model.addAttribute("dsSinhVien", sinhVienRepository.findAll());
        return "sinhvien";
    }
    @GetMapping("/sinhvien/them")
public String hienThiFormThemSinhVien() {
    return "them-sinhvien";
}

@PostMapping("/sinhvien/them")
public String themSinhVien(@ModelAttribute SinhVien sinhVien) {
    sinhVienRepository.save(sinhVien);
    return "redirect:/sinhvien";
}
@GetMapping("/sinhvien/xoa/{id}")
public String xoaSinhVien(@PathVariable Long id) {
    sinhVienRepository.deleteById(id);
    return "redirect:/sinhvien";
}

@GetMapping("/sinhvien/sua/{id}")
public String hienThiFormSuaSinhVien(@PathVariable Long id, Model model) {
    SinhVien sinhVien = sinhVienRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên"));

    model.addAttribute("sinhVien", sinhVien);
    return "sua-sinhvien";
}
@PostMapping("/sinhvien/sua/{id}")
public String suaSinhVien(@PathVariable Long id,
                          @ModelAttribute SinhVien sinhVien) {
    sinhVien.setId(id);
    sinhVienRepository.save(sinhVien);
    return "redirect:/sinhvien";
}
}