(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('event', {
            parent: 'entity',
            url: '/event',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Events'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event/events.html',
                    controller: 'EventController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('event-detail', {
            parent: 'entity',
            url: '/event/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Event'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/event/event-detail.html',
                    controller: 'EventDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Event', function($stateParams, Event) {
                    return Event.get({id : $stateParams.id});
                }]
            }
        })
        .state('event.new', {
            parent: 'event',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-dialog.html',
                    controller: 'EventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                year: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('event', null, { reload: true });
                }, function() {
                    $state.go('event');
                });
            }]
        })
        .state('event.edit', {
            parent: 'event',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-dialog.html',
                    controller: 'EventDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Event', function(Event) {
                            return Event.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('event.delete', {
            parent: 'event',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/event/event-delete-dialog.html',
                    controller: 'EventDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Event', function(Event) {
                            return Event.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('event', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
