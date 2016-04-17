(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageNoteController', ChristmasPackageNoteController);

    ChristmasPackageNoteController.$inject = ['$scope', '$state', 'ChristmasPackageNote', 'ChristmasPackageNoteSearch'];

    function ChristmasPackageNoteController ($scope, $state, ChristmasPackageNote, ChristmasPackageNoteSearch) {
        var vm = this;
        vm.christmasPackageNotes = [];
        vm.loadAll = function() {
            ChristmasPackageNote.query(function(result) {
                vm.christmasPackageNotes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ChristmasPackageNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.christmasPackageNotes = result;
            });
        };
        vm.loadAll();
        
    }
})();
