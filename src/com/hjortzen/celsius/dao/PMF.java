package com.hjortzen.celsius.dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PMF {
    private static PersistenceManagerFactory pm = null;

    public synchronized static PersistenceManagerFactory getInstance() {
        if (pm == null) {
            pm = JDOHelper.getPersistenceManagerFactory("transactions-optional");
        }
        return pm;
    }
}
