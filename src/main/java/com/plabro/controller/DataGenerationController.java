package com.plabro.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.plabro.dao.DataGeneratorThread;

public class DataGenerationController extends HttpServlet {
	private static final long serialVersionUID = 7188253153405799472L;
	private static final String RESOURCE_FILE_NAME = "client.log.1";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// for more than one files, create a vector array of callables
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		final Callable<Boolean> dataThread = new DataGeneratorThread(
				RESOURCE_FILE_NAME);
		final Future<Boolean> dataThreadFuture = executor.submit(dataThread);

		String responseMessage = null;

		try {
			if (dataThreadFuture.get()) {
				responseMessage = "Data generation was successfully completed.";
			} else {
				responseMessage = "Data generation process failed, please see logs for details.";
			}
			System.out.println(responseMessage);
		} catch (InterruptedException e) {
			responseMessage = "Interruption exception occurred while data generation.";
			System.out.println(responseMessage);
			e.printStackTrace();
		} catch (ExecutionException e) {
			responseMessage = "Execution exception occurred while data generation.";
			System.out.println(responseMessage);
			e.printStackTrace();
		}

		PrintWriter out = response.getWriter();
		out.write(responseMessage);
	}
}
