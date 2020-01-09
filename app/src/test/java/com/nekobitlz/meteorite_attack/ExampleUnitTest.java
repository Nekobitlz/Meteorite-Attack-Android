package com.nekobitlz.meteorite_attack;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.nekobitlz.meteorite_attack.options.Utils.formatMoney;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)
public class ExampleUnitTest {

    @Test
    public void testFormatMoney() {
        assertEquals("1 000", formatMoney("1000"));
        assertEquals("1 000 000", formatMoney("1000000"));
        assertEquals("12 000", formatMoney("12000"));
        assertEquals("145 000", formatMoney("145000"));
        assertEquals("10", formatMoney("10"));
    }
}