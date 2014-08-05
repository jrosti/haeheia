haeheia
=======

haeheia hakee Heiaheian harkat ja tallentaa ne tekstimuotoon. 

Vaatimukset
===========

Ajamiseen:
- git tai muu tapa kloonata githubin projekti
- java ajoympäristö

Kääntämiseen:
- leiningen http://leiningen.org/
- Java käännösympäristö.

käyttöohje
==========

Ajaminen
--------

- git clone https://github.com/jrosti/haeheia.git
- cd haeheia
- java -jar target/haeheia-0.0.1-standalone.jar sahkoposti@osoite.fi salasana vuosi

Ajo tulostaa yhden harjoituksen tiedot puolipisteellä erotettuna ja hakee kerrallaan koko vuoden. Lisäksi se luo tiedoston vuosi.json, jossa harjoitukset ovat mahdollisimman täydellisinä json-muodossa. Lisänä tekstimuotoon on Heiaheiassa harjoituksen yksilöivä heiaheian oma id "hid", sekä "body" -kenttä, joka sisältää Heiaheian harjoituksen merkinnät. 

Esimerkki: 
----------

    $ java -jar target/haeheia-0.0.1-standalone.jar maija@malli.fi xuzzy 2014
    17.03.2014;Juoksu;rivi1;2 h 2 min;22 km
    17.03.2014;Lenkkeily;lyhyt;2 min;0,5 km
    17.03.2014;Kävely;afs;12 h 12 min;123 km
    ...
    
    $ cat 2014.json
    [{"duration":"2 h 2 min","sport":"Juoksu","distance":"22 km","creationDate":"17.03.2014","body":"sisalto",
    "hid":"18711690591696","title":"rivi1"},... ]


