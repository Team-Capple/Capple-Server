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
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Lock 테스트 ")
public class LockTest {
    private static String redisHost;
    private static Integer redisPort;
    private static Integer redisDatabase;

    @BeforeAll
    static void beforeAll() {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("application-test.yml"));
        Properties props = yamlFactory.getObject();
        redisHost = props.getProperty("spring.data.redis.host");
        redisPort = Integer.valueOf(props.getProperty("spring.data.redis.port"));
        redisDatabase = Integer.valueOf(props.getProperty("spring.data.redis.database"));
    }

    private static int lettuceNum = 0;
    @Test
    @DisplayName("Lettuce")
    void concurrentLettuce() throws InterruptedException {
        int numberOfThreads = 30;
        int countDown = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        connectionFactory.setDatabase(redisDatabase);
        connectionFactory.afterPropertiesSet();

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.afterPropertiesSet();

        long waitTime = 1L, leaseTime = 120L;
        AtomicInteger failedCnt = new AtomicInteger(0);

        String key = "test-key";

        for (int i = 0; i < countDown; i++) {
            executorService.submit(() -> {
                try {
                    boolean locked;
                    int retryCnt = 0;
                    while ((locked = Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, "lock", leaseTime, TimeUnit.SECONDS)))) {
                        Thread.sleep(Duration.ofSeconds(waitTime));
                        if (++retryCnt == 2) {
                            break;
                        }
                    }
                    if (!locked) {
                        int tmp = lettuceNum;
                        Thread.sleep(10);
                        lettuceNum = tmp + 1;
                        redisTemplate.delete(key);
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

        System.out.println("success : " + lettuceNum);
        System.out.println("failed : " + failedCnt.get());
        System.out.println("total : " + (lettuceNum + failedCnt.get()));

        assertThat(lettuceNum + failedCnt.get()).isEqualTo(countDown);
    }

    private static int redissonNum = 0;
    @Test
    @DisplayName("Redisson RLock")
    void concurrentRedisson() throws InterruptedException {
        int numberOfThreads = 30;
        int countDown = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(countDown);
        Config config = new Config();
        config.useSingleServer().setAddress(String.format("redis://%s:%d", redisHost, redisPort)).setDatabase(redisDatabase);
        RedissonClient redissonClient = Redisson.create(config);

        long waitTime = 1L, leaseTime = 30L;
        AtomicInteger failedCnt = new AtomicInteger(0);

        for (int i = 0; i < countDown; i++) {
            executorService.submit(() -> {
                try {
                    RLock rLock = redissonClient.getLock("test-key");
                    if (rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
                        int tmp = redissonNum;
                        Thread.sleep(10);
                        redissonNum = tmp + 1;
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

        System.out.println("success : " + redissonNum);
        System.out.println("failed : " + failedCnt.get());
        System.out.println("total : " + (redissonNum + failedCnt.get()));

        assertThat(redissonNum + failedCnt.get()).isEqualTo(countDown);
    }
}
