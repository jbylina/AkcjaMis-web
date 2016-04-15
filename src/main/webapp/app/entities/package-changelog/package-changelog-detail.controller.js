(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageChangelogDetailController', PackageChangelogDetailController);

    PackageChangelogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PackageChangelog', 'PackageGift'];

    function PackageChangelogDetailController($scope, $rootScope, $stateParams, entity, PackageChangelog, PackageGift) {
        var vm = this;
        vm.packageChangelog = entity;
        vm.load = function (id) {
            PackageChangelog.get({id: id}, function(result) {
                vm.packageChangelog = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:packageChangelogUpdate', function(event, result) {
            vm.packageChangelog = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
