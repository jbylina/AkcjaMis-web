(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('family-note', {
            parent: 'entity',
            url: '/family-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FamilyNotes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/family-note/family-notes.html',
                    controller: 'FamilyNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('family-note-detail', {
            parent: 'entity',
            url: '/family-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FamilyNote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/family-note/family-note-detail.html',
                    controller: 'FamilyNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FamilyNote', function($stateParams, FamilyNote) {
                    return FamilyNote.get({id : $stateParams.id});
                }]
            }
        })
        .state('family-note.new', {
            parent: 'family-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family-note/family-note-dialog.html',
                    controller: 'FamilyNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                time: null,
                                text: null,
                                archived: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('family-note', null, { reload: true });
                }, function() {
                    $state.go('family-note');
                });
            }]
        })
        .state('family-note.edit', {
            parent: 'family-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family-note/family-note-dialog.html',
                    controller: 'FamilyNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FamilyNote', function(FamilyNote) {
                            return FamilyNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('family-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('family-note.delete', {
            parent: 'family-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/family-note/family-note-delete-dialog.html',
                    controller: 'FamilyNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FamilyNote', function(FamilyNote) {
                            return FamilyNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('family-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
