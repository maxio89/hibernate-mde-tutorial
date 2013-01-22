package pl.itcrowd.tutorials.hibernate;

import pl.itcrowd.tutorials.hibernate.domain.Group;
import pl.itcrowd.tutorials.hibernate.domain.User;

import javax.annotation.Resource;
import javax.ejb.EJBTransactionRequiredException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;
import java.util.logging.Logger;

@Stateless
public class TransactionTesterCMT {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private TransactionSynchronizationRegistry txReg;

    private static final Logger LOGGER = Logger.getLogger(TransactionTesterCMT.class.getCanonicalName());

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void setupDB() {

//      Start transaction CMT if not exist any transaction

        final Group group = new Group();
        final User user = new User();

        group.setGroupName("Group");
        user.setGroup(group);
        user.setUserName("User");

        LOGGER.info("%%%%%  Transaction status: " + txReg.getTransactionStatus() + " key: " + txReg.getTransactionKey());

//      Cascade persist
        entityManager.persist(user);
        LOGGER.info("%%%%%  Transaction status: " + txReg.getTransactionStatus() + " key: " + txReg.getTransactionKey());
    }

    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    public User getUserFromDB() throws EJBTransactionRequiredException {

//      Lazy loading
        LOGGER.info("%%%%%  Transaction status: " + txReg.getTransactionStatus() + " key: " + txReg.getTransactionKey());
        final User user = (User) entityManager.createQuery("SELECT u FROM User u").getSingleResult();
        LOGGER.info("%%%%%  Transaction status: " + txReg.getTransactionStatus() + " key: " + txReg.getTransactionKey());

        return user;
    }

    public void bulkDetach() {
        entityManager.clear();
    }

    public void explicitDetach(Object object) {
//      Cascade detach
        entityManager.detach(object);
    }
}
