package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static vttp.batch5.csf.assessment.server.MongoConstants.C_MENUS;
import static vttp.batch5.csf.assessment.server.MongoConstants.C_ORDERS;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_NAME;

@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate template;

  

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  Native MongoDB query here
  // db.menus.find()
  // .sort({name: 1})
  public List<Document> getMenu() {
    Query query = new Query();
    query.with(Sort.by(Direction.ASC, F_NAME));
    List<Document> menus = template.find(query, Document.class, C_MENUS);

    return menus;
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  Native MongoDB query here
  /*db.orders.insert({
    _id: <value>,
    order_id: <value>,
    ...
  })*/

  public void saveOrderDetails(Document d) {
    template.insert(d, C_ORDERS);
  }
  
}
