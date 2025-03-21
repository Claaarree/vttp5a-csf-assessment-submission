package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import netscape.javascript.JSObject;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restaurantService;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus() {
    JsonArray menus = restaurantService.getMenu();

    return ResponseEntity.ok(menus.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path = "/food_order", consumes = MediaType.APPLICATION_JSON_VALUE,
  produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    JsonObject orderDetails = Json.createReader(new StringReader(payload)).readObject();
    String username = orderDetails.getString("username");
    String password = orderDetails.getString("password");
    Boolean isUserValidated = restaurantService.validateUser(username, password);
    if(!isUserValidated){
      JsonObject error = Json.createObjectBuilder()
        .add("message", "Invalid username and/or password")
        .build();

      return ResponseEntity.status(401).body(error.toString());
    } else {
      try {
        JsonObject receipt = restaurantService.makePayment(payload);
        // restaurantService.saveToMongo(payload);

      } catch (Exception e) {
        // TODO: handle exception
      }
    }

    return ResponseEntity.ok("{}");
  }
}
