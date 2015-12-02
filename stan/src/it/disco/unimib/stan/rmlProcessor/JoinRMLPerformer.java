package it.disco.unimib.stan.rmlProcessor;

import it.disco.unimib.stan.core.LogEvents;

import java.util.HashMap;

import net.antidot.semantic.rdf.model.impl.sesame.SesameDataSet;

import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

/**
 * Performer to do joins without any join conditions
 *
 * @author mielvandersande, andimou
 */
public class JoinRMLPerformer extends NodeRMLPerformer{
    
	private static LogEvents log = new LogEvents();
	private Resource subject;
    private URI predicate;

    public JoinRMLPerformer(RMLProcessor processor, Resource subject, URI predicate) {
        super(processor);
        this.subject = subject;
        this.predicate = predicate;
    }

    /**
     * Compare expressions from join to complete it
     * 
     * @param node current object in parent iteration
     * @param dataset
     * @param map 
     */
    @Override
    public void perform(HashMap<String, String> node, SesameDataSet dataset, TriplesMap map) {
        Value object = processor.processSubjectMap(dataset, map.getSubjectMap(), node);

        if (object == null){
            return;
        }       
        log.debug("[JoinRMLPerformer:addTriples] Subject "
                    + subject + " Predicate " + predicate + "Object " + object.toString());
        
        //add the join triple
        dataset.add(subject, predicate, object);
    }


}
