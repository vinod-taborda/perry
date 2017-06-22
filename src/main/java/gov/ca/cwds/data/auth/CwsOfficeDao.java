package gov.ca.cwds.data.auth;


import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.auth.CwsOffice;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * DAO for {@link CwsOffice}.
 *
 * @author CWDS API Team
 */
@Transactional
@Repository
public class CwsOfficeDao extends CrudsDaoImpl<CwsOffice> {

    @Autowired
    public CwsOfficeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public CwsOffice[] findByStaff(String staffId) {
        Query query =
                this.getSessionFactory().getCurrentSession()
                        .getNamedQuery("gov.ca.cwds.data.persistence.auth.CwsOffice.findByStaff")
                        .setString("staffId", staffId);
        return (CwsOffice[]) query.list().toArray(new CwsOffice[0]);

    }

}
