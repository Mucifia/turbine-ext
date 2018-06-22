package com.mmy.example.turbine.service;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.mmy.example.turbine.constants.TurbineConstants;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.turbine.TurbineAggregatorProperties;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.stereotype.Service;

/**
 * @author: mmy
 * @date: 2018/06/15
 * @description:
 */
@Service
public class TurbinePropertyService {

  @Autowired
  TurbineProperties turbineProperties;
  @Autowired
  TurbineAggregatorProperties turbineAggregatorProperties;
  @Autowired
  TurbineConfigureService turbineConfigureService;

  private final Joiner joiner = Joiner.on(TurbineConstants.STRING_SEPARATE_COMMA);
  private final Splitter splitter = Splitter.on(TurbineConstants.STRING_SEPARATE_COMMA)
      .omitEmptyStrings();

  public String addTurbineAppConfig(String serviceIds){
    List turbineAppConfig = turbineProperties.getAppConfigList();
    List appList;
    if (null==turbineAppConfig){
      appList = new ArrayList<String>();
    }else {
      appList = new ArrayList<String>(turbineAppConfig);
    }
    List<String> serviceIdList = convertToList(serviceIds);
    serviceIdList.stream()
        .filter(s->!appList.contains(s))
        .forEach(s->appList.add(s));
    String appConfigString = convertToString(appList);
    turbineProperties.setAppConfig(appConfigString);
    return  appConfigString;
  }

  public String deleteTurbineAppConfig(String serviceIds){
    List turbineAppConfig = turbineProperties.getAppConfigList();
    List appList;
    if (null==turbineAppConfig){
      appList = new ArrayList<String>();
    }else {
      appList = new ArrayList<String>(turbineAppConfig);
    }
    List<String> serviceIdList = convertToList(serviceIds);
    serviceIdList.stream()
        .filter(s->appList.contains(s))
        .forEach(s->appList.remove(s));
    String appConfigString = convertToString(appList);
    turbineProperties.setAppConfig(appConfigString);
    return  appConfigString;
  }

  public String addTurbineClusterConfig(String clusters){
    List<String> clusterConfigList = turbineAggregatorProperties.getClusterConfig();
    if (null==clusterConfigList){
      clusterConfigList = new ArrayList<>();
    }
    List<String> clusterList = convertToList(clusters);
    List<String> finalClusterConfigList = clusterConfigList;
    List<String> toAdd = new ArrayList<>();
    clusterList.stream()
        .filter(c->!finalClusterConfigList.contains(c))
        .forEach(c->toAdd.add(c));
    clusterConfigList.addAll(toAdd);
    turbineConfigureService.loadClusterMonitor(toAdd);
    turbineAggregatorProperties.setClusterConfig(clusterConfigList);
    return convertToString(clusterConfigList);
  }

  public String deleteTurbineClusterCongfig(String clusters){
    List<String> clusterConfigList = turbineAggregatorProperties.getClusterConfig();
    List<String> clusterList = convertToList(clusters);
    List<String> toRemove = new ArrayList<>();
    clusterList.stream()
        .filter(c->clusterConfigList.contains(c))
        .forEach(c->toRemove.add(c));
    clusterConfigList.removeAll(toRemove);
    turbineConfigureService.removeClusterMonitor(toRemove);
    turbineAggregatorProperties.setClusterConfig(clusterConfigList);
    return convertToString(clusterConfigList);
  }






  private List convertToList(String string) {
    return splitter.splitToList(string);
  }

  private String convertToString(List list) {
    return joiner.join(list);
  }
}
