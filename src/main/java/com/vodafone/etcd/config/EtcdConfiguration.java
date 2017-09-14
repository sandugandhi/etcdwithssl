package com.vodafone.etcd.config;

import static io.netty.handler.ssl.ClientAuth.REQUIRE;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.File;
import java.io.IOException;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import mousio.client.retry.RetryWithTimeout;
import mousio.etcd4j.EtcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableConfigurationProperties(EtcdProperties.class)
@Slf4j
public class EtcdConfiguration {

  @Autowired
  private EtcdProperties etcdProperties;

  //it will retry after every 100 ms till etcd timout occurs
  private static final int RETRY_TIME_IN_MS = 100;

  @Bean
  public EtcdClient etcdClient() throws IOException {

    File keyCertChainFile = new ClassPathResource("s1.pem").getFile();
    File keyFile = new ClassPathResource("s1-key-pkcs8.pem").getFile();
    File caCertFile = new ClassPathResource("etcd-root-ca.pem").getFile();

    SslContextBuilder clientSslContextBuilder = SslContextBuilder.forClient();
    log.info("Enabling one-way auth with no certs checks.");
    SslContext sslcontext = clientSslContextBuilder
        .keyManager(keyCertChainFile,keyFile)
        .sslProvider(SslProvider.JDK)
        .trustManager(caCertFile)
        .build();

    EtcdClient etcdClient = new EtcdClient(sslcontext, getEtcdUrls());
    etcdClient.setRetryHandler(new RetryWithTimeout(RETRY_TIME_IN_MS, etcdProperties.getTimeout()));
    return etcdClient;
  }

  /**
   * It returns the cluster etcdProperties URL which is configured.
   */
  private URI[] getEtcdUrls() {

    return etcdProperties.getHosts().toArray(new URI[etcdProperties.getHosts().size()]);
  }
}
