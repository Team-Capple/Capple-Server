package com.server.capple.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Redisson 의 ")
public class RedissonLockTest {
    static int num = 0;
    private static String redisHost;
    private static String redisPort;
    private static Integer redisDatabase;

    @BeforeAll
    static void beforeAll() {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("application-test.yml"));
        Properties props = yamlFactory.getObject();
        redisHost = props.getProperty("spring.data.redis.host");
        redisPort = props.getProperty("spring.data.redis.port");
        redisDatabase = Integer.valueOf(props.getProperty("spring.data.redis.database"));
    }

    @Test
    @DisplayName("RLock 테스트")
    void concurrentRedisson() throws InterruptedException {
        int numberOfThreads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        Config config = new Config();
        config.useSingleServer().setAddress(String.format("redis://%s:%s", redisHost, redisPort)).setDatabase(redisDatabase);
        RedissonClient redissonClient = Redisson.create(config);

        long waitTime = 1L, leaseTime = 30L;
        AtomicInteger failedCnt = new AtomicInteger(0);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    RLock rLock = redissonClient.getLock("test");
                    if (rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                        int tmp = num;
                        Thread.sleep(10);
                        num = tmp + 1;
                        rLock.unlock();
                    } else {
                        failedCnt.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        System.out.println("success : " + num);
        System.out.println("failed : " + failedCnt.get());
        System.out.println("total : " + numberOfThreads);

        assertThat(num + failedCnt.get()).isEqualTo(numberOfThreads);
    }
}
