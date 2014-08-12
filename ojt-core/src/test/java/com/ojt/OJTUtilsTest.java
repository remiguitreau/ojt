package com.ojt;

import org.junit.Assert;
import org.junit.Test;

public class OJTUtilsTest {

    @Test
    public void test_build_code_clubs() {
        Assert.assertEquals("SEPA050001", OJTUtils.buildClubCodeFromDepartment("05", 1));
        Assert.assertEquals("SEPA050013", OJTUtils.buildClubCodeFromDepartment("05", 13));
        Assert.assertEquals("SEPA050013", OJTUtils.buildClubCodeFromDepartment("5", 13));
        Assert.assertEquals("SEPA830010", OJTUtils.buildClubCodeFromDepartment("83", 10));
    }
}
