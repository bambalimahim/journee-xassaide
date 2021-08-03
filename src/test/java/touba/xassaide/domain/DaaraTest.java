package touba.xassaide.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import touba.xassaide.web.rest.TestUtil;

class DaaraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Daara.class);
        Daara daara1 = new Daara();
        daara1.setId(1L);
        Daara daara2 = new Daara();
        daara2.setId(daara1.getId());
        assertThat(daara1).isEqualTo(daara2);
        daara2.setId(2L);
        assertThat(daara1).isNotEqualTo(daara2);
        daara1.setId(null);
        assertThat(daara1).isNotEqualTo(daara2);
    }
}
