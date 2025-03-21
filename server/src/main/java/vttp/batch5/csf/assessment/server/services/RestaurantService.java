package vttp.batch5.csf.assessment.server.services;

import static vttp.batch5.csf.assessment.server.MongoConstants.F_DESCRIPTION;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_ID;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_NAME;
import static vttp.batch5.csf.assessment.server.MongoConstants.F_PRICE;

import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Value("${MY_NAME}")
  String myName;

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;
  
  private RestTemplate restTemplate = new RestTemplate();

  public final String baseUrl = "https://payment-service-production-a75a.up.railway.app/";

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
        .add("id", m.getString(F_ID))
        .add(F_NAME, m.getString(F_NAME))
        .add(F_DESCRIPTION, m.getString(F_DESCRIPTION))
        .add(F_PRICE, m.getDouble(F_PRICE))
        .build();

    return jObject;
  }
  
  // TODO: Task 4
  public Boolean validateUser(String username, String password) {
    Boolean isUserValid = restaurantRepository.validateUser(username, password);

    return isUserValid;
  }

  @Transactional
  public JsonObject makePayment(String payload) throws Exception {
    JsonObject orderDetails = Json.createReader(new StringReader(payload)).readObject();
    JsonArray items =  orderDetails.getJsonArray("items");
    String username = orderDetails.getString("username");
   
    
    String orderId = UUID.randomUUID().toString().substring(0, 8);
    Double totalPrice = 0.0;

    for(int i = 0; i< items.size(); i++) {
      JsonObject item = items.get(i).asJsonObject();
      Double unitPrice = item.getJsonNumber("price").doubleValue();
      int quantity = item.getInt("quantity");
      Double linePrice = unitPrice * quantity;
      totalPrice += linePrice;
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-AUTHENTICATE", username);
    JsonObject toSend = Json.createObjectBuilder()
      .add("order_id", orderId)
      .add("payer", username)
      .add("payee", myName)
      .add("payment", totalPrice)
      .build();

    RequestEntity<String> req = RequestEntity.post(baseUrl, headers)
        .body(toSend.toString());

    ResponseEntity<String> res = restTemplate.exchange(req, String.class);

    JsonObject receipt = Json.createReader(new StringReader(res.toString())).readObject();
    JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder(receipt);
    jsonObjectBuilder.add("total", totalPrice);

    String paymentId = receipt.getString("payment_id");

    Date orderDate = new Date(receipt.getJsonNumber("timestamp").longValueExact());

    // save to sql
    restaurantRepository.saveOrderDetails(orderId, paymentId, orderDate, totalPrice, username);

    Document d = Document.parse(payload);
    d.append("_id", orderId);
    d.append("order_id", orderId);
    d.append("payment_id", paymentId);
    d.append("total", totalPrice);
    d.append("timestamp", orderDate);
    d.remove("password");

    ordersRepository.saveOrderDetails(d);

    return jsonObjectBuilder.build();     
  }

  public void saveOrderDetailsToSQL() {

  }


}
