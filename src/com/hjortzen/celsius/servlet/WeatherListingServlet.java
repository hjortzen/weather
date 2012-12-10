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
import java.util.List;
import java.util.logging.Logger;

/**
 *
 */
public class WeatherListingServlet extends HttpServlet {
    private Logger logger = Logger.getLogger("WeatherListingServlet");
    private WeatherObservationDao weatherDao = new WeatherObservationDao();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String type = req.getParameter("type");
        String fromDate = req.getParameter("from");
        String toDate = req.getParameter("to");

        if (type == null || type.equals("json")) {
            JsonResponse response = new JsonResponse();
            response.data = weatherDao.getAll();

            String json = getGson().toJson(response);
            resp.getWriter().println(json);
        } else {
            //logger.info("Running weather listing servlet!");
            List<WeatherObservation> observations = weatherDao.getAll();
            req.setAttribute("observations", observations);

            req.getRequestDispatcher("/jsp/listAvailableData.jsp").forward(req, resp);
            //resp.sendRedirect("/jsp/listAvailableData.jsp");
        }


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
}
