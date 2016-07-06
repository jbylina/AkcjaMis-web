(function () {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackagesMapController', ChristmasPackagesMapController);

    ChristmasPackagesMapController.$inject = ['uiGmapGoogleMapApi', 'ChristmasPackagesMap'];

    function ChristmasPackagesMapController(uiGmapGoogleMapApi, ChristmasPackagesMap) {
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
            clusters: []
        };

        vm.showFamiliesClusters = function () {
            vm.map.markers = [];
            ChristmasPackagesMap.families
            (
                {},
                function (data) {
                    for (var i = 0; i < data.length; i++) {
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
                            showWindow: false
                        });
                    }
                }
            );
        };

        uiGmapGoogleMapApi.then(function(maps) {
            vm.showFamiliesClusters();
        });

    }
})();
