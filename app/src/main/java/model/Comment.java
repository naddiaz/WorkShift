package model;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by NESTOR on 20/06/2015.
 */
public class Comment {

    private DatabaseHelper databaseHelper;
    private Dao<Comment, Integer> commentDAO;

    public Comment(DatabaseHelper databaseHelper){
        this.databaseHelper = databaseHelper;
    }

    public Dao<Comment, Integer> getCommentDAO() throws SQLException {
        if (commentDAO == null) {
            commentDAO = databaseHelper.getDao(Comment.class);
        }
        return commentDAO;
    }
}