(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackagesMapController', ChristmasPackagesMapController);

    ChristmasPackagesMapController.$inject = ['$scope', '$state', 'Family', 'uiGmapGoogleMapApi', 'ParseLinks', 'AlertService'];

    function ChristmasPackagesMapController ($scope, $state, Family, uiGmapGoogleMapApi, ParseLinks, AlertService) {
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

        // Do stuff with your $scope.
        // Note: Some of the directives require at least something to be defined originally!
        // e.g. $scope.markers = []

        // uiGmapGoogleMapApi is a promise.
        // The "then" callback function provides the google.maps object.
        uiGmapGoogleMapApi.then(function(maps) {

        });

        vm.loadAll = function() {
            Family.query({
                page: vm.page,
                size: 100
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.packages = data;

                for (var i = 0; i < data.length; i++) {
                    if(data[i].locationGeom != null){
                        vm.map.markers.push({
                            id : data[i].id,
                            longitude: data[i].locationGeom.coordinates[0],
                            latitude: data[i].locationGeom.coordinates[1],
                            title: 'test',
                            icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
                        });
                    }
                }
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        };

        //(function(marker, i) {
        //    // add click event
        //    google.maps.event.addListener(marker, 'click', function() {
        //        infowindow = new google.maps.InfoWindow({
        //            content: 'Hello, World!!'
        //        });
        //        infowindow.open(map, marker);
        //    });
        //})(marker, i);

        vm.loadAll();
    }
})();
