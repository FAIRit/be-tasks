package pl.antonina.tasks.child;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChildMapperTest {

    private final ChildMapper childMapper = new ChildMapper();

    @Test
    void mapChildView() {
        String name = "Natalia";
        Integer points = 10;
        Child child = new Child();
        child.setName(name);
        child.setPoints(points);

        ChildView childView = childMapper.mapChildView(child);

        assertThat(childView.getName()).isEqualTo(name);
        assertThat(childView.getPoints()).isEqualTo(points);
    }
}