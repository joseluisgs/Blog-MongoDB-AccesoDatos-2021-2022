package es.joseluisgs.dam.blog.repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.model.Comment;
import es.joseluisgs.dam.blog.model.Post;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class CommentRepository implements CrudRespository<Comment, ObjectId> {
    @Override
    public List<Comment> findAll() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        List<Comment> list = commentCollection.find().into(new ArrayList<Comment>());
        mongoController.close();
        return list;
    }

    @Override
    public Comment getById(ObjectId ID) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        Comment comment = commentCollection.find(eq("_id", ID)).first();
        mongoController.close();
        if (comment != null)
            return comment;
        throw new SQLException("Error CommentRepository no existe comentario con ID: " + ID);
    }

    @Override
    public Comment save(Comment comment) throws SQLException {
        UUID uuid = UUID.randomUUID();
        comment.setUuid(uuid.toString());
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        try {
            commentCollection.insertOne(comment);
            return comment;
        } catch (Exception e) {
            throw new SQLException("Error CommentRepository al insertar comentario en BD: " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Comment update(Comment comment) throws SQLException {
        UUID uuid = UUID.randomUUID();
        comment.setUuid(uuid.toString());
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        try {
            Document filtered = new Document("_id", comment.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            return commentCollection.findOneAndReplace(filtered, comment, returnDoc);
        } catch (Exception e) {
            throw new SQLException("Error CommentRepository al actualizar comentario con id: " + comment.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }

    }

    @Override
    public Comment delete(Comment comment) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        try {
            Document filtered = new Document("_id", comment.getId());
            return commentCollection.findOneAndDelete(filtered);
        } catch (Exception e) {
            throw new SQLException("Error CommentRepository al eliminar comentario con id: " + comment.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    public List<Comment> getByUserId(ObjectId userId) {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Comment> commentCollection = mongoController.getCollection("blog", "comment", Comment.class);
        List<Comment> list = commentCollection.find(eq("user", userId)).into(new ArrayList<Comment>());
        mongoController.close();
        return list;
    }
}
