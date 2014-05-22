/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package com.github.nickvl.xspring.core.log.aop.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.github.nickvl.xspring.core.log.aop.annotation.LogDebug;
import com.github.nickvl.xspring.core.log.aop.annotation.LogException;
import com.github.nickvl.xspring.core.log.aop.annotation.LogPoint;
import com.github.nickvl.xspring.core.log.aop.annotation.LogTrace;
import com.github.nickvl.xspring.core.log.aop.annotation.Lp;

import static com.github.nickvl.xspring.core.log.aop.annotation.LogException.Exc;

/**
 * Simple service, implementation of {@link FooService}.
 */
@Service
public class SimpleFooService implements FooService {

    @LogDebug(LogPoint.IN)
    @LogTrace
    @Override
    public void voidMethodZero() {
        // nothing to do
    }

    @Override
    public String stringMethodOne(String first) {
        return "stringMethodOne:" + first;
    }

    @LogDebug
    @Override
    public String stringMethodTwo(String first, @Lp String second) {
        return "stringMethodTwo:" + first + ":" + second;
    }

    @LogDebug
    @Override
    public String stringMethodThree(String first, String second, String third) {
        return "stringMethodThree:" + first + ":" + second + ":" + third;
    }

    @LogTrace
    @LogException
    @Override
    public String stringMethodTwoVarargs(String first, @Lp String... second) {
        return "stringMethodTwoVarargs:" + first + ":" + Arrays.toString(second);
    }

    @LogDebug
    @LogException(value = {@Exc(value = Exception.class, stacktrace = true)}, warn = {@Exc({IllegalArgumentException.class, IOException.class})})
    @Override
    public void voidExcMethodZero() throws IOException {
        throw new IOException("io fail");
    }
}
