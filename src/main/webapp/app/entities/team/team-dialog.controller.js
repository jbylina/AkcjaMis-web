(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'Event'];

    function TeamDialogController ($scope, $stateParams, $uibModalInstance, entity, Team, Event) {
        var vm = this;
        vm.team = entity;
        vm.events = Event.query();
        vm.load = function(id) {
            Team.get({id : id}, function(result) {
                vm.team = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
