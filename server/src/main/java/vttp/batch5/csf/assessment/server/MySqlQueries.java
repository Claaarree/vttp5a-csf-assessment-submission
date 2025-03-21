package vttp.batch5.csf.assessment.server;

public class MySqlQueries {
    public static final String SELECT_CUSTOMER = """
        select * from customers 
            where username = ? 
            and password = ?
    """;

    public static final String INSERT_PLACE_ORDERS = """
        insert into place_orders(order_id, payment_id, order_date, total, username)
	        values(?, ?, ?, ?, ?)
            """;
}
