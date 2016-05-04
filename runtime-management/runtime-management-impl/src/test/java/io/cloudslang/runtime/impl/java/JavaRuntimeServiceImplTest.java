package io.cloudslang.runtime.impl.java;

import io.cloudslang.dependency.api.services.DependencyService;
import io.cloudslang.runtime.api.java.JavaRuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JavaRuntimeServiceImplTest.TestConfig.class)
public class JavaRuntimeServiceImplTest {
    static {
        System.setProperty("java.executor.provider", "JavaCachedExecutionEngine");
    }

    @Autowired
    private JavaRuntimeService javaRuntimeServiceImpl;

    @Test
    public void testJavaRuntimeService() {
        System.out.println("+++++++++++++++++++++++++[" + javaRuntimeServiceImpl.execute("", "java.util.Date", "toGMTString") + "]");
        System.out.println("+++++++++++++++++++++++++[" + javaRuntimeServiceImpl.execute("nothing", "java.util.Date", "toGMTString") + "]");
    }

    @Configuration
    static class TestConfig {
        @Bean
        public JavaRuntimeService javaRuntimeService() {return new JavaRuntimeServiceImpl();}

        @Bean
        public JavaExecutionEngine javaExecutorProvider() {return new JavaCachedStaticsSharedExecutionEngine();}

        @Bean
        public DependencyService dependencyService() {return new DependencyService() {
            @Override
            public Set<String> getDependencies(Set<String> resources) {
                return new HashSet<>(Arrays.asList("c:\\a.jar", "c:\\b.jar"));
            }
        };}
    }
}
