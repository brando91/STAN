/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.disco.unimib.stan.rmlProcessor;

import org.openrdf.model.Statement;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 *
 * @author mielvandersande
 */
class StatementCounter extends RDFHandlerBase {

    private int countedStatements = 0;

    @Override
    public void handleStatement(Statement st) {
        countedStatements++;
    }

    public int getCountedStatements() {
        return countedStatements;
    }
}
