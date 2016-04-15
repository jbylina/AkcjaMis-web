(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChildDialogController', ChildDialogController);

    ChildDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Child', 'Family'];

    function ChildDialogController ($scope, $stateParams, $uibModalInstance, entity, Child, Family) {
        var vm = this;
        vm.child = entity;
        vm.familys = Family.query();
        vm.load = function(id) {
            Child.get({id : id}, function(result) {
                vm.child = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:childUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.child.id !== null) {
                Child.update(vm.child, onSaveSuccess, onSaveError);
            } else {
                Child.save(vm.child, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.birthYear = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
