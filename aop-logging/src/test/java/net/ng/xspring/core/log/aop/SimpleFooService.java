/***********************************************************************************
 * Copyright (c) 2013. Nickolay Gerilovich. Russia.
 *   Some Rights Reserved.
 ************************************************************************************/

package net.ng.xspring.core.log.aop;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import net.ng.xspring.core.log.aop.annotation.LogDebug;
import net.ng.xspring.core.log.aop.annotation.LogException;
import net.ng.xspring.core.log.aop.annotation.LogPoint;
import net.ng.xspring.core.log.aop.annotation.LogTrace;
import net.ng.xspring.core.log.aop.annotation.Lp;

import static net.ng.xspring.core.log.aop.annotation.LogException.Exc;

/**
 * Simple service.
 */
@Service("simpleFooService")
public class SimpleFooService implements FooService {

    @LogDebug
    @LogTrace
    @Override
    public void voidMethodZero() {
        // nothing to do
    }

    @LogDebug(LogPoint.OUT)
    @Override
    public void voidMethodOne(String first) {
        // nothing to do
    }

    @LogDebug
    @Override
    public String stringMethodZero() {
        return "stringMethodZero:";
    }

    @LogDebug
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
