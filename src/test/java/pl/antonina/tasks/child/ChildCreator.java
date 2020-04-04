package pl.antonina.tasks.child;

import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

import java.time.LocalDate;
import java.util.UUID;

class ChildCreator {

    static ChildData createChildData() {
        ChildData childData = new ChildData();
        childData.setGender(Gender.FEMALE);
        childData.setName("Name");
        childData.setBirthDate(LocalDate.of(2017, 2, 2));
        UserData userData = new UserData();
        final String randomEmail = UUID.randomUUID().toString() + "@gmail.com";
        userData.setEmail(randomEmail);
        userData.setPassword("Password");
        childData.setUserData(userData);
        return childData;
    }

    static ChildData createNewChildData() {
        ChildData childData = new ChildData();
        childData.setName("new name");
        childData.setGender(Gender.MALE);
        childData.setBirthDate(LocalDate.of(2019, 3, 3));
        UserData userData = new UserData();
        final String randomEmail = UUID.randomUUID().toString() + "@gmail.com";
        userData.setEmail(randomEmail);
        userData.setPassword("new Password");
        childData.setUserData(userData);
        return childData;
    }
}