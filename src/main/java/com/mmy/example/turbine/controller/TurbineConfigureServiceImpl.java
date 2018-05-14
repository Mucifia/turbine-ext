package com.mmy.example.turbine.controller;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.mmy.example.turbine.service.TurbineConfigureService;
import com.netflix.turbine.init.TurbineInit;
import com.mmy.example.turbine.api.ITurbineConfigureService;
import com.mmy.example.turbine.constants.TurbineConstants;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
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


  private final Joiner joiner = Joiner.on(TurbineConstants.STRING_SEPARATE_COMMA);
  private final Splitter splitter = Splitter.on(TurbineConstants.STRING_SEPARATE_COMMA)
      .omitEmptyStrings();
  @Autowired
  TurbineProperties turbineProperties;
  @Autowired
  TurbineAggregatorProperties turbineAggregatorProperties;
  @Autowired
  TurbineConfigureService turbineConfigureService;

  @Override
  public String addTurbineAppConfigItem(@RequestBody String serviceIds) {
    if (StringUtils.isBlank(serviceIds)) {
      return "fail";
    }
    List appConfigList = new ArrayList<>(turbineProperties.getAppConfigList());
    List serviceIdList = convertToList(serviceIds);
    appConfigList.addAll(serviceIdList);
    String appConfigString = convertToString(appConfigList);
    turbineProperties.setAppConfig(appConfigString);
    return "success";
  }

  @Override
  public String deleteTurbineAppConfigItem(@PathVariable String serviceIds) {
    if (StringUtils.isBlank(serviceIds)) {
      return "fail";
    }
    List<String> appConfigList = new ArrayList<>(turbineProperties.getAppConfigList());
    List serviceIdList = convertToList(serviceIds);
    appConfigList.removeAll(serviceIdList);
    String appConfigString = convertToString(appConfigList);
    turbineProperties.setAppConfig(appConfigString);
    return "success";
  }

  @Override
  public String addTurbineClusterConfig(@RequestBody String clusters) {
    if (StringUtils.isBlank(clusters)) {
      return "fail";
    }
    List<String> clusterConfigList = turbineAggregatorProperties.getClusterConfig();
    List clusterList = convertToList(clusters);
    clusterConfigList.addAll(clusterList);
    turbineConfigureService.loadClusterMonitor(clusterList);
    turbineAggregatorProperties.setClusterConfig(clusterConfigList);
    return "success";
  }

  @Override
  public String deleteTurbineClusterConfig(@PathVariable String clusters) {
    if (StringUtils.isBlank(clusters)) {
      return "fail";
    }
    List<String> clusterConfigList = turbineAggregatorProperties.getClusterConfig();
    List clusterList = convertToList(clusters);
    clusterConfigList.removeAll(clusterList);
    turbineConfigureService.removeClusterMonitor(clusterList);
    turbineAggregatorProperties.setClusterConfig(clusterConfigList);
    return "success";
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


  private List convertToList(String string) {
    return splitter.splitToList(string);
  }

  private String convertToString(List list) {
    return joiner.join(list);
  }


}
