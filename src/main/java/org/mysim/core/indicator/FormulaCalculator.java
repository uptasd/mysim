package org.mysim.core.indicator;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
public class FormulaCalculator implements IndicatorCalculator{
    String serviceName;
    String formula;

    @Override
    public Object calculate() {
        try {
            String resolvedFormula = resolveVariables(formula);
            return evaluateExpression(resolvedFormula);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    private String resolveVariables(String formula) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 正则表达式匹配 ${variable} 格式的占位符
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(formula);

        StringBuilder resolvedFormula = new StringBuilder();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            // 从 IndicatorFactory 中获取变量的值
            Object value = IndicatorFactory.getIndicator(serviceName, variableName);
            // 替换占位符为实际值
            matcher.appendReplacement(resolvedFormula, value.toString());
        }
        matcher.appendTail(resolvedFormula);

        return resolvedFormula.toString();
    }
    private Object evaluateExpression(String expression) {
        try {
            // 使用 exp4j 计算表达式
            Expression exp = new ExpressionBuilder(expression).build();
            double result = exp.evaluate();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to evaluate expression: " + expression, e);
        }
    }

}
