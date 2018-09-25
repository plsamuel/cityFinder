package com.plsamuel.cityfinder;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.plsamuel.cityfinder.CityDocument.CITY;
import static org.junit.Assert.*;

public class CitySearchTest {

    final private String indexDir = "/tmp/CitySearchTest";

    @Before
    public void loadIndex() {
        List<String> resources = new ArrayList<String>();
        resources.add("CA/CAtest.txt");

        CityIndex.populateIndex(indexDir, resources);
    }

    @Test
    public void noLatLong_search_2result() throws Exception {
        CitySearch citySearch = new CitySearch(indexDir);
        String json = citySearch.search("Basin", null, null);

        assertEquals("{\"suggestions\":[{\"name\":\"Kane Basin, NU, Canada\",\"latitude\":\"79.3886\",\"longitude\":\"-70.42541\",\"score\":0.8333333333333334},{\"name\":\"Downing Basin, NL, Canada\",\"latitude\":\"47.05216\",\"longitude\":\"-50.79163\",\"score\":0.7333333333333333}]}", json);
    }

    @Test
    public void withLatLong_search_2result() throws Exception {
        CitySearch citySearch = new CitySearch(indexDir);
        String json = citySearch.search("Basin", -12.4, 22.3);

        assertEquals("{\"suggestions\":[{\"name\":\"Kane Basin, NU, Canada\",\"latitude\":\"79.3886\",\"longitude\":\"-70.42541\",\"score\":0.6317243519520054},{\"name\":\"Downing Basin, NL, Canada\",\"latitude\":\"47.05216\",\"longitude\":\"-50.79163\",\"score\":0.622709189365181}]}", json);
    }

    @Test
    public void fullWordSearch_searchByCityName_1result() throws Exception {

        CitySearch citySearch = new CitySearch(indexDir);
        IndexSearcher searcher = citySearch.createSearcher();
        TopDocs topDocs = citySearch.searchByCityName("Downing", searcher);

        assertEquals(1, topDocs.scoreDocs.length);
        assertEquals("Downing Basin", searcher.doc(topDocs.scoreDocs[0].doc).getField(CITY).stringValue());
    }

    @Test
    public void incompleteWordSearch_searchByCityName_2results() throws Exception {

        CitySearch citySearch = new CitySearch(indexDir);
        IndexSearcher searcher = citySearch.createSearcher();
        TopDocs topDocs = citySearch.searchByCityName("Bas", searcher);

        assertEquals(2, topDocs.scoreDocs.length);
        assertEquals("Downing Basin", searcher.doc(topDocs.scoreDocs[0].doc).getField(CITY).stringValue());
        assertEquals("Kane Basin", searcher.doc(topDocs.scoreDocs[1].doc).getField(CITY).stringValue());
    }

}