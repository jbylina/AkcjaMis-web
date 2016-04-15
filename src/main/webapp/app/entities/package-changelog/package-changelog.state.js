(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('package-changelog', {
            parent: 'entity',
            url: '/package-changelog',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageChangelogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-changelog/package-changelogs.html',
                    controller: 'PackageChangelogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('package-changelog-detail', {
            parent: 'entity',
            url: '/package-changelog/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageChangelog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-changelog/package-changelog-detail.html',
                    controller: 'PackageChangelogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PackageChangelog', function($stateParams, PackageChangelog) {
                    return PackageChangelog.get({id : $stateParams.id});
                }]
            }
        })
        .state('package-changelog.new', {
            parent: 'package-changelog',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-changelog/package-changelog-dialog.html',
                    controller: 'PackageChangelogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                typeCode: null,
                                time: null,
                                text: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('package-changelog', null, { reload: true });
                }, function() {
                    $state.go('package-changelog');
                });
            }]
        })
        .state('package-changelog.edit', {
            parent: 'package-changelog',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-changelog/package-changelog-dialog.html',
                    controller: 'PackageChangelogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PackageChangelog', function(PackageChangelog) {
                            return PackageChangelog.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-changelog', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('package-changelog.delete', {
            parent: 'package-changelog',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-changelog/package-changelog-delete-dialog.html',
                    controller: 'PackageChangelogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PackageChangelog', function(PackageChangelog) {
                            return PackageChangelog.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-changelog', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
