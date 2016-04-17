(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('christmas-package-change', {
            parent: 'entity',
            url: '/christmas-package-change',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackageChanges'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package-change/christmas-package-changes.html',
                    controller: 'ChristmasPackageChangeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('christmas-package-change-detail', {
            parent: 'entity',
            url: '/christmas-package-change/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackageChange'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package-change/christmas-package-change-detail.html',
                    controller: 'ChristmasPackageChangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ChristmasPackageChange', function($stateParams, ChristmasPackageChange) {
                    return ChristmasPackageChange.get({id : $stateParams.id});
                }]
            }
        })
        .state('christmas-package-change.new', {
            parent: 'christmas-package-change',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-change/christmas-package-change-dialog.html',
                    controller: 'ChristmasPackageChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                type_code: null,
                                time: null,
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('christmas-package-change', null, { reload: true });
                }, function() {
                    $state.go('christmas-package-change');
                });
            }]
        })
        .state('christmas-package-change.edit', {
            parent: 'christmas-package-change',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-change/christmas-package-change-dialog.html',
                    controller: 'ChristmasPackageChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChristmasPackageChange', function(ChristmasPackageChange) {
                            return ChristmasPackageChange.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package-change', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('christmas-package-change.delete', {
            parent: 'christmas-package-change',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-change/christmas-package-change-delete-dialog.html',
                    controller: 'ChristmasPackageChangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChristmasPackageChange', function(ChristmasPackageChange) {
                            return ChristmasPackageChange.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package-change', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
