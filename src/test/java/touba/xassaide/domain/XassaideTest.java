package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class XassaideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Xassaide.class);
        Xassaide xassaide1 = new Xassaide();
        xassaide1.setId(1L);
        Xassaide xassaide2 = new Xassaide();
        xassaide2.setId(xassaide1.getId());
        assertThat(xassaide1).isEqualTo(xassaide2);
        xassaide2.setId(2L);
        assertThat(xassaide1).isNotEqualTo(xassaide2);
        xassaide1.setId(null);
        assertThat(xassaide1).isNotEqualTo(xassaide2);
    }
}
