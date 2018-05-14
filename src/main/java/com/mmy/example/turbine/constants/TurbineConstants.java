package com.mmy.example.turbine.constants;

/**
 * @author: mmy
 * @date: 2018/04/24
 * @description:
 */
public class TurbineConstants {

  public static final String PATH_APPCONFIG = "/appconfig";
  public static final String PATH_CLUSTERCONFIG = "/clusterconfig";
  public static final String PATH_CLUSTERNAMEEXPRESSION = "/clusterNameExpression";
  public static final String REST_PATH_CLUSTERSTOP = "/stop";

  public static final String STRING_SEPARATE_COMMA = ",";


  public static final String REST_PATH_APPCONFIG = PATH_APPCONFIG + "/{serviceIds}";
  public static final String REST_PATH_CLUSTERCONFIG = PATH_CLUSTERCONFIG + "/{clusters}";
  public static final String REST_PATH_CLUSTERNAMEEXPRESSION =
      PATH_CLUSTERNAMEEXPRESSION + "/{expression}";


}
