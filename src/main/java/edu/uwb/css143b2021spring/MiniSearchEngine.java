package edu.uwb.css143b2021spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uwb.css143b2021spring.model.Index;
import edu.uwb.css143b2021spring.repository.IndexRepository;
import edu.uwb.css143b2021spring.service.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/*
DO NOT CHANGE
 */

@SpringBootApplication
public class MiniSearchEngine implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MiniSearchEngine.class);
    @Autowired
    private IndexRepository indexRepository;
    @Autowired
    private Indexer indexer;

    private static List<String> documents() {
        List<String> result = new ArrayList<>();
        try {

            File myObj = ResourceUtils.getFile("classpath:default_docs_do_not_change");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                result.add(myReader.nextLine());
            }
            myReader.close();
        } catch (Exception e) {
            logger.info("failed to load docs: " + e);
        }
        return result;
    }

    public static void main(String[] args) {
        SpringApplication.run(MiniSearchEngine.class, args);
        logger.info("service ready");
    }

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        List<String> docs = documents();
        Map<String, List<List<Integer>>> indices = indexer.index(docs);

        // convert map to json for storage
        // hacky. need better way to store it in DB
        String jsonIdx = new ObjectMapper().writeValueAsString(indices);
        String jsonDoc = new ObjectMapper().writeValueAsString(docs);
        indexRepository.save(new Index(jsonIdx, jsonDoc));
    }
}