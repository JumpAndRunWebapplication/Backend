package pr.jku.backend.jump.run.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pr.jku.backend.jump.run.model.AddScore;
import pr.jku.backend.jump.run.model.JwtTokenModel;
import pr.jku.backend.jump.run.model.LoginUser;
import pr.jku.backend.jump.run.model.User;
import pr.jku.backend.jump.run.service.UserRegistrationService;
import pr.jku.backend.jump.run.utils.JwtUtils;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserRegistrationController {
    private final UserRegistrationService userRegistrationService;

    /**
     * sets a REST-API for creating a new user (registration of a new user)
     * throws exception if the username already exists
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/newUser")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        User newUser = this.userRegistrationService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * creates a REST-API for user login
     * throws exception if username of password do not exist
     * creates a JWT with the username - this JWT gets stored in the localStorage header of the frontend
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<JwtTokenModel> loginUser(@RequestBody LoginUser user) throws Exception {
        User fetchedUser = userRegistrationService.fetchUserByUsernameAndPassword(user.getUsername(), user.getPassword());

        JwtTokenModel jwtToken = new JwtTokenModel(JwtUtils.generateToken(fetchedUser.getUsername(), fetchedUser.getId().toString()));

        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    /**
     * REST-API for adding score to a registered user by username
     * @param score
     * @return
     * @throws Exception
     */
    @PostMapping("/addScore")
    public ResponseEntity<User> saveScore(@RequestBody AddScore score) throws Exception {
        User newUser = this.userRegistrationService.addScore(score.getUsername(), score.getScore());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * return user by decrypting JWT (JWT is stored within the header of the frontend)
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping("/getUser")
    public ResponseEntity<User> getUser(HttpServletRequest request) throws Exception {
        String jwtToken = request.getHeader("Authorization");
        if(jwtToken == null || (!JwtUtils.isJwtTokenValid(jwtToken))) {
            System.err.println("No authorization-header set or invalid jwtToken provided.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        System.out.println("test");
        String username = JwtUtils.getUsernameFromJwtToken(jwtToken);
        User fetchedUser = this.userRegistrationService.findUserByUsername(username);
        return new ResponseEntity<>(fetchedUser, HttpStatus.OK);
    }

    /**
     * return the current score of a user by username
     * @param username
     * @return
     * @throws Exception
     */
    @GetMapping("/getScore/{username}")
    public ResponseEntity<String> getScore(@PathVariable String username) throws Exception {
        User fetchedScore = this.userRegistrationService.findUserByUsername(username);
        return new ResponseEntity<>(fetchedScore.getUsername(), HttpStatus.OK);
    }
}
