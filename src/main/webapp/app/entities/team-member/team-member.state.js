(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('team-member', {
            parent: 'entity',
            url: '/team-member',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TeamMembers'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-member/team-members.html',
                    controller: 'TeamMemberController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('team-member-detail', {
            parent: 'entity',
            url: '/team-member/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TeamMember'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/team-member/team-member-detail.html',
                    controller: 'TeamMemberDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TeamMember', function($stateParams, TeamMember) {
                    return TeamMember.get({id : $stateParams.id});
                }]
            }
        })
        .state('team-member.new', {
            parent: 'team-member',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-member/team-member-dialog.html',
                    controller: 'TeamMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                usrUUID: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('team-member', null, { reload: true });
                }, function() {
                    $state.go('team-member');
                });
            }]
        })
        .state('team-member.edit', {
            parent: 'team-member',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-member/team-member-dialog.html',
                    controller: 'TeamMemberDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TeamMember', function(TeamMember) {
                            return TeamMember.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-member', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('team-member.delete', {
            parent: 'team-member',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/team-member/team-member-delete-dialog.html',
                    controller: 'TeamMemberDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TeamMember', function(TeamMember) {
                            return TeamMember.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('team-member', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
