haeheia
=======

haeheia hakee heiaheihan harkat ja tallentaa ne tekstimuotoon. 

vaatimukset
===========

Ajamiseen:
- git
- java ajoympäristö

Kääntämiseen:
- leiningen [Leiningen](http://leiningen.org/)

käyttöohje
==========

Ajaminen
--------

- git clone https://github.com/jrosti/haeheia.git
- cd haeheia
- java -jar target/haeheia-0.1.1.jar sahkoposti@osoite.fi salasana vuosi

Ajo tulostaa hakemansa harjoituksen tiedot puolipisteellä erotettuna. Lisäksi se luo tiedoston vuosi.json, jossa on edeltävän lisäksi harjoitukset mahdollisimman täydellisinä json-muodossa:

Esimerkki: 
----------

    $ java -jar target/haeheia-0.0.1-standalone.jar maija@malli.fi xuzzy 2014
    17.03.2014;Juoksu;rivi1;2 h 2 min;22 km
    17.03.2014;Lenkkeily;lyhyt;2 min;0,5 km
    17.03.2014;Kävely;afs;12 h 12 min;123 km

    $ cat 2014.json
    [{"duration":"2 h 2 min","sport":"Juoksu","distance":"22 km","creationDate":"17.03.2014","body":"sisalto",
    "hid":"18711690591696","title":"rivi1"},... ]

Kääntäminen
-----------

- git clone ...
- cd haeheia
- lein run sposti@x.fi salasana vuosi


