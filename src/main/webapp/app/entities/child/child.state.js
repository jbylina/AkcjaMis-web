(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('child', {
            parent: 'entity',
            url: '/child',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Children'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/child/children.html',
                    controller: 'ChildController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('child-detail', {
            parent: 'entity',
            url: '/child/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Child'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/child/child-detail.html',
                    controller: 'ChildDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Child', function($stateParams, Child) {
                    return Child.get({id : $stateParams.id});
                }]
            }
        })
        .state('child.new', {
            parent: 'child',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/child/child-dialog.html',
                    controller: 'ChildDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                chldNo: null,
                                firstName: null,
                                lastName: null,
                                sex: null,
                                birthYear: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('child', null, { reload: true });
                }, function() {
                    $state.go('child');
                });
            }]
        })
        .state('child.edit', {
            parent: 'child',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/child/child-dialog.html',
                    controller: 'ChildDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Child', function(Child) {
                            return Child.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('child', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('child.delete', {
            parent: 'child',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/child/child-delete-dialog.html',
                    controller: 'ChildDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Child', function(Child) {
                            return Child.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('child', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
