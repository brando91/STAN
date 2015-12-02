
package it.disco.unimib.stan.rmlProcessor;

import it.disco.unimib.stan.rmlProcessor.Vocab.QLTerm;

/**
 * Interface for creating processors
 * @author mielvandersande
 */
public interface RMLProcessorFactory {
    
    public  RMLProcessor create(QLTerm term);
    
}
