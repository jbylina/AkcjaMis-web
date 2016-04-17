(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageChangeDialogController', ChristmasPackageChangeDialogController);

    ChristmasPackageChangeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChristmasPackageChange', 'ChristmasPackage'];

    function ChristmasPackageChangeDialogController ($scope, $stateParams, $uibModalInstance, entity, ChristmasPackageChange, ChristmasPackage) {
        var vm = this;
        vm.christmasPackageChange = entity;
        vm.christmaspackages = ChristmasPackage.query();
        vm.load = function(id) {
            ChristmasPackageChange.get({id : id}, function(result) {
                vm.christmasPackageChange = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:christmasPackageChangeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.christmasPackageChange.id !== null) {
                ChristmasPackageChange.update(vm.christmasPackageChange, onSaveSuccess, onSaveError);
            } else {
                ChristmasPackageChange.save(vm.christmasPackageChange, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.time = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
