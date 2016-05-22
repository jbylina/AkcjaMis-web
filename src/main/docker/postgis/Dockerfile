FROM starefossen/pgrouting:latest

RUN apt-get update -y && \
    apt-get install -y \
    osm2pgrouting \
    curl

# dane geoprzestrzenne mazowsza http://download.geofabrik.de/europe/poland/mazowieckie.html
ENV AKCJAMIS_MAZOWIECKIE_OSM_FILE="/tmp/mazowieckie-latest.osm"

RUN curl -s http://download.geofabrik.de/europe/poland/mazowieckie-latest.osm.bz2 \
    | bzip2 -dc \
    > $AKCJAMIS_MAZOWIECKIE_OSM_FILE

#RUN osm2pgrouting \
#    --file "$AKCJAMIS_MAZOWIECKIE_OSM_FILE" \
#    --dbname "$POSTGRES_DB" \
#    --prefix osm_ \
#   --clean

RUN mkdir -p /docker-entrypoint-initdb.d
COPY ./initdb-pgrouting-data.sh /docker-entrypoint-initdb.d/routingOsmData.sh

