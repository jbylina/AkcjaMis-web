#!/bin/sh
set -e

osm2pgrouting \
  --file "$AKCJAMIS_MAZOWIECKIE_OSM_FILE" \
  --dbname "$POSTGRES_DB" \
  --prefix osm_ \
#  --clean

