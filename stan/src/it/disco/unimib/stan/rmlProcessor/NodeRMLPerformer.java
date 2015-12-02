package it.disco.unimib.stan.rmlProcessor;

import it.disco.unimib.stan.core.LogEvents;

import java.util.HashMap;

import net.antidot.semantic.rdf.model.impl.sesame.SesameDataSet;

import org.openrdf.model.Resource;

/**
 * Performs the normal handling of an object in the iteration.
 * 
 * @author mielvandersande, andimou
 * 
 */
public class NodeRMLPerformer implements RMLPerformer{
    
	private static LogEvents log = new LogEvents();
    protected RMLProcessor processor;

    /**
     * 
     * @param processor the instance processing these nodes
     */
    public NodeRMLPerformer(RMLProcessor processor) {
        this.processor = processor;
    }

    /**
     * Process the subject map and predicate-object maps
     * 
     * @param node current object in the iteration
     * @param dataset dataset for endresult
     * @param map current triple map that is being processed
     */
    @Override
    public void perform(HashMap<String, String> node, SesameDataSet dataset, TriplesMap map) {
        Resource subject = processor.processSubjectMap(dataset, map.getSubjectMap(), node);
        processor.processSubjectTypeMap(dataset, subject, map.getSubjectMap(), node);
        if (subject == null) 
            log.debug("[NodeRMLPerformer:perform] No subject was generated for " + map.getName() + "triple Map and row " +node.toString());
        else {
            for (PredicateObjectMap pom : map.getPredicateObjectMaps()) 
                processor.processPredicateObjectMap(dataset, subject, pom, node, map);
        }
    }
    
    /**
     *
     * @param node
     * @param dataset
     * @param map
     * @param subject
     */
    @Override
    public void perform(HashMap<String, String> node, SesameDataSet dataset, TriplesMap map, Resource subject) {
        processor.processSubjectTypeMap(dataset, subject, map.getSubjectMap(), node);
        for (PredicateObjectMap pom : map.getPredicateObjectMaps()) 
            processor.processPredicateObjectMap(dataset, subject, pom, node, map);
    }
}
