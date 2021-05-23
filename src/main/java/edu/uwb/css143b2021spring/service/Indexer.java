package edu.uwb.css143b2021spring.service;

import java.util.List;
import java.util.Map;

/*
DO NOT CHANGE
 */

public interface Indexer {
    Map<String, List<List<Integer>>> index(List<String> docs);
}
