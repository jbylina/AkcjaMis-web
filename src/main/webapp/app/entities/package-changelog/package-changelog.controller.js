(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageChangelogController', PackageChangelogController);

    PackageChangelogController.$inject = ['$scope', '$state', 'PackageChangelog', 'PackageChangelogSearch'];

    function PackageChangelogController ($scope, $state, PackageChangelog, PackageChangelogSearch) {
        var vm = this;
        vm.packageChangelogs = [];
        vm.loadAll = function() {
            PackageChangelog.query(function(result) {
                vm.packageChangelogs = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PackageChangelogSearch.query({query: vm.searchQuery}, function(result) {
                vm.packageChangelogs = result;
            });
        };
        vm.loadAll();
        
    }
})();
