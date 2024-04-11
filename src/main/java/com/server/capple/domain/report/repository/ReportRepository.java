package com.server.capple.domain.report.repository;

import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.question.entity.Question;
import com.server.capple.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Boolean existsReportByAnswer(Answer answer);
}
