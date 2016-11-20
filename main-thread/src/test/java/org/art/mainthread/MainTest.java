package org.art.mainthread;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class MainTest {

    @Test
    public void testMain() throws Exception {
        Main.main(new String[]{});
    }

}