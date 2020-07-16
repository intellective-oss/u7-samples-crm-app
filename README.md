# Unity 7 Sample CRM Application

Build in development mode (no JS obfuscation/minimization):
```
mvn clean package -P web-dev-mode
```
Build for production with Docker image:
```
mvn clean package -P docker
```