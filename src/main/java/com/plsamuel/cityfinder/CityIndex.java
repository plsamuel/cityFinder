package com.plsamuel.cityfinder;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class CityIndex {
    public static final String INDEX_DIR = "/tmp/lucene";

    public static void populateIndex(String indexDir, List<String> resourceList) {

        try {
            IndexWriter writer = getIndexWriter(indexDir);
            writer.deleteAll();

            for (String resourceName: resourceList) {
                System.out.println("Indexing "+resourceName);
                indexFile(writer, Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName));
            }

            System.out.println("Merging index");
            writer.commit();
            writer.close();

            System.out.println("Index complete");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static IndexWriter getIndexWriter(String indexDir) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexDir));
        IndexWriterConfig iwc = new IndexWriterConfig(new StandardAnalyzer());

        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        iwc.setRAMBufferSizeMB(128.0);

        return new IndexWriter(dir, iwc);
    }

    protected static void indexFile(IndexWriter writer, InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            writer.addDocument(CityDocument.parseLine(line));
        }
    }

}
