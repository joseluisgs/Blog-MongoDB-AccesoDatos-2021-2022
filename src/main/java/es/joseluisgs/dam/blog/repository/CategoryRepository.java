package es.joseluisgs.dam.blog.repository;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import es.joseluisgs.dam.blog.database.MongoDBController;
import es.joseluisgs.dam.blog.model.Category;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class CategoryRepository implements CrudRespository<Category, ObjectId> {
    @Override
    public List<Category> findAll() {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Category> categoryCollection = mongoController.getCollection("blog", "category", Category.class);
        List<Category> list = categoryCollection.find().into(new ArrayList<Category>());
        mongoController.close();
        return list;
    }

    @Override
    public Category getById(ObjectId ID) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Category> categoryCollection = mongoController.getCollection("blog", "category", Category.class);
        Category category = categoryCollection.find(eq("_id", ID)).first();
        mongoController.close();
        if (category != null)
            return category;
        throw new SQLException("Error CategoryRepository no existe categoría con ID: " + ID);
    }

    @Override
    public Category save(Category category) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Category> categoryCollection = mongoController.getCollection("blog", "category", Category.class);
        try {
            categoryCollection.insertOne(category);
            return category;
        } catch (Exception e) {
            throw new SQLException("Error CategoryRepository al insertar cantegoria en BD: " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }

    @Override
    public Category update(Category category) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Category> categoryCollection = mongoController.getCollection("blog", "category", Category.class);
        try {
            Document filtered = new Document("_id", category.getId());
            FindOneAndReplaceOptions returnDoc = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
            return categoryCollection.findOneAndReplace(filtered, category, returnDoc);
        } catch (Exception e) {
            throw new SQLException("Error CategoryRepository al actualizar categoria con id: " + category.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }

    }

    @Override
    public Category delete(Category category) throws SQLException {
        MongoDBController mongoController = MongoDBController.getInstance();
        mongoController.open();
        MongoCollection<Category> categoryCollection = mongoController.getCollection("blog", "category", Category.class);
        try {
            Document filtered = new Document("_id", category.getId());
            return categoryCollection.findOneAndDelete(filtered);
        } catch (Exception e) {
            throw new SQLException("Error CategoryRepository al eliminar categoria con id: " + category.getId() + " " + e.getMessage());
        } finally {
            mongoController.close();
        }
    }
}
