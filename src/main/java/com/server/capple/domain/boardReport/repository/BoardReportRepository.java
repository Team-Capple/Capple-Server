package com.server.capple.domain.boardReport.repository;

import com.server.capple.domain.boardReport.entity.BoardReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReportRepository extends JpaRepository<BoardReport, Long> {
}
