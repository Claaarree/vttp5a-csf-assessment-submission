package vttp.batch5.csf.assessment.server.services;

import static vttp.batch5.csf.assessment.server.MongoConstants.F_DESCRIPTION;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_ID;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_NAME;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_PRICE;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

@Service
public class RestaurantService {

  @Autowired
  private OrdersRepository ordersRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {
    List<Document> menus = ordersRepository.getMenu();
    JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();
    menus.forEach(m -> {
      JsonObject jObject = menuDocumentToJson(m);
      jArrayBuilder.add(jObject);
    });

    return jArrayBuilder.build();
  }

  public JsonObject menuDocumentToJson(Document m) {
    JsonObject jObject = Json.createObjectBuilder()
        .add(F_ID, m.getString(F_ID))
        .add(F_NAME, m.getString(F_NAME))
        .add(F_DESCRIPTION, m.getString(F_DESCRIPTION))
        .add(F_PRICE, m.getDouble(F_PRICE))
        .build();

    return jObject;
  }
  
  // TODO: Task 4


}
