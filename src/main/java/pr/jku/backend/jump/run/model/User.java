package pr.jku.backend.jump.run.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Document
@Data
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private int score;
    private Date registrationDate;
    private Date lastLogin;

    public User (String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.registrationDate = new Timestamp(System.currentTimeMillis());
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }
}
