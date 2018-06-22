package com.mmy.example.turbine.controller;


import com.mmy.example.turbine.api.ITurbineConfigureService;
import com.mmy.example.turbine.service.TurbinePropertyService;
import com.netflix.turbine.init.TurbineInit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.turbine.TurbineAggregatorProperties;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: mmy
 * @date: 2018/04/04
 * @description:
 */
@RestController
public class TurbineConfigureServiceImpl implements ITurbineConfigureService {


  private static final Log log = LogFactory.getLog(TurbineConfigureServiceImpl.class);


  @Autowired
  TurbineProperties turbineProperties;
  @Autowired
  TurbineAggregatorProperties turbineAggregatorProperties;
  @Autowired
  TurbinePropertyService turbinePropertyService;

  @Override
  public String addTurbineAppConfigItem(@RequestBody String serviceIds) {
    if (StringUtils.isBlank(serviceIds)) {
      log.info("serviceIds shouldn't Empty ,add TurbineApp failed");
      return "fail";
    }
    String currentApps = turbinePropertyService.addTurbineAppConfig(serviceIds);
    log.debug("add TurbineApp success, Current AppConfig are :" + currentApps);
    return currentApps;
  }

  @Override
  public String deleteTurbineAppConfigItem(@PathVariable String serviceIds) {
    if (StringUtils.isBlank(serviceIds)) {
      log.info("serviceIds shouldn't Empty ,delete TurbineApp failed");
      return "fail";
    }
    String currentApps = turbinePropertyService.deleteTurbineAppConfig(serviceIds);
    log.info("delete TurbineApp success, Current AppConfig are :" + currentApps);
    return currentApps;
  }

  @Override
  public String addTurbineClusterConfig(@RequestBody String clusters) {
    if (StringUtils.isBlank(clusters)) {
      log.info("clusters shouldn't Empty ,add TurbineCluster failed");
      return "fail";
    }
    String currentClusters = turbinePropertyService.addTurbineClusterConfig(clusters);
    log.info("add TurbineApp success, Current ClusterConfig are :" + currentClusters);
    return currentClusters;
  }

  @Override
  public String deleteTurbineClusterConfig(@PathVariable String clusters) {
    if (StringUtils.isBlank(clusters)) {
      log.info("clusters shouldn't Empty ,delete TurbineCluster failed");
      return "fail";
    }
    String currentClusters = turbinePropertyService.deleteTurbineClusterCongfig(clusters);
    log.info("delete TurbineApp success, Current ClusterConfig are :" + currentClusters);
    return currentClusters;
  }

  @Override
  public String turbineClusterStop() {
    try {
      TurbineInit.stop();
    } catch (NullPointerException e) {
      e.printStackTrace();
      return "TurbineCluster have not been init";
    }
    return "success";
  }

  @Override
  public String updateClusterNameExpressionConfig(@RequestBody String expression) {
    if (StringUtils.isBlank(expression)) {
      return "fail";
    }
    turbineProperties.setClusterNameExpression(expression);
    return "success";
  }


}

