package TOPSECRET.domain;

import TOPSECRET.domain.valueobject.Condition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConditionTest {


    @Test
    void shouldHaveFourConditions() {
        assertEquals(4, Condition.values().length);
    }

    @Test
    void shouldReturnCorrectDescription() {
        assertEquals("Used but in perfect condition", Condition.LIKE_NEW.getDescription());
        assertEquals("Minor imperfections", Condition.GOOD.getDescription());
        assertEquals("Visible imperfections but readable", Condition.FAIR.getDescription());
        assertEquals("Damaged or incomplete", Condition.POOR.getDescription());
    }

   @Test
   void shouldConvertFromString() {
        assertEquals(Condition.GOOD, Condition.valueOf("GOOD"));
        assertEquals(Condition.POOR, Condition.valueOf("POOR"));
        }

   @Test
   void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Condition.valueOf("INVALID");
            });
        }

    }

