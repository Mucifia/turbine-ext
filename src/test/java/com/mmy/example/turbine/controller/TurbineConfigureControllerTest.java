package com.mmy.example.turbine.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.google.common.base.Joiner;
import com.mmy.example.turbine.bootstrap.ApplicationStartup;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.turbine.TurbineAggregatorProperties;
import org.springframework.cloud.netflix.turbine.TurbineProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


/**
 * @author: mmy
 * @date: 2018/05/04
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationStartup.class)
@FixMethodOrder(MethodSorters.DEFAULT)
public class TurbineConfigureControllerTest {

  @Autowired
  TurbineAggregatorProperties turbineAggregatorProperties;
  @Autowired
  TurbineProperties turbineProperties;
  Joiner joiner = Joiner.on(",");
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Before
  public void init() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }


  @Test
  public void test_AppConfig_Insert() throws Exception {
    List<String> appConfigList = turbineProperties.getAppConfigList();
    Assert.assertTrue(!appConfigList.contains("test_serviceId"));
    MvcResult mvcResult = mockMvc.perform(post("/appconfig").content("test_serviceId")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    String resString = mvcResult.getResponse().getContentAsString();
    Assert.assertEquals(200, status);
    Assert.assertEquals("success", resString);
    appConfigList = turbineProperties.getAppConfigList();
    Assert.assertTrue(appConfigList.contains("test_serviceId"));

  }

  @Test
  public void test_AppConfig_Delete() throws Exception {
    List<String> appConfigList = new ArrayList<>(turbineProperties.getAppConfigList());
    appConfigList.add("test_serviceId");
    turbineProperties.setAppConfig(joiner.join(appConfigList));
    appConfigList = turbineProperties.getAppConfigList();
    Assert.assertTrue(appConfigList.contains("test_serviceId"));
    MvcResult mvcResult = mockMvc.perform(delete("/appconfig/{1}", "test_serviceId")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    String resString = mvcResult.getResponse().getContentAsString();
    Assert.assertEquals(200, status);
    Assert.assertEquals("success", resString);
    appConfigList = turbineProperties.getAppConfigList();
    Assert.assertTrue(!appConfigList.contains("test_serviceId"));
  }

  @Test
  public void test_Cluster_Insert() throws Exception {
    List<String> clusterList = turbineAggregatorProperties.getClusterConfig();
    Assert.assertTrue(!clusterList.contains("test_clusterId"));
    MvcResult mvcResult = mockMvc.perform(
        post("/clusterconfig").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content("test_clusterId")).andReturn();
    int status = mvcResult.getResponse().getStatus();
    String resString = mvcResult.getResponse().getContentAsString();
    Assert.assertEquals(200, status);
    Assert.assertEquals("success", resString);
    clusterList = turbineAggregatorProperties.getClusterConfig();
    Assert.assertTrue(clusterList.contains("test_clusterId"));
  }

  @Test
  public void test_Cluster_Delete() throws Exception {
    List<String> clusterList = turbineAggregatorProperties.getClusterConfig();
    clusterList.add("test_clusterId");
    turbineAggregatorProperties.setClusterConfig(clusterList);
    clusterList = turbineAggregatorProperties.getClusterConfig();
    Assert.assertTrue(clusterList.contains("test_clusterId"));
    MvcResult mvcResult = mockMvc.perform(delete("/clusterconfig/{1}", "test_clusterId")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andReturn();
    int status = mvcResult.getResponse().getStatus();
    String resString = mvcResult.getResponse().getContentAsString();
    Assert.assertEquals(200, status);
    Assert.assertEquals("success", resString);
    clusterList = turbineAggregatorProperties.getClusterConfig();
    Assert.assertTrue(!clusterList.contains("test_serviceId"));
  }

  @Test
  public void test_ClusterExpression_Update() throws Exception {
    String clusterExpression = turbineProperties.getClusterNameExpression();
    Assert.assertTrue(!StringUtils.equals(clusterExpression, "test_clusterNameExpression"));
    MvcResult mvcResult = mockMvc.perform(
        put("/clusterNameExpression").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content("test_clusterNameExpression")).andReturn();
    int status = mvcResult.getResponse().getStatus();
    String resString = mvcResult.getResponse().getContentAsString();
    Assert.assertEquals(200, status);
    Assert.assertEquals("success", resString);
    clusterExpression = turbineProperties.getClusterNameExpression();
    Assert.assertTrue(StringUtils.equals(clusterExpression, "test_clusterNameExpression"));
  }
}
