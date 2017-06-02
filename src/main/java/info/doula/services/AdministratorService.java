package info.doula.services;

import info.doula.domain.Administrator;
import info.doula.repository.AdministratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by saad on 10/5/2016.
 */
@Service
public class AdministratorService {
    private static final Logger logger =
            LoggerFactory.getLogger(AdministratorService.class);
    @Autowired
    private AdministratorRepository adminDao;

    public AdministratorService() {
    }

    AdministratorService(AdministratorRepository adminDao) {
        this.adminDao = adminDao;
    }

    public Administrator findByEmail(String email) throws Exception {
        return adminDao.findByEmail(email);
    }

    public Administrator save(Administrator administrator) throws Exception {
        return adminDao.save(administrator);
    }
}
