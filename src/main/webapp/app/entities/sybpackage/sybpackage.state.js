(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sybpackage', {
            parent: 'entity',
            url: '/sybpackage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sybpackages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sybpackage/sybpackages.html',
                    controller: 'SybpackageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('sybpackage-detail', {
            parent: 'entity',
            url: '/sybpackage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Sybpackage'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sybpackage/sybpackage-detail.html',
                    controller: 'SybpackageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Sybpackage', function($stateParams, Sybpackage) {
                    return Sybpackage.get({id : $stateParams.id});
                }]
            }
        })
        .state('sybpackage.new', {
            parent: 'sybpackage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sybpackage/sybpackage-dialog.html',
                    controller: 'SybpackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sbkID: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sybpackage', null, { reload: true });
                }, function() {
                    $state.go('sybpackage');
                });
            }]
        })
        .state('sybpackage.edit', {
            parent: 'sybpackage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sybpackage/sybpackage-dialog.html',
                    controller: 'SybpackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sybpackage', function(Sybpackage) {
                            return Sybpackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sybpackage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sybpackage.delete', {
            parent: 'sybpackage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sybpackage/sybpackage-delete-dialog.html',
                    controller: 'SybpackageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sybpackage', function(Sybpackage) {
                            return Sybpackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('sybpackage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
