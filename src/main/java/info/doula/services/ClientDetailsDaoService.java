package info.doula.services;

import info.doula.domain.ClientDetails;
import info.doula.repository.ClientDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by tasnim on 5/27/2017.
 */
@Service
public class ClientDetailsDaoService {

    private static final Logger logger = LoggerFactory.getLogger(AdministratorService.class);
    @Autowired
    private ClientDetailsRepository clientDao;

    public ClientDetailsDaoService() {
    }

    public ClientDetailsDaoService(ClientDetailsRepository clientDao) {
        this.clientDao = clientDao;
    }

    public void save(ClientDetails clientDetails){
        clientDao.save(clientDetails);
    }

    public ClientDetails get(String id){
        return clientDao.findOne(id);
    }
}
