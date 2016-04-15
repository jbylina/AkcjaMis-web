(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('TagController', TagController);

    TagController.$inject = ['$scope', '$state', 'Tag', 'TagSearch'];

    function TagController ($scope, $state, Tag, TagSearch) {
        var vm = this;
        vm.tags = [];
        vm.loadAll = function() {
            Tag.query(function(result) {
                vm.tags = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TagSearch.query({query: vm.searchQuery}, function(result) {
                vm.tags = result;
            });
        };
        vm.loadAll();
        
    }
})();
