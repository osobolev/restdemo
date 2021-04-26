package ru.mirea.my_di;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.springframework.context.annotation.Bean;
import ru.mirea.books3.BookConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodCacher {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(BookConfig.class);
        Map<Method, Object> cachedBeans = new HashMap<>();
        enhancer.setCallback((MethodInterceptor) (obj, method, args1, proxy) -> {
            if (method.isAnnotationPresent(Bean.class)) {
                Object cached = cachedBeans.get(method);
                if (cached == null) {
                    System.out.println("Invoking: " + method.getName());
                    cached = proxy.invokeSuper(obj, args1);
                    cachedBeans.put(method, cached);
                } else {
                    System.out.println("Already cached: " + method.getName());
                }
                return cached;
            } else {
                return method.invoke(obj, args1);
            }
        });

        BookConfig cachingConfig = (BookConfig) enhancer.create();
        cachingConfig.bookService();
        cachingConfig.bookRest();
    }
}
