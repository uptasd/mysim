package org.mysim.core.indicator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class IndicatorFactory {
    //serviceName:{indicatorId,IndicatorInfo}
    static Map<String, Map<String, IndicatorMetaInfo>> indicatorMetaInfoMap = IndicatorMetaInfoLoader.getIndicatorMetaInfoMap();

    public static Object getIndicator(String serviceName, String indicatorName) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!indicatorMetaInfoMap.containsKey(serviceName)) {
            return 0;
        }
        Map<String, IndicatorMetaInfo> indicatorMap = indicatorMetaInfoMap.get(serviceName);
        if (!indicatorMap.containsKey(indicatorName)) {
            return 0;
        }
        IndicatorMetaInfo indicatorMetaInfo = indicatorMap.get(indicatorName);
        String implement = indicatorMetaInfo.getImplement();

        switch (implement) {
            case "formula":
                FormulaCalculator formulaCalculator = new FormulaCalculator(serviceName, indicatorMetaInfo.getFormula());
                return formulaCalculator.calculate();
            case "class":
                Class<? extends IndicatorCalculator> aClass = (Class<? extends IndicatorCalculator>) Class.forName(indicatorMetaInfo.getClassName());
                Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();
                IndicatorCalculator calculator = (IndicatorCalculator) declaredConstructor.newInstance();
                return calculator.calculate();
            default:
                throw new RuntimeException("Unknown implementation:" + implement);

        }

    }
}
