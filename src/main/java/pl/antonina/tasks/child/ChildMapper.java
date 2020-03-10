package pl.antonina.tasks.child;

import org.springframework.stereotype.Component;

@Component
public class ChildMapper {

    public ChildView mapChildView(Child child){
       ChildView childView = new ChildView();
       childView.setName(child.getName());
       childView.setBirthDate(child.getBirthDate());
       childView.setEmail(child.getUser().getEmail());
       childView.setGender(child.getGender());
       return childView;
    }
}