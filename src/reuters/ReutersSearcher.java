package reuters;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class ReutersSearcher {

	//Main-Methode
	public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {
		
		final boolean searchNumeric = true;
		
		String indexDir = "idx";
		String q = "suchwort";
		
		if (!searchNumeric){
			search(indexDir, q);
		} else {
			long min = 50;
			long max = 200;
			searchNumeric(indexDir, q, min, max);
		}
	}

	public static void searchNumeric(String indexDir, String q, long min, long max) throws IOException, ParseException {

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is = new IndexSearcher(reader); // 3
		QueryParser parser = new QueryParser(ReutersIndexer.CONTENT, new StandardAnalyzer());

		Query query = parser.parse(q); // 4
		
		// TODO: hier bitte implementieren!
	}
	
	public static void search(String indexDir, String q) throws IOException, ParseException {

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is = new IndexSearcher(reader); // 3
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());

		Query query = parser.parse(q); // 4
		
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 1000); // 5
		long end = System.currentTimeMillis();

		System.err.println("Found " + hits.totalHits + // 6
				" document(s) (in " + (end - start) + // 6
				" milliseconds) that matched query '" + // 6
				q + "':"); // 6

		int counter = 0;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc); // 7
			System.out.println(++counter + ": " + doc.get("path")); // 8
		}
	}
}
