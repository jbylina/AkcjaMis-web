(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageDialogController', SubpackageDialogController);

    SubpackageDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Subpackage', 'SubpackageNote', 'Child', 'ChristmasPackage'];

    function SubpackageDialogController ($scope, $stateParams, $uibModalInstance, entity, Subpackage, SubpackageNote, Child, ChristmasPackage) {
        var vm = this;
        vm.subpackage = entity;
        vm.subpackagenotes = SubpackageNote.query();
        vm.childs = Child.query();
        vm.christmaspackages = ChristmasPackage.query({event_id : 1});
        vm.load = function(id) {
            Subpackage.get({id : id}, function(result) {
                vm.subpackage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:subpackageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.subpackage.id !== null) {
                Subpackage.update(vm.subpackage, onSaveSuccess, onSaveError);
            } else {
                Subpackage.save(vm.subpackage, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
