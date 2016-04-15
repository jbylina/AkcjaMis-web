(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('package-gift', {
            parent: 'entity',
            url: '/package-gift',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageGifts'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-gift/package-gifts.html',
                    controller: 'PackageGiftController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('package-gift-detail', {
            parent: 'entity',
            url: '/package-gift/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageGift'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-gift/package-gift-detail.html',
                    controller: 'PackageGiftDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PackageGift', function($stateParams, PackageGift) {
                    return PackageGift.get({id : $stateParams.id});
                }]
            }
        })
        .state('package-gift.new', {
            parent: 'package-gift',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-gift/package-gift-dialog.html',
                    controller: 'PackageGiftDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mark: null,
                                delivered: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('package-gift', null, { reload: true });
                }, function() {
                    $state.go('package-gift');
                });
            }]
        })
        .state('package-gift.edit', {
            parent: 'package-gift',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-gift/package-gift-dialog.html',
                    controller: 'PackageGiftDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PackageGift', function(PackageGift) {
                            return PackageGift.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-gift', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('package-gift.delete', {
            parent: 'package-gift',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-gift/package-gift-delete-dialog.html',
                    controller: 'PackageGiftDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PackageGift', function(PackageGift) {
                            return PackageGift.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-gift', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
