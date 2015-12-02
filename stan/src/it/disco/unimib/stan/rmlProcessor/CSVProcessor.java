package it.disco.unimib.stan.rmlProcessor;

import static java.nio.charset.StandardCharsets.UTF_8;
import it.disco.unimib.stan.core.LogEvents;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.antidot.semantic.rdf.model.impl.sesame.SesameDataSet;

import org.openrdf.model.Resource;

import com.csvreader.CsvReader;

/**
 *
 * @author mielvandersande, andimou
 */
public class CSVProcessor extends AbstractRMLProcessor {

	private static LogEvents log = new LogEvents();
    private char getDelimiter(LogicalSource ls) {
        String d = RMLEngine.getFileMap().getProperty(ls.getIdentifier() + ".delimiter");
        if (d == null) {
            return ',';
        }
        return d.charAt(0);
    }

    @Override
    public void execute(SesameDataSet dataset, TriplesMap map, RMLPerformer performer, InputStream input) {
        //InputStream fis = null;
        try {
            char delimiter = getDelimiter(map.getLogicalSource());

            //TODO: add charset guessing
            CsvReader reader = new CsvReader(input, Charset.defaultCharset());
            reader.setDelimiter(delimiter);
            
            reader.readHeaders();
            //Iterate the rows
            while (reader.readRecord()) {
                HashMap<String, String> row = new HashMap<>();
               for (String header : reader.getHeaders()) {
                   //log.debug("[CSVProcessor:extractValueFromNode] header " + header);
                   row.put(new String(header.getBytes("iso8859-1"), UTF_8), reader.get(header));
                    //row.put(header, reader.get(header));
                }
                //let the performer handle the rows
                performer.perform(row, dataset, map);
            }

        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage(), ex);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } 
    }

    @Override
    public List<String> extractValueFromNode(HashMap<String, String> node, String expression) {
        HashMap<String, String> row = (HashMap<String, String>) node;
        for(String key : row.keySet())
            key = new String(key.getBytes(), UTF_8);
        //call the right header in the row
        List<String> list = new ArrayList<String>();
        if (row.containsKey(expression)){
            list.add(row.get(expression));
        }
        return list;
    }

    @Override
    public void execute_node(SesameDataSet dataset, String expression, TriplesMap parentTriplesMap, RMLPerformer performer, Object node, Resource subject) {
        throw new UnsupportedOperationException("Not applicable for CSV sources."); //To change body of generated methods, choose Tools | Templates.
    }
}
