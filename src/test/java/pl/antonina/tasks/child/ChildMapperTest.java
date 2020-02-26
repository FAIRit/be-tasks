package pl.antonina.tasks.child;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChildMapperTest {

    private final ChildMapper childMapper = new ChildMapper();

    @Test
    void mapChildView() {

        Child child = new Child();
        child.setName("Natalia");

        ChildView childView = childMapper.mapChildView(child);

        assertThat(childView.getName())
                .isEqualTo("Natalia");
    }
}