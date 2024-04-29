package com.example.demo.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.ExcelData;

@Repository
public interface ExcelDataRepository extends JpaRepository<ExcelData, Long> {
    @Query("SELECT e FROM excel_data e WHERE e.candidate_name = :candidateName")
    ExcelData findByCandidateName(@Param("candidateName") String candidateName);
    
}
