(function () {
        'use strict';

        angular
            .module('akcjamisApp')
            .factory('ChristmasPackagesMap', ChristmasPackagesMap);

        ChristmasPackagesMap.$inject = ['$resource'];

        function ChristmasPackagesMap($resource) {
            return $resource( 'api/families', {},{
                    'families': {
                        method: 'GET',
                        isArray: true
                    }
            });
        }
    })
();
