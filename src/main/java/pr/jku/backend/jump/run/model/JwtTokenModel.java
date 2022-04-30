package pr.jku.backend.jump.run.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
public class JwtTokenModel {
    @Id
    private String jwtToken;

    public JwtTokenModel() {}

    public JwtTokenModel(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
