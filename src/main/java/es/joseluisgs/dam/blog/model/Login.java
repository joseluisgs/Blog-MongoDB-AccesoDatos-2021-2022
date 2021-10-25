package es.joseluisgs.dam.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Login {
    private ObjectId id;
    private Timestamp ultimoAcceso;
    private String token;

    public ObjectId getId() {
        // return userId;
        return id;
    }

    public Timestamp getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(Timestamp ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Login login = (Login) o;
        return id == login.id && Objects.equals(ultimoAcceso, login.ultimoAcceso) && Objects.equals(token, login.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ultimoAcceso, token);
    }


    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", ultimoAcceso=" + ultimoAcceso +
                ", token='" + token + '\'' +
                '}';
    }
}
