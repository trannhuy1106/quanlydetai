package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.BoMon;
import com.ute.quanlydetai.entity.DeTai;
import com.ute.quanlydetai.entity.DotDangKy;
import com.ute.quanlydetai.entity.TaiKhoan;

import com.ute.quanlydetai.repository.BoMonRepository;
import com.ute.quanlydetai.repository.DotDangKyRepository;
import com.ute.quanlydetai.service.DeTaiService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DeTaiController {

    private final DeTaiService deTaiService;
    private final BoMonRepository boMonRepository;
    private final DotDangKyRepository dotDangKyRepository;

    public DeTaiController(
            DeTaiService deTaiService,
            BoMonRepository boMonRepository,
            DotDangKyRepository dotDangKyRepository) {

        this.deTaiService = deTaiService;
        this.boMonRepository = boMonRepository;
        this.dotDangKyRepository = dotDangKyRepository;
    }

    @GetMapping("/detai")
    public String danhSachDeTai(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        List<DeTai> dsDeTai =
                deTaiService.getAllDeTai();

        if ("SINH_VIEN".equals(taiKhoan.getVaiTro())) {

            dsDeTai = dsDeTai.stream()
                    .filter(deTai ->
                            "CON_TRONG".equals(deTai.getTrangThai())
                                    || "DA_DANG_KY".equals(deTai.getTrangThai())
                                    || "DANG_THUC_HIEN".equals(deTai.getTrangThai())
                                    || "HOAN_THANH".equals(deTai.getTrangThai())
                    )
                    .toList();
        }

        model.addAttribute(
                "dsDeTai",
                dsDeTai
        );

        return "detai";
    }

    @GetMapping("/detai/them")
    public String hienThiFormThem(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())
                && !"GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            return "redirect:/khong-co-quyen";
        }

        model.addAttribute(
                "dsBoMon",
                boMonRepository.findAll()
        );

        model.addAttribute(
                "dsDotDangKy",
                dotDangKyRepository.findAll()
        );

        model.addAttribute(
                "taiKhoan",
                taiKhoan
        );

        return "them-detai";
    }

    @PostMapping("/detai/them")
    public String themDeTai(
            @RequestParam String maDeTai,
            @RequestParam String tenDeTai,
            @RequestParam(required = false) String moTa,
            @RequestParam Long boMonId,
            @RequestParam(required = false) Long dotDangKyId,
            @RequestParam(required = false) String trangThai,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"ADMIN".equals(taiKhoan.getVaiTro())
                && !"GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            return "redirect:/khong-co-quyen";
        }

        BoMon boMon =
                boMonRepository.findById(boMonId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy bộ môn"
                                )
                        );

        DeTai deTai = new DeTai();

        deTai.setMaDeTai(maDeTai);
        deTai.setTenDeTai(tenDeTai);
        deTai.setMoTa(moTa);
        deTai.setBoMon(boMon);

        if ("GIANG_VIEN".equals(taiKhoan.getVaiTro())) {

            if (taiKhoan.getGiangVien() == null) {

                return hienThiLoiThem(
                        model,
                        taiKhoan,
                        "Tài khoản chưa được liên kết với giảng viên."
                );
            }

            if (dotDangKyId == null) {

                return hienThiLoiThem(
                        model,
                        taiKhoan,
                        "Vui lòng chọn đợt đăng ký."
                );
            }

            DotDangKy dotDangKy =
                    dotDangKyRepository.findById(dotDangKyId)
                            .orElse(null);

            if (dotDangKy == null) {

                return hienThiLoiThem(
                        model,
                        taiKhoan,
                        "Không tìm thấy đợt đăng ký."
                );
            }

            LocalDateTime hienTai =
                    LocalDateTime.now();

            if (hienTai.isBefore(dotDangKy.getGvBatDau())
                    || hienTai.isAfter(dotDangKy.getGvKetThuc())) {

                return hienThiLoiThem(
                        model,
                        taiKhoan,
                        "Hiện không nằm trong thời gian giảng viên đăng ký đề tài."
                );
            }

            deTai.setDotDangKy(dotDangKy);

            deTai.setGiangVien(
                    taiKhoan.getGiangVien().getHoTen()
            );

            deTai.setTrangThai("CHO_DUYET");

        } else {

            if (dotDangKyId != null) {

                DotDangKy dotDangKy =
                        dotDangKyRepository
                                .findById(dotDangKyId)
                                .orElse(null);

                deTai.setDotDangKy(dotDangKy);
            }

            deTai.setGiangVien(null);

            if (trangThai == null
                    || trangThai.isBlank()) {

                deTai.setTrangThai("CON_TRONG");

            } else {

                deTai.setTrangThai(trangThai);
            }
        }

        deTaiService.saveDeTai(deTai);

        return "redirect:/detai";
    }

    @GetMapping("/detai/duyet/{id}")
    public String duyetDeTai(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (!laAdmin(taiKhoan)) {
            return xuLyKhongPhaiAdmin(taiKhoan);
        }

        DeTai deTai =
                deTaiService.getDeTaiById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        if ("CHO_DUYET".equals(deTai.getTrangThai())) {

            deTai.setTrangThai("CON_TRONG");

            deTaiService.saveDeTai(deTai);
        }

        return "redirect:/detai";
    }

    @GetMapping("/detai/tuchoi/{id}")
    public String tuChoiDeTai(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (!laAdmin(taiKhoan)) {
            return xuLyKhongPhaiAdmin(taiKhoan);
        }

        DeTai deTai =
                deTaiService.getDeTaiById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        if ("CHO_DUYET".equals(deTai.getTrangThai())) {

            deTai.setTrangThai("TU_CHOI");

            deTaiService.saveDeTai(deTai);
        }

        return "redirect:/detai";
    }

    @GetMapping("/detai/sua/{id}")
    public String hienThiFormSua(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (!laAdmin(taiKhoan)) {
            return xuLyKhongPhaiAdmin(taiKhoan);
        }

        DeTai deTai =
                deTaiService.getDeTaiById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        model.addAttribute(
                "deTai",
                deTai
        );

        model.addAttribute(
                "dsBoMon",
                boMonRepository.findAll()
        );

        model.addAttribute(
                "dsDotDangKy",
                dotDangKyRepository.findAll()
        );

        return "sua-detai";
    }

    @PostMapping("/detai/sua")
    public String suaDeTai(
            @RequestParam Long id,
            @RequestParam String maDeTai,
            @RequestParam String tenDeTai,
            @RequestParam(required = false) String moTa,
            @RequestParam Long boMonId,
            @RequestParam(required = false) Long dotDangKyId,
            @RequestParam(required = false) String giangVien,
            @RequestParam(required = false) String trangThai,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (!laAdmin(taiKhoan)) {
            return xuLyKhongPhaiAdmin(taiKhoan);
        }

        DeTai deTai =
                deTaiService.getDeTaiById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy đề tài"
                                )
                        );

        BoMon boMon =
                boMonRepository.findById(boMonId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy bộ môn"
                                )
                        );

        deTai.setMaDeTai(maDeTai);
        deTai.setTenDeTai(tenDeTai);
        deTai.setMoTa(moTa);
        deTai.setBoMon(boMon);

        if (dotDangKyId != null) {

            DotDangKy dotDangKy =
                    dotDangKyRepository
                            .findById(dotDangKyId)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Không tìm thấy đợt đăng ký"
                                    )
                            );

            deTai.setDotDangKy(dotDangKy);

        } else {

            deTai.setDotDangKy(null);
        }

        if (giangVien != null) {
            deTai.setGiangVien(giangVien);
        }

        if (trangThai != null
                && !trangThai.isBlank()) {

            deTai.setTrangThai(trangThai);
        }

        deTaiService.saveDeTai(deTai);

        return "redirect:/detai";
    }

    @GetMapping("/detai/xoa/{id}")
    public String xoaDeTai(
            @PathVariable Long id,
            HttpSession session) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (!laAdmin(taiKhoan)) {
            return xuLyKhongPhaiAdmin(taiKhoan);
        }

        deTaiService.deleteDeTai(id);

        return "redirect:/detai";
    }

    private String hienThiLoiThem(
            Model model,
            TaiKhoan taiKhoan,
            String loi) {

        model.addAttribute(
                "dsBoMon",
                boMonRepository.findAll()
        );

        model.addAttribute(
                "dsDotDangKy",
                dotDangKyRepository.findAll()
        );

        model.addAttribute(
                "taiKhoan",
                taiKhoan
        );

        model.addAttribute(
                "loi",
                loi
        );

        return "them-detai";
    }

    private boolean laAdmin(
            TaiKhoan taiKhoan) {

        return taiKhoan != null
                && "ADMIN".equals(
                        taiKhoan.getVaiTro()
                );
    }

    private String xuLyKhongPhaiAdmin(
            TaiKhoan taiKhoan) {

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        return "redirect:/khong-co-quyen";
    }
}