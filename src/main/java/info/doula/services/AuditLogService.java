package info.doula.services;

import com.doulat.administrator.domain.AuditLog;
import com.doulat.administrator.repository.AuditLogRepository;
import info.doula.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by saad on 10/5/2016.
 */
@Service
public class AuditLogService {
    private static final Logger logger =
            LoggerFactory.getLogger(AuditLogService.class);
    @Autowired
    private AuditLogRepository auditLogDao;

    public Page<AuditLog> findAllLogs(String query, String fromDate, String toDate, Pageable pageable) throws Exception {
        Page<AuditLog> page = auditLogDao.findAllByOrderByIdDesc(pageable);
        //Search with string
        if (query != null && fromDate == null && toDate == null) {
            page = auditLogDao.searchWithJPQLQueryForString(query, pageable);
        }
        //Search with string and dates
        else if (query != null && fromDate != null && toDate != null) {
            page = auditLogDao.searchWithJPQLQueryForStringWithDateRange(query, DateUtils.formatDate(fromDate), DateUtils.formatDate(toDate), pageable);
        }
        //Search with Two dates
        else if (fromDate != null && toDate != null) {
            page = auditLogDao.searchWithJPQLQueryForDateRange(DateUtils.formatDate(fromDate), DateUtils.formatDate(toDate), pageable);
        }
        //Search with string and start date
        else if (query != null && fromDate != null && toDate == null) {
            page = auditLogDao.searchWithJPQLQueryForStringAndStartDate(query, DateUtils.formatDate(fromDate), pageable);
        }

        return page;
    }

    public List<AuditLog> getJsonLogs() {
        List<AuditLog> auditLog = auditLogDao.findAll();
        return auditLog;
    }

    public void save(AuditLog auditLog) throws Exception {
        auditLogDao.save(auditLog);
    }
}
