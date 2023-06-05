# OAuth2 with OIDC playground
This project runs 3 servers:
- Authorization Server
- Resource Server
- Client Application

The code originates from https://github.com/spring-projects/spring-authorization-server/tree/main/samples with some slight modifications.

# Authorization Code Flow
![Authorization Code Flow](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/maddingo/oauth2-playground/develop/doc/pkce.puml)

# Client Credential Flow
![Client Secret Flow](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/maddingo/oauth2-playground/develop/doc/client-credentials.puml)


# Deploy to local Maven Repository
```shell
mvn clean deploy -DaltDeploymentRepository=local::file://${HOME}/tmp/maven-local 
```
# Start Docker Registry
```shell
docker run -d -p 5000:5000 --restart=always --name registry registry:2
``` 

# Deploy to local Docker registry
```shell
mvn clean deploy -Dspring-boot.build-image.publish=true -Ddocker.image.registry=localhost:5000
```

# Spring Boot and Rest
See: https://developer.okta.com/blog/2022/06/17/simple-crud-react-and-spring-boot

```shell
npx create-react-app frontend
cd frontend
npm i bootstrap react-cookie react-router-dom reactstrap

# At this point there were 6 high severity vulnerabilities
npm audit fix --force
# now there are 79 vulnerabilities (41 high, 6 critical), skip the audit for now
```
