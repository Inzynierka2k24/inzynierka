package com.inzynierka2k24;

import com.inzynierka2k24.messagingservice.MessagingServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MessagingServiceApplication.class)
public class ContextTest {

   @Test
   void contextLoads() {
      System.out.println("All good!");
   }
}
