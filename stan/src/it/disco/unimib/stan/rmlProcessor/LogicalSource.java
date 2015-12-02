package it.disco.unimib.stan.rmlProcessor;


/**
 *
 * @author mielvandersande
 */
public interface LogicalSource {

    /**
     * Every logical source has an expression resulting in a list of iterating
     * values.
     */
    public String getReference();

    /**
     * Every logical source has an identifier, which is a schema-qualified name
     * pointing at a source.
     */
    public String getIdentifier();

    /**
     * Every logical source can indicate how its expression should be
     * interpreted
     */
    public Vocab.QLTerm getReferenceFormulation();
}
