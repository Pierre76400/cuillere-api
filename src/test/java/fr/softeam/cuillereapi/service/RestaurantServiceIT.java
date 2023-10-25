package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.api.RestaurantAvecInfoComplementaireDto;
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
        //FIXME trouver une façon propre de récupérer
        RestaurantAvecInfoComplementaireDto dto= restaurantService.getRestaurantDetail(1l, 2.3522d,48.8566d,false);
        assertEquals("Le coq de la maison blanche",dto.getNom());
        assertEquals(0.73d,dto.getD(),0.01d);
    }
}
