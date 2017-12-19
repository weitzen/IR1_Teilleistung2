package reuters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Klasse zum Indexieren der Texte
 */
public class ReutersIndexer {

	// Namen der Felder
	public static final String CONTENT = "content";
	public static final String PATH = "path";
	public static final String FILESIZE = "filesize";

	// Main-Methode
	public static void main(String[] args) throws Exception {

		// TODO: passen Sie die Pfade ggf. an!
		String filePath = new File("").getAbsolutePath();
		String fileName = "data/2000reuters";
		String indexDir = "idx";

		ReutersIndexer indexer = new ReutersIndexer(indexDir);
		
		long start = System.currentTimeMillis();
		int numIndexed;
		try {
			numIndexed = indexer.index(new File(fileName).listFiles());
		} finally {
			indexer.close();
		}
		long end = System.currentTimeMillis();

		System.out.println("Indexing " + numIndexed + " files took " + (end - start) + " milliseconds");
	}

	// Zugriff auf den Index
	private IndexWriter writer;

	// Konstruktor
	public ReutersIndexer(String indexDir) throws IOException {
		Directory dir = FSDirectory.open(Paths.get(indexDir));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(dir, iwc);
	}

	/**
	 * Schliessen des IndexWriters.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		writer.close();
	}

	protected int index(File[] files) throws Exception {
		// TODO Hier bitte implementieren
	
		return -1;
	}
}
