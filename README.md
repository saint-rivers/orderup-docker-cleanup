# Control Tower

## Setup

Clone the main repo.

```bash
git clone https://gitlab.com/kshrd-10th-generation-ite/advanced-course/we_order/we-order-api.git
```

I've added the config-server configuration in a separate git repo but included it as a git submodule.

```bash
git submodule update --init --recursive
```

You will find the YAML configuration files in the `control-tower-config` folder.

Start all project with this command.

```bash
./deploy.sh
```




## To run application the first time must to running zipkin 
+ first step : config-server
+ second step : eureka (optional)
+ third step :each service (user-service ...)

+ Noted (for keycloak-client must to run with user-service together) ,not container for "localhost" and for container run as "container name" at the wabclient of user-service

+ if u want to "mail not container" : (need to have rabbitmq-local first in docker-compose) and "profile is dev" 
+ else run "mail as container" don't run "rabbitmq-local" but just run "rabbitmq" and "profile is docker"


