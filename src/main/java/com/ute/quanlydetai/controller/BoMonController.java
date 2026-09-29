package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.BoMon;
import com.ute.quanlydetai.repository.BoMonRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bomon")
public class BoMonController {

    private final BoMonRepository boMonRepository;

    public BoMonController(BoMonRepository boMonRepository) {
        this.boMonRepository = boMonRepository;
    }

    @GetMapping
    public String danhSachBoMon(Model model) {

        model.addAttribute(
                "dsBoMon",
                boMonRepository.findAll()
        );

        return "bomon";
    }

    @GetMapping("/them")
    public String hienThiFormThem() {
        return "them-bomon";
    }

    @PostMapping("/them")
    public String themBoMon(
            @RequestParam String maBoMon,
            @RequestParam String tenBoMon,
            @RequestParam String trangThai) {

        BoMon boMon = new BoMon();

        boMon.setMaBoMon(maBoMon);
        boMon.setTenBoMon(tenBoMon);
        boMon.setTrangThai(trangThai);

        boMonRepository.save(boMon);

        return "redirect:/bomon";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(
            @PathVariable Long id,
            Model model) {

        BoMon boMon =
                boMonRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy bộ môn"
                                )
                        );

        model.addAttribute(
                "boMon",
                boMon
        );

        return "sua-bomon";
    }

    @PostMapping("/sua/{id}")
    public String suaBoMon(
            @PathVariable Long id,
            @RequestParam String maBoMon,
            @RequestParam String tenBoMon,
            @RequestParam String trangThai) {

        BoMon boMon =
                boMonRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy bộ môn"
                                )
                        );

        boMon.setMaBoMon(maBoMon);
        boMon.setTenBoMon(tenBoMon);
        boMon.setTrangThai(trangThai);

        boMonRepository.save(boMon);

        return "redirect:/bomon";
    }

    @GetMapping("/xoa/{id}")
    public String xoaBoMon(
            @PathVariable Long id) {

        boMonRepository.deleteById(id);

        return "redirect:/bomon";
    }
}