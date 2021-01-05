// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.lang.Process;
import java.lang.ProcessBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles file upload and calls GTFS validator */
@WebServlet("/data")
public class GTFSServlet extends HttpServlet {
  private static final Logger LOGGER = Logger.getLogger(DataServlet.class.getName());
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html;");
    response.getWriter().println("<h1>Running MobilityData GTFS Validator</h1>");
    LOGGER.info("Logger Name: " + LOGGER.getName());
    ProcessBuilder processBuilder = new ProcessBuilder();
    processBuilder.command("java", "-jar", "gtfs-validator-v1.4.0_cli.jar", "--input",
        "sample-feed.zip", "--output", "/", "--feed_name", "feed", "--threads", "2");
    try {
      Process process = processBuilder.start();
      int ret = process.waitFor();
      if (ret != 0) {
        LOGGER.log(Level.INFO, "Issue with process. Program exited with code: " + ret);
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "GTFS validator process did not occur", e);
    }
  }
}
