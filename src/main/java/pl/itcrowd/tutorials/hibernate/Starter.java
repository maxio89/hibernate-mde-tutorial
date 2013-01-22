package pl.itcrowd.tutorials.hibernate;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Starter {

    @EJB
    private TransactionTesterBMT transactionTesterBMT;

    @PostConstruct
    public void postConstruct() throws Exception {

        transactionTesterBMT.test();

    }
}
