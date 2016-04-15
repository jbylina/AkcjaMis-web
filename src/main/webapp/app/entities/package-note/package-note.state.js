(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('package-note', {
            parent: 'entity',
            url: '/package-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageNotes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-note/package-notes.html',
                    controller: 'PackageNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('package-note-detail', {
            parent: 'entity',
            url: '/package-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'PackageNote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/package-note/package-note-detail.html',
                    controller: 'PackageNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'PackageNote', function($stateParams, PackageNote) {
                    return PackageNote.get({id : $stateParams.id});
                }]
            }
        })
        .state('package-note.new', {
            parent: 'package-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-note/package-note-dialog.html',
                    controller: 'PackageNoteDialogController',
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
                    $state.go('package-note', null, { reload: true });
                }, function() {
                    $state.go('package-note');
                });
            }]
        })
        .state('package-note.edit', {
            parent: 'package-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-note/package-note-dialog.html',
                    controller: 'PackageNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PackageNote', function(PackageNote) {
                            return PackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('package-note.delete', {
            parent: 'package-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/package-note/package-note-delete-dialog.html',
                    controller: 'PackageNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PackageNote', function(PackageNote) {
                            return PackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('package-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
