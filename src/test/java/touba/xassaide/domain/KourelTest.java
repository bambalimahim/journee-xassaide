package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class KourelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kourel.class);
        Kourel kourel1 = new Kourel();
        kourel1.setId(1L);
        Kourel kourel2 = new Kourel();
        kourel2.setId(kourel1.getId());
        assertThat(kourel1).isEqualTo(kourel2);
        kourel2.setId(2L);
        assertThat(kourel1).isNotEqualTo(kourel2);
        kourel1.setId(null);
        assertThat(kourel1).isNotEqualTo(kourel2);
    }
}
