package es.joseluisgs.dam.blog.repository;

import com.mongodb.client.MongoCollection;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.model.Login;
import org.bson.Document;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class LoginRepository implements CrudRespository<Login, Long> {
    @Override
    public List<Login> findAll() throws SQLException {
        throw new SQLException("Error: Método findAll no implementado");
    }

    @Override
    public Login getById(Long aLong) throws SQLException {
        throw new SQLException("Error: Método getById  no implementado");
    }

    @Override
    public Login save(Login login) throws SQLException {
        UUID uuid = UUID.randomUUID();
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> loginCollection = mongoController.getCollection("blog", "login", Login.class);
        try {
            loginCollection.insertOne(login);
            return login;
        } catch (Exception e) {
            throw new SQLException("Error LoginRepository al insertar login en BD");
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Login update(Login login) throws SQLException {
        throw new SQLException("Error: Método update no implementado");
    }

    @Override
    public Login delete(Login login) throws SQLException {
        throw new SQLException("Error: Método update no implementado");
    }

    public boolean deleteByUserId(Long userId) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Login> commentCollection = mongoController.getCollection("blog", "login", Login.class);
        try {
            Document filtered = new Document("user_id", userId);
            return true;
        } catch (Exception e) {
            throw new SQLException("Error LoginRepository al eliminar login con id: " + userId);
        } finally {
            mongoController.close();
        }
    }


}
