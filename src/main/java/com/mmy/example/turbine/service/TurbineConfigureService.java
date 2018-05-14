package com.mmy.example.turbine.service;

import com.netflix.turbine.data.AggDataFromCluster;
import com.netflix.turbine.discovery.Instance;
import com.netflix.turbine.handler.PerformanceCriteria;
import com.netflix.turbine.handler.TurbineDataHandler;
import com.netflix.turbine.monitor.cluster.AggregateClusterMonitor;
import com.netflix.turbine.monitor.cluster.ClusterMonitor;
import com.netflix.turbine.monitor.cluster.ClusterMonitorFactory;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.turbine.SpringAggregatorFactory;
import org.springframework.stereotype.Service;

/**
 * @author: mmy
 * @date: 2018/05/02
 * @description:
 */
@Service
public class TurbineConfigureService {

  private static final Log log = LogFactory.getLog(SpringAggregatorFactory.class);

  @Autowired
  ClusterMonitorFactory<AggDataFromCluster> clusterClusterMonitorFactory;

  private PerformanceCriteria nonCriticalCriteria = new PerformanceCriteria() {

    @Override
    public boolean isCritical() {
      return false;
    }

    @Override
    public int getMaxQueueSize() {
      return 0;
    }

    @Override
    public int numThreads() {
      return 0;
    }

  };
  private TurbineDataHandler<AggDataFromCluster> staticListener = new TurbineDataHandler<AggDataFromCluster>() {

    @Override
    public String getName() {
      return "StaticListener_For_Aggregator";
    }

    @Override
    public void handleData(Collection<AggDataFromCluster> stats) {
    }

    @Override
    public void handleHostLost(Instance host) {
    }

    @Override
    public PerformanceCriteria getCriteria() {
      return TurbineConfigureService.this.nonCriticalCriteria;
    }

  };

  public void loadClusterMonitor(List<String> clusterList) {
    if (clusterClusterMonitorFactory instanceof SpringAggregatorFactory) {
      for (String clusterName : clusterList) {
        ClusterMonitor<AggDataFromCluster> clusterMonitor = (ClusterMonitor<AggDataFromCluster>) SpringAggregatorFactory
            .findOrRegisterAggregateMonitor(clusterName);
        clusterMonitor
            .registerListenertoClusterMonitor(TurbineConfigureService.this.staticListener);
        try {
          clusterMonitor.startMonitor();
        } catch (Exception ex) {
          log.warn("Could not init cluster monitor for: " + clusterName);
          clusterMonitor.stopMonitor();
          clusterMonitor.getDispatcher().stopDispatcher();
        }
      }
    }
  }

  /**
   * @param clusterList
   */
  public void removeClusterMonitor(List<String> clusterList) {
    if (clusterClusterMonitorFactory instanceof SpringAggregatorFactory) {
      for (String clusterName : clusterList) {
        ClusterMonitor<AggDataFromCluster> clusterMonitor = (ClusterMonitor<AggDataFromCluster>) AggregateClusterMonitor
            .findOrRegisterAggregateMonitor(clusterName);
        clusterMonitor.stopMonitor();
        clusterMonitor.getDispatcher().stopDispatcher();
      }
    }
  }
}
