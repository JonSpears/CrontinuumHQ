package com.jonspears.schedulerservice.util;

import com.jonspears.schedulerservice.exception.LockConfigurationException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/** Utility class for evaluating Spring Expression Language (SpEL) expressions
 *  in the context of AOP join points.
 */
public class SpelEvaluationUtils {

    private SpelEvaluationUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    public static String evaluate(String expression, JoinPoint jp) {
        if (expression.isEmpty()) {
            return "default-resource";
        }

        try {
            // Create evaluation context
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("args", jp.getArgs());

            // Extract parameter names (requires -parameters compiler flag)
            Object[] args = jp.getArgs();
            String[] paramNames = extractParameterNames(jp, args);

            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }

            // Parse and evaluate
            Expression exp = PARSER.parseExpression(expression);
            return exp.getValue(context, String.class);
        } catch (Exception e) {
            throw new LockConfigurationException(
                    "Failed to evaluate lock expression: " + expression, e
            );
        }

    }

    private static String[] extractParameterNames(JoinPoint jp, Object[] args) {
        String[] paramNames = ((MethodSignature) jp.getSignature()).getParameterNames();

        if (paramNames == null || paramNames.length == 0) {
            throw new IllegalArgumentException("No parameter names found. Ensure you compile with -parameters flag.");
        }
        if (args.length != paramNames.length) {
            throw new IllegalArgumentException("Parameter count mismatch: " + args.length + " args but " + paramNames.length + " parameters.");
        }
        return paramNames;
    }
}
