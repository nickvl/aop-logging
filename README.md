aop-logging
===========

Declarative annotation based logging in Java Spring applications.
It is a small logging utility. Spring AOP is used for implementation.
Apache commons-logging is used to log messages (the same component used in Spring framework itself).

It allows to flexible configure log message levels, provides exception handling taking into account 
exception classes hierarchy (alike try-catch). Log annotations could be applied for both methods and classes.
This logger is capable to log method parameters and result using reflection if the corresponding classes
do not provide toString() method.

Quick start
-----------

### Add the dependency to your maven pom.xml

    <dependencies>
    ...
      <dependency>
        <groupId>net.ng.xspring</groupId>
        <artifactId>aop-logging</artifactId>
        <version>0.2.2</version>
      </dependency>
    ...
    </dependencies>

### Apply the logging utility in your project

1.Activates the logger in spring's context

    <?xml version="1.0" encoding="UTF-8"?>
    <beans
            xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:aop-logger="urn:nickvl/xspring/aop-logger"
            xsi:schemaLocation="
            http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            urn:nickvl/xspring/aop-logger urn:nickvl/xspring/aop-logger/aop-logger.xsd">

        <!-- Activates the logger and @AspectJ style of Spring AOP. There are additional configuration options. -->
        <aop-logger:annotation-logger/>
        ...
    </beans>

2.Add log annotation on required methods


    package com.me.shop;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.ws.server.endpoint.annotation.Endpoint;
    import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
    import org.springframework.ws.server.endpoint.annotation.RequestPayload;
    import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

    import com.me.shop.oxm.PaymentContract;
    import com.me.shop.shop.oxm.PaymentContractResponse;
    import com.me.shop.shop.NotEnoughMoneyException;

    import net.ng.xspring.core.log.aop.annotation.LogDebug;
    import net.ng.xspring.core.log.aop.annotation.LogInfo;

    /**
     * Billing shop endpoint.
     */
    @LogDebug
    @Endpoint
    public class BillingShopEndpoint {

        private static final String NS = "urn:PaycashShopService";

        @Autowired
        private ShopService shop;

        @LogInfo
        @LogException(value = {@Exc(value = Exception.class, stacktrace = true)}, warn = {@Exc({IllegalArgumentException.class, NotEnoughMoneyException.class})})
        @ResponsePayload
        @PayloadRoot(localPart = "PaymentContract", namespace = NS)
        public PaymentContractResponse processPaymentContract(@RequestPayload PaymentContract request) {
            return shop.checkPayment(request);
        }

        // other methods
    }

3.Configure logging in your application

### Example

Commons logging configured to log using log4j framework:

    2013-01-26 22:26:44,045 TRACE [net.ng.xspring.core.log.aop.benchmark.LoggableServiceImpl] (main) - calling: aopLogMethod(2 arguments: b=33)
    2013-01-26 22:26:44,046 TRACE [net.ng.xspring.core.log.aop.benchmark.LoggableServiceImpl] (main) - returning: aopLogMethod(2 arguments):34

### Performance measuring

A simple test shows the following results:

    Running net.ng.xspring.core.log.aop.benchmark.AOPLoggerPerformanceITCase
    Service invocation benchmark (AOPLoggerPerformanceITCase):
        600 ns takes a method when no logging is used
        1053 ns takes a method when direct logging is used
        5263 ns takes a method when aop logging is used

    Running net.ng.xspring.core.log.aop.benchmark.UniversalLogAdapterPerformanceITCase
    Building toString value benchmark (UniversalLogAdapterPerformanceITCase):
        2979 ns takes a method when reflection is used
        98 ns takes a method when overridden toString is used
