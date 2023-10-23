package fr.softeam.cuillereapi.util;

import fr.softeam.cuillereapi.model.Restaurant;

public class ModelHelper {

    public static Restaurant createRestaurantLaPuce(){
        Restaurant laPuce=new Restaurant();
        laPuce.setAdresse("12 rue Ernest Renan");
        laPuce.setNom("La Puce");
        laPuce.setVegetarien("Non");

        return laPuce;
    }

    public static Restaurant createRestaurantLeRipailleur(){
        Restaurant leRipailleur=new Restaurant();
        leRipailleur.setAdresse("3 rue Emile Cordon");
        leRipailleur.setNom("Le Ripailleur");
        leRipailleur.setVegetarien("Non");

        return leRipailleur;
    }


}
