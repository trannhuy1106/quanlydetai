package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.DotDangKy;
import com.ute.quanlydetai.repository.DotDangKyRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DotDangKyController {

    private final DotDangKyRepository dotDangKyRepository;

    public DotDangKyController(DotDangKyRepository dotDangKyRepository) {
        this.dotDangKyRepository = dotDangKyRepository;
    }

    @GetMapping("/dotdangky")
    public String danhSachDotDangKy(Model model) {
        model.addAttribute("dsDotDangKy", dotDangKyRepository.findAll());
        return "dotdangky";
    }

    @GetMapping("/dotdangky/them")
    public String hienThiFormThem() {
        return "them-dotdangky";
    }

    @PostMapping("/dotdangky/them")
    public String themDotDangKy(@ModelAttribute DotDangKy dotDangKy) {
        dotDangKyRepository.save(dotDangKy);
        return "redirect:/dotdangky";
    }
    @GetMapping("/dotdangky/xoa/{id}")
public String xoaDotDangKy(@PathVariable Long id) {
    dotDangKyRepository.deleteById(id);
    return "redirect:/dotdangky";
}
@GetMapping("/dotdangky/sua/{id}")
public String hienThiFormSua(@PathVariable Long id, Model model) {
    DotDangKy dotDangKy = dotDangKyRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đợt đăng ký"));

    model.addAttribute("dotDangKy", dotDangKy);
    return "sua-dotdangky";
}

@PostMapping("/dotdangky/sua/{id}")
public String suaDotDangKy(@PathVariable Long id,
                           @ModelAttribute DotDangKy dotDangKy) {
    dotDangKy.setId(id);
    dotDangKyRepository.save(dotDangKy);
    return "redirect:/dotdangky";
}
}