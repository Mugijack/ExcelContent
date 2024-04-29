package com.example.demo.service;



import com.example.demo.entity.ExcelData;
import com.example.demo.repo.ExcelDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExcelDataService {

    @Autowired
    private ExcelDataRepository excelDataRepository;

    public ExcelData getExcelDataById(Long id) {
        return excelDataRepository.findById(id).orElse(null);
    }

    public ExcelData getExcelDataByCandidateName(String candidateName) {
        return excelDataRepository.findByCandidateName(candidateName);
    }

    public List<ExcelData> getAllExcelData() {
        return excelDataRepository.findAll();
    }

    public void saveExcelData(List<ExcelData> excelDataList) {
        excelDataRepository.saveAll(excelDataList);
    }

    public void syncExcelDataWithDatabase(List<ExcelData> excelDataList) {
        for (ExcelData excelData : excelDataList) {
            ExcelData existingData = getExcelDataByCandidateName(excelData.getCandidate_name());
            if (existingData != null) {
                updateExcelData(excelData);
            } else {
                saveExcelData(List.of(excelData));
            }
        }
    }

    public void deleteExcelData(ExcelData excelData) {
        excelDataRepository.delete(excelData);
    }

    public void updateExcelData(ExcelData excelData) {
        Optional<ExcelData> optionalExcelData = Optional.ofNullable(excelDataRepository.findByCandidateName(excelData.getCandidate_name()));
        if (optionalExcelData.isPresent()) {
            ExcelData existingExcelData = optionalExcelData.get();
            existingExcelData.setJoining_month(excelData.getJoining_month());
            existingExcelData.setPhone_number(excelData.getPhone_number());
            existingExcelData.setReference(excelData.getReference());
            existingExcelData.setBp_name(excelData.getBp_name());
            existingExcelData.setCommunication(excelData.getCommunication());
            existingExcelData.setTechnical(excelData.getTechnical());
            existingExcelData.setStatus(excelData.getStatus());
            existingExcelData.setScholarship(excelData.getScholarship());
            existingExcelData.setEnquired_by(excelData.getEnquired_by());

            excelDataRepository.save(existingExcelData);
        }
    }
    
}