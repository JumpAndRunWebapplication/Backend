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

    @PostMapping("/newUser")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws Exception {
        User newUser = this.userRegistrationService.saveUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenModel> loginUser(@RequestBody LoginUser user) throws Exception {
        User fetchedUser = userRegistrationService.fetchUserByUsernameAndPassword(user.getUsername(), user.getPassword());

        JwtTokenModel jwtToken = new JwtTokenModel(JwtUtils.generateToken(fetchedUser.getUsername(), fetchedUser.getId().toString()));

        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
    }

    @PostMapping("/newScore")
    public ResponseEntity<User> saveScore(@RequestBody AddScore score) throws Exception {
        User newUser = this.userRegistrationService.addScore(score.getUsername(), score.getScore());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

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
}
