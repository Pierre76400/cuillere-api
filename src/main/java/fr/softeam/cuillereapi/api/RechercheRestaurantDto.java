package fr.softeam.cuillereapi.api;

import java.util.List;

public class RechercheRestaurantDto {

    private int nbResultat;

    private List<RestaurantDetailDto> restaurants;

    public List<RestaurantDetailDto> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDetailDto> restaurants) {
        this.restaurants = restaurants;
    }

    public int getNbResultat() {
        return nbResultat;
    }

    public void setNbResultat(int nbResultat) {
        this.nbResultat = nbResultat;
    }
}
