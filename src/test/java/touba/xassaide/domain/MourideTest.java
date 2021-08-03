package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class MourideTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Mouride.class);
        Mouride mouride1 = new Mouride();
        mouride1.setId(1L);
        Mouride mouride2 = new Mouride();
        mouride2.setId(mouride1.getId());
        assertThat(mouride1).isEqualTo(mouride2);
        mouride2.setId(2L);
        assertThat(mouride1).isNotEqualTo(mouride2);
        mouride1.setId(null);
        assertThat(mouride1).isNotEqualTo(mouride2);
    }
}
