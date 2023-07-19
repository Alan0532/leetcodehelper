package com.leetcode.helper;

import com.leetcode.helper.model.util.JsonUtils;
import com.leetcode.helper.model.util.HelperException;
import com.leetcode.helper.model.HelperNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class LeetCodeHelper {

    public static void code(String parameter) {
        code(parameter, null);
    }

    public static void code(String parameter, String methodName) {
        try {
            Class<?> mainClass = getMainApplicationClass();
            Class<?> clazz = Class.forName(mainClass.getName() + "$Solution");
            Object outerObject = mainClass.getDeclaredConstructor().newInstance();
            Constructor<?> constructor = clazz.getDeclaredConstructor(getMainApplicationClass());
            constructor.setAccessible(true);
            // new Solution
            Object object = constructor.newInstance(outerObject);
            Method[] methods = clazz.getMethods();
            Method invokeMethod = null;

            for (Method method : methods) {
                if (methodName != null && methodName.equals(method.getName())) {
                    invokeMethod = method;
                    break;
                } else if (methodName == null) {
                    Class<?> declaringClass = method.getDeclaringClass();
                    if (declaringClass.equals(clazz)) {
                        if (invokeMethod == null) {
                            invokeMethod = method;
                        } else {
                            throw new HelperException("More than one method was found, please specify methodName.");
                        }
                    }
                }
            }

            if (invokeMethod == null) {
                if (methodName != null) {
                    throw new HelperException("Method " + methodName + " in the Class Solution was not found");
                } else {
                    throw new HelperException("No method was found.");
                }
            }
            invokeSolution(parameter, object, invokeMethod);
        } catch (HelperException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void invokeSolution(String strParameter, Object object, Method method) throws Exception {
        method.setAccessible(true);
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = strParametersToParameters(getStringParameters(strParameter, parameterTypes.length), parameterTypes);
        Object returnOb = method.invoke(object, parameters);
        if (returnOb instanceof HelperNode) {
            System.out.println(returnOb);
        } else {
            if (returnOb != null) {
                System.out.println(JsonUtils.toJSONString(returnOb));
            }
        }
    }

    private static String[] getStringParameters(String parameter, int length) throws Exception {
        int strlen = parameter.length();
        String[] parameters = new String[length];
        int index = parameter.lastIndexOf(" = ", strlen);
        if (index == -1) {
            throw new HelperException("Illegal parameter");
        }
        while (strlen > 0) {
            if (--length < 0) {
                throw new HelperException("The number of parameter lists does not match");
            } else {
                parameters[length] = parameter.substring(index + 3, strlen);
                strlen = parameter.lastIndexOf(", ", strlen - 1);
                index = parameter.lastIndexOf(" = ", strlen);
            }
        }
        return parameters;
    }

    private static Object[] strParametersToParameters(String[] strParameters, Class<?>[] parameterTypes) throws Exception {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> clazz = parameterTypes[i];
            String strParameter = strParameters[i];
            if (clazz.isPrimitive() || clazz.isArray() || clazz.isAssignableFrom(String.class)) {
                buildParameters(clazz, strParameter, i, parameters);
            } else {
                Class<?>[] interfaces = clazz.getInterfaces();
                for (int j = 0; j < interfaces.length; j++) {
                    if (interfaces[j].isAssignableFrom(HelperNode.class)) {
                        parameters[i] = ((HelperNode) clazz.getDeclaredConstructor().newInstance()).convert(strParameter);
                        break;
                    }
                    if (j == interfaces.length - 1) {
                        throw new HelperException("Unsupported parameter type: " + clazz.getName());
                    }
                }
            }
        }
        return parameters;
    }


    private static void buildParameters(Class<?> clazz, String strParameter, int i, Object[] parameters) throws Exception {
        if (clazz.isPrimitive() || clazz.isAssignableFrom(String.class)) {
            //primitive or string object
            parameters[i] = JsonUtils.parseObject(strParameter, clazz);
        } else if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive() || componentType.isAssignableFrom(String.class)) {
                //primitive or string array
                parameters[i] = JsonUtils.parseObject(strParameter, clazz);
            } else if (componentType.isArray()) {
                buildArray(clazz, clazz, strParameter, i, parameters);
            } else {
                throw new HelperException("Unsupported parameter type: " + clazz.getName());
            }
        } else {
            throw new HelperException("Unsupported parameter type: " + clazz.getName());
        }
    }

    private static void buildArray(Class<?> rootClass, Class<?> clazz, String strParameter, int i, Object[] parameters) throws Exception {
        if (clazz.isPrimitive() || clazz.isAssignableFrom(String.class)) {
            parameters[i] = JsonUtils.parseObject(strParameter, rootClass);
        } else if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive() || componentType.isAssignableFrom(String.class)) {
                parameters[i] = JsonUtils.parseObject(strParameter, rootClass);
            } else if (componentType.isArray()) {
                buildArray(rootClass, componentType, strParameter, i, parameters);
            } else {
                //LeetCodeNode not support matrix
                throw new HelperException("Unsupported parameter array type: " + clazz.getName());
            }
        }
     }

    private static Class<?> getMainApplicationClass() throws Exception {
        Class<?> clazz = null;
        StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("main".equals(stackTraceElement.getMethodName())) {
                clazz = Class.forName(stackTraceElement.getClassName());
            }
        }
        if (clazz == null) {
            throw new ClassNotFoundException();
        }
        return clazz;
    }

}