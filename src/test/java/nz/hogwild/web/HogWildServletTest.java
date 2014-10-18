package nz.hogwild.web;

import org.junit.Test;

import static org.junit.Assert.*;

public class HogWildServletTest {

    @Test
    public void test(){
        assertNotNull(new HogWildServlet().getStream());
    }
}