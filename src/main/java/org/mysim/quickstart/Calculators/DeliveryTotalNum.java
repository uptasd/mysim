package org.mysim.quickstart.Calculators;

import org.mysim.core.indicator.IndicatorCalculator;
import org.mysim.quickstart.service.DeliveryOrderService;
import org.mysim.quickstart.utils.ServiceFactory;

public class DeliveryTotalNum implements IndicatorCalculator {
    @Override
    public Object calculate() {
        DeliveryOrderService deliveryOrderService = (DeliveryOrderService) ServiceFactory.getService("deliveryOrderServiceImpl");
        return deliveryOrderService.count();
    }
}
