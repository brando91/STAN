package it.disco.unimib.stan.rmlProcessor;

import it.disco.unimib.stan.rmlProcessor.Vocab.QLTerm;

/**
 * This factory class creates language-dependent processors processors
 *
 * @author mielvandersande
 */
public class ConcreteRMLProcessorFactory implements RMLProcessorFactory{

    /**
     * Create the language-dependent processor based on the given language
     * @param term Expression language
     * @return processor able to process the specified language
     */
    @Override
    public RMLProcessor create(QLTerm term) {
        switch (term){
            case CSV_CLASS:
                return new CSVProcessor();
		default:
			break;
        }
        return null;
    }
    
}
