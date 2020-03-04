package pl.antonina.tasks.parent;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParentMapperTest {

    private final ParentMapper parentMapper = new ParentMapper();

    @Test
    void mapParentView() {
        String name = "Antonina";
        Parent parent = new Parent();
        parent.setName(name);

        ParentView parentView = parentMapper.mapParentView(parent);

        assertThat(parentView.getName()).isEqualTo(name);
    }
}