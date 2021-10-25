package es.joseluisgs.dam.blog.dto;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

import java.sql.Timestamp;


@Data
@Builder
public class LoginDTO {
    private ObjectId id;
    private Timestamp ultimoAcceso;
    private String token;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "userID=" + id +
                ", ultimoAcceso=" + ultimoAcceso +
                ", token='" + token + '\'' +
                '}';
    }
}
