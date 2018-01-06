package reuters;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class ReutersSearcher {

	//Main-Methode
	public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {
		
		final boolean searchNumeric = false;
		
		String indexDir = "index";
		String q = "oil"; //7.2.1
		//Found 135 document(s) (in 0 milliseconds) that matched query 'oil':
		//String q = "company"; //7.2.2
		//Found 476 document(s) (in 15 milliseconds) that matched query 'company':
		//String q = "\"oil\" AND \"company\"";  //7.2.3
		//Found 28 document(s) (in 31 milliseconds) that matched query '"oil" AND "company"':
		
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
		/*
		 * powerful package,
called QueryParser, to process the user’s text into a query object according to a common
search syntax*/
		QueryParser parser = new QueryParser("content", new StandardAnalyzer());

		Query query = parser.parse(q); // 4
		//Query query_one = new TermQuery(new Term("content", "oil")); //7.2.1
		
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 2000); // 5
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
