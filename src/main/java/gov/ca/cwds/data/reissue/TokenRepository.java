package gov.ca.cwds.data.reissue;

import gov.ca.cwds.data.reissue.model.PerryTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



/**
 * Created by TPT2 on 10/26/2017.
 */
@Repository
@Transactional("tokenTransactionManager")
public interface TokenRepository extends JpaRepository<PerryTokenEntity, String> {
}
