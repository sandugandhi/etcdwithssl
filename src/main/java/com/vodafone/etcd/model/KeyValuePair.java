package com.vodafone.etcd.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyValuePair {
  String key;
  String value;
}
