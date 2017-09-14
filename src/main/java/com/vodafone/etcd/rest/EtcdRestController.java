package com.vodafone.etcd.rest;

import com.vodafone.etcd.model.KeyValuePair;
import com.vodafone.etcd.service.EtcdDemoService;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/etcd")
public class EtcdRestController {

  @Autowired
  private EtcdDemoService etcd;

  @RequestMapping(value = "",
      method = RequestMethod.PUT,
      consumes = {"application/json"},
      produces = {"application/json"})
  @ResponseStatus(HttpStatus.OK)
  public void put(@RequestBody KeyValuePair data) {
    try {
      this.etcd.put(data.getKey(), data.getValue());
    } catch (TimeoutException | EtcdAuthenticationException | IOException e) {
      log.error(e.getMessage(), e);
    }
  }



  @RequestMapping(value = "/{key}",
      method = RequestMethod.GET,
      consumes = {"application/json"},
      produces = {"application/json"})
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody KeyValuePair get(@PathVariable String key) {
    KeyValuePair kvp = null;
    try {
      kvp = new KeyValuePair(key, this.etcd.get(key));
    } catch (TimeoutException | EtcdAuthenticationException | IOException e) {
      log.error(e.getMessage(), e);
    }
    return kvp;
  }
}
