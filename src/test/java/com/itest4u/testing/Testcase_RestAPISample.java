package com.itest4u.testing;

import com.itest4u.itel.implementations.Execute;
import com.itest4u.itel.annotations.Resource;
import com.itest4u.testing.implementations.RestTest;
import com.itest4u.testing.ResourceInterfaces.IPost;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class Testcase_RestAPISample {
    @Resource
    RestTest restTest;

    @BeforeClass
    public void setup() {
        restTest.setCallback((m,r) -> {
            System.out.printf("%s %n",m);
            System.out.println("result ="+r);

        });

    }

    @Test(description = "Check the functionality of listing all posts")
    public void checkAllPosts() {
        IPost[] posts = restTest.getAllPosts();
        assertEquals(posts.length, 100, "The size of posts should be 100");
    }

    @Test(description = "Check the functionality of single post CURD")
    public void checkPost() {
        IPost post = new IPost();
        new Execute("Create a new post")
                .run(() ->
                        restTest.createPost(post))
                .restore(() -> {
                    restTest.deletePost(post.getId());
                        }

                );

    }

    @Test(description = "Check the functionality of single post update/get")
    public void updatePost() {
        IPost post = restTest.getPost(100);
        assertEquals(post.getId(), 100);

    }

}
