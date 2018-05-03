#!/bin/bash
echo "Inserire indirizzo IP server nella forma xxx.xxx.xxx.xxx "
read indirizzo
echo "Inserire il numero della porta TCP "
read porta
java -cp sdm-1.0-SNAPSHOT-jar-with-dependencies.jar it.unica.adm.sdm.SensorDataManager $indirizzo $porta

