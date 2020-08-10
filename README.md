# [Unity 7 Customization Tutorial](https://intellectivelab.github.io/u7-samples-crm-app/)

This tutorial implements a very basic application that uses only simple document management capabilities of 
[Unity 7](https://www.intellective.com/unity) and
[IBM FileNet Content Manager](https://www.ibm.com/products/filenet-content-manager).

Build in development mode (no JS obfuscation/minimization):
```
mvn clean package -P web-dev-mode
```
Build for production with Docker image:
```
mvn clean package -P docker
```