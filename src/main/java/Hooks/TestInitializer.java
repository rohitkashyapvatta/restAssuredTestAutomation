package Hooks;

import Wrapper.RestAssuredWrapper;
import io.cucumber.java.Before;


public class TestInitializer {

    @Before
    public void TestSetup() {
        RestAssuredWrapper restAssuredWrapper = new RestAssuredWrapper();
    }
}
