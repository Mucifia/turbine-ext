package com.mmy.example.turbine.api;

import com.mmy.example.turbine.constants.TurbineConstants;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: mmy
 * @date: 2018/04/24
 * @description:
 */
public interface ITurbineConfigureService {

  /**
   * 更改turbine.cluster-name-expression值，作用为turbine使用此表达式判断实例属于哪个集群
   *
   * @param expression 例如meta['cluster'] 即Eureka元数据中cluster的值为所属集群
   * @return success or fail
   */
  @PutMapping(value = TurbineConstants.PATH_CLUSTERNAMEEXPRESSION, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String updateClusterNameExpressionConfig(@RequestBody String expression);

  /**
   * 在Turbine Server上新增监控应用 相关初始配置 ： turbine.app-config: node01,node02
   *
   * @param serviceIds 一个或多个serviceId，使用逗号(,)间隔
   * @return success or fail
   */
  @PostMapping(value = TurbineConstants.PATH_APPCONFIG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String addTurbineAppConfigItem(@RequestBody String serviceIds);

  /**
   * 在Turbine Server上删除监控应用 相关初始配置 ： turbine.app-config: node01,node02
   *
   * @param serviceIds 一个或多个serviceId，使用逗号(,)间隔
   * @return success or fail
   */
  @DeleteMapping(value = TurbineConstants.REST_PATH_APPCONFIG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String deleteTurbineAppConfigItem(@PathVariable String serviceIds);

  /**
   * 在Turbine Server上新增所需监控的集群 相关初始配置 ： turbine.aggregator.cluster-config: default
   *
   * @param clusters 一个或多个clusterName，使用逗号(,)间隔
   * @return success or fail
   */
  @PostMapping(value = TurbineConstants.PATH_CLUSTERCONFIG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String addTurbineClusterConfig(@RequestBody String clusters);

  /**
   * 在Turbine Server上删除所需监控的集群 相关初始配置 ： turbine.aggregator.cluster-config: default
   *
   * @param clusters 一个或多个clusterName，使用逗号(,)间隔
   * @return success or fail
   */
  @DeleteMapping(value = TurbineConstants.REST_PATH_CLUSTERCONFIG, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String deleteTurbineClusterConfig(@PathVariable String clusters);

  /**
   * 停止Turbine监控 由于spring-boot-starter-actuator的shutdown模式由于Turbine监控未关闭而退出失败，则暴露一个关闭Turbine监控接口，需要spring-security验证
   *
   * @return success or fail
   */
  @GetMapping(value = TurbineConstants.REST_PATH_CLUSTERSTOP, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String turbineClusterStop();


}
