package tech.qmates.openchat.web;

import tech.qmates.openchat.database.InMemoryUserRepository;
import tech.qmates.openchat.domain.repository.UserRepository;

public class AppFactory {

    private static InMemoryUserRepository userRepositoryInstance;

    public static UserRepository getUserRepository() {
        if(userRepositoryInstance == null)
            userRepositoryInstance = new InMemoryUserRepository();

        return userRepositoryInstance;
    }

    public static void resetRepositories() {
        if(userRepositoryInstance != null)
            userRepositoryInstance.reset();
    }

}
