package com.hjortzen.celsius.servlet;

import com.google.appengine.api.socket.SocketServicePb;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hjortzen.celsius.dao.WeatherObservationDao;
import com.hjortzen.celsius.data.WeatherObservation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 */
public class WeatherListingServlet extends HttpServlet {
    private Logger logger = Logger.getLogger("WeatherListingServlet");
    private WeatherObservationDao weatherDao = new WeatherObservationDao();
    private SimpleDateFormat formatter = new SimpleDateFormat(WeatherObservation.datePattern);

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String type = req.getParameter("type");
        String fromDate = req.getParameter("from");
        String toDate = req.getParameter("to");

        //Get the data within boxed limits

        List<WeatherObservation> observations = getData(fromDate, toDate);

        if (type == null || type.equals("json")) {
            JsonResponse response = new JsonResponse();
            response.data = observations;

            String json = getGson().toJson(response);
            setCORSHeaders(resp, req);
            resp.setContentType("application/json");
            resp.getWriter().println(json);
        } else {
            //logger.info("Running weather listing servlet!");
            req.setAttribute("observations", observations);
            req.getRequestDispatcher("/jsp/listAvailableData.jsp").forward(req, resp);
            //resp.sendRedirect("/jsp/listAvailableData.jsp");
        }
    }

    private List<WeatherObservation> getData(String fromDateAsString, String toDateAsString) {
        if (fromDateAsString == null) {
            return weatherDao.getAll();
        } else {
            try {
                Date fromDate = formatter.parse(fromDateAsString);
                if (toDateAsString != null) {
                    Date toDate = formatter.parse(toDateAsString);
                    return weatherDao.getBetween(fromDate, toDate);
                } else {
                    return weatherDao.getFrom(fromDate);
                }
            } catch(ParseException pe) {
                logger.info("Date couldn't be parsed " + pe.getMessage());
            }
        }
        return null;
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new SuppressFields());
        builder.setDateFormat(WeatherObservation.datePattern);

        return builder.create();
    }

    private class SuppressFields implements ExclusionStrategy {
        @Override
        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
            if (fieldAttributes.getName().equals("key")) {
                return true;
            }
            return false;
        }
        @Override
        public boolean shouldSkipClass(Class<?> aClass) {
            return false;
        }
    }

    private class JsonResponse {
        List<WeatherObservation> data;
        String version = "0.1";
    }

    private void setCORSHeaders(HttpServletResponse response, HttpServletRequest request) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // pre-flight request processing
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Headers", "content-type");
    }
}
