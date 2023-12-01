package fr.softeam.cuillereapi.util;

import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Plat;
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

    public static Plat createPlatBoeufBourguignon(Restaurant leRipailleur, CategoriePlat cpPrincipal) {
        Plat boeuf= new Plat();
        boeuf.setLibelle("Boeuf bourguignon");
        boeuf.setCategoriePlat(cpPrincipal);
        boeuf.setPrix(10.5d);
        boeuf.setRestaurant(leRipailleur);
        return boeuf;
    }

    public static CategoriePlat getCategoriePlatPlatPrincipal(String CP, String Plat_principal) {
        CategoriePlat cpPrincipal=new CategoriePlat();
        cpPrincipal.setCode(CP);
        cpPrincipal.setLibelle(Plat_principal);
        return cpPrincipal;
    }

    public static Plat createPlatOeufMayo(Restaurant leRipailleur, CategoriePlat cpEntree) {
        Plat oeuf=new Plat();
        oeuf.setLibelle("Oeuf mayo");
        oeuf.setCategoriePlat(cpEntree);
        oeuf.setPrix(2.5d);
        oeuf.setRestaurant(leRipailleur);
        return oeuf;
    }

    public static Plat createPlatSteakFrite(Restaurant laPuce, CategoriePlat cpPrincipal) {
        Plat steak=new Plat();
        steak.setLibelle("Steak frite");
        steak.setCategoriePlat(cpPrincipal);
        steak.setPrix(12.5d);
        steak.setRestaurant(laPuce);
        return steak;
    }
}
