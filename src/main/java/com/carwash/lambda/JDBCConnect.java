package com.carwash.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.sql.*;

public class JDBCConnect {

    public static final String SUCCESSFULLY_EXECUTED_QUERY = "Successfully executed query. ";
    public static final String CAUGHT_EXCEPTION = "Caught exception: ";
    private final Context context;

    private static final String URL = "jdbc:mysql://database-1.cktiweavgcsr.us-east-1.rds.amazonaws.com:3306";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin123*+";

    public JDBCConnect(Context context) {
        this.context = context;
    }


    public long saveUser(UserDTO userDTO) {
        LambdaLogger logger = context.getLogger();
        logger.log("Invoked JDBCSample.getCurrentTime");
        long idGenerated= 0L;
        String sql = "INSERT INTO carwash.user (firts_name, last_name, birth_date," +
                " address, gender, location, email, phone, city_id,photo, enable, email_confirmed, phone_confirmed, completed)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, Types.VARCHAR);
            stmt.setNull(2, Types.VARCHAR);
            stmt.setNull(3, Types.DATE);
            stmt.setNull(4, Types.VARCHAR);
            stmt.setNull(5, Types.VARCHAR);
            stmt.setNull(6, Types.VARCHAR);
            stmt.setString(7, userDTO.getEmail());
            stmt.setNull(8, Types.VARCHAR);
            stmt.setNull(9, Types.INTEGER);
            stmt.setNull(10, Types.VARCHAR);
            stmt.setBoolean(11, true);
            stmt.setInt(12, 0);
            stmt.setBoolean(13, false);
            stmt.setBoolean(14, false);


            int affectedRows = stmt.executeUpdate();
            logger.log(SUCCESSFULLY_EXECUTED_QUERY + affectedRows);

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idGenerated = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.log(CAUGHT_EXCEPTION + e.getMessage());
        }
        return idGenerated;
    }

    public String updateUser(UserDTO userDTO) {
        LambdaLogger logger = context.getLogger();
        logger.log("Invoked JDBCSample.getCurrentTime");
        String result = "unavailable";
        String sql = "UPDATE carwash.user t SET t.email_confirmed = 1 WHERE t.email = ?";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1,userDTO.getEmail());
            boolean execute = stmt.execute();
            logger.log(SUCCESSFULLY_EXECUTED_QUERY + execute);
        }catch (Exception e) {
        e.printStackTrace();
        logger.log(CAUGHT_EXCEPTION + e.getMessage());
    }
        return result;
    }

    public void inserRole(UserDTO dto) {
        LambdaLogger logger = context.getLogger();
        logger.log("Invoked JDBC inser Role");
        String sql = "INSERT INTO carwash.role_user (id_role, id_user) values (?,?)";
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, ConstantsCarwash.ID_CLIENT);
            stmt.setLong(2, dto.getId());

            boolean execute = stmt.execute();
            logger.log(SUCCESSFULLY_EXECUTED_QUERY + execute);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(CAUGHT_EXCEPTION + e.getMessage());
        }
    }
}