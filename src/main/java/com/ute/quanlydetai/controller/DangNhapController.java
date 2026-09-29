package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.repository.TaiKhoanRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class DangNhapController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;

    public DangNhapController(
            TaiKhoanRepository taiKhoanRepository,
            PasswordEncoder passwordEncoder) {

        this.taiKhoanRepository = taiKhoanRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/dangnhap")
    public String hienThiDangNhap() {
        return "dangnhap";
    }

    @PostMapping("/dangnhap")
    public String dangNhap(
            @RequestParam String tenDangNhap,
            @RequestParam String matKhau,
            HttpSession session,
            Model model) {

        Optional<TaiKhoan> optional =
                taiKhoanRepository.findByTenDangNhap(tenDangNhap);

        if (optional.isEmpty()) {
            model.addAttribute(
                    "loi",
                    "Tên đăng nhập hoặc mật khẩu không đúng."
            );

            return "dangnhap";
        }

        TaiKhoan taiKhoan = optional.get();

        String matKhauTrongDatabase =
                taiKhoan.getMatKhau();

        boolean dungMatKhau;

        if (matKhauTrongDatabase != null
                && matKhauTrongDatabase.startsWith("$2")) {

            dungMatKhau =
                    passwordEncoder.matches(
                            matKhau,
                            matKhauTrongDatabase
                    );

        } else {

            dungMatKhau =
                    matKhauTrongDatabase != null
                    && matKhauTrongDatabase.equals(matKhau);

            if (dungMatKhau) {

                taiKhoan.setMatKhau(
                        passwordEncoder.encode(matKhau)
                );

                taiKhoanRepository.save(taiKhoan);
            }
        }

        if (!dungMatKhau) {

            model.addAttribute(
                    "loi",
                    "Tên đăng nhập hoặc mật khẩu không đúng."
            );

            return "dangnhap";
        }

        if (!"HOAT_DONG".equals(taiKhoan.getTrangThai())) {

            model.addAttribute(
                    "loi",
                    "Tài khoản hiện không hoạt động."
            );

            return "dangnhap";
        }

        session.setAttribute(
                "taiKhoan",
                taiKhoan
        );

        session.setAttribute(
                "vaiTro",
                taiKhoan.getVaiTro()
        );

        return "redirect:/";
    }

    @GetMapping("/dangxuat")
    public String dangXuat(HttpSession session) {

        session.invalidate();

        return "redirect:/dangnhap";
    }

    @GetMapping("/khong-co-quyen")
    public String khongCoQuyen() {

        return "khong-co-quyen";
    }
}