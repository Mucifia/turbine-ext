package com.mmy.example.turbine.configuration;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.turbine.discovery.Instance;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EurekaInstanceDiscovery;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author: mmy
 * @date: 2018/05/28
 * @description: 定义自己的InstanceDiscovery
 */
//@Component   需要覆盖EurekaInstanceDiscovery时打开注解
public class EurekaInstanceDiscoveryExt  extends EurekaInstanceDiscovery{
  private static final Log log = LogFactory.getLog(EurekaInstanceDiscovery.class);

  private  EurekaClient eurekaClient;

  public EurekaInstanceDiscoveryExt(
      TurbineProperties turbineProperties,
      EurekaClient eurekaClient) {
    super(turbineProperties, eurekaClient);
    this.eurekaClient = eurekaClient;
  }

  @Override
  protected List<Instance> getInstancesForApp(String serviceId) throws Exception {
    List<Instance> instances = new ArrayList<>();
    log.info("Fetching instances for app: " + serviceId);
    Application app = eurekaClient.getApplication(serviceId);
    if (app == null) {
      log.warn("Eureka returned null for app: " + serviceId);
      return instances;
    }
    try {
      List<InstanceInfo> instancesForApp = app.getInstances();
      if (instancesForApp != null) {
        log.info("Received instance list for app: " + serviceId + ", size="
            + instancesForApp.size());
        for (InstanceInfo iInfo : instancesForApp) {
          Instance instance = marshall(iInfo);
          if (instance != null) {
            instances.add(instance);
          }
        }
      }
    }
    catch (Exception e) {
      log.warn("Failed to retrieve instances from Eureka", e);
    }
    return instances;
  }

  /**
   * 自定义将eureka InstanceInfo转变成turbine Instance的方法
   * @param instanceInfo
   * @return
   */
  Instance marshall(InstanceInfo instanceInfo) {
    //Override this method for your own implementation for parsing Eureka info.
    return null;
  }


  @Override
  protected String getClusterName(Object object) {
    //自定义集群定义规则
    return null;
  }



}
