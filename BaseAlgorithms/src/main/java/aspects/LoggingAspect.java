package aspects;

import annotations.LogAction;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * User: dmitry
 * Date: 29.01.12
 * Time: 19:46
 */

@Aspect
public class LoggingAspect {

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() {}

    @Before("anyPublicMethod() && @annotation(logAction)")
    public void log(JoinPoint pjp, LogAction logAction) {
        Class targetClass = pjp.getTarget().getClass();
        Logger logger = Logger.getLogger(targetClass);

        logger.info("--------------------------------------------------");
        logger.info("The target is: " + targetClass);

        final String comments = logAction.comments();
        logger.info("The comments are: " + comments);
        logger.info("--------------------------------------------------");
    }
}
