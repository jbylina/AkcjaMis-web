(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackagesMapController', ChristmasPackagesMapController);

    ChristmasPackagesMapController.$inject = ['$scope', '$state', 'Family', 'uiGmapGoogleMapApi', 'ParseLinks', 'AlertService', 'ChristmasPackagesMap'];

    function ChristmasPackagesMapController ($scope, $state, Family, uiGmapGoogleMapApi, ParseLinks, AlertService, ChristmasPackagesMap) {
        var vm = this;
        vm.packages = [];
        vm.page = 0;

        vm.map = {
            center: {
                latitude: 52.251947,
                longitude: 21.0191543
            },
            zoom: 11,
            markers: [],
            routes: []
        };

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

        function convertGeoJSONToGoogleMaps(coords){
            return coords.map(function(coord){
                return new google.maps.LatLng(coord[1], coord[0])
            });
        }

        vm.loadAll = function() {
            ChristmasPackagesMap.clusters
            (
                {distance:'0.1'},
                function(data)
                {
                    var clusterFam = [];
                    for(var i = 0; i < data.length; i++) {

                        if( !(data[i].clusterNum in clusterFam))
                            clusterFam[data[i].clusterNum] = [];

                        clusterFam[data[i].clusterNum].push(data[i].id);

                        vm.map.markers.push
                        ({
                            id: data[i].id,
                            street: data[i].street,
                            houseNo: data[i].houseNo,
                            flatNo: data[i].flatNo,
                            postalcode: data[i].postalcode,
                            city: data[i].city,

                            longitude: data[i].locationGeom.coordinates[0],
                            latitude: data[i].locationGeom.coordinates[1],
                            showWindow: false,
                            icon: vm.colors[data[i].clusterNum]
                        });
                    }

                    for(var cluster in clusterFam){
                        if(clusterFam[cluster].length > 3)
                            ChristmasPackagesMap.optimalRoute(
                                {families: clusterFam[cluster]},
                                function(data)
                                {
                                    for(var i = 0; i < data.routePaths.length; i++) {

                                        if(data.routePaths[i].type == "MultiLineString"){
                                            for(var j = 0; j < data.routePaths[i].coordinates.length; j++){
                                                var obj = {
                                                    id : data.optimalOrder[i] + "_" + j,
                                                    "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates[j])
                                                };
                                                vm.map.routes.push(obj);
                                            }
                                        }
                                        else{
                                            var obj = {
                                                id : data.optimalOrder[i],
                                                "geometry": convertGeoJSONToGoogleMaps(data.routePaths[i].coordinates)
                                            };
                                            vm.map.routes.push(obj);
                                        }
                                    }
                                }
                            );
                    }

                }
            );
        };

        vm.loadAll();
    }
})();
