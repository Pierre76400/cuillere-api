-- Restaurants
insert into restaurant (id,adresse,nom,vegetarien) values (NEXTVAL('restaurant_seq'),'1 rue du lac','Le coq de la maison blanche','NON');
insert into restaurant (id,adresse,nom,vegetarien) values (NEXTVAL('restaurant_seq'),'3 rue du St Anne','Wakazi','NON');
insert into restaurant (id,adresse,nom,vegetarien) values (NEXTVAL('restaurant_seq'),'32 allée de Paris','Le loucher ben','NON');
insert into restaurant (id,adresse,nom,vegetarien) values (NEXTVAL('restaurant_seq'),'11 avenue du tertre','L''herbe folle','OUI');

-- Catégorie plat
insert into categorie_plat (code,libelle) values ('ENT','Entrée');
insert into categorie_plat (code,libelle) values ('PLA','Plat');
insert into categorie_plat (code,libelle) values ('DES','Dessert');
insert into categorie_plat (code,libelle) values ('BOI','Boisson');

--Plats
insert into plat (id,categorie_plat_code,libelle,prix,restaurant_id)
    values (NEXTVAL('plat_seq'),'ENT','Oeufs mayo',3.2,1);
insert into plat (id,categorie_plat_code,libelle,prix,restaurant_id)
    values (NEXTVAL('plat_seq'),'PLA','Boeuf bourguignon',13.2,1);
insert into plat (id,categorie_plat_code,libelle,prix,restaurant_id)
    values (NEXTVAL('plat_seq'),'DES','Ile flottante',5.2,1);
insert into plat (id,categorie_plat_code,libelle,prix,restaurant_id)
    values (NEXTVAL('plat_seq'),'DES','Eclair au chocolat',4.2,1);

--Avis
insert into avis (id,auteur,commentaire,note,restaurant_id) values (NEXTVAL('avis_seq'),'Pierre','Génial',5,1);
insert into avis (id,auteur,commentaire,note,restaurant_id) values (NEXTVAL('avis_seq'),'Alfred','Bof',2,1);
insert into avis (id,auteur,commentaire,note,restaurant_id) values (NEXTVAL('avis_seq'),'Nicolas','Bien',4,1);
insert into avis (id,auteur,commentaire,note,restaurant_id) values (NEXTVAL('avis_seq'),'Anne','',4,1);
