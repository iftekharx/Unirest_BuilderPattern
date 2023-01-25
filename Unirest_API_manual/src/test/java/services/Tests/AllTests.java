package services.Tests;

import category.CategoryRequest;
import category.CategoryResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.Assert;
import org.testng.annotations.Test;
import product.ProductRequest;
import product.ProductResponse;
import user.UserRequest;
import user.UserResponse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class AllTests {

    private void setUpMapper(){
        Unirest.setObjectMapper(new ObjectMapper() {
            com.fasterxml.jackson.databind.ObjectMapper mapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public String writeValue(Object value) {
                try {
                    return mapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return mapper.readValue(value, valueType);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Test
    public void getAllProducts() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/products")
                .header("accept", "application/json")
                .asJson();

        assertNotNull(jsonResponse.getBody());
        assertEquals(200, jsonResponse.getStatus());
    }

    @Test
    public void getSingleProduct() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/products/45")
                .header("accept", "application/json")
                .asJson();

        assertNotNull(jsonResponse.getBody());
        assertEquals(200, jsonResponse.getStatus());

        Gson gson= new Gson();
        ProductResponse productResponse=gson.fromJson(String.valueOf(jsonResponse.getBody()),ProductResponse.class);
        String title= productResponse.getTitle();
        System.out.println(title);
        Assert.assertEquals(title,"Intelligent Rubber Car");
    }


    @Test
    public void checkAllProductFields() throws UnirestException, FileNotFoundException {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/products/")
                .header("accept", "application/json")
                .asJson();


        Gson gson= new Gson();


        BufferedReader buf = new BufferedReader(new FileReader("products_data.json"));
        ProductResponse[] productsFromFile = gson.fromJson(buf,ProductResponse[].class);


        ProductResponse[] productResponses = gson.fromJson(String.valueOf(jsonResponse.getBody()),ProductResponse[].class);

        int index = 0;

        System.out.println(productResponses.length);
        System.out.println(productsFromFile.length);

        for(int i = 0; i < productsFromFile.length; i++){
            Assert.assertEquals(productResponses[i], productResponses[index]);
            index += 1;
        }



    }



    @Test
    public void createProduct() throws UnirestException {

        setUpMapper();

        String[] images = {"pic2.png"};

        String description = "This Car has nice color combination";
        ProductRequest productRequest = ProductRequest.builder().title("Soft Rubber Car").price(200).images(images).description(description).categoryId(1).build();

        HttpResponse<JsonNode> jsonResponse
                = Unirest.post("https://api.escuelajs.co/api/v1/products")
                .header("Content-Type", "application/json")
                .body(productRequest)
                .asJson();
        System.out.println(jsonResponse.getBody().toString());
        assertEquals(201, jsonResponse.getStatus());
    }

    @Test
    public void updateProduct() throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.put("https://api.escuelajs.co/api/v1/products/273")
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"title\": \"abcd\",\n" +
                        "   \"price\": 100,\n" +
                        "}")
                .asJson();
//        System.out.println(jsonResponse.getBody().toString());
        assertEquals(200, jsonResponse.getStatus());
    }

    @Test
    public void deleteProduct() throws UnirestException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.delete("https://api.escuelajs.co/api/v1/products/273").asJson();
//        System.out.println(jsonResponse.getBody().toString());
        assertEquals(200, jsonResponse.getStatus());
    }



    @Test
    public void deleteCategory() throws UnirestException {

        int id = 6;

        HttpResponse<JsonNode> jsonResponse
                = Unirest.delete("https://api.escuelajs.co/api/v1/categories/" + String.valueOf(id)).asJson();
        System.out.println(jsonResponse.getBody().toString());
        assertEquals(200, jsonResponse.getStatus());
    }

    @Test
    public void checkAllCategories() throws UnirestException, FileNotFoundException {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/categories/")
                .header("accept", "application/json")
                .asJson();

        assertNotNull(jsonResponse.getBody());

        Gson gson= new Gson();


        BufferedReader buf = new BufferedReader(new FileReader("cat_data.json"));
        CategoryResponse[] catFromFile = gson.fromJson(buf, CategoryResponse[].class);


        CategoryResponse[] catResponses = gson.fromJson(String.valueOf(jsonResponse.getBody()),CategoryResponse[].class);

        int index = 0;

        System.out.println(catResponses.length);
        System.out.println(catFromFile.length);

        for(int i = 0; i < catFromFile.length; i++){
            Assert.assertEquals(catResponses[i], catResponses[index]);
            index += 1;
        }



    }


    @Test
    public void createCategories() throws UnirestException {

        setUpMapper();

        String name="Shoes";
        String image="shoe.png";
        CategoryRequest categoryRequest = CategoryRequest.builder().name(name).image(image).build();
        HttpResponse<JsonNode> jsonResponse
                = Unirest.post("https://api.escuelajs.co/api/v1/categories")
                .header("Content-Type", "application/json")
                .body(categoryRequest)
                .asJson();
      System.out.println(jsonResponse.getBody());
      assertEquals(201, jsonResponse.getStatus());

    }

    @Test
    public void getSingleCategory() throws UnirestException {
        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/categories/1")
                .header("accept", "application/json")
                .asJson();

        assertNotNull(jsonResponse.getBody());
        assertEquals(200, jsonResponse.getStatus());

        Gson gson= new Gson();
        CategoryResponse categoryResponse=gson.fromJson(String.valueOf(jsonResponse.getBody()),CategoryResponse.class);
        String name= categoryResponse.getName();
        //System.out.println(name);
        Assert.assertEquals(name,"Clothes");
    }



    @Test
    public void createUser() throws UnirestException {

        setUpMapper();

        String role = "customer";
        UserRequest userRequest = UserRequest.builder().name("Robin").email("robin@gmail.com").password("1234").role(role).avatar("myprof.png").build();
        HttpResponse<JsonNode> jsonResponse
                = Unirest.post("https://api.escuelajs.co/api/v1/users")
                .header("Content-Type", "application/json")
                .body(userRequest)
                .asJson();
        System.out.println(jsonResponse.getBody());
        assertEquals(201, jsonResponse.getStatus());

    }

    @Test
    public void updateUser() throws UnirestException {

        setUpMapper();

        String role = "customer";
        UserRequest userRequest = UserRequest.builder().name("Bob").email("bob@gmail.com").build();
        HttpResponse<JsonNode> jsonResponse
                = Unirest.put("https://api.escuelajs.co/api/v1/users/6")
                .header("Content-Type", "application/json")
                .body(userRequest)
                .asJson();
        System.out.println(jsonResponse.getBody());
        assertEquals(201, jsonResponse.getStatus());

    }


    @Test
    public void checkAllUserFields() throws UnirestException, FileNotFoundException {

        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/users")
                .header("Content-Type", "application/json")
                .asJson();
        System.out.println(jsonResponse.getBody());


        Gson gson= new Gson();
        //UserResponse userResponse=gson.fromJson(jsonResponse.getBody(),)


        BufferedReader buf = new BufferedReader(new FileReader("users_data.json"));
        UserResponse[] userFromFile = gson.fromJson(buf, UserResponse[].class);


        UserResponse[] userResponses = gson.fromJson(String.valueOf(jsonResponse.getBody()),UserResponse[].class);

        int index = 0;

//        System.out.println(userResponses.length);
//        System.out.println(userFromFile.length);

        for(int i = 0; i < userFromFile.length; i++){
            Assert.assertEquals(userResponses[i], userFromFile[index]);
            index += 1;
        }


    }
    @Test
    public void getAllUsers() throws UnirestException, FileNotFoundException {


        HttpResponse<JsonNode> jsonResponse
                = Unirest.get("https://api.escuelajs.co/api/v1/users")
                .header("Content-Type", "application/json")
                .asJson();
        System.out.println(jsonResponse.getBody());
        assertEquals(200, jsonResponse.getStatus());



    }

    @Test
    public void deleteUser() throws UnirestException {

        int id = 6;

        HttpResponse<JsonNode> jsonResponse
                = Unirest.delete("https://api.escuelajs.co/api/v1/users/" + String.valueOf(id)).asJson();
        System.out.println(jsonResponse.getBody().toString());
        assertEquals(200, jsonResponse.getStatus());
    }







}