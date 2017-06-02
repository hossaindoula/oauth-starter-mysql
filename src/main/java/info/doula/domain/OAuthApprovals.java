package info.doula.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-05-24.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oauth_approvals")
public class OAuthApprovals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String userId;
    private String clientId;
    private String scope;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedAt = new Date();


}
