package org.mysim.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class CommonUtils {
    //从集合中随机获取一个对象
    public static <T> T getRandom(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Collection must not be null or empty");
        }
        List<T> list = new ArrayList<>(collection);

        // 随机获取元素
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
