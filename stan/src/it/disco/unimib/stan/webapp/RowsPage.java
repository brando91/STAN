package it.disco.unimib.stan.webapp;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.FileResource;
import it.disco.unimib.stan.core.Json;
import it.disco.unimib.stan.core.Row;
import it.disco.unimib.stan.core.WorkingAreaPaths;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RowsPage implements Page {

	@Override
	public String route() {
		return "annotation/rows";
	}

	@Override
	public String process(Communication requestAndResponse) throws Exception {
		ArrayList<Json> jsonRows = new ArrayList<Json>();
		int total = 0;
		
		if(new ParameterValidator("limit", "offset").isCorrectRequest(requestAndResponse)){
			String limit = requestAndResponse.getParameter("limit");
			String offset = requestAndResponse.getParameter("offset");
			
			ArrayList<Row> rows = rows(new CookieManager(requestAndResponse));
			total = rows.size();
			
			for(Row row : pagination(rows, limit, offset)){
				ArrayList<String> rowCells = row.cells();
				Json jsonRow = new Json();
				for(int i = 0; i < rowCells.size(); i++){
					jsonRow.parameter((i+1) + "",  rowCells.get(i));
				}
				jsonRows.add(jsonRow);
			}
		}
		
		requestAndResponse.setContentType("application/json");
		return new Json().parameter("total", total + "")
						 .jsonParameters("rows", jsonRows)
						 .serialize();
	}

	private ArrayList<Row> rows(CookieManager cookieManager) throws IOException {
		String user = cookieManager.getUser();
		WorkingTable workingTable = new WorkingTable(cookieManager.getTable(), user);
		String tablePath = new WorkingAreaPaths().tables(user).file(workingTable.getTableName()).path();
		
		CSVTable table = new CSVTable(new FileResource(tablePath))
								.withSeparator(workingTable.getSeparator())
								.withTextDelimiter(workingTable.getDelimiter());
		if(workingTable.hasHeader()) table.withHeader();
		return table.rows();
	}

	private List<Row> pagination(List<Row> rows, String limit, String offset) throws IOException {
		int lowBound = Integer.parseInt(offset);
		int upBound = Integer.parseInt(limit) + lowBound;
		upBound = (upBound < rows.size()) ? upBound : rows.size();
		
		return rows.subList(lowBound, upBound);
	}

}
