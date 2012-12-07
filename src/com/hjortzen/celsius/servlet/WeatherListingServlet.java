package com.hjortzen.celsius.servlet;

import com.hjortzen.celsius.dao.WeatherObservationDao;
import com.hjortzen.celsius.data.WeatherObservation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 */
public class WeatherListingServlet extends HttpServlet {
    private Logger logger = Logger.getLogger("WeatherListingServlet");
    private WeatherObservationDao weatherDao = new WeatherObservationDao();

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //logger.info("Running weather listing servlet!");
        /*resp.setContentType("text/plain");
        resp.getWriter().println("Hej!");*/
        List<WeatherObservation> observations = weatherDao.getAll();
        req.setAttribute("observations", observations);

        req.getRequestDispatcher("/jsp/listAvailableData.jsp").forward(req, resp);
        //resp.sendRedirect("/jsp/listAvailableData.jsp");
    }
}
