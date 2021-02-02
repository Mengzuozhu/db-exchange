package com.github.mzz.exchange.mysql.setter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author mengzz
 **/
public class BeanPreparedStatementSetter<T> implements PreparedStatementSetter<T> {
    private Method[] readMethods;

    public BeanPreparedStatementSetter(Class<T> type) {
        List<PropertyDescriptor> propertyDescriptors = BeanInfoUtil.getDeclaredPropertyDescriptors(type);
        readMethods = new Method[propertyDescriptors.size()];
        for (int i = 0; i < propertyDescriptors.size(); i++) {
            readMethods[i] = propertyDescriptors.get(i).getReadMethod();
        }
    }

    public BeanPreparedStatementSetter(Class<T> type, String... properties) {
        Map<String, PropertyDescriptor> propertyDescriptorsMap = BeanInfoUtil.getPropertyDescriptorsMap(type);
        readMethods = new Method[properties.length];
        int index = 0;
        for (String property : properties) {
            readMethods[index++] = propertyDescriptorsMap.get(property).getReadMethod();
        }
    }

    @Override
    public void fillStatement(PreparedStatement preparedStatement, T inputs) throws SQLException {
        try {
            for (int i = 0; i < readMethods.length; i++) {
                Method method = readMethods[i];
                preparedStatement.setObject(i + 1, method.invoke(inputs));
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            ExceptionUtils.rethrow(e);
        }
    }

}
