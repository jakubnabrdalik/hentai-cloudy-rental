# Hentai rental: your porn on somebody else's computer (cloud)

This is a sample project for Codepot.pl 2015 workshops, on Spring Core, Boot, Cloud and pr0n.

To learn more, look at the commit history and description below.



	                        .s$$$Ss.
	            .8,         $$$. _. .              ..sS$$$$$"  ...,.;
	 o.   ,@..  88        =.$"$'  '          ..sS$$$$$$$$$$$$s. _;"'
	  @@@.@@@. .88.   `  ` ""l. .sS$$.._.sS$$$$$$$$$$$$S'"'
	   .@@@q@@.8888o.         .s$$$$$$$$$$$$$$$$$$$$$'
	     .:`@@@@33333.       .>$$$$$$$$$$$$$$$$$$$$'
	     .: `@@@@333'       ..>$$$$$$$$$$$$$$$$$$$'
	      :  `@@333.     `.,   s$$$$$$$$$$$$$$$$$'
	      :   `@33       $$ S.s$$$$$$$$$$$$$$$$$'
	      .S   `Y      ..`  ,"$' `$$$$$$$$$$$$$$
	      $s  .       ..S$s,    . .`$$$$$$$$$$$$.
	      $s .,      ,s ,$$$$,,sS$s.$$$$$$$$$$$$$.
	      / /$$SsS.s. ..s$$$$$$$$$$$$$$$$$$$$$$$$$.
	     /`.`$$$$$dN.ssS$$'`$$$$$$$$$$$$$$$$$$$$$$$.
	    ///   `$$$$$$$$$'    `$$$$$$$$$$$$$$$$$$$$$$.
	   ///|     `S$$S$'       `$$$$$$$$$$$$$$$$$$$$$$.
	  / /                      $$$$$$$$$$$$$$$$$$$$$.
	                           `$$$$$$$$$$$$$$$$$$$$$s.
	                            $$$"'        .?T$$$$$$$
	                           .$'        ...      ?$$#\
	                           !       -=S$$$$$s
	                         .!       -=s$$'  `$=-_      :
	                        ,        .$$$'     `$,       .|
	                       ,       .$$$'          .        ,
	                      ,     ..$$$'
	                          .s$$$'                 `s     .
	                   .   .s$$$$'                    $s. ..$s
	                  .  .s$$$$'                      `$s=s$$$
	                    .$$$$'                         ,    $$s
	               `   " .$$'                               $$$
	               ,   s$$'                              .  $$$s
	            ` .s..s$'                                .s ,$$
	             .s$$$'                                   "s$$$,
	          -   $$$'                                     .$$$$.
	        ."  .s$$s                                     .$',',$.
	        $s.s$$$$S..............   ................    $$....s$s......
	         `""'           `     ```"""""""""""""""         `""   ``
	                                                           [banksy]dp  

## Basic info

### Authentication/Authorization

Basic auth

For development:
user = test
password = test

(all sample data are in data.sql)

## Functional

### Film catalogue

Url: http://localhost:<PORT>/films{?page,size,sort}

### Viewing your rentals

Url: http://localhost:<PORT>/rents/{rentId}

Warning: you have to be logged as the same user who's rental it is. 

### Renting

If your username doesn't match the logged user, you'll not be allowed.

Sample Post:
 
	POST /rents HTTP/1.1
	Host: localhost:8080
	Cache-Control: no-cache
	Content-Disposition: form-data; name="filmId"
	1
	Content-Disposition: form-data; name="numberOfDays"
	2
	Content-Disposition: form-data; name="username"
	test

### Returning

I assumed everyone can return the film, not only the person who rented (happened a lot back in the VHS days, with
my sisters/dad returning my films)

Sample Post:

	POST /returns HTTP/1.1
	Host: localhost:8080
	Cache-Control: no-cache
	Content-Disposition: form-data; name="rentId"
	3

# TUTORIAL

# Step by step

## start.spring.io ticks
  - Security
  - AOP
  - Web
  - Rest Repositories
  - HATEOAS
  - JPA
  - H2
  - Cloud Bootstrap
  - Config Client
  - Config Server
  - Eureka
  - Eureka Server
  - Feign 
  - Hystrix
  - Hystrix Dashboard
  - Actuator

## additional libs
  - gradle bootRun
  - http://localhost:8080/

## empty acceptance tests
  - Outcomes (empty tests do not run)
  - file:///home/jnb/workspace/codepot/hentai-cloudy-rental-final/build/reports/tests/index.html
  - Draw Rent & Film in a DB

