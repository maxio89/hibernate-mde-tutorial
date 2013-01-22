package pl.itcrowd.tutorials.hibernate;


import org.hibernate.LazyInitializationException;
import pl.itcrowd.tutorials.hibernate.domain.User;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class TransactionTesterBMT {

    @Resource
    private TransactionSynchronizationRegistry txReg;

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private TransactionTesterCMT transactionTesterCMT;

    @Resource
    private UserTransaction userTransaction;

    private static final Logger LOGGER = Logger.getLogger(TransactionTesterBMT.class.getCanonicalName());


    public void test() throws Exception {

        User user;

        LOGGER.info("##### BeanTransactionStatus= " + txReg.getTransactionStatus());
        transactionTesterCMT.setupDB();
        LOGGER.info("##### BeanTransactionStatus= " + txReg.getTransactionStatus());

//      Start transaction BMT
        userTransaction.begin();

        user = getUserFromDB();
        LOGGER.info("******* User: " + user.getUserName() + ", group_name: " + user.getGroup().getGroupName());

//        transactionTesterCMT.bulkDetach();
        transactionTesterCMT.explicitDetach(user);
        user.getGroup().setGroupName("Changed");
        user.setUserName("Changed");

//      entityManager.persist(user);
//      Exception: Caused by: org.hibernate.PersistentObjectException:
//                 detached entity passed to persist: pl.itcrowd.tutorials.hibernate.domain.User

//      Cascade merge
        entityManager.merge(user);

        user = getUserFromDB();
        LOGGER.info("******* User: " + user.getUserName() + ", group_name: " + user.getGroup().getGroupName());

//      End transaction BMT
        userTransaction.commit();

    }

    public User getUserFromDB() {
        User user = null;
        try {
            user = transactionTesterCMT.getUserFromDB();
            return user;
        } catch (LazyInitializationException e) {
            LOGGER.info("We expected LaizyInitializationException and it was thrown");

        }
        return user;
    }


}
