package pl.antonina.tasks.child;

import org.springframework.stereotype.Component;

@Component
public class ChildMapper {

    public ChildView mapChildView(Child child){
       ChildView childView = new ChildView();
       childView.setName(child.getName());
       childView.setPoints(child.getPoints());
       return childView;
    }
}