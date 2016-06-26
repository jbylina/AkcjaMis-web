(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageDetailController', ChristmasPackageDetailController);

    ChristmasPackageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ChristmasPackage', 'Event', 'Team', 'ChristmasPackageNote', 'ChristmasPackageChange', 'Subpackage', 'SubpackageNote', 'Family'];

    function ChristmasPackageDetailController($scope, $rootScope, $stateParams, entity, ChristmasPackage, Event, Team, ChristmasPackageNote, ChristmasPackageChange, Subpackage, SubpackageNote, Family) {
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
            $scope.contentModel = '';
            vm.load();
        };

        var onSaveError = function (result) {
        };

        vm.saveNote = function (text) {
            var note = new ChristmasPackageNote;
            note.content = text;
            ChristmasPackageNote.save({id : vm.christmasPackage.id}, note, onSaveSuccess, onSaveError);
        }

        var onUpdateSuccess = function (result) {
        };

        var onUpdateError = function () {
        };

        vm.updateNote = function (note) {
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

        var onSubSaveSuccess = function (result) {
            $scope.subContent = '';
            vm.load();
        };

        var onSubSaveError = function (result) {
        };

        vm.saveSubNote = function (text) {
            var note = new SubpackageNote;
            note.content = text;
            // SubpackageNote.save(note, onSubSaveSuccess, onSubSaveError);
        }

        var onSubUpdateSuccess = function (result) {
        };

        var onSubUpdateError = function () {
        };

        vm.upadateSubNote = function (note) {
            // SubpackageNote.update(note, onSubUpdateSuccess, onSubUpdateError);
        }

        var onSubDeleteSuccess = function (result) {
            vm.load();
        };

        var onSubDeleteError = function () {
        };

        vm.deleteSubNote = function (id) {
            // SubpackageNote.delete({id : id}, onSubDeleteSuccess, onSubDeleteError);
        }
    }
})();
