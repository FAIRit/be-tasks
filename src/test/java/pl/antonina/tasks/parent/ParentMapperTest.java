package pl.antonina.tasks.parent;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ParentMapperTest {

    private final ParentMapper parentMapper = new ParentMapper();

    @Test
    void mapParentView() {

        Parent parent = new Parent();
        parent.setName("Antonina");

        ParentView parentView = parentMapper.mapParentView(parent);

        assertThat(parentView.getName())
                .isEqualTo("Antonina");
    }
}