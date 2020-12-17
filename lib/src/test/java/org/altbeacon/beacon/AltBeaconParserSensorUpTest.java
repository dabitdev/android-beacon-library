package org.altbeacon.beacon;

import org.altbeacon.beacon.logging.LogManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
/*
HOW TO SEE DEBUG LINES FROM YOUR UNIT TESTS:
1. set a line like this at the start of your test:
           org.robolectric.shadows.ShadowLog.stream = System.err;
2. run the tests from the command line
3. Look at the test report file in your web browser, e.g.
   file:///Users/dyoung/workspace/AndroidProximityLibrary/build/reports/tests/index.html
4. Expand the System.err section
 */
public class AltBeaconParserSensorUpTest {

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    @Test
    public void testReconizeiBeacon() {
        BeaconManager.setDebug(true);
        byte[] bytes = hexStringToByteArray("0201061AFF4C000215B9407F30F5F8466EAFF925556B57FE6DDC4EDC5FC1");
        AltBeaconParser parser = new AltBeaconParser();
        parser.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        Beacon beacon = parser.fromScanData(bytes, -55, null, 123456L);
        String uuid = beacon.getId1().toUuid().toString().toUpperCase();
        int majorId = beacon.getId2().toInt();
        int minorId = beacon.getId3().toInt();

        assertEquals("manData should be parsed", "B9407F30-F5F8-466E-AFF9-25556B57FE6D", uuid);
        assertEquals("tx power should be parsed", beacon.getTxPower(), -63);
        assertEquals("major id should be parsed", majorId, 56398);
        assertEquals("minor id should be parsed", minorId, 56415);
    }
}