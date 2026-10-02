# Obelix & Co. Webshop 🪨

Die Hinkelsteine sind jetzt im eigenen Service der `obelix-quarry-impl` heisst auf Port 8081.
Der Webshop auf Port 8080 holt sie mit HTTP, die URL steht in `obelix.quarry.base-url`.

## Starten

Docker Desktop muss laufen!

docker compose up -d
./gradlew :obelix-quarry-impl:bootRun
./gradlew :obelix-webshop:bootRun

1 Zipkin: http://localhost:9411
2 Prometheus: http://localhost:9090
3 Grafana: http://localhost:3000

## Grafana

Prometheus ist als Datenquelle schon alles eingerichtet, das Dashboard muss man aber selber machen:

1 http://localhost:3000 öffnen, Login `admin` / `admin`
2 Dashboards -> New -> New dashboard -> Add visualization
3 Datenquelle Prometheus wählen
4 Auf Code umschalten und `obelix_menhirs` eingeben
5 Speichern

Wenn man einen Menhir kauft, geht die Zahl runter.
