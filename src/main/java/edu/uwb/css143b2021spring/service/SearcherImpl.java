package edu.uwb.css143b2021spring.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearcherImpl implements Searcher {
    /*
    this search won't work. why?
     */
    // public List<Integer> search(String keyPhrase, List<String> docs) {
    //
    //     List<Integer> result = new ArrayList<>();
    //
    //     for (int i = 0; i < docs.size(); i++) {
    //         if (docs.get(i).contains(keyPhrase)) {
    //             result.add(i);
    //         }
    //     }
    //     return result;
    // }

    // a naive search
    // DO NOT CHANGE
    public List<Integer> search(String keyPhrase, List<String> docs) {
        List<Integer> result = new ArrayList<>();
        String[] searchWords = keyPhrase.trim().toLowerCase().split("\\s+");
        for (int i = 0; i < docs.size(); i++) {
            String doc = docs.get(i).trim();
            if (doc.isEmpty()) {
                continue;
            }
            String[] docWords = doc.split("\\s+");

            for (int j = 0; j < docWords.length; j++) {
                boolean matchFound = true;
                for (int k = 0; k < searchWords.length; k++) {
                    if (j + k >= docWords.length || !searchWords[k].equals(docWords[j + k])) {
                        matchFound = false;
                        break;
                    }
                }
                if (matchFound) {
                    result.add(i);
                    break;
                }
            }
        }
        return result;
    }

    public List<Integer> search(String keyPhrase, Map<String, List<List<Integer>>> index) {
        List<Integer> result = new ArrayList<>();
        // add your code
        return result;
    }
}
