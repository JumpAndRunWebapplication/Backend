package pr.jku.backend.jump.run.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class LoginUser {
    @Id
    private String username;
    private String password;

    public LoginUser() {}

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
