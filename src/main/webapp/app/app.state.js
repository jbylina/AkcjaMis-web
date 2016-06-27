(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider', 'uiGmapGoogleMapApiProvider'];

    function stateConfig($stateProvider, uiGmapGoogleMapApiProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/layouts/navbar/navbar.html',
                    controller: 'NavbarController',
                    controllerAs: 'vm'
                },
                'sidebar@': {
                    templateUrl: 'app/layouts/sidebar/sidebar.html',
                    controller: 'SidebarController',
                    controllerAs: 'sb'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ]
            }
        });

        uiGmapGoogleMapApiProvider.configure({
            key: 'AIzaSyA_Z7yxF8_HXhgrU8i-OK1LkfeRfpnZLio',  // TODO parameterize based on env
            v: '3.23',
            libraries: 'visualization'
        });
    }

    $scope.loadOtherEntities = function(searchQuery) {
        if (searchQuery){
            OtherEntitySearch.query({query: '*' + searchQuery + '*'}, function(result) {
                $scope.otherEntities = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.otherEntities = Family.query({isActive: true});
                }
            });
        } else {
            $scope.otherEntities = Family.query({isActive: true});
        }
    };
})();
