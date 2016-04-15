(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageChangelogDialogController', PackageChangelogDialogController);

    PackageChangelogDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PackageChangelog', 'PackageGift'];

    function PackageChangelogDialogController ($scope, $stateParams, $uibModalInstance, entity, PackageChangelog, PackageGift) {
        var vm = this;
        vm.packageChangelog = entity;
        vm.packagegifts = PackageGift.query();
        vm.load = function(id) {
            PackageChangelog.get({id : id}, function(result) {
                vm.packageChangelog = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:packageChangelogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.packageChangelog.id !== null) {
                PackageChangelog.update(vm.packageChangelog, onSaveSuccess, onSaveError);
            } else {
                PackageChangelog.save(vm.packageChangelog, onSaveSuccess, onSaveError);
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
