(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('ChristmasPackageController', ChristmasPackageController)
        .service('dataService', function() {
            var allExpanded  = true;

            return {
                getProperty: function () {
                    return allExpanded;
                },
                setProperty: function(value) {
                    allExpanded = value;
                }
            };
        });

    ChristmasPackageController.$inject = ['$scope', '$state', 'ChristmasPackage', 'ChristmasPackageSearch', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'dataService'];

    function ChristmasPackageController ($scope, $state, ChristmasPackage, ChristmasPackageSearch, ParseLinks, AlertService, pagingParams, paginationConstants, dataService) {
        var vm = this;
        vm.year = $state.params.year;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.clear = clear;
        vm.search = search;
        vm.expandAll = expandAll;
        vm.searchQuery = pagingParams.search;
        vm.currentSearch = pagingParams.search;
        vm.loadAll(vm.year);
        vm.date = new Date().getFullYear();
        vm.allExpanded = dataService.getProperty();

        function loadAll (year) {
            if (pagingParams.search) {
                ChristmasPackageSearch.query({
                    query: pagingParams.search,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            } else {
                ChristmasPackage.getList( {
                    year : year,
                    page: pagingParams.page - 1,
                    size: paginationConstants.itemsPerPage,
                    sort: sort()
                }, onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.christmasPackages = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                year: vm.year,
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

        function search (searchQuery) {
            if (!searchQuery){
                return vm.clear();
            }
            vm.links = null;
            vm.page = 1;
            vm.predicate = '_score';
            vm.reverse = false;
            vm.currentSearch = searchQuery;
            vm.transition();
        }

        function clear () {
            vm.links = null;
            vm.page = 1;
            vm.predicate = 'id';
            vm.reverse = true;
            vm.currentSearch = null;
            vm.transition();
        }

        function expandAll(expanded) {
            dataService.setProperty(expanded);
        }

    }
})();
