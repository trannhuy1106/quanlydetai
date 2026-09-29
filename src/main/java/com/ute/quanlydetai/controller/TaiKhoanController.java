package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.repository.TaiKhoanRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaiKhoanController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    public TaiKhoanController(
            TaiKhoanRepository taiKhoanRepository,
            PasswordEncoder passwordEncoder) {

        this.taiKhoanRepository = taiKhoanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/taikhoan")
    public String danhSachTaiKhoan(Model model) {

        model.addAttribute(
                "dsTaiKhoan",
                taiKhoanRepository.findAll()
        );

        return "taikhoan";
    }

    @GetMapping("/taikhoan/them")
    public String hienThiFormThem() {
        return "them-taikhoan";
    }

    @PostMapping("/taikhoan/them")
    public String themTaiKhoan(
            @RequestParam String tenDangNhap,
            @RequestParam String matKhau,
            @RequestParam String hoTen,
            @RequestParam String vaiTro,
            Model model) {

        if (taiKhoanRepository
                .findByTenDangNhap(tenDangNhap)
                .isPresent()) {

            model.addAttribute(
                    "loi",
                    "Tên đăng nhập đã tồn tại."
            );

            return "them-taikhoan";
        }

        TaiKhoan taiKhoan = new TaiKhoan();

        taiKhoan.setTenDangNhap(tenDangNhap);

        taiKhoan.setMatKhau(
                passwordEncoder.encode(matKhau)
        );

        taiKhoan.setHoTen(hoTen);
        taiKhoan.setVaiTro(vaiTro);
        taiKhoan.setTrangThai("HOAT_DONG");

        taiKhoanRepository.save(taiKhoan);

        return "redirect:/taikhoan";
    }

    @GetMapping("/taikhoan/khoa/{id}")
    public String khoaTaiKhoan(
            @PathVariable Long id,
            Model model) {

        TaiKhoan taiKhoan = taiKhoanRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy tài khoản"
                        ));

        if ("ADMIN".equals(taiKhoan.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Không thể khóa tài khoản quản trị viên."
            );

            model.addAttribute(
                    "dsTaiKhoan",
                    taiKhoanRepository.findAll()
            );

            return "taikhoan";
        }

        taiKhoan.setTrangThai("KHOA");

        taiKhoanRepository.save(taiKhoan);

        return "redirect:/taikhoan";
    }

    @GetMapping("/taikhoan/mo/{id}")
    public String moTaiKhoan(@PathVariable Long id) {

        TaiKhoan taiKhoan = taiKhoanRepository
                .findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy tài khoản"
                        ));

        taiKhoan.setTrangThai("HOAT_DONG");

        taiKhoanRepository.save(taiKhoan);

        return "redirect:/taikhoan";
    }
}