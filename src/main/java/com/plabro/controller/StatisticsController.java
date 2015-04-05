package com.plabro.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.plabro.dao.DataStore;

public class StatisticsController extends HttpServlet {
	private static final long serialVersionUID = -2512249372075184869L;
	private static final IsPositiveIntegerValidator isPositiveInt = new IsPositiveIntegerValidator();
	private static final DataStore dataStore  = DataStore.getInstance();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// expected parameters
//		final String fromYear = request.getParameter("from_year");
//		final String toYear = request.getParameter("to_year");
//		final String fromDay = request.getParameter("from_day");
//		final String toDay = request.getParameter("to_day");
//		final String fromHour = request.getParameter("from_hour");
//		final String toHour = request.getParameter("to_hour");

		/*
		 * days and years must be passed. respond with empty json if none of the
		 * required parameters were passed
		 */
		
		Map<Integer, Integer> histogramStatistics = null;
		
		// serve all the content
		if (dataStore.getSize() > 0) {
			histogramStatistics = dataStore.getAllDistributions();
		}
		response.setContentType("application/json");
		
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(histogramStatistics));
		writer.close();
	}

//	private Date getDateFromHourAndDay(final String year, final String month,
//			final String day, final String hour) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.YEAR, Integer.parseInt(year));
//		calendar.set(Calendar.MONTH, Integer.parseInt(month));
//		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
//		calendar.set(Calendar.HOUR, Integer.parseInt(hour));
//		return calendar.getTime();
//	}
	
//	private String validate(final String year, final String month,
//			final String day, final String hour) {
//		String msg;
//		if (!isPositiveInt.validate(year)) {
//			msg = "Please enter a valid yar"
//		}
//	}
}

interface Validator<T> {
	boolean validate(T t);
}

class IsPositiveIntegerValidator implements Validator<String> {
	@Override
	public boolean validate(String input) {
		boolean isInteger = false;
		if ((null != input) && !input.isEmpty()) {
			try {
				if (Integer.parseInt(input) > 0) {
					isInteger = true;
				}
			} catch (NumberFormatException e) {

			}
		}
		return isInteger;
	}
}