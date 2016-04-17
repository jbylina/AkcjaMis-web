(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('SubpackageDetailController', SubpackageDetailController);

    SubpackageDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Subpackage', 'SubpackageNote', 'Child', 'ChristmasPackage'];

    function SubpackageDetailController($scope, $rootScope, $stateParams, entity, Subpackage, SubpackageNote, Child, ChristmasPackage) {
        var vm = this;
        vm.subpackage = entity;
        vm.load = function (id) {
            Subpackage.get({id: id}, function(result) {
                vm.subpackage = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:subpackageUpdate', function(event, result) {
            vm.subpackage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
