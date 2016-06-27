(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('christmas-packages-map', {
                parent: 'entity',
                url: '/event/:year/christmas-packages-map',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ChristmasPackagesMap'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/christmas-packages-map/christmas-packages-map.html',
                        controller: 'ChristmasPackagesMapController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                }
            });
    }

})();
