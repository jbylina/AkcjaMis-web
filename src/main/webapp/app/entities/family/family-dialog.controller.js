(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDialogController', FamilyDialogController);

    FamilyDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Family', 'Contact', 'Child', 'FamilyNote', 'ChristmasPackage'];

    function FamilyDialogController ($scope, $stateParams, $uibModalInstance, entity, Family, Contact, Child, FamilyNote, ChristmasPackage) {
        var vm = this;
        vm.family = entity;
        vm.contacts = Contact.query();
        vm.childs = Child.query();
        vm.familynotes = FamilyNote.query();
        vm.christmaspackages = ChristmasPackage.query();
        vm.load = function(id) {
            Family.get({id : id}, function(result) {
                vm.family = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:familyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.family.id !== null) {
                Family.update(vm.family, onSaveSuccess, onSaveError);
            } else {
                Family.save(vm.family, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
