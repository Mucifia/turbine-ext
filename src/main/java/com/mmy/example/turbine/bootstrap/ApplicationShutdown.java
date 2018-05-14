package com.mmy.example.turbine.bootstrap;

import com.mmy.example.turbine.constants.TurbineConstants;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java.net.HttpURLConnection;
import java.net.URL;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: mmy
 * @date: 2018/04/28
 * @description:
 */
public class ApplicationShutdown {

  public static void main(String[] args) {

    System.setProperty("logging.level.root", "ERROR");

    SpringApplication springApplication = new SpringApplicationBuilder(ApplicationShutdown.class)
        .web(false)
        .bannerMode(Banner.Mode.OFF).build();
    ConfigurableApplicationContext context = springApplication.run(args);
    String serverPort = context.getEnvironment().getProperty("server.port");

    String managementHost = context.getEnvironment().getProperty("management.address", "127.0.0.1");
    String managementPort = context.getEnvironment().getProperty("management.port", "18060");

    String shutdownPath = context.getEnvironment()
        .getProperty("endpoints.shutdown.path", "/shutdown");
    if (shutdownPath.charAt(0) == '/') {
      shutdownPath = shutdownPath.substring(1);
    }

    String username = context.getEnvironment().getProperty("security.user.name", "admin");
    String password = context.getEnvironment().getProperty("security.user.password", "admin");

    HttpURLConnection shutdownConnection = null;
    HttpURLConnection stopTurbineConnection = null;
    int responseCode;
    try {
      // stop the TurbineCluster Thread using TurbineInit.stop
      URL turbineStopURL = new URL(String.format("http://127.0.0.1:%s/%s", serverPort,
          TurbineConstants.REST_PATH_CLUSTERSTOP.substring(1)));
      stopTurbineConnection = (HttpURLConnection) turbineStopURL.openConnection();
      stopTurbineConnection.setRequestProperty("Authorization",
          "Basic " + Base64.encode((username + ":" + password).getBytes()));
      stopTurbineConnection.setRequestMethod("GET");
      stopTurbineConnection.setRequestProperty("Content-Type", "application/json");
      stopTurbineConnection.connect();
      responseCode = stopTurbineConnection.getResponseCode();
      if (responseCode / 100 == 2) {
        System.out.println("Stop Turbine Successfully");
      }

      // stop Application using spring-boot-start-actuator endpoint;
      URL shutdownURL = new URL(
          String.format("http://%s:%s/%s", managementHost, managementPort, shutdownPath));
      shutdownConnection = (HttpURLConnection) shutdownURL.openConnection();
      shutdownConnection.setRequestProperty("Authorization",
          "Basic " + Base64.encode((username + ":" + password).getBytes()));
      shutdownConnection.setRequestMethod("POST");
      shutdownConnection.connect();
      responseCode = shutdownConnection.getResponseCode();
      if (responseCode / 100 == 2) {
        System.out.println("Shutdown Application Successfully");
        System.exit(0);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    // 若仍无法关闭进程，则使用pid文件kill进程（体现在停止脚本中）
  }
}
