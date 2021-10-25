package es.joseluisgs.dam.blog.repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.model.Post;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PostRepository implements CrudRespository<Post, ObjectId> {
    @Override
    public List<Post> findAll() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        List<Post> list = postCollection.find().into(new ArrayList<Post>());
        mongoController.close();
        return list;
    }

    @Override
    public Post getById(ObjectId ID) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        Post post = postCollection.find(eq("_id", ID)).first();
        mongoController.close();
        if (post != null)
            return post;
        throw new SQLException("Error PostRepository no existe post con ID: " + ID);
    }

    @Override
    public Post save(Post post) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        try {
            postCollection.insertOne(post);
            return post;
        } catch (Exception e) {
            throw new SQLException("Error PostRepository al insertar post en BD: " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Post update(Post post) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        try {
            Document filtered = new Document("_id", post.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            return postCollection.findOneAndReplace(filtered, post, returnDoc);
        } catch (Exception e) {
            throw new SQLException("Error PostRepository al actualizar post con id: " + post.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }

    }

    @Override
    public Post delete(Post post) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        try {
            Document filtered = new Document("_id", post.getId());
            return postCollection.findOneAndDelete(filtered);
        } catch (Exception e) {
            throw new SQLException("Error PostRepository al eliminar post con id: " + post.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    public List<Post> getByUserId(Long userId) {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Post> postCollection = mongoController.getCollection("blog", "post", Post.class);
        List<Post> list = postCollection.find(eq("user_id", userId)).into(new ArrayList<Post>());
        mongoController.close();
        return list;
    }

}
