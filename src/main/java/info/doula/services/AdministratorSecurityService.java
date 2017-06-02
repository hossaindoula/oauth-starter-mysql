package info.doula.services;

/**
 * Created by saad on 10/5/2016.
 */

import javax.transaction.Transactional;

/**
 * @author ekansh
 * @since 2/4/16
 */
@Transactional
public class AdministratorSecurityService  {

    /*private static final Logger LOGGER = LoggerFactory.getLogger(AdministratorSecurityService.class);

    private AdministratorRepository userRepository;

    public AdministratorSecurityService(AdministratorRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Administrator user = userRepository.findByEmail(username);
            if (user == null) {
                LOGGER.debug("user not found with the provided username");
                return null;
            }
            LOGGER.debug(" user from username " + user.toString());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Administrator user) {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for (AdministratorRole role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        LOGGER.debug("user authorities are " + authorities.toString());
        return authorities;
    }*/


}
