aop-logging
===========

Declarative annotation based logging in Java Spring applications.
It is a small logging utility. Spring AOP is used for implementation.
Apache commons-logging is used to log messages (the same component used in Spring framework itself).

It allows to flexible configure log message levels, provides exception handling taking into account 
exception classes hierarchy (alike try-catch). Log annotations could be applied for both methods and classes.

Quick start
-----------

### Add the dependency to your maven pom.xml

    <dependencies>
    ...
      <dependency>
        <groupId>net.ng.xspring</groupId>
        <artifactId>aop-logging</artifactId>
        <version>0.1.2</version>
      </dependency>
    ...
    </dependencies>

### Apply the logging utility in your project

1.Add bean definition

`<bean class="net.ng.xspring.core.log.aop.AOPLogger"/>`

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
        561 ns takes a method when no logging is used
        1008 ns takes a method when direct logging is used
        4794 ns takes a method when aop logging is used
