package info.doula.controllers;

import info.doula.domain.Administrator;
import info.doula.domain.ClientDetails;
import info.doula.dto.ClientDetailsBean;
import info.doula.dto.UserBean;
import info.doula.services.AdministratorService;
import info.doula.services.ClientDetailsDaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by saad on 9/28/2016.
 */
@RestController
@RequestMapping("/api/v1/administrators")
@Api(value = "/api/v1/administrators", description = "Endpoint for Vault Holders")
public class AdministratorController {
    private static final Logger logger =
            LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    private AdministratorService adminService;

    @Autowired
    private ClientDetailsDaoService clientDetailsDaoService;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*******************************
     * Authenticate using the username and password and generates a secured access token..
     * EndPoint:/api/v1/authentication
     **********************************/
    @ApiOperation(
            value = "Administrator Creation API",
            notes = "Administrator Creation API"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Administrator Creation Successfully done!"),
            @ApiResponse(code = 401, message = "Authentication failed miserably!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(
            value = "/create",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAdmin(@Validated @RequestBody UserBean userBean) {
        Map<Object, Object> response = new HashMap<>();
        try {
            Administrator user = new Administrator();
            user.setPassword(getPasswordEncoder().encode(userBean.getPassword()));
            user.setEmail(userBean.getUsername());
            user.setEnabled(true);
            adminService.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            logger.error("error", ex.getLocalizedMessage());
            response.put("error", ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /*******************************
     * Authenticate using the username and password and generates a secured access token..
     * EndPoint:/api/v1/authentication
     **********************************/
    @ApiOperation(
            value = "Administrator Creation API",
            notes = "Administrator Creation API"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Administrator Creation Successfully done!"),
            @ApiResponse(code = 401, message = "Authentication failed miserably!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(
            value = "/createClientDetails",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createClientDetails(@Validated @RequestBody ClientDetailsBean client) {
        Map<Object, Object> response = new HashMap<>();
        try {
            ClientDetails clientDetails = new ClientDetails();
            clientDetails.setClientId(client.getClientId());
            clientDetails.setAccessTokenValidity(client.getAccessTokenValidity());
            clientDetails.setAuthorities(client.getAuthorities());
            clientDetails.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
            clientDetails.setRefreshTokenValidity(client.getRefreshTokenValidity());
            clientDetails.setResourceIds(client.getResourceIds());
            clientDetails.setScope(client.getScope());
            clientDetails.setClientSecret(client.getClientSecret());
            clientDetails.setWebServerRedirectUri(client.getWebServerRedirectUri());
            clientDetails.setAutoApprove(client.getAutoApprove());
            logger.info(" Client Details = " + clientDetails.getAutoApprove());
            clientDetailsDaoService.save(clientDetails);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            logger.error("error", ex.getLocalizedMessage());
            response.put("error", ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}