## working single app
  - Walk over diagram.
  - Show no connection  between Film & Rent (FilmType being an enum - value object)
  - Show how FilmRepository provides Film
  - Show separate packages for film and rentals
  - DDD baby: https://msdn.microsoft.com/en-us/library/jj591560.aspx
  - Show separate configuration

  goto:
  - http://localhost:8080/
  - http://localhost:8080/films
  - Postman

  What do we need now to go distributed?

### Step 1: Separate API
  - Remove SpringDataWebSupport
  - Remove @RepositoryRestResource
  - Add Optional<Film> instead of Film + Fix RentCreator
  - Add FilmCatalogueController

### Step 2: add Feign
  - Feign: the Spring Data of JSON Rest
  - @EnableFeignClients (and build.gradle)
  - FilmCatalogueClient: declaration of Spring MVC again? Why it's important to copy&paste
  - Insert FilmCatalogueClient into RentCreator
  - Add FilmCatalogueClientStub for tests
  - Add bootstrap.yml with application name
  - Security: remove authentication from films
  - quirk: @Profile doesn't work on interfaces
  - Still, we have a dependency between bounded contexts: rentals uses the same classes as films

### Step 3: remove dependencies between bounded contexts

Easy peasy

### Step 4: cut out that fucker

Wooohaaaa!

## Hystrix
  - Suddenly, our app become very brittle. We have only a happy scenario.
  - Let's add hystrix to solve that.
  - @EnableHystrix & @EnableHystrixDashboard
  - security - open hystrix without authentication
  - create hystrix client
  - register hystrix command bean and aspect
  - change RentCreator to behave if failed
  - http://localhost:8080/hystrix.stream
  - http://localhost:8080/hystrix

## Config server & client
  
  We create ConfigServerApplication and client dependency + point to server from clients
  - config: http://localhost:8888/rentals/default
  - See it's being consumed: http://localhost:8080/env
  - Modify: subl /home/jnb/workspace/codepot/config/rentals.yml
  - Now you can see it in http://localhost:8888/rentals/default but not in http://localhost:8080/env. Why?
  - Add @RefreshScope
  - http://localhost:8080/env doesn't change
  - Post to /refresh
  - Now it works!  

## Eureka!
  - create the server app & add @EnableEurekaServer
  - @EnableDiscoveryClient to clients
  - add leaseRenewalIntervalInSeconds and defaultZone URL to props
  - change feign to use @FeignClient("FILMS")
  - goto Eureka server and see it working

## What we haven't covered
  - details of all the netflix libs
  - Spring Cloud Bus
  - Ribbon (client side load balancer)
  - Zuul (router/load balancer)
  - Turbine (hystrix dashboard over all apps)
  - Archaius (client side configuration)
  - setting up config in Eureka
  - OAuth2, config encryption/decryption, and securing this sucker
  - ZooKeeper and other 4finance/micro-infra/mgrzejszczak goodies 

# Other random cloud notes (or just read the docs)

### Cloud configuration
  Use the power of: https://github.com/spring-cloud/spring-cloud-starters

  Spring Cloud Context (bootstrap context, encryption, refresh scope and environment endpoints)

  Spring Cloud Commons (eg. Spring Cloud Netflix vs. Spring Cloud Consul)

  install the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files, for descryption/encryption


### Bootstrap context
  the parent of the most senior ancestor that you create yourself

  loading external configuration, decrypting properties

  shares Environment with the normal app

  properties: high precedence (PropertySourceLocator - cannot be overridden by local configuration) and low precedence (bootstrap.yml - can)

  uses bootstrap.yml or bootstrap.properties (also with profile suffix)

  sibling contexts in particular do not need to have the same profiles or property sources, even though they will share common things with their parent


### Basic config
  spring.application.name (in bootstrap.yml or application.yml)

  spring.cloud.bootstrap.name:  bootstrap.yml/properties file name (default "bootstrap") 

  spring.cloud.bootstrap.location: bootstrap.yml/properties location (default empty)

  behaves like normal properties (profile in Environment, etc.)

!

### Bootstrap customization
  /META-INF/spring.factories : org.springframework.cloud.bootstrap.BootstrapConfiguration

  comma-separated list of Spring @Configuration classes which will be used to create the context

  @Beans implementing ApplicationContextInitializer are added to main SpringApplication 

  do not double load via @ComponentScanned 

  implement @Bean PropertySourceLocator to load properties from wherever

!

### Refreshable configuration
  The application will listen for an EnvironmentChangedEvent and react to the change in a couple of standard ways 

  If you have a scaled-out client application then it is better to broadcast the EnvironmentChangedEvent to all the instances instead of having them polling for changes (e.g. using the Spring Cloud Bus).

  refreshing: via @ConfigurationProperties after EnvironmentChangedEvent, or @RefreshScope

  @RefreshScope: lazy proxies that (re)initialize when they are used (cache)

  call RefreshScope.refreshAll() to force them, or refresh(String) to choose one

  be carefull with @RefreshScope on @Configuration


