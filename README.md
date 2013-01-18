aop-logging
===========

Declarative annotation based logging in Java Spring applications.

Quick start
-----------

1.Add bean defenition

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