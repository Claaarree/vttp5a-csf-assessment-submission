package vttp.batch5.csf.assessment.server.repositories;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import static vttp.batch5.csf.assessment.server.MySqlQueries.INSERT_PLACE_ORDERS;
import static vttp.batch5.csf.assessment.server.MySqlQueries.SELECT_CUSTOMER;

// Use the following class for MySQL database
@Repository
public class RestaurantRepository {

    @Autowired
    private JdbcTemplate template;

    public Boolean validateUser(String username, String password) {
        
        String encryptedPassword = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) 
                hexString.append('0');
                hexString.append(hex);
            }
            encryptedPassword = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SqlRowSet rs = template.queryForRowSet(SELECT_CUSTOMER, 
            username.toLowerCase(), encryptedPassword);
        
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
        
    }

    public void saveOrderDetails(String orderId, String paymentId, 
    Date orderDate, Double totalPrice, String username) 
    throws DataAccessException{
        
        template.update(INSERT_PLACE_ORDERS, orderId, paymentId, orderDate, totalPrice, username);
    }

}
