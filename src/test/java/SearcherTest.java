import edu.uwb.css143b2021spring.service.Searcher;
import edu.uwb.css143b2021spring.service.SearcherImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/*
DO NOT CHANGE
 */

public class SearcherTest {
    private Searcher searcher;

    @Before
    public void setUp() {
        searcher = new SearcherImpl();
    }

    @Test
    public void testSearcher() {
        List<SearcherTest.TestCase> cases = getTestCase();
        for (SearcherTest.TestCase testCase : cases) {
            List<Integer> actual = searcher.search(testCase.target, testCase.indices);
            assertEquals(testCase.expect, actual);
        }
    }

    private List<TestCase> getTestCase() {
        Map<String, List<List<Integer>>> indices = Util.getIndexForIndexerTest();
        List<TestCase> testCases = new ArrayList<>(Arrays.asList(
                new TestCase(
                        indices,
                        "",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "ttle hello",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "hello",
                        new ArrayList<>(Arrays.asList(0, 2, 3))
                ),
                new TestCase(
                        indices,
                        "world",
                        new ArrayList<>(Arrays.asList(0, 1, 2, 3))
                ),
                new TestCase(
                        indices,
                        "seattle hello",
                        new ArrayList<>(Arrays.asList(2))
                ),
                new TestCase(
                        indices,
                        "hello world",
                        new ArrayList<>(Arrays.asList(0, 3))
                ),
                new TestCase(
                        indices,
                        "hello world fun",
                        new ArrayList<>(Arrays.asList(3))
                ),
                new TestCase(
                        indices,
                        "world world fun",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "chicago",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "need coffee",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "need coffee",
                        Util.emptyResult()
                ),
                new TestCase(
                        indices,
                        "sunny",
                        new ArrayList<>(Arrays.asList(4, 6))
                )
        ));

        return testCases;
    }

    private class TestCase {
        private Map<String, List<List<Integer>>> indices;
        private String target;
        private List<Integer> expect;

        public TestCase(Map<String, List<List<Integer>>> indices, String target, List<Integer> expect) {
            this.indices = indices;
            this.target = target;
            this.expect = expect;
        }
    }
}
