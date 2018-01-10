package reuters;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.MultiTermQuery;

public class ReutersSearcher {

	//Main-Methode
	public static void main(String[] args) throws IllegalArgumentException, IOException, ParseException {
		
		final boolean searchNumeric = true;
		
		String indexDir = "index";
		//String q = "oil"; //7.2.1
		//Found 135 document(s) (in 0 milliseconds) that matched query 'oil':
		//String q = "company"; //7.2.2
		//Found 476 document(s) (in 15 milliseconds) that matched query 'company':
		//String q = "\"oil\" AND \"company\"";  //7.2.3 boolean operators
		//Found 28 document(s) (in 31 milliseconds) that matched query '"oil" AND "company"':
		//String q = "company NOT oil"; // 7.2.4 boolean operators
		//Found 448 document(s) (in 63 milliseconds) that matched query 'company NOT oil':
		//String q = "\"oil company\"";  // 7.2.5 Phrases
		//Found 2 document(s) (in 89 milliseconds) that matched query '"oil company"':
		//String q = "\"oil company\"~5";  // 7.2.6 proximity search
		//Found 6 document(s) (in 24 milliseconds) that matched query '"oil company"~5':
		//String q = "(oil OR gas) AND company"; // 7.2.7 grouping
		//Found 35 document(s) (in 303 milliseconds) that matched query '(oil OR gas) AND company':
		//String q = "comp*"; // 7.2.8 multiple character wildcard search
		//Found 856 document(s) (in 60 milliseconds) that matched query 'comp*':
		
		//Teil 7.3
		//String q = "\"the\"";  
		//String q = "\"and\"";  
		
		//Teil 7.4
		String q = "oil";  
		
		
		
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
		//QueryParser parser = new QueryParser(ReutersIndexer.CONTENT, new StandardAnalyzer()); //TODO: ersetzen mit NoStopAnalyzer
		QueryParser parser = new QueryParser(ReutersIndexer.CONTENT, new MyNoStopAnalyzer());
		
		Query query = parser.parse(q); // 4
		
		// TODO: hier bitte implementieren!

		TermQuery searchingOil = new TermQuery(new Term("content", q));
		Query rangeQuery = LongPoint.newRangeQuery("filesize", min, max);
		
		BooleanQuery booleanQuery = new BooleanQuery.Builder()
			    .add(searchingOil, Occur.MUST)
			    .add(rangeQuery, Occur.MUST)
			    .build();

		
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(booleanQuery, 2000); // 5
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
	
	public static void search(String indexDir, String q) throws IOException, ParseException {

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDir)));
		IndexSearcher is = new IndexSearcher(reader); // 3
	
		//QueryParser parser = new QueryParser("content", new StandardAnalyzer()); //TODO: ersetzen mit NoStopAnalyzer
		QueryParser parser = new QueryParser("content", new MyNoStopAnalyzer());
		Query query = parser.parse(q); // 4
		
		long start = System.currentTimeMillis();
		TopDocs hits = is.search(query, 2000); // 5
		long end = System.currentTimeMillis();

		
		System.err.println("Found " + hits.totalHits + // 6
				" document(s) (in " + (end - start) + // 6
				" milliseconds) that matched query '" + // 6
				q + "':"); // 6

		int counter = 0;
//		for (ScoreDoc scoreDoc : hits.scoreDocs) {
//			Document doc = is.doc(scoreDoc.doc); // 7
//			System.out.println(++counter + ": " + doc.get("path")); // 8
//		}
	}
}
