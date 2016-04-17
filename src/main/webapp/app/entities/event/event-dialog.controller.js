(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'Team', 'ChristmasPackage'];

    function EventDialogController ($scope, $stateParams, $uibModalInstance, entity, Event, Team, ChristmasPackage) {
        var vm = this;
        vm.event = entity;
        vm.teams = Team.query();
        vm.christmaspackages = ChristmasPackage.query();
        vm.load = function(id) {
            Event.get({id : id}, function(result) {
                vm.event = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.year = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
