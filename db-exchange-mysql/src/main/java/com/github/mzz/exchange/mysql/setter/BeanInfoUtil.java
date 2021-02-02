package com.github.mzz.exchange.mysql.setter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.beans.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Bean info util.
 *
 * @author mengzz
 */
public class BeanInfoUtil {

    /**
     * Gets declared property descriptors.
     *
     * @param type the type
     * @return the declared property descriptors
     */
    public static List<PropertyDescriptor> getDeclaredPropertyDescriptors(Class<?> type) {
        Map<String, PropertyDescriptor> propertyDescriptorsMap = getPropertyDescriptorsMap(type);
        Field[] declaredFields = type.getDeclaredFields();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();
        for (Field declaredField : declaredFields) {
            propertyDescriptors.add(propertyDescriptorsMap.get(declaredField.getName()));
        }
        return propertyDescriptors;
    }

    /**
     * Gets property descriptors map.
     *
     * @param type the type
     * @return the property descriptors map
     */
    public static Map<String, PropertyDescriptor> getPropertyDescriptorsMap(Class<?> type) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            Map<String, PropertyDescriptor> descriptorMap = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .collect(Collectors.toMap(FeatureDescriptor::getName, v -> v));
            // ignore class
            descriptorMap.remove("class");
            return descriptorMap;
        } catch (IntrospectionException e) {
            ExceptionUtils.rethrow(e);
        }
        return new HashMap<>();
    }
}
