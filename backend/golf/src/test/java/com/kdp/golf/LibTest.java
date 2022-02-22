package com.kdp.golf;

import com.kdp.golf.lib.Lib;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class LibTest {

     @Test
     void pickItems() {
         var items = List.of(1, 2, 3, 4);
         assertEquals(List.of(1, 3), Lib.pickItems(items, List.of(0, 2)));
     }

     @Test
     void allEqual() {
         var l0 = List.of(1, 2, 3, 4);
         assertFalse(Lib.allEqual(l0));

         var l1 = List.of(4, 4, 4, 4);
         assertTrue(Lib.allEqual(l1));
     }

     @Test
    void extendMap() {
         var map = Map.of("foo", "bar", "baz", "quux");
         var changes = Map.of("foo", "fifi");

         var updated = Lib.updateMap(map, changes);
         assertEquals("quux", updated.get("baz"));
         assertEquals("fifi", updated.get("foo"));
     }
}
