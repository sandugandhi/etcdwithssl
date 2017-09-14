package com.vodafone.etcd.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EtcdDemoService {


  @Autowired
  private EtcdClient etcdClient;

  public void put(String key, String value) throws IOException, EtcdAuthenticationException, TimeoutException {

      log.info("etcd cluster healthy ?  {}", etcdClient.getHealth().getHealth());
      String existingValue = null;
      try {
         existingValue = etcdClient.get(key).send().get().getNode().getValue();
      } catch(EtcdException e) {
        log.info("key [{}] not found, hey", key);
      }

      log.info("Replacing existing value [{}] for key [{}] with [{}]", existingValue, key, value);
    try {
      String newValue = etcdClient.put(key,value).send().get().getNode().getValue();
      log.info("New value for key [{}] is [{}]", key, newValue);
    } catch (EtcdException e) {
      log.error(e.getMessage(), e);
    }
  }

  public String get(String key)  throws IOException, EtcdAuthenticationException, TimeoutException {
    String existingValue = null;
    try {
      log.info("etcd cluster healthy ?  {}", etcdClient.getHealth().getHealth());
       existingValue = etcdClient.get(key).send().get().getNode().getValue();
      log.info("Existing value for key [{}] is [{}]", key, existingValue);
    } catch (EtcdException e) {
      log.error(e.getMessage(), e);
    }
    return existingValue;
  }


}
