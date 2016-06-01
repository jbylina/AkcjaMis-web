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
            markers : []
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
            'http://maps.google.com/mapfiles/ms/icons/purple-dot.png',
        ];

        // Do stuff with your $scope.
        // Note: Some of the directives require at least something to be defined originally!
        // e.g. $scope.markers = []

        // uiGmapGoogleMapApi is a promise.
        // The "then" callback function provides the google.maps object.
        uiGmapGoogleMapApi.then(function(maps) {

        });

        vm.loadAll = function() {

            ChristmasPackagesMap.query
            (
                {userId:'0.06'},
                function(data)
                {
                    for(var i = 0; i < data.length; i++)
                        vm.map.markers.push
                        ({
                            id : data[i].id,
                            street : data[i].street,
                            houseNo : data[i].houseNo,
                            flatNo : data[i].flatNo,
                            postalcode : data[i].postalcode,
                            city : data[i].city,

                            longitude: data[i].locationGeom.coordinates[0],
                            latitude: data[i].locationGeom.coordinates[1],
                            showWindow: false,
                            icon: vm.colors[data[i].clusterNum]
                        });

                }
            );
        };

        vm.loadAll();
    }
})();
