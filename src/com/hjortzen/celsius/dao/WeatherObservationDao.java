package com.hjortzen.celsius.dao;

import com.hjortzen.celsius.data.WeatherObservation;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles all persistency for weather observations
 */
public class WeatherObservationDao {
    private Logger logger = Logger.getLogger("WODao");

    public void add(WeatherObservation dto) {
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            pm.makePersistent(dto);
        } catch(Exception e) {
            logger.severe("Unexpected result when adding a new weather observation" + e.getMessage());
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }

    public List<WeatherObservation> get(Date observationTime) {
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            Query q = pm.newQuery(WeatherObservation.class);
            q.setFilter("observationTimestamp == observationTimestampParam");
            q.declareParameters("java.util.Date observationTimestampParam");

            try {
                List<WeatherObservation> results = (List<WeatherObservation>) q.execute(observationTime);
                if (!results.isEmpty()) {
                    logger.info("Found " + results.size() + " objects");
                } else {
                    logger.info("No objects found");
                }
                return results;
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logger.severe("Unexpected result querying JDO " + e.getMessage());
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return null;
    }

    public List<WeatherObservation> getAll() {
        /*PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            Query q = pm.newQuery(WeatherObservation.class);

            try {
                List<WeatherObservation> results = (List<WeatherObservation>) q.execute();
                if (!results.isEmpty()) {
                    logger.info("Found " + results.size() + " objects");
                } else {
                    logger.info("No objects found");
                }
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logger.severe("Unexpected result querying JDO " + e.getMessage());
            e.printStackTrace();
        } finally {
            pm.close();
        }   */
        return null;
    }
}
