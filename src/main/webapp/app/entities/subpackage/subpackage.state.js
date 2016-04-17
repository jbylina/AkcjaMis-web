(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subpackage', {
            parent: 'entity',
            url: '/subpackage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Subpackages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subpackage/subpackages.html',
                    controller: 'SubpackageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('subpackage-detail', {
            parent: 'entity',
            url: '/subpackage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Subpackage'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subpackage/subpackage-detail.html',
                    controller: 'SubpackageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Subpackage', function($stateParams, Subpackage) {
                    return Subpackage.get({id : $stateParams.id});
                }]
            }
        })
        .state('subpackage.new', {
            parent: 'subpackage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage/subpackage-dialog.html',
                    controller: 'SubpackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                subpackageNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subpackage', null, { reload: true });
                }, function() {
                    $state.go('subpackage');
                });
            }]
        })
        .state('subpackage.edit', {
            parent: 'subpackage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage/subpackage-dialog.html',
                    controller: 'SubpackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Subpackage', function(Subpackage) {
                            return Subpackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('subpackage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subpackage.delete', {
            parent: 'subpackage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage/subpackage-delete-dialog.html',
                    controller: 'SubpackageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Subpackage', function(Subpackage) {
                            return Subpackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('subpackage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
