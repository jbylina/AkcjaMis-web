(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageNoteController', PackageNoteController);

    PackageNoteController.$inject = ['$scope', '$state', 'PackageNote', 'PackageNoteSearch'];

    function PackageNoteController ($scope, $state, PackageNote, PackageNoteSearch) {
        var vm = this;
        vm.packageNotes = [];
        vm.loadAll = function() {
            PackageNote.query(function(result) {
                vm.packageNotes = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PackageNoteSearch.query({query: vm.searchQuery}, function(result) {
                vm.packageNotes = result;
            });
        };
        vm.loadAll();
        
    }
})();
