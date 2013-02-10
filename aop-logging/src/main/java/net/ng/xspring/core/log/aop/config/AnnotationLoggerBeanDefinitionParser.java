/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop.config;

import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import net.ng.xspring.core.log.aop.AOPLogger;
import net.ng.xspring.core.log.aop.SimpleLogAdapter;
import net.ng.xspring.core.log.aop.UniversalLogAdapter;

/**
 * Implements {@link AbstractBeanDefinitionParser} which configures aop logger.
 */
public class AnnotationLoggerBeanDefinitionParser extends AbstractBeanDefinitionParser {

    private static final String NET_NG_XSPRING_CORE_LOG_AOP_INTERNAL_AOPLOGGER_NAME = "net.ng.xspring.core.log.aop.internal.aoplogger.name";
    private static final String CONFIG = "config";
    private static final String TO_STRING = "to-string";
    private static final String REFLECTION_TO_STRING = "reflection-to-string";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        // AopNamespaceUtils.registerAutoProxyCreatorIfNecessary(parserContext, element);
        AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(parserContext, element);
        if (parserContext.getRegistry().containsBeanDefinition(NET_NG_XSPRING_CORE_LOG_AOP_INTERNAL_AOPLOGGER_NAME)) {
            return null;
        }
        return parseLoggerElement(element);
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        return NET_NG_XSPRING_CORE_LOG_AOP_INTERNAL_AOPLOGGER_NAME;
    }

    private AbstractBeanDefinition parseLoggerElement(Element element) {
        BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(AOPLogger.class);
        factory.addPropertyValue("logAdapter", parseLogAdapter(element));
        return factory.getBeanDefinition();
    }

    private BeanDefinition parseLogAdapter(Element element) {
        Element configElement = DomUtils.getChildElementByTagName(element, CONFIG);
        if (configElement == null) {
            return getUniversalLogAdapterBeanDefinition(null);
        }

        if (DomUtils.getChildElementByTagName(configElement, TO_STRING) != null) {
            return getSimpleLogAdapterBeanDefinition();
        }
        Element universalLogAdapterElement = DomUtils.getChildElementByTagName(configElement, REFLECTION_TO_STRING);
        return getUniversalLogAdapterBeanDefinition(universalLogAdapterElement);
    }

    private BeanDefinition getUniversalLogAdapterBeanDefinition(Element universalLogAdapterElement) {
        BeanDefinitionBuilder logAdapter = BeanDefinitionBuilder.genericBeanDefinition(UniversalLogAdapter.class);
        String skipNullFields = universalLogAdapterElement.getAttribute("skip-null-fields");
        logAdapter.addConstructorArgValue(Boolean.valueOf(skipNullFields));
        Attr crop = universalLogAdapterElement.getAttributeNode("multi-element-structure-crop-threshold");
        if (crop != null) {
            logAdapter.addConstructorArgValue(Integer.valueOf(crop.getValue()));
        }
        logAdapter.addConstructorArgValue(null);
        return logAdapter.getBeanDefinition();
    }

    private BeanDefinition getSimpleLogAdapterBeanDefinition() {
        BeanDefinitionBuilder logAdapter = BeanDefinitionBuilder.genericBeanDefinition(SimpleLogAdapter.class);
        return logAdapter.getBeanDefinition();
    }
}