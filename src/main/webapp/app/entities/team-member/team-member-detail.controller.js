(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamMemberDetailController', TeamMemberDetailController);

    TeamMemberDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TeamMember', 'Team'];

    function TeamMemberDetailController($scope, $rootScope, $stateParams, entity, TeamMember, Team) {
        var vm = this;
        vm.teamMember = entity;
        vm.load = function (id) {
            TeamMember.get({id: id}, function(result) {
                vm.teamMember = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:teamMemberUpdate', function(event, result) {
            vm.teamMember = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
