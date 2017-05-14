package fragment.submissions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import fragment.submissions.Application.Reassembler;

public class ApplicationTest {
	
	@Test
	public void testReasembleShort() {
		final String fragments = "O draconia;conian devil! Oh la;h lame sa;saint!";
		final String result = "O draconian devil! Oh lame saint!";
		assertThat(Reassembler.reassemble(fragments)).isEqualTo(result);
	}
	
	@Test
	public void testReasembleLong() {
		final String fragments = "m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al";
		final String result = "Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.";
		assertThat(Reassembler.reassemble(fragments)).isEqualTo(result);
	}
	
	@Test
	public void testGetOverlap() {
		assertThat(Reassembler.getOverlap("ABCDEF", "DEFG")).isEqualTo(3);
		assertThat(Reassembler.getOverlap("ABCDEF", "XYZABC")).isEqualTo(3);
		assertThat(Reassembler.getOverlap("ABCDEF", "BCDE")).isEqualTo(4);
		assertThat(Reassembler.getOverlap("ABCDEF", "XCDEZ")).isEqualTo(0);
	}
}
