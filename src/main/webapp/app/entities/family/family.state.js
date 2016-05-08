(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('family', {
            parent: 'entity',
            url: '/family?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Families'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/family/families.html',
                    controller: 'FamilyController',
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
        .state('family-detail', {
            parent: 'entity',
            url: '/family/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Family'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/family/family-detail.html',
                    controller: 'FamilyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Family', function($stateParams, Family) {
                    return Family.get({id : $stateParams.id});
                }]
            }
        })
            .state('family.create',{
                parent:'entity',
                url: '/family/create',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Nowa rodzina'
                },
                views:{
                    'content@': {
                        templateUrl: 'app/entities/family/new-family.html',
                        controller: 'FamilyCreateController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    entity: function () {
                        return {
                            street: null,
                            houseNo: null,
                            flatNo: null,
                            postalcode: null,
                            district: null,
                            city: null,
                            region: null,
                            source: null,
                            id: null
                        };
                    }
                }
            })
        .state('family.new', {
            parent: 'family',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family/family-dialog.html',
                    controller: 'FamilyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                street: null,
                                houseNo: null,
                                flatNo: null,
                                postalcode: null,
                                district: null,
                                city: null,
                                region: null,
                                source: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('family', null, { reload: true });
                }, function() {
                    $state.go('family');
                });
            }]
        })
        .state('family.edit', {
            parent: 'family',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family/family-dialog.html',
                    controller: 'FamilyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Family', function(Family) {
                            return Family.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('family', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('family.delete', {
            parent: 'family',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family/family-delete-dialog.html',
                    controller: 'FamilyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Family', function(Family) {
                            return Family.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('family', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
