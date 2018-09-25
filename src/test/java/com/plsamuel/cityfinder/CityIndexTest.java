package com.plsamuel.cityfinder;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.plsamuel.cityfinder.CityDocument.CITY;
import static org.junit.Assert.*;

public class CityIndexTest {

    final private String indexDir = "/tmp/CityIndexTest";

    @Test
    public void test_populateIndex() throws IOException {
        List<String> resources = new ArrayList<String>();
        resources.add("CA/CAtest.txt");

        CityIndex.populateIndex(indexDir, resources);

        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        assertEquals(10, reader.getDocCount(CITY));
    }
}