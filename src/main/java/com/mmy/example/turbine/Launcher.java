package com.mmy.example.turbine;


import com.mmy.example.turbine.bootstrap.ApplicationShutdown;
import com.mmy.example.turbine.bootstrap.ApplicationStartup;
import org.springframework.util.ObjectUtils;

/**
 * @author: mmy
 * @date: 2018/04/28
 * @description:
 */
public class Launcher {

  public static void main(String[] args) {
    if (ObjectUtils.containsElement(args, "shutdown")) {
      ApplicationShutdown.main(args);
      return;
    }
    ApplicationStartup.main(args);
  }
}
