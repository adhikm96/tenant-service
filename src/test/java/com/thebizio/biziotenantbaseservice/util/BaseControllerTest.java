package com.thebizio.biziotenantbaseservice.util;

import com.thebizio.biziotenantbaseservice.testcontaines.BaseTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseControllerTest extends BaseTestContainer {

    @Autowired
    public MvcReqHelper mvcReqHelper;

    @Autowired
    public DemoEntitiesGenerator demoEntitiesGenerator;

    @Autowired
    public MockMvc mvc;

    @Autowired
    DataCleaner dataCleaner;

    @BeforeEach
    public void beforeEach() {
        dataCleaner.clean();
        System.out.println("~~~~~~~~~~~~~");
        System.out.println("Data Cleaned");
    }
}
