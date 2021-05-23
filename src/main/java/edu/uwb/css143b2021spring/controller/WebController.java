package edu.uwb.css143b2021spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwb.css143b2021spring.model.Index;
import edu.uwb.css143b2021spring.model.SearchKey;
import edu.uwb.css143b2021spring.repository.IndexRepository;
import edu.uwb.css143b2021spring.service.Indexer;
import edu.uwb.css143b2021spring.service.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/*
DO NOT CHANGE
 */

@Controller
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private IndexRepository indexRepository;

    @Autowired
    private Searcher searcher;

    @Autowired
    private Indexer indexer;

    @RequestMapping(value = {"/", "/search", "/home"}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("searchKey", new SearchKey());
        model.addAttribute("hasResult", false);
        return "page";
    }

    @GetMapping("/docs")
    public String docs(Model model) throws JsonProcessingException {
        // get index from in-memory storage
        Index indexInJson = indexRepository.findAll().get(0);
        List<String> docs = new ObjectMapper().readValue(indexInJson.getDocs(), List.class);
        model.addAttribute("docs", docs);

        Map<String, List<List<Integer>>> index = new ObjectMapper().readValue(indexInJson.getIndices(), Map.class);
        String idxStr = indexToString(index);
        // uncomment the following line to print index to console
        // System.out.println(idxStr);
        model.addAttribute("index", idxStr);
        return "docs";
    }

    private String indexToString(Map<String, List<List<Integer>>> index) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<List<Integer>>> entry : index.entrySet()) {
            sb.append("\"" + entry.getKey() + "\" : {<br />");
            List<List<Integer>> idxInDoc = entry.getValue();
            for (int i = 0; i < idxInDoc.size(); i++) {
                sb.append(String.format("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;document[%d]: %s", i, idxInDoc.get(i).toString()));
                sb.append("<br />");
            }
            sb.append("}<br />");
        }
        return sb.toString();
    }

    @GetMapping("/reindex")
    public String reindex(Model model) {
        model.addAttribute("toIndex", new Index());
        return "reindex";
    }

    @PostMapping("/reindex")
    public String reindex(@ModelAttribute Index idx, Model model) throws JsonProcessingException {
        String[] docs = idx.getDocs().split("\n");
        for (int i = 0; i < docs.length; i++) {
            docs[i] = docs[i].trim().toLowerCase();
        }

        List<String> newDocs = Arrays.asList(docs);
        String jsonIdx = new ObjectMapper().writeValueAsString(indexer.index(newDocs));
        String jsonDoc = new ObjectMapper().writeValueAsString(newDocs);

        indexRepository.deleteAll();
        indexRepository.save(new Index(jsonIdx, jsonDoc));
        return "redirect:/docs";
    }

    @PostMapping("/search")
    public String search(@ModelAttribute SearchKey key, Model model) throws JsonProcessingException {
        // get index from in-memory storage
        Index indexInJson = indexRepository.findAll().get(0);
        Map<String, List<List<Integer>>> indices = new ObjectMapper().readValue(indexInJson.getIndices(), Map.class);
        List<String> docs = new ObjectMapper().readValue(indexInJson.getDocs(), List.class);

        // conduct search
        long startTime = System.nanoTime();

        List<Integer> result = searcher.search(key.getPhrase(), indices);

        long searchTime = System.nanoTime() - startTime;

        // naive search
        startTime = System.nanoTime();

        List<Integer> naiveSearchResult = searcher.search(key.getPhrase(), docs);

        long naiveSearchTime = System.nanoTime() - startTime;

        // render result page
        model.addAttribute("docs", docs);
        model.addAttribute("phrase", "\"" + key.getPhrase() + "\"");
        model.addAttribute("searchResult", result.toString());
        model.addAttribute("naiveSearchResult", naiveSearchResult.toString());
        model.addAttribute("searchResultMatch", result.equals(naiveSearchResult));
        model.addAttribute("emptyResult", result.isEmpty());
        model.addAttribute("searchKey", new SearchKey());
        model.addAttribute("hasResult", true);
        model.addAttribute("searchTime", searchTime);
        model.addAttribute("naiveSearchTime", naiveSearchTime);
        return "page";
    }
}