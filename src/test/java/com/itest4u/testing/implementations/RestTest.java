package com.itest4u.testing.implementations;

import com.itest4u.itel.annotations.API;
import com.itest4u.itel.implementations.Callbackable;
import com.itest4u.testing.ResourceInterfaces.IPost;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestTest extends Callbackable {
    final String BaseURL = "https://jsonplaceholder.typicode.com/";
    HashMap<String,String> custom_headers = new HashMap<>();



    private RequestSpecification base(boolean ...args) {
        RequestSpecification myBase = given().filter(new AllureRestAssured() ).baseUri(BaseURL).header("Content-Type", "application/json");
        if (args.length>0) {
            for (Map.Entry<String, String> h : custom_headers.entrySet()) {
                myBase.header(h.getKey(), h.getValue());
            }
        }
        return myBase;
    }
    public RestTest() {
    }

    @API(template = "get all posts")
    public IPost[] getAllPosts() {
        return base().get("/posts").as(IPost[].class);

    }

    @API(template = "get post/${id}")
    public IPost getPost(int id) {
        return base().get("/posts/"+id).as(IPost.class);
    }
    @API(template = "update post/${id}")
    public IPost updatePost(int id, IPost post) {
        return base().body(post).put("/posts/"+id).as(IPost.class);
    }
    @API(template = "delete post/${id}")
    public void deletePost(int id) {
        base().delete("/posts/"+id);
    }
    @API(template = "create new post")
    public IPost createPost(IPost post) {
        return base().body(post).post("/posts").as(IPost.class);
    }



}
