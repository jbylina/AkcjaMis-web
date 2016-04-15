(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('subpackage-note', {
            parent: 'entity',
            url: '/subpackage-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SubpackageNotes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subpackage-note/subpackage-notes.html',
                    controller: 'SubpackageNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('subpackage-note-detail', {
            parent: 'entity',
            url: '/subpackage-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SubpackageNote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/subpackage-note/subpackage-note-detail.html',
                    controller: 'SubpackageNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SubpackageNote', function($stateParams, SubpackageNote) {
                    return SubpackageNote.get({id : $stateParams.id});
                }]
            }
        })
        .state('subpackage-note.new', {
            parent: 'subpackage-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage-note/subpackage-note-dialog.html',
                    controller: 'SubpackageNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                text: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('subpackage-note', null, { reload: true });
                }, function() {
                    $state.go('subpackage-note');
                });
            }]
        })
        .state('subpackage-note.edit', {
            parent: 'subpackage-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage-note/subpackage-note-dialog.html',
                    controller: 'SubpackageNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SubpackageNote', function(SubpackageNote) {
                            return SubpackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('subpackage-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('subpackage-note.delete', {
            parent: 'subpackage-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/subpackage-note/subpackage-note-delete-dialog.html',
                    controller: 'SubpackageNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SubpackageNote', function(SubpackageNote) {
                            return SubpackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('subpackage-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
