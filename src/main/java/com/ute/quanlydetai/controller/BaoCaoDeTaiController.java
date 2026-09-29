package com.ute.quanlydetai.controller;

import com.ute.quanlydetai.entity.BaoCaoDeTai;
import com.ute.quanlydetai.entity.DangKyDeTai;
import com.ute.quanlydetai.entity.TaiKhoan;
import com.ute.quanlydetai.entity.ThanhVienNhom;

import com.ute.quanlydetai.repository.BaoCaoDeTaiRepository;
import com.ute.quanlydetai.repository.DangKyDeTaiRepository;
import com.ute.quanlydetai.repository.ThanhVienNhomRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BaoCaoDeTaiController {

    private final BaoCaoDeTaiRepository baoCaoDeTaiRepository;
    private final ThanhVienNhomRepository thanhVienNhomRepository;
    private final DangKyDeTaiRepository dangKyDeTaiRepository;

    private static final String UPLOAD_DIR = "uploads/baocao";

    public BaoCaoDeTaiController(
            BaoCaoDeTaiRepository baoCaoDeTaiRepository,
            ThanhVienNhomRepository thanhVienNhomRepository,
            DangKyDeTaiRepository dangKyDeTaiRepository) {

        this.baoCaoDeTaiRepository = baoCaoDeTaiRepository;
        this.thanhVienNhomRepository = thanhVienNhomRepository;
        this.dangKyDeTaiRepository = dangKyDeTaiRepository;
    }

    @GetMapping("/baocao")
    public String danhSachBaoCao(Model model) {

        model.addAttribute(
                "dsBaoCao",
                baoCaoDeTaiRepository.findAll()
        );

        return "baocao";
    }

    @GetMapping("/baocao/them")
    public String hienThiFormThem(
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"SINH_VIEN".equals(taiKhoan.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Chỉ sinh viên mới được nộp báo cáo."
            );

            return "them-baocao";
        }

        if (taiKhoan.getSinhVien() == null) {

            model.addAttribute(
                    "loi",
                    "Tài khoản chưa được liên kết với sinh viên."
            );

            return "them-baocao";
        }

        ThanhVienNhom thanhVien =
                thanhVienNhomRepository.findBySinhVienId(
                        taiKhoan.getSinhVien().getId()
                );

        if (thanhVien == null) {

            model.addAttribute(
                    "loi",
                    "Sinh viên chưa thuộc nhóm nào."
            );

            return "them-baocao";
        }

        if (!"NHOM_TRUONG".equals(thanhVien.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Chỉ nhóm trưởng mới được nộp báo cáo."
            );

            return "them-baocao";
        }

        Optional<DangKyDeTai> dangKyOptional =
                dangKyDeTaiRepository
                        .findFirstByNhomIdAndTrangThaiOrderByIdDesc(
                                thanhVien.getNhom().getId(),
                                "DA_DUYET"
                        );

        if (dangKyOptional.isEmpty()) {

            model.addAttribute(
                    "loi",
                    "Nhóm chưa có đề tài được duyệt."
            );

            return "them-baocao";
        }

        DangKyDeTai dangKy =
                dangKyOptional.get();

        model.addAttribute(
                "nhom",
                thanhVien.getNhom()
        );

        model.addAttribute(
                "dsDeTai",
                List.of(dangKy.getDeTai())
        );

        return "them-baocao";
    }

    @PostMapping("/baocao/them")
    public String themBaoCao(
            @RequestParam Long deTaiId,
            @RequestParam String tenBaoCao,
            @RequestParam("fileBaoCao") MultipartFile fileBaoCao,
            HttpSession session,
            Model model) {

        TaiKhoan taiKhoan =
                (TaiKhoan) session.getAttribute("taiKhoan");

        if (taiKhoan == null) {
            return "redirect:/dangnhap";
        }

        if (!"SINH_VIEN".equals(taiKhoan.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Chỉ sinh viên mới được nộp báo cáo."
            );

            return "them-baocao";
        }

        if (taiKhoan.getSinhVien() == null) {

            model.addAttribute(
                    "loi",
                    "Tài khoản chưa được liên kết với sinh viên."
            );

            return "them-baocao";
        }

        ThanhVienNhom thanhVien =
                thanhVienNhomRepository.findBySinhVienId(
                        taiKhoan.getSinhVien().getId()
                );

        if (thanhVien == null) {

            model.addAttribute(
                    "loi",
                    "Sinh viên chưa thuộc nhóm nào."
            );

            return "them-baocao";
        }

        if (!"NHOM_TRUONG".equals(thanhVien.getVaiTro())) {

            model.addAttribute(
                    "loi",
                    "Chỉ nhóm trưởng mới được nộp báo cáo."
            );

            return "them-baocao";
        }

        Optional<DangKyDeTai> dangKyOptional =
                dangKyDeTaiRepository
                        .findFirstByNhomIdAndTrangThaiOrderByIdDesc(
                                thanhVien.getNhom().getId(),
                                "DA_DUYET"
                        );

        if (dangKyOptional.isEmpty()) {

            model.addAttribute(
                    "loi",
                    "Nhóm chưa có đề tài được duyệt."
            );

            return "them-baocao";
        }

        DangKyDeTai dangKy =
                dangKyOptional.get();

        model.addAttribute(
                "nhom",
                thanhVien.getNhom()
        );

        model.addAttribute(
                "dsDeTai",
                List.of(dangKy.getDeTai())
        );

        if (!dangKy.getDeTai().getId().equals(deTaiId)) {

            model.addAttribute(
                    "loi",
                    "Bạn chỉ được nộp báo cáo cho đề tài của nhóm mình."
            );

            return "them-baocao";
        }

        if (fileBaoCao == null || fileBaoCao.isEmpty()) {

            model.addAttribute(
                    "loi",
                    "Vui lòng chọn file báo cáo."
            );

            return "them-baocao";
        }

        String tenFileGoc =
                fileBaoCao.getOriginalFilename();

        if (tenFileGoc == null || tenFileGoc.isBlank()) {

            model.addAttribute(
                    "loi",
                    "File báo cáo không hợp lệ."
            );

            return "them-baocao";
        }

        String tenFileThuong =
                tenFileGoc.toLowerCase();

        if (!tenFileThuong.endsWith(".pdf")
                && !tenFileThuong.endsWith(".doc")
                && !tenFileThuong.endsWith(".docx")) {

            model.addAttribute(
                    "loi",
                    "Chỉ chấp nhận file PDF, DOC hoặc DOCX."
            );

            return "them-baocao";
        }

        try {

            Path thuMucUpload =
                    Paths.get(UPLOAD_DIR)
                            .toAbsolutePath()
                            .normalize();

            Files.createDirectories(thuMucUpload);

            String phanMoRong = "";

            int viTriCham =
                    tenFileGoc.lastIndexOf('.');

            if (viTriCham >= 0) {

                phanMoRong =
                        tenFileGoc.substring(viTriCham);
            }

            String tenFileMoi =
                    UUID.randomUUID()
                            + phanMoRong;

            Path duongDanFile =
                    thuMucUpload
                            .resolve(tenFileMoi)
                            .normalize();

            Files.copy(
                    fileBaoCao.getInputStream(),
                    duongDanFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            BaoCaoDeTai baoCao =
                    new BaoCaoDeTai();

            baoCao.setNhom(
                    thanhVien.getNhom()
            );

            baoCao.setDeTai(
                    dangKy.getDeTai()
            );

            baoCao.setTenBaoCao(
                    tenBaoCao
            );

            baoCao.setFileBaoCao(
                    tenFileMoi
            );

            baoCao.setNgayNop(
                    LocalDateTime.now()
            );

            baoCao.setTrangThai(
                    "DA_NOP"
            );

            baoCaoDeTaiRepository.save(
                    baoCao
            );

            return "redirect:/baocao";

        } catch (IOException e) {

            model.addAttribute(
                    "loi",
                    "Không thể tải file báo cáo lên. Vui lòng thử lại."
            );

            return "them-baocao";
        }
    }

    @GetMapping("/baocao/tai/{id}")
    public ResponseEntity<Resource> taiBaoCao(
            @PathVariable Long id) {

        BaoCaoDeTai baoCao =
                baoCaoDeTaiRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Không tìm thấy báo cáo"
                                )
                        );

        try {

            Path filePath =
                    Paths.get(UPLOAD_DIR)
                            .toAbsolutePath()
                            .normalize()
                            .resolve(
                                    baoCao.getFileBaoCao()
                            )
                            .normalize();

            Resource resource =
                    new UrlResource(
                            filePath.toUri()
                    );

            if (!resource.exists()
                    || !resource.isReadable()) {

                return ResponseEntity
                        .notFound()
                        .build();
            }

            return ResponseEntity
                    .ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + resource.getFilename()
                                    + "\""
                    )
                    .body(resource);

        } catch (MalformedURLException e) {

            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @GetMapping("/baocao/xoa/{id}")
    public String xoaBaoCao(
            @PathVariable Long id) {

        BaoCaoDeTai baoCao =
                baoCaoDeTaiRepository.findById(id)
                        .orElse(null);

        if (baoCao != null) {

            if (baoCao.getFileBaoCao() != null
                    && !baoCao.getFileBaoCao().isBlank()) {

                try {

                    Path filePath =
                            Paths.get(UPLOAD_DIR)
                                    .toAbsolutePath()
                                    .normalize()
                                    .resolve(
                                            baoCao.getFileBaoCao()
                                    )
                                    .normalize();

                    Files.deleteIfExists(
                            filePath
                    );

                } catch (IOException e) {

                    System.out.println(
                            "Không thể xóa file: "
                                    + baoCao.getFileBaoCao()
                    );
                }
            }

            baoCaoDeTaiRepository.delete(
                    baoCao
            );
        }

        return "redirect:/baocao";
    }
}