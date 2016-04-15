(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('PackageGiftDetailController', PackageGiftDetailController);

    PackageGiftDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'PackageGift', 'Family', 'Event', 'Team'];

    function PackageGiftDetailController($scope, $rootScope, $stateParams, entity, PackageGift, Family, Event, Team) {
        var vm = this;
        vm.packageGift = entity;
        vm.load = function (id) {
            PackageGift.get({id: id}, function(result) {
                vm.packageGift = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:packageGiftUpdate', function(event, result) {
            vm.packageGift = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
