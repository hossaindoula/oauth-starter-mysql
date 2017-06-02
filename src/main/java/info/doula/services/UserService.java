package info.doula.services;

import com.doulat.administrator.components.clients.HttpClientConfig;
import com.doulat.administrator.constants.VaultAccessor;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import info.doula.domain.Users;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;


/**
 * Created by Faruque on 1/27/2017.
 */
@Service
public class UserService {
    @Autowired
    private SponsoredAdvisorRepository sponsoredAdvisorDao;
    @Autowired
    private VaultHolderRepository userDao;
    @Autowired
    private CategoryPermissionRepository categoryPermissionDao;
    @Autowired
    private VaultHolderRepository vaultHolderDao;
    @Autowired
    private VaultRepository vaultDao;
    @Autowired
    private UserVaultRepository userVaultDao;
    @Autowired
    private SponsorAdministratorRepository sponsorAdministratorDao;
    @Autowired
    private PackageSponsorRepository sponsorPackageDao;
    @Autowired
    private SubscriptionRepository subscriptionDao;
    @Autowired
    private SponsorDefaultPackageRepository sponsorDefaultPackageDao;
    @Autowired
    private InvitationRepository invitationDao;
    @Autowired
    private PackageRepository packageDao;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Users updateUser(Users existingUsers) {
        try {
            Users users = vaultHolderDao.save(existingUsers);
            return users;
        } catch (Exception e) {
            logger.error("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public Users createVaultHolder(String vaultId, Sponsor sponsor, List<SponsoredAdvisor> sponsoredAdvisorList, Users users) throws Exception {
        try {
            int statusCode = 0;
            logger.info("Successful. Saving vault holder.");
            Users newVaultHolder = new Users();
            Vault existingVault = vaultDao.findByOriginalOwnerAndSponsorId(users.getEmail(), sponsor.getId());
            Users existingUsers = vaultHolderDao.findByEmail(users.getEmail());

            if (existingVault == null) {
                if (existingUsers == null) {
                    newVaultHolder = vaultHolderDao.save(users);
                }
                int packageId = users.getPackageId();
                Invitation invitation = invitationDao.findByInviteCode(users.getInvitationCode());
                if (invitation != null && packageId == 0) {
                    packageId = invitation.getPackageId();
                }
                if (packageId == 0) {
                    SponsorDefaultPackage sponsorDefaultPackage = sponsorDefaultPackageDao.findBySponsorId(sponsor.getId());
                    if (sponsorDefaultPackage == null) {
                        throw new Exception("SponsorDefaultPackage code was not found in the admin portal");
                    }
                    packageId = sponsorDefaultPackage.getPackageId();
                }

                PackageSponsor packageSponsor = sponsorPackageDao.findPackageBySponsorIdAndPackageId(sponsor.getId(), packageId);
                if (packageSponsor == null) {
                    throw new Exception("Package was not found assigned to the sponsor.");
                }
                Vault vault = new Vault();
                vault.setVaultId(vaultId);
                vault.setSponsorId(sponsor.getId());
                vault.setOriginalOwner(newVaultHolder.getEmail());
                vault.setOriginalOwnerId(newVaultHolder.getId());
                vault.setPackageId(packageId);
                Date effectiveDate = new Date();
                vault.setEffectiveDate(effectiveDate);
                vault.setPrice(packageSponsor.getPriceSale());
                logger.info("Successful. Saving vault.");

                Vault v = vaultDao.save(vault);
                Subscription subscription = new Subscription();
                subscription.setUserId(newVaultHolder.getId());
                subscription.setSponsorId(sponsor.getId());
                subscription.setVaultId(v.getId());
                subscription.setVaultEmail(newVaultHolder.getEmail());
                subscription.setPrice(packageSponsor.getPriceSale());
                subscription.setEffectiveDate(effectiveDate);
                subscription.setEndDate(null);

                logger.info("Set packageId as packageSponsorId");
                subscription.setPackageId(packageSponsor.getId());
                subscriptionDao.save(subscription);

                VaultAccess ownerVault = new VaultAccess();
                ownerVault.setVaultsId(v.getId());
                ownerVault.setUserId(newVaultHolder.getId());
                ownerVault.setVaultAccessor(VaultAccessor.OWNER);
                userVaultDao.save(ownerVault);

                VaultAccess sponsorVault = new VaultAccess();
                sponsorVault.setVaultsId(0);
                sponsorVault.setUserId(newVaultHolder.getId());
                sponsorVault.setVaultAccessor(VaultAccessor.SPONSOR);
                userVaultDao.save(sponsorVault);
                logger.info("Successful. Saving . Saving user vault.");
            }
            logger.info("Check Sponsored Advisor for : ", newVaultHolder.getEmail());
            if (sponsoredAdvisorList == null || sponsoredAdvisorList.size() == 0) {
                logger.info("Sponsor trusted advisor is not set for : ", newVaultHolder.getEmail());
                return newVaultHolder;
            } else {
                for (SponsoredAdvisor sponsoredAdvisor : sponsoredAdvisorList) {
                    Users u = vaultHolderDao.findOne(sponsoredAdvisor.getUserId());
                    if (u == null) {
                        logger.error("Sponsor trusted advisor user not found in users table.");
                        return newVaultHolder;
                    }
                    SponsorAdministrator sponsorAdministrator = sponsorAdministratorDao.findOne(sponsor.getSponsorAdminId());
                    HttpPost post = new HttpPost();
                    String apiUrl = sponsor.getApiUrl();
                    HttpClient httpClient = HttpClientConfig.load(apiUrl);

                    Gson gson = new Gson();
                    HashMap<String, String> contact = new HashMap<>();
                    Gson gg = new Gson();
                    contact.put("primaryEmail", u.getEmail());
                    contact.put("firstName", u.getFirstName());
                    contact.put("lastName", u.getLastName());
                    contact.put("title", sponsor.getTitle());
                    contact.put("company", sponsor.getCompany());
                    contact.put("hasVault", "false");
                    contact.put("sponsor", "true");
                    contact.put("userId", vaultId);
                    contact.put("contactUserId", sponsoredAdvisor.getSponsorUserFvId());
                    contact.put("active", "true");

                    String gg1 = gg.toJson(contact);
                    StringEntity ee = new StringEntity(gg1);

                    logger.info("Calling doulat to create contacts.", u.getEmail());
                    post.setHeader("Accept", "application/json");
                    post.setHeader("Content-Type", "application/json");
                    post.setEntity(ee);
                    post.setURI(new URI(apiUrl + "/contacts"));
                    post.setHeader("accessToken", sponsorAdministrator.getAccessToken());

                    HttpResponse xxx = httpClient.execute(post);
                    org.apache.http.HttpEntity xxxx = xxx.getEntity();
                    String xxxxx = EntityUtils.toString(xxxx, "UTF-8");
                    statusCode = xxx.getStatusLine().getStatusCode();

                    if (statusCode == 200) {
                        logger.info("Successful.");
                        Map<String, Object> map111 = gson.fromJson(xxxxx, new TypeToken<Map<String, Object>>() {
                        }.getType());

                        Map m = (Map) map111.get("apiresponse");
                        Map m1 = (Map) m.get("created");
                        String contactId = (String) m1.get("id");

                        HashMap<String, Object> permission = new HashMap<>();
                        permission.put("contactId", contactId);
                        permission.put("userId", vaultId);
                        permission.put("trustedByUserEmail", sponsor.getContactEmail());
                        permission.put("trustedUserEmail", users.getEmail());

                        List<String> perm = new ArrayList<>();
                        perm = categoryPermissionDao.findCategoryPermissionBySponsorId(String.valueOf(sponsor.getId()));

                        permission.put("permissions", perm);
                        String permj = gg.toJson(permission);
                        StringEntity permee = new StringEntity(permj);

                        logger.info("Successful. Calling doulat to save permission.");
                        HttpPut put = new HttpPut(apiUrl + "/advisors/permissions");
                        put.setEntity(permee);
                        put.setHeader("Accept", "application/json");
                        put.setHeader("Content-Type", "application/json");
                        put.setHeader("accessToken", sponsorAdministrator.getAccessToken());

                        HttpResponse permPut = httpClient.execute(put);
                        org.apache.http.HttpEntity permputee = permPut.getEntity();
                        String permputees = EntityUtils.toString(permputee, "UTF-8");
                        logger.info("Vault holder information", permputees);
                        statusCode = permPut.getStatusLine().getStatusCode();
                    } else {
                        logger.error("Sponsor Administrator Unable to loginTodoulatCore to doulat while attempting to create a contact.");
                        throw new Exception(xxx.getStatusLine().getReasonPhrase());
                    }
                }
                if (statusCode == 200) {
                    logger.info("Successful.");
                    return newVaultHolder;
                }
            }
        } catch (Exception e) {
            logger.error("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
        return null;
    }

    public List<SponsoredAdvisor> getSponsoredTrustedAdvisor(Users users) {
        try {
            logger.info("get existing user.");
            List<SponsoredAdvisor> sponsoredAdvisorList = new ArrayList<>();
            List<Integer> su = users.getSponsoredAdvisorUserId();
            for (Integer sTAId : su) {
                SponsoredAdvisor sponsoredAdvisor = sponsoredAdvisorDao.findOne(sTAId);
                sponsoredAdvisorList.add(sponsoredAdvisor);
            }
            return sponsoredAdvisorList;
        } catch (Exception e) {
            logger.error("An error occurred while trying get users information: ", e);
            return null;
        }
    }

    public SponsoredAdvisor findSponsoredAdvisorById(Integer sponsorId, boolean isDefault) {
        try {
            SponsoredAdvisor sponsoredAdvisor = sponsoredAdvisorDao.findBySponsorIdAndIsDefault(sponsorId, isDefault);
            return sponsoredAdvisor;
        } catch (Exception e) {
            logger.info("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public Users getUserByEmail(String email) {
        try {
            return userDao.findByEmail(email);
        } catch (Exception e) {
            logger.info("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public Users createNewUser(Users users, Map<String, Object> map, Sponsor sponsor) {
        Users newUser = new Users();
        try {
            Map mm = (Map) map.get("userInfo");
            String vaultId = (String) mm.get("id");
            String active = (String) mm.get("active");
            String invitationCode = (String) mm.get("invitationCode");
            boolean isActive = ("1".equals(active));
            users.setActive(isActive);
            users.setInvitationCode(invitationCode);

            newUser.setFirstName(users.getFirstName());
            newUser.setLastName(users.getLastName());
            newUser.setEmail(users.getEmail());
            newUser.setActive(users.isActive());
            newUser.setInvitationCode(users.getInvitationCode());
            newUser.setVaultId(vaultId);

            newUser = userDao.save(newUser);
            return newUser;
        } catch (Exception e) {
            logger.info("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public Users findUserByEmail(String email) {
        try {
            return userDao.findByEmail(email);
        } catch (Exception e) {
            logger.info("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public Users findUserById(Integer userId) {
        try {
            return userDao.findOne(userId);
        } catch (Exception e) {
            logger.info("DB operation failed.The possible causes of the error are: '{}'", e);
            return null;
        }
    }

    public HashMap<Object, Object> getSubscriptionDetails(Users users, Sponsor sponsor) throws Exception {
        HashMap<Object, Object> response = new HashMap<>();
        try {
            PackageBase packageBase = packageDao.findOne(users.getPackageId());
            PackageSponsor packageSponsor = sponsorPackageDao.findPackageBySponsorIdAndPackageId(sponsor.getId(), users.getPackageId());
            response.put("userId", "blank");
            response.put("packageId", packageBase.getId());
            response.put("packageName", packageBase.getPackageName());
            response.put("salePrice", packageSponsor.getPriceSale());
            response.put("limitAdvisors", packageBase.getLimitAdvisors());
            response.put("limitDisk", packageBase.getLimitDisk());
            response.put("limitEntities", packageBase.getLimitEntities());
            response.put("archived", packageBase.isArchived());

            return response;
        } catch (Exception e) {
            logger.error("Failed to get the subscription details ", e);
            return null;
        }
    }
}
