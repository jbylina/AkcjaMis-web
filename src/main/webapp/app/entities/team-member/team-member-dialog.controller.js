(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamMemberDialogController', TeamMemberDialogController);

    TeamMemberDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'TeamMember', 'Team'];

    function TeamMemberDialogController ($scope, $stateParams, $uibModalInstance, entity, TeamMember, Team) {
        var vm = this;
        vm.teamMember = entity;
        vm.teams = Team.query();
        vm.load = function(id) {
            TeamMember.get({id : id}, function(result) {
                vm.teamMember = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:teamMemberUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.teamMember.id !== null) {
                TeamMember.update(vm.teamMember, onSaveSuccess, onSaveError);
            } else {
                TeamMember.save(vm.teamMember, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
