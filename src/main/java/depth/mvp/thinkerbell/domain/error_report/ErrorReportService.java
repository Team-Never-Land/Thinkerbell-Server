package depth.mvp.thinkerbell.domain.error_report;

import org.springframework.stereotype.Service;

@Service
public class ErrorReportService {
    private final ErrorReportRepository errorReportRepository;

    public ErrorReportService(ErrorReportRepository errorReportRepository) {
        this.errorReportRepository = errorReportRepository;
    }
    public void saveErrorReport(ErrorReportDTO errorReportDTO) {
        ErrorReport errorReport = new ErrorReport(errorReportDTO.getErrorMessage());
        errorReportRepository.save(errorReport);
    }
}
