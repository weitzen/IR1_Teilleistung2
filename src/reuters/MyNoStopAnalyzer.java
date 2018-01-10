package reuters;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public class MyNoStopAnalyzer extends Analyzer {

	  public static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

	  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		   final StandardTokenizer src = new StandardTokenizer();
		    src.setMaxTokenLength(maxTokenLength);
		    TokenStream tok = new StandardFilter(src);
		    tok = new LowerCaseFilter(tok);
		
		    return new TokenStreamComponents(src, tok) {
		      @Override
		      protected void setReader(final Reader reader) {
		        // So that if maxTokenLength was changed, the change takes
		        // effect next time tokenStream is called:
		        src.setMaxTokenLength(MyNoStopAnalyzer.this.maxTokenLength);
		        super.setReader(reader);
		      }
		    };
	}
}