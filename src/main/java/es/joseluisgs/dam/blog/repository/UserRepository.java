package es.joseluisgs.dam.blog.repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class UserRepository implements CrudRespository<User, ObjectId> {
    @Override
    public List<User> findAll() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);
        List<User> list = userCollection.find().into(new ArrayList<User>());
        mongoController.close();
        return list;
    }

    @Override
    public User getById(ObjectId ID) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);
        User user = userCollection.find(eq("_id", ID)).first();
        mongoController.close();
        if (user != null)
            return user;
        throw new SQLException("Error UserRepository no existe usuario con ID: " + ID);
    }

    @Override
    public User save(User user) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);
        try {
            userCollection.insertOne(user);
            return user;
        } catch (Exception e) {
            throw new SQLException("Error UserRepository al insertar usuario en BD:" + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public User update(User user) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);
        try {
            Document filtered = new Document("_id", user.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            return userCollection.findOneAndReplace(filtered, user, returnDoc);
        } catch (Exception e) {
            throw new SQLException("Error UserRepository al actualizar usuario con id: " + user.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }

    }

    @Override
    public User delete(User user) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);
        try {
            Document filtered = new Document("_id", user.getId());
            return userCollection.findOneAndDelete(filtered);
        } catch (Exception e) {
            throw new SQLException("Error UserRepository al eliminar usuario con id: " + user.getId() + ": " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    public User getByEmail(String userMail) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<User> userCollection = mongoController.getCollection("blog", "user", User.class);

        User user = userCollection.find(eq("email", userMail)).first();
        mongoController.close();
        if (user != null)
            return user;

        throw new SQLException("Error UserRepository no existe usuario con Email: " + userMail);
    }
}
