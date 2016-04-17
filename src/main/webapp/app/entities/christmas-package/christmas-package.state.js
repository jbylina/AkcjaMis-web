(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('christmas-package', {
            parent: 'entity',
            url: '/christmas-package?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackages'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package/christmas-packages.html',
                    controller: 'ChristmasPackageController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('christmas-package-detail', {
            parent: 'entity',
            url: '/christmas-package/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackage'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package/christmas-package-detail.html',
                    controller: 'ChristmasPackageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ChristmasPackage', function($stateParams, ChristmasPackage) {
                    return ChristmasPackage.get({id : $stateParams.id});
                }]
            }
        })
        .state('christmas-package.new', {
            parent: 'christmas-package',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package/christmas-package-dialog.html',
                    controller: 'ChristmasPackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mark: null,
                                delivered: false,
                                packageNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('christmas-package', null, { reload: true });
                }, function() {
                    $state.go('christmas-package');
                });
            }]
        })
        .state('christmas-package.edit', {
            parent: 'christmas-package',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package/christmas-package-dialog.html',
                    controller: 'ChristmasPackageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChristmasPackage', function(ChristmasPackage) {
                            return ChristmasPackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('christmas-package.delete', {
            parent: 'christmas-package',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package/christmas-package-delete-dialog.html',
                    controller: 'ChristmasPackageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChristmasPackage', function(ChristmasPackage) {
                            return ChristmasPackage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
