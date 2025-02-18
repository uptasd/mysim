package org.mysim.quickstart.utils;

import org.junit.jupiter.api.Test;
import org.mysim.core.rt.container.BaseContainer;
import org.mysim.core.utils.TimeUtils;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class TestTimeUtil {
    @Test
    public void testGetTime() {
        LocalDateTime logicTime = TimeUtils.getLogicTime(0);
        System.out.println(logicTime);
    }

    @Test
    public void testGetContainerTime() {
        BaseContainer container = new BaseContainer();
        System.out.println("before:" + container.getTime());
        for (int i = 0; i < 5; i++) {
            container.step();
        }
        System.out.println("after:" + container.getTime());
    }
}
