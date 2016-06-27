(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
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
