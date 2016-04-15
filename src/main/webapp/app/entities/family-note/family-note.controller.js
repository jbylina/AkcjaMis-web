(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyNoteController', FamilyNoteController);

    FamilyNoteController.$inject = ['$scope', '$state', 'FamilyNote', 'FamilyNoteSearch'];

    function FamilyNoteController ($scope, $state, FamilyNote, FamilyNoteSearch) {
        var vm = this;
        vm.familyNotes = [];
        vm.loadAll = function() {
            FamilyNote.query(function(result) {
                vm.familyNotes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            FamilyNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.familyNotes = result;
            });
        };
        vm.loadAll();
        
    }
})();
