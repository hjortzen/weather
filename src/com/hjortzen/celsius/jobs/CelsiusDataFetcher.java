package com.hjortzen.celsius.jobs;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.hjortzen.celsius.dao.WeatherObservationDao;
import com.hjortzen.celsius.data.WeatherObservation;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Author: marhj346
 */
public class CelsiusDataFetcher  extends HttpServlet {
    private static int beenRun = 0;
    private Logger logger = Logger.getLogger("CelsiusDataFetch");
    private String celsiusURL = "http://celsius.met.uu.se/uppsala/obs.txt";
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("yyyyMMddHHmm");
    private WeatherObservationDao weatherDao = new WeatherObservationDao();


    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("Running! ("  + new Date() + ")");
        beenRun++;
        URL url = new URL(celsiusURL);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader( url.openStream() ));
            String line;
            while ( (line = reader.readLine()) != null ) {
                logger.info("Read the following line from Celsius:" + line);
                WeatherObservation wo = parse(line);
                if (wo == null) {
                    continue;
                }
                logger.info("Found a weather observation: " + wo);
                List<WeatherObservation> existing = weatherDao.get(wo.getObservationTimestamp());
                if (existing == null || existing.size() == 0) {
                    weatherDao.add(wo);
                }
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        resp.setContentType("text/plain");
        resp.getWriter().println("Been run " + beenRun + " times");
    }

    private String nextToken(StringTokenizer tokenizer) {
        String token = tokenizer.nextToken();
        //token = token.replace(',',' ').trim();
        return token;
    }

    private WeatherObservation parse(String row) {
        try {
            StringTokenizer tokenizer = new StringTokenizer(row, " \t\n\r\f,");
            WeatherObservation wo = new WeatherObservation();
            try {
                //                          Year                Month                   Day                          Hour                  Minutes
                String dateAsString = tokenizer.nextToken() + tokenizer.nextToken() + tokenizer.nextToken() + tokenizer.nextToken() + tokenizer.nextToken() ;
                wo.setObservationTimestamp(dateTimeFormatter.parse( dateAsString ));   //In GMT
            } catch (ParseException e) {
                logger.log(Level.SEVERE, "Couldn't parse observation timestamp", e);
                return null;
            }
            wo.setTemperature( Float.parseFloat(nextToken(tokenizer)) );
            wo.setTempMax12h( Float.parseFloat(nextToken(tokenizer)) );
            wo.setTempMin12h( Float.parseFloat(nextToken(tokenizer)) );
            wo.setWindSpeed( Float.parseFloat(nextToken(tokenizer)) );
            wo.setWindDirection( Float.parseFloat(nextToken(tokenizer)) );
            wo.setAirPressure( Float.parseFloat(nextToken(tokenizer)) );
            wo.setRelativeHumidity( Float.parseFloat(nextToken(tokenizer)) );
            wo.setPrecipation1h( Float.parseFloat(nextToken(tokenizer)) );
            wo.setPrecipation24h( Float.parseFloat(nextToken(tokenizer)) );
            wo.setSnowDepth( Float.parseFloat(nextToken(tokenizer)) );

            return wo;
        } catch(Exception e) {
            logger.log(Level.WARNING, "Something went wrong when trying to parse celsius data", e);
            return null;
        }
    }
}

