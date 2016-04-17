(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageDialogController', ChristmasPackageDialogController);

    ChristmasPackageDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ChristmasPackage', 'Event', 'Team', 'ChristmasPackageNote', 'ChristmasPackageChange', 'Subpackage', 'Family'];

    function ChristmasPackageDialogController ($scope, $stateParams, $uibModalInstance, entity, ChristmasPackage, Event, Team, ChristmasPackageNote, ChristmasPackageChange, Subpackage, Family) {
        var vm = this;
        vm.christmasPackage = entity;
        vm.events = Event.query();
        vm.teams = Team.query();
        vm.christmaspackagenotes = ChristmasPackageNote.query();
        vm.christmaspackagechanges = ChristmasPackageChange.query();
        vm.subpackages = Subpackage.query();
        vm.familys = Family.query();
        vm.load = function(id) {
            ChristmasPackage.get({id : id}, function(result) {
                vm.christmasPackage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('akcjamisApp:christmasPackageUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.christmasPackage.id !== null) {
                ChristmasPackage.update(vm.christmasPackage, onSaveSuccess, onSaveError);
            } else {
                ChristmasPackage.save(vm.christmasPackage, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
