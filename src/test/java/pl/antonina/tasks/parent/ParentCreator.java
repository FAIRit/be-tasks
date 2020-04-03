package pl.antonina.tasks.parent;

import pl.antonina.tasks.user.Gender;
import pl.antonina.tasks.user.UserData;

import java.util.UUID;

class ParentCreator {

    static ParentData createParentData() {
        ParentData parentData = new ParentData();
        parentData.setGender(Gender.FEMALE);
        parentData.setName("Name");
        UserData userData = new UserData();
        final String randomEmail = UUID.randomUUID().toString() + "@gmail.com";
        userData.setEmail(randomEmail);
        userData.setPassword("Password");
        parentData.setUserData(userData);
        return parentData;
    }

    static ParentData createNewParentData() {
        ParentData parentData = new ParentData();
        parentData.setName("new name");
        parentData.setGender(Gender.MALE);
        UserData userData = new UserData();
        final String randomEmail = UUID.randomUUID().toString() + "@gmail.com";
        userData.setEmail(randomEmail);
        userData.setPassword("new Password");
        parentData.setUserData(userData);
        return parentData;
    }
}