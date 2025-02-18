simulation:
  log:
    action:
      target: mysql
      auto-log: true
  container:
    time-unit: minutes
    start-time: "2025-01-01 08:00:00"
    turn-mill-times: 5000

spring:
  data:
    redis:
      host: ${host}
      port: ${port}
      password: ${redis_pwd}
      database: ${databse}

  datasource:
    url: ${datasource_url}
    username: ${datasource_user_name}
    password: ${datasource_user_pwd}
logging:
  level:
    org.mysim.*: DEBUG