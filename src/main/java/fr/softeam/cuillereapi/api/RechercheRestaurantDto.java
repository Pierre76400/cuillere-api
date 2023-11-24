package fr.softeam.cuillereapi.api;

import java.util.List;

public class RechercheRestaurantDto {

    private int nbResultat;

    private List<RestaurantDto> restaurants;

    public List<RestaurantDto> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantDto> restaurants) {
        this.restaurants = restaurants;
    }

    public int getNbResultat() {
        return nbResultat;
    }

    public void setNbResultat(int nbResultat) {
        this.nbResultat = nbResultat;
    }
}
