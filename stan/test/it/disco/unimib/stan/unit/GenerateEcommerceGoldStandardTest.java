package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.core.VirtualResource;
import it.disco.unimib.stan.experiments.GenerateEcommerceGoldStandard;
import it.disco.unimib.stan.experiments.GoldStandard;
import it.disco.unimib.stan.experiments.GoldStandardEntry;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GenerateEcommerceGoldStandardTest {

	@Test
	public void emptyGoldStandard() throws Exception {
		GoldStandard goldStandard = new GenerateEcommerceGoldStandard(new VirtualResource(), listings()).generate();
		
		assertThat(goldStandard.entries().size(), is(equalTo(0)));
	}
	
	@Test
	public void oneMerchantOneColumn() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop]")
										.addLine("Col1=marca text");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop-col1 Q0 marca 1"));
	}

	@Test
	public void moreMerchants() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=marca text")
										.addLine("")
										.addLine("[shop2]")
										.addLine("Col1=prezzo double")
										.addLine("")
										.addLine("[shop3]")
										.addLine("Col1=prezzo double");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1", "shop2", "shop3")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop1-col1 Q0 marca 1"));
		assertThat(entries.get(1).display(), equalTo("shop2-col1 Q0 prezzo 1"));
		assertThat(entries.get(2).display(), equalTo("shop3-col1 Q0 prezzo 1"));
	}
	
	@Test
	public void oneMerchantWithTwoColumns() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=Marca text")
										.addLine("Col2=Prezzo double");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop1-col1 Q0 marca 1"));
		assertThat(entries.get(1).display(), equalTo("shop1-col2 Q0 prezzo 1"));
	}
	
	@Test
	public void merchantWithOtherEntriesThanColumns() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("any=any")
										.addLine("Col1=Prezzo double");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop1-col1 Q0 prezzo 1"));
	}
	
	@Test
	public void shouldIgnoreOtherEntryStartingWithCol() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("ColNameHeader=any")
										.addLine("Col1=Prezzo double");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop1-col1 Q0 prezzo 1"));
	}
	
	@Test
	public void shouldIgnoreNumericValuesInColumnNames() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=Immagine1 memo")
										.addLine("Col2=Immagine12 memo");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.get(0).display(), equalTo("shop1-col1 Q0 immagine 1"));
		assertThat(entries.get(1).display(), equalTo("shop1-col2 Q0 immagine 1"));
	}
	
	@Test
	public void NoDuplicateMerchants() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=Immagine1 text")
										.addLine("")
										.addLine("[shop1]")
										.addLine("Col1=Immagine1 text");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.size(), equalTo(1));
	}
	
	@Test
	public void shouldIgnoreMerchantWithNoPipeDelimiter() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=Immagine1 text")
										.addLine("Format=Delimited(#)")
										.addLine("")
										.addLine("[shop2]")
										.addLine("Format=Delimited(|)")
										.addLine("Col1=Immagine1 text");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1", "shop2")).generate().entries();
		
		assertThat(entries.size(), equalTo(1));
		assertThat(entries.get(0).display(), equalTo("shop2-col1 Q0 immagine 1"));
	}
	
	@Test
	public void shouldIgnoreMerchantsWithNoListing() throws Exception {
		VirtualResource schemaIni = new VirtualResource()
										.addLine("[shop1]")
										.addLine("Col1=Immagine1 memo")
										.addLine("")
										.addLine("[shop2]")
										.addLine("Col1=Immagine1 memo");
		
		List<GoldStandardEntry> entries = new GenerateEcommerceGoldStandard(schemaIni, listings("shop1")).generate().entries();
		
		assertThat(entries.size(), equalTo(1));
	}
	
	private ArrayList<String> listings(String... merchants) {
		ArrayList<String> listings = new ArrayList<String>();
		for(String merchant : merchants){
			listings.add(merchant);
		}
		return listings;
	}
}
