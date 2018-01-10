package reuters;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.BinaryDocValuesField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
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

		// TODO passen Sie die Pfade ggf. an!
		//String filePath = new File("").getAbsolutePath();
		
		/**
		 * fileName = contentDir
		 */
		String fileName = "data/2000reuters";
		String indexDir = "index";

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
		//Analyzer analyzer = new StandardAnalyzer(); //TODO  ersetzen mit NoStop
		Analyzer analyzer = new MyNoStopAnalyzer();
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
		
		for (File f : files) {
			if (!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead()) {
				indexFile(f);
			}
		}

		return writer.numDocs(); 
	}
	
	protected Document getDocument(File f) throws Exception {
		Document doc = new Document();
		
		// XXX 7: neu: TextField, um Dateiinhalt zu indexieren
		doc.add(new TextField(CONTENT, new FileReader(f))); // 7
		// XXX 8: neu: numerische Feld, um Filegröße zu indexieren
		doc.add(new LongPoint(FILESIZE, f.length()));// 8
		// XXX 9: neu: StringField, um Dateipfad zu indexieren
		doc.add(new StringField(PATH, f.getCanonicalPath(), // 9
				Field.Store.YES));// 9
		return doc;
	}
	
	private void indexFile(File f) throws Exception {
		System.out.println("Indexing " + f.getCanonicalPath());
		Document doc = getDocument(f);
		writer.addDocument(doc); // 10
	}
}
