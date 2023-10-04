import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorseTest {

    @Test
    public void name_Not_Null() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            new Horse(null, 1, 1);
        });
        assertEquals(exception.getMessage(), "Name cannot be null.");
    }

    @ParameterizedTest
    @MethodSource("space_Args_Factory")
    public void name_Has_No_Spaces(String argument) {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            new Horse(argument, 1, 1);
        });
        assertEquals(exception.getMessage(), "Name cannot be blank.");
    }

    public static Stream<String> space_Args_Factory() {
        return Stream.of("", " ", "\t", "\n", "\r", "\f");
    }

    @Test
    public void speed_Distance_arePositive() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
           new Horse("Плотва", -1, 1);
        });
        assertEquals(exception.getMessage(), "Speed cannot be negative.");

        exception = assertThrows(IllegalArgumentException.class, () -> {
            new Horse("Плотва", 1, -1);
        });
        assertEquals(exception.getMessage(), "Distance cannot be negative.");
    }

    @Test
    public void getters() {
        Horse horse = new Horse("Плотва", 1);
        assertEquals(horse.getName(), "Плотва");
        assertEquals(horse.getSpeed(), 1);
        assertEquals(horse.getDistance(), 0);

        horse = new Horse("Плотва", 1, 1);
        assertEquals(horse.getDistance(), 1);
    }

    @Test
    public void move() {
        Horse horse = new Horse("Плотва", 2, 2);
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            horse.move();
            mockedStatic.verify(() -> Horse.getRandomDouble(0.2, 0.9));
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6})
    public void distanceCheck(double value) {
        Horse horse = new Horse("Плотва", 2, 2);
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)) {
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(value);
            horse.move();
            assertEquals(2 + 2 * value, horse.getDistance());
        }

    }



}