package it.disco.unimib.stan.experiments;

import it.disco.unimib.stan.core.CSVTable;
import it.disco.unimib.stan.core.Column;
import it.disco.unimib.stan.core.LogEvents;
import it.disco.unimib.stan.core.Resource;
import it.disco.unimib.stan.core.TrainingDataPath;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class SplitDataset {

	private ArrayList<GoldStandardEntry> goldStandardEntries;
	private ArrayList<MerchantConfiguration> configurations;

	public SplitDataset(ArrayList<GoldStandardEntry> goldStandardEntries, SchemaIni schemaIni) throws Exception {
		this.goldStandardEntries = goldStandardEntries;
		this.configurations = schemaIni.parse();
	}

	public void split(TrainingDataPath where, Resource... tables) throws Exception {
		double completed = 0;
		for(Resource table : tables){
			new LogEvents().info("Started splitting " + table.name());
			for(Column column : table(table).columns()){
				String label = labelFor(column);
				if(!label.isEmpty()){
					String savePath = where.column(label).file(column.id()).path();
					ArrayList<String> members = column.members();
					members.add(0, column.header());
					FileUtils.writeLines(new File(savePath), "UTF-8", members);
				}
			}
			completed++;
			new LogEvents().info(table.name() + " splitted (" + new DecimalFormat("###.##").format((completed/tables.length)*100) + "%)");
		}
	}

	private CSVTable table(Resource fileTable) {
		CSVTable csvTable = new CSVTable(fileTable);
		for(MerchantConfiguration configuration : this.configurations){
			if(configuration.merchantName().equals(fileTable.name())){
				if(configuration.hasHeader()) csvTable.withHeader();
				break;
			}
		}
		return csvTable;
	}

	private String labelFor(Column column) {
		for(GoldStandardEntry entry : this.goldStandardEntries){
			if(entry.id().equals(column.id())){
				return entry.label();
			}
		}
		return "";
	}

}
