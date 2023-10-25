package fr.softeam.cuillereapi.prodist;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    @Test
    void testParisBerlin() {
        Coordinates coord1 = new Coordinates(52.5200d, 13.4050d); // Berlin, Allemagne
        Coordinates coord2 = new Coordinates(48.8566d, 2.3522d);  // Paris, France

        double distance = Coordinates.calculateDistance(coord1, coord2);
        System.out.println("Distance entre Berlin et Paris : " + distance + " km");
    }


    @Test
    void testAutourParis() {
        Coordinates coord1 = new Coordinates(48.8566d, 2.3422d);
        Coordinates coord2 = new Coordinates(48.8566d, 2.3522d);

        double distance = Coordinates.calculateDistance(coord1, coord2);
        System.out.println("Distance entre Berlin et Paris : " + distance + " km");
    }
}