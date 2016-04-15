(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SybpackageDialogController', SybpackageDialogController);

    SybpackageDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sybpackage', 'PackageGift'];

    function SybpackageDialogController ($scope, $stateParams, $uibModalInstance, entity, Sybpackage, PackageGift) {
        var vm = this;
        vm.sybpackage = entity;
        vm.packagegifts = PackageGift.query();
        vm.load = function(id) {
            Sybpackage.get({id : id}, function(result) {
                vm.sybpackage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:sybpackageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.sybpackage.id !== null) {
                Sybpackage.update(vm.sybpackage, onSaveSuccess, onSaveError);
            } else {
                Sybpackage.save(vm.sybpackage, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
