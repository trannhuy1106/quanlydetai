package com.ute.quanlydetai.service;

import com.ute.quanlydetai.entity.DeTai;
import com.ute.quanlydetai.repository.DeTaiRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeTaiService {

    private final DeTaiRepository deTaiRepository;

    public DeTaiService(DeTaiRepository deTaiRepository) {
        this.deTaiRepository = deTaiRepository;
    }

    public List<DeTai> getAllDeTai() {
        return deTaiRepository.findAll();
    }

    public Optional<DeTai> getDeTaiById(Long id) {
        return deTaiRepository.findById(id);
    }

    public DeTai saveDeTai(DeTai deTai) {
        return deTaiRepository.save(deTai);
    }

    public void deleteDeTai(Long id) {
        deTaiRepository.deleteById(id);
    }
}