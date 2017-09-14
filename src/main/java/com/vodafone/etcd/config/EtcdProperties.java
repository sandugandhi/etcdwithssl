package com.vodafone.etcd.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import java.net.URI;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties("etcd.client")
public class EtcdProperties {

  private List<URI> hosts;

  @Value("${etcd.ttlSeconds}")
  @Min(1) // ttl value should positive and non zero integer
  private int ttlSeconds;

  @Min(1) // etcd timeout
  private int timeout;

}
