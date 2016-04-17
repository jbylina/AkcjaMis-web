(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('christmas-package-note', {
            parent: 'entity',
            url: '/christmas-package-note',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackageNotes'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package-note/christmas-package-notes.html',
                    controller: 'ChristmasPackageNoteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('christmas-package-note-detail', {
            parent: 'entity',
            url: '/christmas-package-note/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ChristmasPackageNote'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/christmas-package-note/christmas-package-note-detail.html',
                    controller: 'ChristmasPackageNoteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ChristmasPackageNote', function($stateParams, ChristmasPackageNote) {
                    return ChristmasPackageNote.get({id : $stateParams.id});
                }]
            }
        })
        .state('christmas-package-note.new', {
            parent: 'christmas-package-note',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-note/christmas-package-note-dialog.html',
                    controller: 'ChristmasPackageNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('christmas-package-note', null, { reload: true });
                }, function() {
                    $state.go('christmas-package-note');
                });
            }]
        })
        .state('christmas-package-note.edit', {
            parent: 'christmas-package-note',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-note/christmas-package-note-dialog.html',
                    controller: 'ChristmasPackageNoteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChristmasPackageNote', function(ChristmasPackageNote) {
                            return ChristmasPackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('christmas-package-note.delete', {
            parent: 'christmas-package-note',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/christmas-package-note/christmas-package-note-delete-dialog.html',
                    controller: 'ChristmasPackageNoteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChristmasPackageNote', function(ChristmasPackageNote) {
                            return ChristmasPackageNote.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('christmas-package-note', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
