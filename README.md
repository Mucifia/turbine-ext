Tubrine ext
===

Dynamic change setting in runtime

Restful
------
  #### /appconfig
  method = POST , Content-Type ：application/json  , body : appserviceId

  #### /appconfig/{serviceId}
  method = DELETE , Content-Type : application/json , Pathvariable : appserviceId

  #### /clusterconfig
  method = POST , Content-Type ：application/json  , body : appserviceId

  #### /clusterconfig/{clustername}
  method = DELETE , Content-Type : application/json , Pathvariable : clustername

  #### /clusterNameExpression
  method = PUT , Content-Type : application/json , body : clusterNameExpression

  #### /stop
  method = GET , Content-Type : application/json , 

参考链接
------
1) http://www.ityouknow.com/springcloud/2017/05/18/hystrix-dashboard-turbine.html
2) https://github.com/Netflix/Turbine/wiki

3) https://www.jianshu.com/p/b7b20fc09ca9

