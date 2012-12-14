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
            logUnexpectedError(e);
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
                logResults(results);
                return results;
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logUnexpectedError(e);
        } finally {
            pm.close();
        }
        return null;
    }

    public List<WeatherObservation> getAll() {
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            Query q = pm.newQuery(WeatherObservation.class);
            try {
                List<WeatherObservation> results = (List<WeatherObservation>) q.execute();
                logResults(results);
                return results;
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logUnexpectedError(e);
        } finally {
            pm.close();
        }
        return null;
    }

    public List<WeatherObservation> getBetween(Date fromDate, Date toDate) {
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            Query q = pm.newQuery(WeatherObservation.class);
            q.setFilter("observationTimestamp >= fromDate && observationTimestamp <= toDate");
            q.declareParameters("java.util.Date fromDate, java.util.Date toDate");
            q.setOrdering("observationTimestamp desc");
            try {
                List<WeatherObservation> results = (List<WeatherObservation>) q.execute(fromDate, toDate);
                logResults(results);
                return results;
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logUnexpectedError(e);
        } finally {
            pm.close();
        }
        return null;
    }

    public List<WeatherObservation> getFrom(Date fromDate) {
        PersistenceManager pm = PMF.getInstance().getPersistenceManager();
        try {
            Query q = pm.newQuery(WeatherObservation.class);
            q.setFilter("observationTimestamp > fromDate");
            q.declareParameters("java.util.Date fromDate");
            q.setOrdering("observationTimestamp desc");
            try {
                List<WeatherObservation> results = (List<WeatherObservation>) q.execute(fromDate);
                logResults(results);
                return results;
            } finally {
                q.closeAll();
            }
        } catch(Exception e) {
            logUnexpectedError(e);
        } finally {
            pm.close();
        }
        return null;
    }

    private void logUnexpectedError(Exception e) {
        logger.severe("Unexpected result querying JDO " + e.getMessage());
        e.printStackTrace();
    }

    private void logResults(List<WeatherObservation> results) {
        if (!results.isEmpty()) {
            logger.info("Found " + results.size() + " objects");
        } else {
            logger.info("No objects found");
        }
    }
}
