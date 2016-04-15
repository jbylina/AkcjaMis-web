(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageNoteController', SubpackageNoteController);

    SubpackageNoteController.$inject = ['$scope', '$state', 'SubpackageNote', 'SubpackageNoteSearch'];

    function SubpackageNoteController ($scope, $state, SubpackageNote, SubpackageNoteSearch) {
        var vm = this;
        vm.subpackageNotes = [];
        vm.loadAll = function() {
            SubpackageNote.query(function(result) {
                vm.subpackageNotes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            SubpackageNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.subpackageNotes = result;
            });
        };
        vm.loadAll();
        
    }
})();
