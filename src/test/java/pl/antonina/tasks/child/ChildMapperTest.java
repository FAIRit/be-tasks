package pl.antonina.tasks.child;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ChildMapperTest {

    private final ChildMapper childMapper = new ChildMapper();

    @Test
    void mapChildView() {
        Child child = new Child();
        child.setName("Alan");

        ChildView childView = childMapper.mapChildView(child);

        // soluton 1
        assertEquals("Alan", childView.getName());
        assertEquals(4, childView.getName().length());
        assertTrue(childView.getName().contains("Al"));

        // solution 2
        assertThat(childView.getName())
                .isEqualTo("Alan")
                .hasSize(4)
                .contains("AL");
    }
}