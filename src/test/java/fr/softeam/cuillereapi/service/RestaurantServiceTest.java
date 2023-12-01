package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantCustomRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.util.ModelHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static fr.softeam.cuillereapi.util.ModelHelper.createPlatBoeufBourguignon;
import static fr.softeam.cuillereapi.util.ModelHelper.getCategoriePlatPlatPrincipal;
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

    @Test
    void getRestaurant(){
        Restaurant leRipailleur= ModelHelper.createRestaurantLeRipailleur();
        CategoriePlat cpPrincipal=getCategoriePlatPlatPrincipal("CP", "Plat principal");
        leRipailleur.setPlats(List.of(createPlatBoeufBourguignon(leRipailleur, cpPrincipal)));

        when(repository.getDetailsById(3l)).thenReturn(leRipailleur);

        RestaurantDetailDto res=restaurantService.getRestaurant(3l);

        assertEquals("Le Ripailleur",res.getNom());
        assertEquals(1,res.getPlats().size());
        assertEquals(10.5d,res.getPlats().get(0).getPrix());
    }
}