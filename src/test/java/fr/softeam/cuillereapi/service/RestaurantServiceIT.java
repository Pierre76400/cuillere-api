package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.api.Dto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CuillereApiApplication.class)
class RestaurantServiceIT {

    @Autowired
    RestaurantService restaurantService;

    @Test
    void getRestaurantDetail(){
        Dto dto= restaurantService.get(1l, 2.3522d,48.8566d,false);
        assertEquals("Le coq de la maison blanche",dto.getNom());
        assertEquals(0.73d,dto.getD(),0.01d);
    }


    @Test
    void getRestaurantDetail_English(){
        Dto dto= restaurantService.get(1l, 2.3522d,48.8566d,true);
        assertEquals("Le coq de la maison blanche",dto.getNom());
        assertEquals(0.45d,dto.getD(),0.01d);
    }
}
