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
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<URI, ISet<URI>>();
        ISet<URI> allLinks = new ChainedHashSet<URI>();
        for (Webpage page : webpages) {
            allLinks.add(page.getUri());
        }
        for (Webpage page : webpages) {
            IList<URI> links = page.getLinks();
            ISet<URI> edges = new ChainedHashSet<URI>();
            for (URI uri : links) {
                if (allLinks.contains(uri) && !uri.equals(page.getUri())) {
                    edges.add(uri);
                }
            }
            graph.put(page.getUri(), edges);
        }
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        
        IDictionary<URI, Double> dict = new ChainedHashDictionary<URI, Double>();
        double rank = 1.0 / graph.size();
        for (KVPair<URI, ISet<URI>> pair : graph) {
            dict.put(pair.getKey(), rank);
        }
        for (int i = 0; i < limit; i++) {
            IDictionary<URI, Double> tempDict = new ChainedHashDictionary<URI, Double>();
            for (KVPair<URI, Double> pair : dict) {
                tempDict.put(pair.getKey(), 0.0);
            }            
            for (KVPair<URI, Double> pair : dict) {               
                URI uri = pair.getKey();
                double oldRank = pair.getValue();
                ISet<URI> links = graph.get(uri);
                int numLinks = links.size();
                if (numLinks < 1) {
                    for (KVPair<URI, ISet<URI>> page : graph) {
                        double newRank = ((decay * oldRank) / graph.size()) + tempDict.get(page.getKey());
                        tempDict.put(page.getKey(), newRank);
                    }
                }else {
                    for (URI link : links) {
                        double newRank = ((decay * oldRank) / numLinks) + tempDict.get(link);
                        tempDict.put(link, newRank);
                    }
                }
            }
            
            for (KVPair<URI, Double> pair : dict) {
                URI uri = pair.getKey();
                double newSurf = (1.0 - decay) / graph.size();
                tempDict.put(uri, tempDict.get(uri) + newSurf);
            }
            boolean converge = true;
            for (KVPair<URI, Double> pair : dict) {
                URI uri = pair.getKey();
                double diff = Math.abs(tempDict.get(uri) - dict.get(uri));
                if (diff > epsilon) {
                    converge = false;
                    break;
                }
            }
            if (converge) {
                return tempDict;
            }else {
                dict = tempDict;
            }
        }
        return dict;    
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        return this.pageRanks.get(pageUri);
        
    }
}
