package aspects;

import annotations.ProfileAction;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

/**
 * User: dmitry
 * Date: 29.01.12
 * Time: 19:10
 */

@Aspect
public class ProfileAspect {

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() {}

    @Around("anyPublicMethod() && @annotation(profileAction)")
    public Object profile(ProceedingJoinPoint pjp, ProfileAction profileAction) throws Throwable {
        Class targetClass = pjp.getTarget().getClass();
        Logger logger =  Logger.getLogger(targetClass);

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object result = pjp.proceed();
        stopWatch.stop();

        String operationName = pjp.getSignature().getName();

        logger.info("---------------------------------------");
        logger.info("The target is: " + targetClass);
        logger.info("The operation " + operationName + "(...) lasted "
                + stopWatch.getTotalTimeSeconds() + " seconds");
        logger.info("---------------------------------------");

        return result;
    }

}
