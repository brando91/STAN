package it.disco.unimib.stan.unit;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import it.disco.unimib.stan.webapp.ContactsPage;

import org.junit.Ignore;

public class ContactsPageTest {

	@Ignore
	public void containsTeam() throws Exception {
		
		String page = new ContactsPage().process(new CommunicationTestDouble());
		
		assertThat(page, containsString("Brando Preda"));
		assertThat(page, containsString("Riccardo Porrini"));
		assertThat(page, containsString("Matteo Palmonari"));
	}
}
