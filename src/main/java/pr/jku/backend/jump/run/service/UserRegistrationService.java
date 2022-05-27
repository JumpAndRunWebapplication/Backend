package pr.jku.backend.jump.run.service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import pr.jku.backend.jump.run.exeption.badRequest.BadRequestException;
import pr.jku.backend.jump.run.exeption.usernameAlreadyExists.UsernameAlreadyExistsException;
import pr.jku.backend.jump.run.exeption.wrongInput.WrongInputException;
import pr.jku.backend.jump.run.model.User;
import pr.jku.backend.jump.run.repository.UserRegistrationRepository;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Base64;

@Service
public class UserRegistrationService {
    private final UserRegistrationRepository userRegistrationRepository;
    private final MongoTemplate mongoTemplate;
    private final String key = "354asdg4asdf451563ew4r1tasdf";

    public UserRegistrationService(UserRegistrationRepository userRegistrationRepository, MongoTemplate mongoTemplate) {
        this.userRegistrationRepository = userRegistrationRepository;
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * encrypts the password with the use of Base64
     * @param password
     * @return
     */
    public String convertToEncrypt(String password) {
        if (password.isEmpty()){
            return null;
        } else {
            password += key;
            return Base64.getEncoder().encodeToString(password.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * searches for user by username in mongoDB database
     * @param username
     * @return
     * @throws Exception
     */
    public User findUserByUsername(String username) throws Exception {
//        User fetchedUser = userRegistrationRepository.findUserByUsername(username);
//        if (fetchedUser == null) {
//            throw new BadRequestException("User not found");
//        } else {
//            return fetchedUser;
//        }
        return userRegistrationRepository.findUserByUsername(username);
    }

    /**
     * Checks if user with the same username already exists and throws exception if true;
     * Creates new user-document in mongoDB database if username does not exist
     * @param user
     * @return
     * @throws Exception
     */
    public User saveUser(User user) throws Exception {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new BadRequestException("Bad request!");
        }
        if (findUserByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("This username is already in use!");
        } else {
            user.setPassword(convertToEncrypt(user.getPassword()));
            return userRegistrationRepository.save(user);
        }
    }

    /**
     * searches for user with the given username and password
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    public User fetchUserByUsernameAndPassword(String username, String password) throws Exception {
        if (username == null || password == null) {
            throw new BadRequestException("Bad request!");
        } else {
            password = convertToEncrypt(password);
            User fetchedUser = userRegistrationRepository.findUserByUsernameAndPassword(username, password);
            if (fetchedUser == null) {
                throw new WrongInputException("Wrong username or password!");
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("username").is(username));
            Update update = new Update();
            update.set("lastLogin", new Timestamp(System.currentTimeMillis()));
            return mongoTemplate.findAndModify(query, update, User.class);
        }
    }

    /**
     * adds a score to the current score of the user
     * @param username
     * @param score
     * @return
     * @throws Exception
     */
    public User addScore(String username, int score) throws Exception {
        User fetchUser = this.findUserByUsername(username);
        if (username == "" || username == null) {
            System.out.println("ERROR!");
        }
        int currentScore = fetchUser.getScore() + score;
        System.out.println(currentScore);

        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        Update update = new Update();
        update.set("score", currentScore);
        return mongoTemplate.findAndModify(query, update, User.class);
    }
}