!

### Spring Cloud Config Server
  add @EnableConfigServer to your new app, start it up  

  default implementation of the server storage: git  

  clones repo at spring.cloud.config.server.git.uri (local as cache)

  if you set it with a file: prefix it works from a local repository (doesn't clone)

  Environment is used to enumerate property sources and publish them via a JSON endpoint.

  supports a single or multiple git repositories

  supports health check of repo, encryption/decryption (values starting with {cipher}), HTTP basic
 
!

### Spring Cloud Config Server: files it reads  
  "label" is an optional git label (defaults to "master"), then it loads from:

  /{application}/{profile}[/{label}]  
  /{application}-{profile}.yml  
  /{label}/{application}-{profile}.yml  
  /{application}-{profile}.properties  
  /{label}/{application}-{profile}.properties

  goto: http://localhost:8888/rentals/dev
  refreshes automatically

!

### Spring Cloud Config Client
  add spring-cloud-config-client or better org.springframework.cloud:spring-cloud-starter-config  

  add @EnableAutoConfiguration to your @Configuration  

  When it runs it will pick up the external configuration from the default local config server on port 8888 if it is running.  

  change spring.cloud.config.uri default in bootstrap.properties

!


### Setting up Eureka
  Add @EnableEurekaServer

  Goto /eureka/*

  By default every Eureka server is also a Eureka client (clustering) and requires (at least one) service URL to locate a peer. If you want it to run in standalone and not complain:
  eureka.client.registerWithEureka=false  
  eureka.client.fetchRegistry=false  
  eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/  

!

### Reach config server via Eureka  
  Make Eureka call run before config
  spring.cloud.config.discovery.enabled=true

  spring.cloud.config.discovery.serviceId="CONFIGSERVER"

  Fail fast  
  spring.cloud.config.failFast=true

!

### Registering in Eureka


  You need to know just one address: the Eureka server. Default: localhost:8761

  A service registers name, host, port, health URL. Default: ${spring.application.name} and ${server.port}
  
  @EnableDiscoveryClient (or @EnableEurekaClient to be more specific) - register and discover

  A service sends heartbeat. If doesn't, considered dead and removed from registry. The status page and health indicators for a Eureka instance default to "/info" and "/health" respectively, default in a Spring Boot Actuator application.

  Several Eureka instances on one host: eureka.instance.metadataMap.instanceId=${spring.application.name}:${spring.application.instance_id:${random.value}}

  Speed up registration: eureka.instance.leaseRenewalIntervalInSeconds=30 (heartbeat on serviceUrl)

### Service discovery: Eureka
  You can use DiscoveryClient (both netflix and spring), RestTemplate or Feign


### Service-to-service calls: Feign
  @EnableFeignClients. 

  @FeignClient("stores") over your interface. Then standard @RequestMapping

  Uses Ribbon underneath, which can use Eureka.

### Client side Load Balancer: Ribbon

  When Eureka is used in conjunction with Ribbon the ribbonServerList is overridden with an extension of DiscoveryEnabledNIWSServerList which populates the list of servers from Eureka. 

  It also replaces the IPing interface with NIWSDiscoveryPing which delegates to Eureka to determine if a server is up. 

  By default the server list will be constructed with "zone" information as provided in the instance metadata (so on the client set eureka.instance.metadataMap.zone), and if that is missing it can use the domain name from the server hostname as a proxy for zone (if the flag approximateZoneFromHostname is set).
 
 
### Circuit brakers and resilient communication: Hystrix
  @EnableHystrix

  The @HystrixCommand is provided by a Netflix contrib library called "javanica". Spring Cloud automatically wraps Spring beans with that annotation in a proxy that is connected to the Hystrix circuit breaker. The circuit breaker calculates when to open and close the circuit, and what to do in case of a failure.

  What does it do besides circuit breaking: https://github.com/Netflix/Hystrix/wiki#how-does-hystrix-accomplish-its-goals
  https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png
  https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/soa-4-isolation-640.png

  Hystrix collapser - collapse several calls into one batch

### Hystrix Dashboard: local

  To run the Hystrix Dashboard annotate your Spring Boot main class with @EnableHystrixDashboard. You then visit /hystrix and point the dashboard to an individual instances /hystrix.stream endpoint in a Hystrix client application.

### Hystrix Dashboard: remote
  @EnableTurbine on a new project.

  Requires Eureka to work.
