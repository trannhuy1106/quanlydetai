package com.ute.quanlydetai.config;

import com.ute.quanlydetai.entity.TaiKhoan;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("/dangnhap");
            return false;
        }

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            response.sendRedirect("/dangnhap");
            return false;
        }

        String vaiTro = taiKhoan.getVaiTro();
        String uri = request.getRequestURI();

        if (uri.startsWith("/taikhoan")
        && !"ADMIN".equals(vaiTro)) {

    response.sendRedirect("/khong-co-quyen");
    return false;
}

        if ("ADMIN".equals(vaiTro)) {
            return true;
        }

        if ("GIANG_VIEN".equals(vaiTro)) {

            if (uri.startsWith("/phancong")
                    || uri.startsWith("/phanbien")
                    || uri.startsWith("/diem")
                    || uri.startsWith("/detai")
                    || uri.startsWith("/thongbao")
                    || uri.equals("/")) {

                return true;
            }

            response.sendRedirect("/khong-co-quyen");
            return false;
        }

        if ("SINH_VIEN".equals(vaiTro)) {

            if (uri.startsWith("/dangky")
                    || uri.startsWith("/baocao")
                    || uri.startsWith("/diem/ketqua")
                    || uri.startsWith("/detai")
                    || uri.startsWith("/thongbao")
                    || uri.equals("/")) {

                return true;
            }

            response.sendRedirect("/khong-co-quyen");
            return false;
        }

        response.sendRedirect("/khong-co-quyen");
        return false;
    }
}