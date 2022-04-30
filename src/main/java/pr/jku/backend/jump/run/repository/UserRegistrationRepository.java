package pr.jku.backend.jump.run.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pr.jku.backend.jump.run.model.User;

import java.util.Optional;

@Repository
public interface UserRegistrationRepository extends MongoRepository<User, String> {
    User findUserByUsernameAndPassword(String username, String password);

    User findUserByUsername(String username);
}
