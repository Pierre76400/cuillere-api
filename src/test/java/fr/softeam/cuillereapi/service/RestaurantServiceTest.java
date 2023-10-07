package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantCustomRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @InjectMocks
    RestaurantService restaurantService;

    @Mock
    RestaurantRepository restaurantRepository;


    @Mock
    RestaurantCustomRepository repository;

    //FIXME Finir test
    //FIXME rajouter plat dans le restaurant
    //FIXME créer un modelhelper
    //FIXME Faire test pour feteDesMeres
    //FIXME Cr&er constantes inutiles
    //FIXME Compléter le () -> déroulé

    @Test
    void getRestaurant(){
        Restaurant leRipailleur=new Restaurant();
        leRipailleur.setAdresse("3 rue Emile Cordon");
        leRipailleur.setNom("Le Ripailleur");
        leRipailleur.setVegetarien("Non");

        CategoriePlat cpPrincipal=new CategoriePlat();
        cpPrincipal.setCode("CP");
        cpPrincipal.setLibelle("Plat principal");

        Plat boeuf=new Plat();
        boeuf.setLibelle("Boeuf bourguignon");
        boeuf.setCategoriePlat(cpPrincipal);
        boeuf.setPrix(10.5d);
        boeuf.setRestaurant(leRipailleur);

        leRipailleur.setPlats(List.of(boeuf));

        when(repository.getDetailsById(3l)).thenReturn(leRipailleur);

        RestaurantDetailDto res=restaurantService.getRestaurant(3l);

        assertEquals("Le Ripailleur",res.getNom());
        assertEquals(1,res.getPlats().size());
        assertEquals(10.5d,res.getPlats().get(0).getPrix());
    }


    @Test
    void getRestaurantFeteDesMeres2019(){
        Restaurant leRipailleur=new Restaurant();
        leRipailleur.setAdresse("3 rue Emile Cordon");
        leRipailleur.setNom("Le Ripailleur");
        leRipailleur.setVegetarien("Non");

        CategoriePlat cpPrincipal=new CategoriePlat();
        cpPrincipal.setCode("CP");
        cpPrincipal.setLibelle("Plat principal");

        Plat boeuf=new Plat();
        boeuf.setLibelle("Boeuf bourguignon");
        boeuf.setCategoriePlat(cpPrincipal);
        boeuf.setPrix(10.5d);
        boeuf.setRestaurant(leRipailleur);

        leRipailleur.setPlats(List.of(boeuf));

        when(repository.getDetailsById(3l)).thenReturn(leRipailleur);

        RestaurantDetailDto res=restaurantService.getRestaurantFeteDesMeres2019(3l);

        assertEquals("Le Ripailleur",res.getNom());
        assertEquals(1,res.getPlats().size());
        assertEquals(9.45d,res.getPlats().get(0).getPrix(),0.01d);
    }
}