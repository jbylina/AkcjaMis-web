(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamMemberDeleteController',TeamMemberDeleteController);

    TeamMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'TeamMember'];

    function TeamMemberDeleteController($uibModalInstance, entity, TeamMember) {
        var vm = this;
        vm.teamMember = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TeamMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
