(function () {
        'use strict';

        angular
            .module('akcjamisApp')
            .factory('ChristmasPackagesMap', ChristmasPackagesMap);

        ChristmasPackagesMap.$inject = ['$resource'];

        function ChristmasPackagesMap($resource) {
            return $resource( 'api/events/:year/families/cluster?distance=:distance', {},{
                    'clusters': {
                        method: 'GET',
                        isArray: true
                    },
                    'optimalRoute': {
                        method: 'GET',
                        url : 'api/families/calculateOptimalRoute?families=:families&latitude=:latitude&longitude=:longitude'
                    }
            });
        }
    })
();
