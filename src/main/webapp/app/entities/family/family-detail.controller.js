(function() {
    'use strict';

    angular
        .module('akcjamisApp')
        .controller('FamilyDetailController', FamilyDetailController);

    FamilyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Family', 'Contact', 'Child', 'FamilyNote', 'ChristmasPackage'];

    function FamilyDetailController($scope, $rootScope, $stateParams, entity, Family, Contact, Child, FamilyNote, ChristmasPackage) {
        var vm = this;
        vm.family = entity;
        vm.load = function (id) {
            Family.get({id: id}, function(result) {
                vm.family = result;
            });
        };
        var unsubscribe = $rootScope.$on('akcjamisApp:familyUpdate', function(event, result) {
            vm.family = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
