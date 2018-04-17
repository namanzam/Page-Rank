package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing how "relevant" any given document is
 * to a given search query.
 *
 * See the spec for more details.
 */
public class TfIdfAnalyzer {
    private IDictionary<String, Double> idfScores;
    private IDictionary<URI, Double> normValues;
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    public TfIdfAnalyzer(ISet<Webpage> webpages) {        
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normValues = this.computeNorms(webpages);
    }
    
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }

    /**
     * Return a dictionary mapping every single unique word found
     * in every single document to their IDF score.
     */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        IDictionary<String, Integer> countDict = new ChainedHashDictionary<String, Integer>();
        IDictionary<String, Double> dict = new ChainedHashDictionary<String, Double>();

        int totDocs = pages.size();
        for (Webpage page : pages) {
            ISet<String> set = new ChainedHashSet<String>();
            for (String word : page.getWords()) {
                if (!set.contains(word)) {
                    set.add(word);
                    if (!countDict.containsKey(word)) {
                        countDict.put(word, 0);
                    }
                    countDict.put(word, countDict.get(word) + 1);
                    double calc = (countDict.get(word) > 0) ? Math.log((double) totDocs 
                            / countDict.get(word)) : 0.0;
                    dict.put(word, calc);
                }
            }
        }
        return dict;
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * The input list represents the words contained within a single document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        IDictionary<String, Integer> dict = new ChainedHashDictionary<String, Integer>();
        IDictionary<String, Double> dictScores = new ChainedHashDictionary<String, Double>();
        int totWords = words.size();
        
        for (String word : words) {
            if (!dict.containsKey(word)) {
                dict.put(word, 0);
            }
            dict.put(word, dict.get(word) + 1);
            double calc = (double) dict.get(word) / totWords;
            dictScores.put(word, calc);
        }
        return dictScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        IDictionary<URI, IDictionary<String, Double>> dict = 
                new ChainedHashDictionary<URI, IDictionary<String, Double>>();
 
        for (Webpage page : pages) {
            URI uri = page.getUri();
            IDictionary<String, Double> tfScores =  this.computeTfScores(page.getWords());
            IDictionary<String, Double> tempDict = this.helperTfIdfVectors(tfScores);
            dict.put(uri, tempDict);
        }
        return dict;
    }
    
    private IDictionary<String, Double> helperTfIdfVectors(IDictionary<String, Double> tfScores) {
        IDictionary<String, Double> dict = new ChainedHashDictionary<String, Double>();
        for (KVPair<String, Double> pair: tfScores) {
            String word = pair.getKey();
            double idfScore = this.idfScores.get(word);
            double tfScore = pair.getValue();
            double calc = idfScore * tfScore;
            dict.put(word, calc);
        }
        return dict;
    }


    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        IDictionary<String, Double> docVector = this.documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> tfScores = this.computeTfScores(query);
        IDictionary<String, Double> queryVector = this.helperTfIdfVectors(tfScores);
        
        double numerator = 0.0;
        for (String word : query) {
            double docWordScore = 0.0;
            if (docVector.containsKey(word)) {
                docWordScore = docVector.get(word);
            }
            double queryWordScore = queryVector.get(word);
            numerator += docWordScore * queryWordScore;
        }
        double denominator = this.normValues.get(pageUri) * this.norm(queryVector);
        if (denominator != 0) {
            return numerator / denominator;
        }
        return 0.0;
        
    }
    
    private IDictionary<URI, Double> computeNorms(ISet<Webpage> webpages) {
        IDictionary<URI, Double> dict = new ChainedHashDictionary<URI, Double>();
        for (Webpage page : webpages) {
            dict.put(page.getUri(), this.norm(this.documentTfIdfVectors.get(page.getUri())));
        }
        return dict;
    }

    private double norm(IDictionary<String, Double> vector) {
        double res = 0.0;
        for (KVPair<String, Double> pair : vector) {
            res += pair.getValue() * pair.getValue();
        }
        return Math.sqrt(res);
    }
}
