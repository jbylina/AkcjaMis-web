(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageDetailController', ChristmasPackageDetailController);

    ChristmasPackageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ChristmasPackage', 'Event', 'Team', 'ChristmasPackageNote', 'ChristmasPackageChange', 'Subpackage', 'Family'];

    function ChristmasPackageDetailController($scope, $rootScope, $stateParams, entity, ChristmasPackage, Event, Team, ChristmasPackageNote, ChristmasPackageChange, Subpackage, Family) {
        var vm = this;
        vm.christmasPackage = entity;
        vm.date = new Date().getFullYear();
        vm.load = function () {
            ChristmasPackage.get({year:$stateParams.year, id: $stateParams.id}, function(result) {
                vm.christmasPackage = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:christmasPackageUpdate', function(event, result) {
            vm.christmasPackage = result;
        });
        $scope.$on('$destroy', unsubscribe);

        var onSaveSuccess = function (result) {
            $scope.inputNote = '';
            vm.christmasPackage.christmasPackageNotes.push(result);
        };

        var onSaveError = function (result) {
        };

        vm.saveNote = function (text) {
            console.log(text);
            var note = new ChristmasPackageNote;
            note.content = text;
            ChristmasPackageNote.update({id : vm.christmasPackage.id}, note, onSaveSuccess, onSaveError);
        }

        var onUpdateSuccess = function (result) {
        };

        var onUpdateError = function () {
        };

        vm.upadateNote = function (note) {
            ChristmasPackageNote.update({id : vm.christmasPackage.id}, note, onUpdateSuccess, onUpdateError);
        }

        var onDeleteSuccess = function (result) {
            vm.load();
        };

        var onDeleteError = function () {
        };

        vm.deleteNote = function (id) {
            ChristmasPackageNote.delete({packageId : vm.christmasPackage.id, id : id}, onDeleteSuccess, onDeleteError);
        }
    }
})();
