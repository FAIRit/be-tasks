package pl.antonina.tasks.parent;

import org.springframework.stereotype.Component;

@Component
public class ParentMapper {

    public ParentView mapParentView(Parent parent){
        ParentView parentView = new ParentView();
        parentView.setName(parent.getName());
        parentView.setEmail(parent.getUser().getEmail());
        parentView.setGender(parent.getGender());
        return parentView;
    }
}