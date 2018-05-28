package com.mmy.example.turbine.configuration;

import org.springframework.cloud.netflix.turbine.SpringAggregatorFactory;
import org.springframework.cloud.netflix.turbine.TurbineClustersProvider;
import org.springframework.stereotype.Component;

/**
 * @author: mmy
 * @date: 2018/05/28
 * @description: 自定义ClusterMonitorFactory
 */
//@Component
public class SpringAggregatorFactoryExt extends SpringAggregatorFactory {

  public SpringAggregatorFactoryExt(
      TurbineClustersProvider clustersProvider) {
    super(clustersProvider);
  }

  @Override
  public void initClusterMonitors(){
    //自己的初始化集群操作
  }

  @Override
  public void shutdownClusterMonitors(){
    //集群终止
  }
}
