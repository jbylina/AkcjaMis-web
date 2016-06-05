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
                'http://maps.google.com/mapfiles/ms/icons/purple-dot.png'
            ];
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
            ChristmasPackagesMap.clusters
            (
                {distance: vm.slider.value / 10000},
                function (data) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].clusterNum--;

                        if (!(data[i].clusterNum in vm.map.clusters))
                            vm.map.clusters[data[i].clusterNum] = {
                                icon: vm.colors[data[i].clusterNum],
                                ids : []
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
                            icon: vm.colors[data[i].clusterNum]
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

        vm.showRoutes = function(families){


            function decimalToHexString(number) {
                return "#" + Math.round(number + 0xFFFF00).toString(16).toUpperCase();
            }

            ChristmasPackagesMap.optimalRoute(
                {families: families},
                function (data) {
                    var step = Math.abs(0x0000FF / data.routePaths.length);

                    for (var i = 0; i < data.routePaths.length; i++) {

                        if (data.routePaths[i].type == "MultiLineString") {
                            for (var j = 0; j < data.routePaths[i].coordinates.length; j++) {
                                var obj = {
                                    id: data.optimalOrder[i] + "_" + j,
                                    "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates[j]),
                                    "stroke": {"color": decimalToHexString(step * i), weight: 5}
                                };
                                vm.map.routes.push(obj);
                            }
                        }
                        else {
                            var obj = {
                                id: data.optimalOrder[i],
                                "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates),
                                "stroke": {"color": decimalToHexString(step * i), weight: 5}
                            };
                            vm.map.routes.push(obj);
                        }
                    }
                }
            );
        };

        vm.showFamiliesClusters();
    }
})();
