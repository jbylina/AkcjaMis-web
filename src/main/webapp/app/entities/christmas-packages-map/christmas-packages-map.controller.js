(function () {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackagesMapController', ChristmasPackagesMapController);

    ChristmasPackagesMapController.$inject = ['$scope', '$state', 'Family', 'uiGmapGoogleMapApi', 'ParseLinks', 'AlertService', 'ChristmasPackagesMap'];

    function ChristmasPackagesMapController($scope, $state, Family, uiGmapGoogleMapApi, ParseLinks, AlertService, ChristmasPackagesMap) {
        var vm = this;
        vm.packages = [];
        vm.page = 0;
        // https://sites.google.com/site/gmapsdevelopment/
        vm.colors =
            [
                'http://maps.google.com/mapfiles/ms/icons/red-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/green-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/blue-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/orange-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/pink-dot.png',
                'http://maps.google.com/mapfiles/ms/icons/purple-dot.png',
                'http://maps.google.com/mapfiles/kml/paddle/A.png',
                'http://maps.google.com/mapfiles/kml/paddle/B.png',
                'http://maps.google.com/mapfiles/kml/paddle/C.png',
                'http://maps.google.com/mapfiles/kml/paddle/D.png',
                'http://maps.google.com/mapfiles/kml/paddle/E.png',
                'http://maps.google.com/mapfiles/kml/paddle/F.png',
                'http://maps.google.com/mapfiles/kml/paddle/G.png',
                'http://maps.google.com/mapfiles/kml/paddle/H.png',
                'http://maps.google.com/mapfiles/kml/paddle/I.png',
                'http://maps.google.com/mapfiles/kml/paddle/J.png',
                'http://maps.google.com/mapfiles/kml/paddle/K.png',
                'http://maps.google.com/mapfiles/kml/paddle/L.png',
                'http://maps.google.com/mapfiles/kml/paddle/M.png',
                'http://maps.google.com/mapfiles/kml/paddle/N.png',
                'http://maps.google.com/mapfiles/kml/paddle/O.png',
                'http://maps.google.com/mapfiles/kml/paddle/P.png',
                'http://maps.google.com/mapfiles/kml/paddle/Q.png',
                'http://maps.google.com/mapfiles/kml/paddle/R.png',
                'http://maps.google.com/mapfiles/kml/paddle/S.png',
                'http://maps.google.com/mapfiles/kml/paddle/T.png',
                'http://maps.google.com/mapfiles/kml/paddle/U.png',
                'http://maps.google.com/mapfiles/kml/paddle/V.png',
                'http://maps.google.com/mapfiles/kml/paddle/W.png',
                'http://maps.google.com/mapfiles/kml/paddle/X.png',
                'http://maps.google.com/mapfiles/kml/paddle/Y.png',
                'http://maps.google.com/mapfiles/kml/paddle/Z.png'
            ];

        vm.startMarker = {
            id: 0,
            coords: {
                latitude: 52.217949,
                longitude: 21.011703
            },
            options: { draggable: true },
            events: {
                dragend: function (marker, eventName, args) {
                    vm.map.routes = [];
                    vm.map.clusters.forEach(function(c){
                        c.order = [];
                    });
                }
            }
        };

        vm.map = {
            center: {
                latitude: 52.251947,
                longitude: 21.0191543
            },
            zoom: 11,
            markers: [],
            routes: [],
            clusters: []
        };

        vm.slider = {
            value: 600,
            options: {
                floor: 1,
                ceil: 1000
            }
        };


        vm.showFamiliesClusters = function () {
            vm.map.markers = [];
            vm.map.clusters = [];
            vm.map.routes = [];
            ChristmasPackagesMap.clusters
            (
                {distance: vm.slider.value / 10000},
                function (data) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].clusterNum--;

                        if (!(data[i].clusterNum in vm.map.clusters))
                            vm.map.clusters[data[i].clusterNum] = {
                                icon: vm.colors[data[i].clusterNum],
                                clusterId: data[i].clusterNum,
                                ids : [],
                                order: []
                            };

                        vm.map.clusters[data[i].clusterNum].ids.push(data[i].id);

                        vm.map.markers.push
                        ({
                            id: data[i].id,
                            street: data[i].street,
                            houseNo: data[i].houseNo,
                            flatNo: data[i].flatNo,
                            postalcode: data[i].postalcode,
                            city: data[i].city,
                            clusterNo: data[i].clusterNum,
                            longitude: data[i].locationGeom.coordinates[0],
                            latitude: data[i].locationGeom.coordinates[1],
                            showWindow: false,
                            icon: {
                                url: vm.colors[data[i].clusterNum],
                                scaledSize: new google.maps.Size(30, 30)
                        }
                        });
                    }
                }
            );
        };

        $scope.$on("slideEnded", function () {
            vm.showFamiliesClusters();
        });



        function convertGeoJSONToGoogleMaps(coords) {
            return coords.map(function (coord) {
                return new google.maps.LatLng(coord[1], coord[0])
            });
        }

        vm.showRoutes = function(cluster){
            var families = cluster.ids;

            ChristmasPackagesMap.optimalRoute(
                {
                    families: families,
                    latitude: vm.startMarker.coords.latitude,
                    longitude: vm.startMarker.coords.longitude
                },
                function (data) {
                    cluster.order = data.optimalOrder;

                    for (var i = 0; i < data.routePaths.length; i++) {
                        if (data.routePaths[i].type == "MultiLineString") {
                            var color = randomColor();
                            for (var j = 0; j < data.routePaths[i].coordinates.length; j++) {
                                var obj = {
                                    id: data.optimalOrder[i] + "_" + j,
                                    "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates[j]),
                                    "stroke": {
                                        "color": color,
                                        weight: 5
                                    }
                                };
                                vm.map.routes.push(obj);
                            }
                        }
                        else {
                            var obj = {
                                id: data.optimalOrder[i],
                                "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates),
                                "stroke": {
                                    "color": randomColor(),
                                    weight: 5
                                }
                            };
                            vm.map.routes.push(obj);
                        }
                    }
                }
            );
        };

        uiGmapGoogleMapApi.then(function(maps) {
            vm.showFamiliesClusters();
        });

    }
})();
